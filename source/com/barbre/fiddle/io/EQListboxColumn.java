
			package com.barbre.fiddle.io;
			
			import com.barbre.fiddle.elements.IListboxColumn;
			import com.barbre.fiddle.elements.IFrameTemplate;
			
			/**
			 * Generated by BeanBuilder for SIDL Fiddle
			 */
			public class EQListboxColumn extends EQClass implements IListboxColumn {
					private String Header;
	private IFrameTemplate HeaderObject;
	private String Heading;
	private int Width;
	private boolean Sortable;
	private String DataType;

				
				
			public String getHeader() {
				return Header;
			}
		
			public void setHeader(String value) {
				String old = Header;
				Header = value;
				firePropertyChange("Header", old, Header);
			}
		
			public IFrameTemplate getHeaderObject() {				
				return HeaderObject;
			}
		
			public void setHeaderObject(IFrameTemplate value) {
				IFrameTemplate old = HeaderObject;
				HeaderObject = value;
				firePropertyChange("HeaderObject", old, HeaderObject);
			}
		
			public String getHeading() {
				return Heading;
			}
		
			public void setHeading(String value) {
				String old = Heading;
				Heading = value;
				firePropertyChange("Heading", old, Heading);
			}
		
			public int getWidth() {
				return Width;
			}
		
			public void setWidth(int value) {
				int old = Width;
				Width = value;
				firePropertyChange("Width", old, Width);
			}
		
			public boolean getSortable() {
				return Sortable;
			}
		
			public void setSortable(boolean value) {
				boolean old = Sortable;
				Sortable = value;
				firePropertyChange("Sortable", old, Sortable);
			}
		
			public String getDataType() {
				return DataType;
			}
		
			public void setDataType(String value) {
				String old = DataType;
				DataType = value;
				firePropertyChange("DataType", old, DataType);
			}
		
			}
		