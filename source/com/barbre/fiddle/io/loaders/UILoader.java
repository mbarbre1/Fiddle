package com.barbre.fiddle.io.loaders;

import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.MessageManager;
import com.barbre.fiddle.elements.IClass;
import com.barbre.fiddle.elements.INode;
import com.barbre.fiddle.elements.UIFile;
import com.barbre.fiddle.elements.UIFileSet;
import com.barbre.fiddle.io.DefaultUIFile;
import com.barbre.fiddle.io.DefaultUIFileSet;
import com.barbre.fiddle.io.EQNode;
import com.barbre.fiddle.io.ItemTypeManager;
import com.barbre.fiddle.io.utility.BeanInfoManager;
import com.barbre.fiddle.io.utility.ChildPropertyListener;
import com.barbre.fiddle.io.utility.IndirectReferenceManager;
import com.barbre44.util.Debug;

public class UILoader {
	public static UIFileSet DEFAULT_FILE_SET = null;
	private static final String DEFAULT_SAX_DRIVER_CLASS = "org.apache.xerces.parsers.SAXParser";
	public static SAXBuilder builder = new SAXBuilder(DEFAULT_SAX_DRIVER_CLASS);
	private static UILoader instance = null;

	/**
	 * @see Object#Object()
	 */
	private UILoader() {
		super();
	}

	/**
	 * Method getInstance.
	 * @return UILoader
	 */
	public static UILoader getInstance() {
		if (instance == null) {
			instance = new UILoader();
		}
		return instance;
	}

	/**
	 * Parse
	 * @param filename
	 * @return IScreen[]
	 */
	public INode parseFile(UIFileSet set, UIFile file) throws IOException {
		try {
			String path = set.getDirectory() + File.separator + file.getName();
			if (new File(path).exists() == false) {
				Debug.println(this, "parseFile.  path doesn't exist: " + path);
				path = DEFAULT_FILE_SET.getDirectory() + File.separator + file.getName();
			}
			Document doc = builder.build(new File(path));
			Namespace ns = ((Element) doc.getRootElement().getChildren().get(0)).getNamespace();
			List elements = doc.getRootElement().getChildren();
			if (elements.size() == 0)
				return null;

			Iterator i = elements.iterator();
			while (i.hasNext()) {
				Element element = (Element) i.next();
				String type = element.getName().toUpperCase();
				if (type.equals("XML") || type.equals("SCHEMA")) {
					// ignore
				} else {
					INode c = (INode) createEQClass(file, element, ns);
					if (c != null) {
						set.getTypeManager().add((IClass) c);
					}
				}

			}
			return null;
		} catch (JDOMException e) {
			e.printStackTrace();
			throw new IOException(e.getMessage());
		}
	}

	/**
	 * Method buildObject.
	 * @param parent
	 * @param el
	 * @param ns
	 * @return INode
	 * @deprecated
	 */
	public INode buildObject(UIFileSet set, INode parent, Element el, Namespace ns) {
		INode c = (INode) createEQClass(parent, el, ns);
		if (c != null) {
			set.getTypeManager().add((IClass) c);
		}
		return c;
	}

	/**
	 * Get the file list from EQUI.xml 
	 */
	public String[] getFilesFromEQUI(UIFileSet set) throws FileNotFoundException {
		try {
			String path = set.getDirectory() + File.separator + "EQUI.XML";
			if (new File(path).exists() == false) {
				Debug.println(this, "getFilesFromEQUI.  path doesn't exist: " + path);
				path = DEFAULT_FILE_SET.getDirectory() + File.separator + "EQUI.XML";
			}
			Document doc = builder.build(path);
			Element composite = doc.getRootElement().getChild("Composite");
			List list = composite.getChildren("Include");
			String[] files = new String[list.size()];

			for (int i = 0; i < files.length; i++) {
				files[i] = ((Element) list.get(i)).getText();
				Debug.println(this, "set=" + set.getName() + ", files[" + i + "]=" + files[i]);
			}

			return files;
		} catch (Exception e) {
			e.printStackTrace();
			MessageManager.addMessage(this, e.getMessage());
			return null;
		}
	}

	/**
	 * Load all UI files
	 */
	public UIFileSet[] loadAllUI() throws FileNotFoundException {
		String fname = System.getProperty(FiddleConstants.HOME) + File.separator + "uifiles";
		String dft = System.getProperty(FiddleConstants.HOME) + File.separator + "uifiles\\default";
		File f = new File(fname);
		String[] dirs = f.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return dir.isDirectory();
			}
		});
		Arrays.sort(dirs);
		if (dirs == null) {
			throw new FileNotFoundException("No directories found.");
		}

		UIFileSet[] sets = new UIFileSet[dirs.length];

		for (int i = 0; i < dirs.length; i++) {
			if (dirs[i].equalsIgnoreCase("default")) {
				sets[0] = loadUI(null, dft, "default");
				DEFAULT_FILE_SET = sets[0];
				String temp = dirs[i];
				dirs[i] = dirs[0];
				dirs[0] = temp;
				break;
			}
		}

		for (int i = 1; i < dirs.length; i++) {
			sets[i] = new DefaultUIFileSet();
			sets[i].setTypeManager(new ItemTypeManager());
			sets[i].setName(dirs[i]);
			sets[i].setDirectory(fname + File.separator + dirs[i]);

			if (dft != null) {
				sets[i].getTypeManager().addDefault(sets[i].getTypeManager());
			}

			String[] files = getFilesFromEQUI(sets[i]);

			if (files != null) {
				//	Arrays.sort(files);
				for (int j = 0; j < files.length; j++) {
					UIFile ui = new DefaultUIFile();
					ui.setSet(sets[i]);
					ui.setName(files[j]);
					sets[i].addFile(ui);
				}
			}
		}
		return sets;
	}

	/**
	 * Method loadUI.
	 * @param directory
	 * @return UIFile[]
	 * @deprecated
	 */
	public UIFileSet loadUI(UIFileSet dft, String directory, String name) throws FileNotFoundException {
		UIFileSet set = new DefaultUIFileSet();
		set.setTypeManager(new ItemTypeManager());
		set.setName(name);
		set.setDirectory(directory);

		if (dft != null) {
			set.getTypeManager().addDefault(dft.getTypeManager());
		}

		//		String[] files = getFiles(directory);
		String[] files = getFilesFromEQUI(set);

		if (files != null) {
			//Arrays.sort(files);
			for (int i = 0; i < files.length; i++) {
				UIFile ui = new DefaultUIFile();
				ui.setSet(set);
				ui.setName(files[i]);
				//					parseFile(set, ui);
				set.addFile(ui);
			}
		}
		return set;
	}

	/**
	 * Method createEQClass.
	 * @param parent
	 * @param el
	 * @param n
	 * @return Object
	 */
	public static final Object createEQClass(INode parent, Element el, Namespace n) {
		if (el.getName().equalsIgnoreCase("xml"))
			return null;

		if (el.getName().equalsIgnoreCase("Schema"))
			return null;

		try {
			Object o = createEQClass(parent, extractType(parent, el));
			if (o != null) {
				new XMLDataLoader().load(el, o);
			}
			return o;
		} catch (Exception e) {
			showError(e, el);
			return null;
		}
	}

	/**
	 * Method createEQClass.
	 * @param parent
	 * @param type
	 * @return Object
	 */
	public static final Object createEQClass(INode parent, String type) {
		try {
			String className = "com.barbre.fiddle.io.EQ" + type;
			Object o = Class.forName(className).newInstance();
			if (o != null) {
				connectNodeToParent(parent, o);
				addListeners(parent, o);
			}
			return o;
		} catch (Exception e) {
			showError(e, null);
			return null;
		}
	}

	/**
	 * Method addListeners.
	 * @param parent
	 * @param o
	 */
	private static void addListeners(INode parent, Object o) {
		((EQNode) o).addPropertyChangeListener(IndirectReferenceManager.getInstance());
		((EQNode) o).addPropertyChangeListener(new ChildPropertyListener(parent));
	}

	/**
	 * Method extractType.
	 * @param parent
	 * @param el
	 * @return String
	 */
	private static String extractType(INode parent, Element el) {
		String type = el.getName();

		if (parent != null) {
			PropertyDescriptor[] pd = BeanInfoManager.getBeanInfo(parent.getClass()).getPropertyDescriptors();
			for (int i = 0; i < pd.length; i++) {
				if (pd[i].getName().equalsIgnoreCase(el.getName())) {
					if (pd[i] instanceof IndexedPropertyDescriptor) {
						type = ((IndexedPropertyDescriptor) pd[i]).getIndexedPropertyType().getName();
					} else {
						type = pd[i].getPropertyType().getName();
					}
					int lastPeriod = type.lastIndexOf('.') + 2;
					type = type.substring(lastPeriod);
					break;
				}
			}
		}
		return type;
	}

	/**
	 * Method connectNodeToParent.
	 * @param parent
	 * @param o
	 */
	private static void connectNodeToParent(INode parent, Object o) {
		if (o instanceof INode) {
			parent.addChild((INode) o);
			((INode) o).setParent(parent);
			((INode) o).setFile(parent.getFile());
		}
	}

	/**
	 * Method showError.
	 * @param e
	 * @param el
	 */
	private static final void showError(Exception e, Element el) {
		//		String msg = e.getClass().getName();
		//		msg = msg.substring(msg.lastIndexOf('.') + 1);
		//		msg += ": type=" + el.getName() + " (" + el.getAttributeValue("item") + ")";
		//		JOptionPane.showMessageDialog(null, msg, LoaderFactory.class.getName(), JOptionPane.ERROR_MESSAGE);
		if ((e instanceof ClassNotFoundException) == false)
			e.printStackTrace();
	}

	/**
	 * Method createUIFolder.
	 * @param name
	 * @return boolean
	 */
	public static boolean createUIFolder(String name) {
		String fname = System.getProperty(FiddleConstants.HOME) + File.separator + "uifiles" + File.separator + name;
		try {
			return new File(fname).mkdir();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Save a uifile
	 */
	public void saveUI(UIFile file) /*throws IOException*/ {
		if (file.getSet().getName().equals("default")) {
			JOptionPane.showMessageDialog(null, "You cannot save changes to files in the default folder.", "", JOptionPane.WARNING_MESSAGE);
			return;
		}

		XMLDataCreator xdc = new XMLDataCreator();

		XMLOutputter outputter = new XMLOutputter();
		outputter.setIndent(8);
		outputter.setNewlines(true);

		Element root = new Element("XML");
		root.setAttribute("ID", "EQInterfaceDefinitionLanguage");

		Element schema = new Element("Schema", Namespace.getNamespace("EverQuestData"));
		schema.addNamespaceDeclaration(Namespace.getNamespace("dt", "EverQuestDataTypes"));
		root.addContent(schema);

		Document doc = new Document(root);
		root.addContent(new Comment("| - - - - - - - - - - - - - - - - - - - - - - - - - - - :"));
		root.addContent(new Comment("| Generated by SIDL Fiddle   " + new Date()));
		root.addContent(new Comment("| - - - - - - - - - - - - - - - - - - - - - - - - - - - :"));

		INode[] children = file.getChildren();
		for (int i = 0; i < children.length; i++) {
			MessageManager.addMessage(this, "Saving " + ((IClass) children[i]).getitem());
			try {
				xdc.createXMLData((IClass) children[i], file, doc, root);
			} catch (Exception e) {
				e.printStackTrace();
				Debug.println(this, e.getMessage());
				MessageManager.addMessage(this, e.getMessage());
			}
		}

		try {
			String fname = System.getProperty(FiddleConstants.HOME) + File.separator + "uifiles" + File.separator + file.getSet().getName() + File.separator + file.getName();
			FileOutputStream fos = new FileOutputStream(fname);
			outputter.output(doc, fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Save failed.\n" + e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
		} finally {
			outputter = null;
		}

	}

	/**
	 * Method loadSIDL.
	 */
	public static void loadSIDL(Map map) {
		try {
			String fname = System.getProperty(FiddleConstants.HOME) + File.separator + "uifiles\\default\\SIDL.xml";
			Document doc = builder.build(new File(fname));
			Namespace ns = ((Element) doc.getRootElement().getChildren().get(0)).getNamespace();
			List types = ((Element) doc.getRootElement().getChildren().get(0)).getChildren("ElementType", ns);
			Iterator i = types.iterator();
			while (i.hasNext()) {
				Element element = (Element) i.next();
				map.put(element.getAttributeValue("name"), element);
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method main.
	 * @param arguments
	 */
	public static void main(String[] arguments) {
		try {
			PropertiesLoader.setEQLocation();
			UIFileSet[] set = getInstance().loadAllUI();
			getInstance().parseFile(set[0], (UIFile) set[0].getFiles().get(5));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}