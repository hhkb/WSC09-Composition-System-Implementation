package ca.concordia.pga.models;

/**
 * The class represents an object with a unique name
 * 
 * @author Ludeng Zhao(Eric)
 * 
 */
public class UniNameObject {

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

}
