package de.fisp.anwesenheit.core.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Klasse zur Darstellung von "Label" "Value" Strukturen, wie sie von
 * JQuery-Autocompletern benutzt werden
 * 
 */
public class LabelValue {
	private String label;
	private String value;

	public LabelValue(String label, String value) {
		this.label = label;
		this.value = value;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
