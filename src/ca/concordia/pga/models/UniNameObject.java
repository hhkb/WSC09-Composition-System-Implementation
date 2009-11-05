package ca.concordia.pga.models;

/**
 * The class represents an object with a unique name
 * 
 * @author Ludeng Zhao(Eric)
 * 
 */
public class UniNameObject extends Object implements Comparable<UniNameObject>{

	private String name;

	UniNameObject(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (UniNameObject.class.isAssignableFrom(o.getClass())) {
			UniNameObject n = (UniNameObject) o;
			return this.name.equals(n.getName());
		} else {
			return this.equals(o);
		}
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
	
	@Override
	public String toString(){
		return name;
	}

	@Override
	public int compareTo(UniNameObject o) {
		Integer v1 = Integer.parseInt(this.name.replaceAll("\\D+", ""));
		Integer v2 = Integer.parseInt(o.name.replaceAll("\\D+", ""));
	
		return v1.compareTo(v2);
	}


}
