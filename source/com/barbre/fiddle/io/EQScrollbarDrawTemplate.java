
			package com.barbre.fiddle.io;
			
			import com.barbre.fiddle.elements.IScrollbarDrawTemplate;
			import com.barbre.fiddle.elements.IButtonDrawTemplate;import com.barbre.fiddle.elements.IButtonDrawTemplate;import com.barbre.fiddle.elements.IFrameTemplate;import com.barbre.fiddle.elements.IRGB;
			
			/**
			 * Generated by BeanBuilder for SIDL Fiddle
			 */
			public class EQScrollbarDrawTemplate extends EQClass implements IScrollbarDrawTemplate {
					private IButtonDrawTemplate UpButton;
	private IButtonDrawTemplate DownButton;
	private IFrameTemplate Thumb;
	private String MiddleTextureInfo;
	private IRGB MiddleTint;

				
				
			public IButtonDrawTemplate getUpButton() {
				return UpButton;
			}
		
			public void setUpButton(IButtonDrawTemplate value) {
				IButtonDrawTemplate old = UpButton;
				UpButton = value;
				firePropertyChange("UpButton", old, UpButton);
			}
		
			public IButtonDrawTemplate getDownButton() {
				return DownButton;
			}
		
			public void setDownButton(IButtonDrawTemplate value) {
				IButtonDrawTemplate old = DownButton;
				DownButton = value;
				firePropertyChange("DownButton", old, DownButton);
			}
		
			public IFrameTemplate getThumb() {
				return Thumb;
			}
		
			public void setThumb(IFrameTemplate value) {
				IFrameTemplate old = Thumb;
				Thumb = value;
				firePropertyChange("Thumb", old, Thumb);
			}
		
			public String getMiddleTextureInfo() {
				return MiddleTextureInfo;
			}
		
			public void setMiddleTextureInfo(String value) {
				String old = MiddleTextureInfo;
				MiddleTextureInfo = value;
				firePropertyChange("MiddleTextureInfo", old, MiddleTextureInfo);
			}
		
			public IRGB getMiddleTint() {
				return MiddleTint;
			}
		
			public void setMiddleTint(IRGB value) {
				IRGB old = MiddleTint;
				MiddleTint = value;
				firePropertyChange("MiddleTint", old, MiddleTint);
			}
		
			}
		