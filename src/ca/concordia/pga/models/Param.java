package ca.concordia.pga.models;
/**
 * This class represents web service parameter
 * @author Ludeng Zhao(Eric)
 *
 */
public class Param extends UniNameObject {

	private Thing thing;
	
	public Param(String name) {
		super(name);
	}

	public Thing getThing() {
		return thing;
	}

	public void setThing(Thing thing) {
		this.thing = thing;
	}
	
	

}
