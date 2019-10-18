
			package com.barbre.fiddle.io;
			
			import com.barbre.fiddle.elements.ISpellGem;
			import com.barbre.fiddle.elements.ISpellGemDrawTemplate;
			
			/**
			 * Generated by BeanBuilder for SIDL Fiddle
			 */
			public class EQSpellGem extends EQControl implements ISpellGem {
					private ISpellGemDrawTemplate SpellGemDrawTemplate;
	private int SpellIconOffsetX;
	private int SpellIconOffsetY;

				
				
			public ISpellGemDrawTemplate getSpellGemDrawTemplate() {
				return SpellGemDrawTemplate;
			}
		
			public void setSpellGemDrawTemplate(ISpellGemDrawTemplate value) {
				ISpellGemDrawTemplate old = SpellGemDrawTemplate;
				SpellGemDrawTemplate = value;
				firePropertyChange("SpellGemDrawTemplate", old, SpellGemDrawTemplate);
			}
		
			public int getSpellIconOffsetX() {
				return SpellIconOffsetX;
			}
		
			public void setSpellIconOffsetX(int value) {
				int old = SpellIconOffsetX;
				SpellIconOffsetX = value;
				firePropertyChange("SpellIconOffsetX", old, SpellIconOffsetX);
			}
		
			public int getSpellIconOffsetY() {
				return SpellIconOffsetY;
			}
		
			public void setSpellIconOffsetY(int value) {
				int old = SpellIconOffsetY;
				SpellIconOffsetY = value;
				firePropertyChange("SpellIconOffsetY", old, SpellIconOffsetY);
			}
		
			}
		