package com.leaderli.li.flow.ui;

import org.eclipse.jface.viewers.LabelProvider;

public class ComboBoxPropertySourceLabelProvider extends LabelProvider {

	private String[] values;

	public ComboBoxPropertySourceLabelProvider(String[] values) {
		super();
		this.values = values;
	}

	public String[] getValues() {
		return values;
	}

	public void setValues(String[] values) {
		this.values = values;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof String) {
			return element.toString();
		}
		return this.propIntToString((Integer) element);
	}

	public String propIntToString(Integer index) {
		if (index >= 0 && index < values.length) {
			return this.values[index];
		}
		return "";
	}

	public int propStringToInt(String property) {
		for (int i = 0; i < values.length; i++) {
			if (this.values[i].equals(property)) {
				return i;
			}

		}
		return -1;
	}

}
