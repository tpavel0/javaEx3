package dataStructure;

import java.io.Serializable;

public class edge implements Serializable,edge_data {

	/* class parameters */
	private String information;
	private double EdgeWieght;
	private int src, dest, tag;
	private static final long serialVersionUID = 1L;

	/* Contractors */
	
	public edge(int src, int dest, double w) {
		this.information="";
		this.src=src;
		this.dest=dest;
		this.tag=0;
		this.EdgeWieght=w;
	}

	public edge(edge o) {
		this.information=o.information;
		this.src=o.src;
		this.dest=o.dest;
		this.tag=o.tag;
		this.EdgeWieght=o.EdgeWieght;
	}

	/* get / set  Overrides */
	
	@Override
	public String getInfo() {return (this.information);}
	
	@Override
	public int getSrc() {return (this.src);}

	@Override
	public int getDest() {return (this.dest);}

	@Override
	public double getWeight() {return (this.EdgeWieght);}

	@Override
	public int getTag() {return (this.tag);}
	
	@Override
	public void setInfo(String str) {this.information = str;}

	@Override
	public void setTag(int tag) {this.tag = tag;}

}