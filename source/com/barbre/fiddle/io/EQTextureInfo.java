
			package com.barbre.fiddle.io;
			
			import com.barbre.fiddle.elements.ITextureInfo;
			import com.barbre.fiddle.elements.ISize;
			
			/**
			 * Generated by BeanBuilder for SIDL Fiddle
			 */
			public class EQTextureInfo extends EQClass implements ITextureInfo {
					private ISize Size;

				
				
			public ISize getSize() {
				return Size;
			}
		
			public void setSize(ISize value) {
				ISize old = Size;
				Size = value;
				firePropertyChange("Size", old, Size);
			}
		
			}
		