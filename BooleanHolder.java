package it.unibz.testhunter.plugin.hudson;

public class BooleanHolder {
	
	private boolean value;
	
	public BooleanHolder(boolean value) {
		this.setValue(value);
	}

	public void setValue(boolean value) {
		this.value = value;
	}

	public boolean getValue() {
		return value;
	}

}
