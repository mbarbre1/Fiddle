
			package com.barbre.fiddle.io;
			
			import com.barbre.fiddle.elements.IStaticFrame;
			import com.barbre.fiddle.elements.IFrameTemplate;
			
			/**
			 * Generated by BeanBuilder for SIDL Fiddle
			 */
			public class EQStaticFrame extends EQStaticScreenPiece implements IStaticFrame {
					private String FrameTemplate;
	private IFrameTemplate FrameTemplateObject;

				
				
			public String getFrameTemplate() {
				return FrameTemplate;
			}
		
			public void setFrameTemplate(String value) {
				String old = FrameTemplate;
				FrameTemplate = value;
				firePropertyChange("FrameTemplate", old, FrameTemplate);
			}
		
			public IFrameTemplate getFrameTemplateObject() {				
				return FrameTemplateObject;
			}
		
			public void setFrameTemplateObject(IFrameTemplate value) {
				IFrameTemplate old = FrameTemplateObject;
				FrameTemplateObject = value;
				firePropertyChange("FrameTemplateObject", old, FrameTemplateObject);
			}
		
			}
		