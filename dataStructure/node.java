package dataStructure;

import java.io.Serializable;

import utils.Point3D;

public class node implements Serializable,node_data {

	/* class parameters */
	public static int SerialNum=0;
	private Point3D loction;
	private double nodeWeight;
	private String information;
	private int tag,key;
	private static final long serialVersionUID = 1L;
		
	/* Contractors */
	
	public node () {
		this.key=SerialNum;
		this.information="";
		this.tag=0;
		this.nodeWeight=0;
		SerialNum++;
	}
	
	public node (Point3D loc) {
		this.key=SerialNum;
		this.information="";
		this.loction=loc;
		this.tag=0;
		this.nodeWeight=0;
		SerialNum++;
	}
	
	protected node (node n) {
		this.key=n.key;
		this.tag=n.tag;
		this.nodeWeight=n.nodeWeight;
		this.information=n.information;
		this.loction=n.loction;
	}

	/* get / set  Overrides */
	
	
	@Override
	public Point3D getLocation() {return (this.loction);}
	
	@Override
	public double getWeight() {return (this.nodeWeight);}
	
	@Override
	public String getInfo() {return (this.information);}
	
	@Override
	public int getKey() {return (this.key);}

	@Override
	public int getTag() {return (this.tag);}

	@Override
	public void setLocation(Point3D p) {this.loction = p;}

	@Override
	public void setWeight(double w) {this.nodeWeight = w;}

	@Override
	public void setInfo(String str) {this.information = str;}
	
	@Override
	public void setTag(int tag) {this.tag = tag;}

}