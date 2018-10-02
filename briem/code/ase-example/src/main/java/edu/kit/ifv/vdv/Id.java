package edu.kit.ifv.vdv;

public class Id {

	private final String prefix;
	private final int id;

	public Id(String prefix, int id) {
		super();
		this.prefix = prefix;
		this.id = id;
	}
	
	@Override
	public String toString() {
		return prefix + id();
	}

	public String id() {
		return String.valueOf(id);
	}

}
