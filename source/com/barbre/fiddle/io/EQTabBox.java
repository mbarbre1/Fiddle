
			package com.barbre.fiddle.io;
			
			import com.barbre.fiddle.elements.ITabBox;
			import com.barbre.fiddle.elements.IFrameTemplate;import com.barbre.fiddle.elements.IFrameTemplate;import com.barbre.fiddle.elements.IPage;
			
			/**
			 * Generated by BeanBuilder for SIDL Fiddle
			 */
			public class EQTabBox extends EQControl implements ITabBox {
					private String TabBorderTemplate;
	private IFrameTemplate TabBorderTemplateObject;
	private String PageBorderTemplate;
	private IFrameTemplate PageBorderTemplateObject;
	private String[] Pages;
	private IPage[] PagesObject;

				
				
			public String getTabBorderTemplate() {
				return TabBorderTemplate;
			}
		
			public void setTabBorderTemplate(String value) {
				String old = TabBorderTemplate;
				TabBorderTemplate = value;
				firePropertyChange("TabBorderTemplate", old, TabBorderTemplate);
			}
		
			public IFrameTemplate getTabBorderTemplateObject() {				
				return TabBorderTemplateObject;
			}
		
			public void setTabBorderTemplateObject(IFrameTemplate value) {
				IFrameTemplate old = TabBorderTemplateObject;
				TabBorderTemplateObject = value;
				firePropertyChange("TabBorderTemplateObject", old, TabBorderTemplateObject);
			}
		
			public String getPageBorderTemplate() {
				return PageBorderTemplate;
			}
		
			public void setPageBorderTemplate(String value) {
				String old = PageBorderTemplate;
				PageBorderTemplate = value;
				firePropertyChange("PageBorderTemplate", old, PageBorderTemplate);
			}
		
			public IFrameTemplate getPageBorderTemplateObject() {				
				return PageBorderTemplateObject;
			}
		
			public void setPageBorderTemplateObject(IFrameTemplate value) {
				IFrameTemplate old = PageBorderTemplateObject;
				PageBorderTemplateObject = value;
				firePropertyChange("PageBorderTemplateObject", old, PageBorderTemplateObject);
			}
		
			public String[] getPages() {
				return Pages;
			}
		
			public void setPages(String[] value) {
				String[] old = Pages;
				Pages = value;
				firePropertyChange("Pages", old, Pages);
			}
		
			public String getPages(int index) {
				return Pages[index];
			}
		
			public void setPages(int index, String value) {
				String[] old = Pages;
				Pages[index] = value;
				firePropertyChange("Pages", old, Pages);
			}
		
			public IPage[] getPagesObject() {				
				return PagesObject;
			}
		
			public void setPagesObject(IPage[] value) {
				IPage[] old = PagesObject;
				PagesObject = value;
				firePropertyChange("PagesObject", old, PagesObject);
			}
		
			public IPage getPagesObject(int index) {
				return PagesObject[index];
			}
		
			public void setPagesObject(int index, IPage value) {
				IPage[] old = PagesObject;
				PagesObject[index] = value;
				firePropertyChange("PagesObject", old, PagesObject);
			}
		
			}
		