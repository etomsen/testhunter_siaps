package it.unibz.testhunter.plugin.hudson;

public class StringHolder {

	private String value;
	
	public StringHolder(String value) {
		this.value = value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
