package com.barbre.fiddle.actions;

import java.awt.event.ActionEvent;
import java.util.Hashtable;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.io.loaders.ImageLoader;
import com.barbre.fiddle.io.loaders.PropertiesLoader;
import com.barbre.fiddle.widgets.utility.IconFactory;

public class ActionGammaCorrection extends AbstractAction implements ChangeListener {
	private JLabel valueLabel = new JLabel();
	private JSlider slider = new JSlider(-100, 100);
	private JPanel panel = new JPanel();

	/**
	 * Constructor for ActionGammaCorrection.
	 */
	public ActionGammaCorrection() {
		super("Gamma Correction...");
		putValue(FiddleConstants.LARGE_ICON, IconFactory.getIcon(IconFactory.EMPTY));
		putValue(SMALL_ICON, IconFactory.getIcon(IconFactory.EMPTY, IconFactory.SIZE_16));

		Hashtable labelTable = new Hashtable();
		labelTable.put(new Integer(-100), new JLabel("-10.0"));
		labelTable.put(new Integer(10), new JLabel("1.0"));
		labelTable.put(new Integer(100), new JLabel("-10.0"));
		slider.setLabelTable(labelTable);
		slider.setPaintLabels(true);

		panel.add(slider);
		panel.add(valueLabel);
		slider.addChangeListener(this);
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent evt) {
		String gc = PropertiesLoader.getProperty(FiddleConstants.GAMMA_CORRECTION);
		if ((gc == null) || (gc.trim().length() == 0))
			gc = "1.0";

		double d = Double.parseDouble(gc);
		slider.setValue((int) (d * 10));

		JOptionPane.showMessageDialog(null, panel, "Gamma Correction", JOptionPane.INFORMATION_MESSAGE);

		try {
			ImageLoader.setGamma(slider.getValue() / 10.0);
//			System.out.println(((double)slider.getValue()) / 10.0);
			PropertiesLoader.setProperty(FiddleConstants.GAMMA_CORRECTION, "" + ((double)slider.getValue() / 10.0));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see javax.swing.event.ChangeListener#stateChanged(ChangeEvent)
	 */
	public void stateChanged(ChangeEvent e) {
		valueLabel.setText("" + (slider.getValue() / 10.0));
	}

	/**
	 * Method main.
	 * @param args
	 */
	public static void main(String[] args) {
		PropertiesLoader.setEQLocation();
		new ActionGammaCorrection().actionPerformed(new ActionEvent("", 0, ""));
		System.exit(0);
	}

}
