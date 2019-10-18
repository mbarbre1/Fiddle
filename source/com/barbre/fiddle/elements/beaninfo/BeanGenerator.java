package com.barbre.fiddle.elements.beaninfo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList; 
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.barbre.fiddle.io.loaders.PropertiesLoader;
import com.barbre.fiddle.io.loaders.UILoader;

public class BeanGenerator {
	private static final String DEFAULT_SAX_DRIVER_CLASS = "org.apache.xerces.parsers.SAXParser";
	private static final SAXBuilder XML = new SAXBuilder(DEFAULT_SAX_DRIVER_CLASS);
	private static Document definitions;

	private Map sidl = new HashMap();
	private String directory = "c:\\temp";
	private String extensions = "";

	/**
	 * Method replace.
	 * @param source
	 * @param old
	 * @param replacement
	 * @return String
	 */
	public static final String replace(String target, String old, String replacement) {
		int start = target.indexOf(old);
		while (start != -1) {
			StringBuffer buffer = new StringBuffer(target);
			buffer.delete(start, start + old.length());
			buffer.insert(start, replacement);
			target = new String(buffer);
			start = target.indexOf(old, start + replacement.length());
		}
		return target;
	}

	/**
	 * Method getElementDefault.
	 * @param el
	 * @return String
	 */
	private String getElementDefault(Element el) {
		String childText = el.getChildText("default", el.getNamespace());
		return childText;
	}

	/**
	 * Method getElementName.
	 * @param el
	 * @return String
	 */
	private String getElementName(Element el) {
		return el.getAttributeValue("name");
	}

	/**
	 * Method getElementsFor.
	 * @param type
	 * @return List
	 */
	private List getElementsFor(String type) {
		List myList = new ArrayList();
		Element el = (Element) sidl.get(type);
		List list = el.getChildren("element", el.getNamespace());
		myList.addAll(list);
		myList.addAll(el.getChildren("attribute", el.getNamespace()));
		return myList;
	}

	/**
	 * Method getElementType.
	 * @param el
	 * @return String
	 */
	private String getElementType(Element el) {
		String type = el.getAttributeValue("type");
		if (type.equals("string"))
			type = "String";
		return type;
	}

	/**
	 * Method getElementTypeClassName.
	 * @param name
	 * @return String
	 */
	private String getElementTypeClassName(String name) {
		String type = name.trim();
		if (type.equals("String"))
			return "String";
		if ((type.equals("boolean")) || (type.equals("int")))
			return type;
		return "I" + type;
	}

	/**
	 * Method getReadName.
	 * @param type
	 * @return String
	 */
	private String getReadName(String type) {
		if (!isReferenceType(type))
			return type;
		return type.substring(0, type.indexOf(':'));
	}

	/**
	 * Method getSuperType.
	 * @param el
	 * @return String
	 */
	private String getSuperType(Element el) {
		Element superType = el.getChild("superType", el.getNamespace());
		if (superType == null) {
			return null;
		}
		return superType.getAttributeValue("type");
	}

	/**
	 * Method getWriteName.
	 * @param type
	 * @return String
	 */
	private String getWriteName(String type) {
		if (!isReferenceType(type))
			return type;
		return type.substring(type.indexOf(':') + 1);
	}

	/**
	 * Method isElementAnArray.
	 * @param el
	 * @return boolean
	 */
	private boolean isElementAnArray(Element el) {
		return el.getAttributeValue("minOccurs") != null;
	}

	/**
	 * Method isReferenceType.
	 * @param type
	 * @return boolean
	 */
	private boolean isReferenceType(String type) {
		return type.indexOf(':') >= 0;
	}

	/**
	 * Method loadSIDL.
	 */
	private void loadSIDL() {
		UILoader.loadSIDL(sidl);
	}

	/**
	 * Method create.
	 * @throws JDOMException
	 */
	private void create() throws JDOMException {
		loadSIDL();

		definitions = XML.build(new File("beanBuilder.xml"));

		Iterator i = sidl.keySet().iterator();
		while (i.hasNext()) {
			String element = (String) i.next();
			try {
				extensions = definitions.getRootElement().getAttributeValue("extDir");
				Element interfaceHome = definitions.getRootElement().getChild("interface");
				createInterface(element, interfaceHome);
				createImpl(element, definitions.getRootElement().getChild("impl"), interfaceHome);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Method createInterface.
	 * @param name
	 * @param home
	 * @throws IOException
	 */
	private void createInterface(String name, Element home) throws IOException {
		String prefix = home.getAttributeValue("prefix");
		String pkg = home.getAttributeValue("package");
		Element el = (Element) sidl.get(name);

		String outputData = replace(home.getChildText("classTemplate"), "[NAME]", name);
		outputData = replace(outputData, "[IPREFIX]", prefix);
		outputData = replace(outputData, "[PACKAGE]", pkg);

		String superType = getSuperType(el);
		if (superType == null)
			superType = "Node";
		outputData = replace(outputData, "[PARENT]", superType);

		StringBuffer buffer = new StringBuffer(2048);
		Iterator i = getElementsFor(name).iterator();
		while (i.hasNext()) {
			Element element = (Element) i.next();
			String type = getOutputType(element, prefix);
			buildCodeBuffer(home, buffer, element, type, prefix);
		}

		outputData = buildVarsAndImports(name, "interface", outputData, buffer);
		outputData = replace(outputData, "[IMPORTS]", "");
		outputData = replace(outputData, "[ATTRIBUTES]", buffer.toString());

		FileWriter writer = new FileWriter(directory + File.separator + prefix + name + ".java");
		writer.write(outputData);
		writer.flush();
		writer.close();
	}

	/**
	 * Method buildVarsAndImports.
	 * @param name
	 * @param home
	 * @param outputData
	 * @param buffer
	 * @return String
	 */
	private String buildVarsAndImports(String name, String type, String outputData, StringBuffer buffer) {
		File theExtension = new File(extensions + File.separator + name + ".xml");
		if (theExtension.exists() == false)
			return outputData;

		Document ext;
		try {
			ext = XML.build(theExtension);
		} catch (JDOMException e) {
			return outputData;
		}

		Element section = ext.getRootElement().getChild(type);
		if (section == null)
			return outputData;

		List imports = section.getChildren("import");
		List methods = section.getChildren("method");

		StringBuffer aBuffer = new StringBuffer(512);
		Iterator i = imports.iterator();
		while (i.hasNext()) {
			Element e = (Element) i.next();
			aBuffer.append("\n\timport " + e.getText() + ";");
		}
		outputData = replace(outputData, "[IMPORTS]", aBuffer.toString());

		i = methods.iterator();
		while (i.hasNext()) {
			Element e = (Element) i.next();
			buffer.append("\n\t" + e.getText());

		}
		return outputData;
	}

	/**
	 * Method buildVarsAndImports.
	 * @param name
	 * @param home
	 * @param outputData
	 * @param buffer
	 * @return String
	 */
	private String buildImplements(String name, String type, String outputData, StringBuffer buffer) {
		File theExtension = new File(extensions + File.separator + name + ".xml");
		if (theExtension.exists() == false) {
			outputData = replace(outputData, "[IMPLEMENTS]", "");
			return outputData;
		}

		Document ext;
		try {
			ext = XML.build(theExtension);
		} catch (JDOMException e) {
			return outputData;
		}

		Element section = ext.getRootElement().getChild(type);
		if (section == null) {
			outputData = replace(outputData, "[IMPLEMENTS]", "");
			return outputData;
		}

		List list = section.getChildren("implements");
		StringBuffer aBuffer = new StringBuffer(512);
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			Element e = (Element) iter.next();
			aBuffer.append("," + e.getText());
		}
		outputData = replace(outputData, "[IMPLEMENTS]", aBuffer.toString());
		outputData = replace(outputData, "[IMPLEMENTS]", "");
		return outputData;
	}

	/**
	 * Method buildCodeBuffer.
	 * @param home
	 * @param buffer
	 * @param element
	 * @param type
	 */
	private void buildCodeBuffer(Element home, StringBuffer buffer, Element element, String type, String prefix) {
		if (isElementAnArray(element)) {
			addToBuffer(buffer, home.getChildText("getTemplate"), "[TYPE]", type + "[]", getElementName(element));
			addToBuffer(buffer, home.getChildText("setTemplate"), "[TYPE]", type + "[]", getElementName(element));
			addToBuffer(buffer, home.getChildText("indexedGetTemplate"), "[TYPE]", type, getElementName(element));
			addToBuffer(buffer, home.getChildText("indexedSetTemplate"), "[TYPE]", type, getElementName(element));

			if (isReferenceType(getElementType(element))) {
				type = getReadName(getElementType(element));
				type = getOutputType(type, prefix);

				addToBuffer(buffer, home.getChildText("getRefTemplate"), "[TYPE]", type + "[]", getElementName(element));
				addToBuffer(buffer, home.getChildText("setRefTemplate"), "[TYPE]", type + "[]", getElementName(element));
				addToBuffer(buffer, home.getChildText("indexedGetRefTemplate"), "[TYPE]", type, getElementName(element));
				addToBuffer(buffer, home.getChildText("indexedSetRefTemplate"), "[TYPE]", type, getElementName(element));
			}

		} else {
			addToBuffer(buffer, home.getChildText("getTemplate"), "[TYPE]", type, getElementName(element));
			addToBuffer(buffer, home.getChildText("setTemplate"), "[TYPE]", type, getElementName(element));

			if (isReferenceType(getElementType(element))) {
				type = getReadName(getElementType(element));
				type = getOutputType(type, prefix);
				addToBuffer(buffer, home.getChildText("getRefTemplate"), "[TYPE]", type, getElementName(element));
				addToBuffer(buffer, home.getChildText("setRefTemplate"), "[TYPE]", type, getElementName(element));
			}
		}
	}

	/**
	 * Method addToBuffer.
	 * @param buffer
	 * @param template
	 * @param tag
	 * @param type
	 * @param name
	 */
	private void addToBuffer(StringBuffer buffer, String template, String tag, String type, String name) {
		String s = replace(template, tag, type);
		s = replace(s, "[NAME]", name);
		buffer.append(s);
	}

	/**
	 * Method createImpl.
	 * @param name
	 * @param home
	 * @param iHome
	 * @throws IOException
	 */
	private void createImpl(String name, Element home, Element iHome) throws IOException {
		String prefix = home.getAttributeValue("prefix");
		String pkg = home.getAttributeValue("package");

		String iPrefix = iHome.getAttributeValue("prefix");
		String iPkg = iHome.getAttributeValue("package");
		Element el = (Element) sidl.get(name);

		String outputData = replace(home.getChildText("classTemplate"), "[NAME]", name);
		outputData = replace(outputData, "[IPREFIX]", iPrefix);
		outputData = replace(outputData, "[IPACKAGE]", iPkg);
		outputData = replace(outputData, "[PREFIX]", prefix);
		outputData = replace(outputData, "[PACKAGE]", pkg);

		String superType = getSuperType(el);
		if (superType == null)
			superType = "Node";
		outputData = replace(outputData, "[PARENT]", superType);

		StringBuffer buffer = new StringBuffer(2048);
		StringBuffer varBuffer = new StringBuffer(1024);
		Iterator i = getElementsFor(name).iterator();
		while (i.hasNext()) {
			Element element = (Element) i.next();

			String type = getOutputType(element, iPrefix);
			buildCodeBuffer(home, buffer, element, type, iPrefix);
			String ports = buildVariables(varBuffer, element, type, iPkg, iPrefix);
			outputData = replace(outputData, "[IMPORTS]", ports);			
		}

		outputData = buildVarsAndImports(name, "impl", outputData, buffer);
		outputData = buildImplements(name, "impl", outputData, buffer);
		outputData = replace(outputData, "[IMPORTS]", "");
		outputData = replace(outputData, "[ATTRIBUTES]", buffer.toString());
		outputData = replace(outputData, "[VARS]", varBuffer.toString());

		FileWriter writer = new FileWriter(directory + File.separator + "EQ" + name + ".java");
		writer.write(outputData);
		writer.flush();
		writer.close();
	}

	/**
	 * Method getOutputType.
	 * @param element
	 * @param prefix
	 * @return String
	 */
	private String getOutputType(Element element, String prefix) {
		String type = getElementType(element);
		if (isReferenceType(getElementType(element))) {
			type = "String";
		}
		return getOutputType(type, prefix);
	}

	/**
	 * Method getOutputType.
	 * @param type
	 * @param prefix
	 * @return String
	 */
	private String getOutputType(String type, String prefix) {
		if (isPrimitive(type))
			return type;
		type = prefix + type;
		return type;
	}

	/**
	 * Method isPrimitive.
	 * @param type
	 * @return boolean
	 */
	private boolean isPrimitive(String type) {
		return (type.equals("String")) || (type.equals("int")) || (type.equals("boolean"));
	}

	/**
	 * Method buildVariables.
	 * @param varBuffer
	 * @param element
	 * @param type
	 * @return import modifiers
	 */
	private String buildVariables(StringBuffer varBuffer, Element element, String type, String pkg, String prefix) {
		StringBuffer importModifiers = new StringBuffer(200);
		
		String def = "";
		if (getElementDefault(element) != null) {
			def = " = " + getElementDefault(element);
		}
		
		if (isPrimitive(type) == false) 
			importModifiers.append("import " + pkg + "." + type + ";");
		
		if (isElementAnArray(element)) {
			varBuffer.append("\tprivate " + type + "[] " + getElementName(element) + def + ";\n");
		} else {
			varBuffer.append("\tprivate " + type + " " + getElementName(element) + def + ";\n");
		}

		if (isReferenceType(getElementType(element))) {
			type = getReadName(getElementType(element));
			type = getOutputType(type, prefix);
			if (isPrimitive(type) == false)
				importModifiers.append("import " + pkg + "." + type + ";");
			if (isElementAnArray(element)) {
				varBuffer.append("\tprivate " + type + "[] " + getElementName(element) + def + "Object;\n");
			} else {
				varBuffer.append("\tprivate " + type + " " + getElementName(element) + def + "Object;\n");
			}
		}
		importModifiers.append("[IMPORTS]");
		return importModifiers.toString();
	}

	/**
	 * Method printChildren.
	 * @param el
	 * @param level
	 */
	private static void printChildren(Element el, int level) {
		String blanks = "                                                                 ";
		System.out.println(blanks.substring(0, level) + el.getName());
		System.out.println(blanks.substring(0, level) + el.getText());
		Iterator i = el.getChildren().iterator();
		while (i.hasNext()) {
			Element element = (Element) i.next();
			printChildren(element, level + 3);
		}
	}

	/**
	 * Method main.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			PropertiesLoader.setEQLocation();
			BeanGenerator bib = new BeanGenerator();
			bib.create();
		} catch (JDOMException e) {
			e.printStackTrace();
		} finally {
			System.out.println("finished.");
		}
	}
}
