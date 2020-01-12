package dataStructure;

import java.util.Collection;
import java.io.Serializable;
import java.util.HashMap;
import java.util.ArrayList;


public class DGraph implements Serializable,graph{

	private static final long serialVersionUID = 1L;
	/* Parameters */
	
	public HashMap<Integer, node_data> nodeMap = new HashMap<Integer, node_data>();
	public HashMap<Integer, HashMap<Integer,edge_data>> edgeMap = new HashMap<Integer, HashMap<Integer,edge_data>>();
	public int MC=0;
	public int edgeCount=0;
	
	/* Contractors */

	public DGraph() {
		this.nodeMap = new HashMap<Integer, node_data>();
		this.edgeMap = new HashMap<Integer, HashMap<Integer,edge_data>>();
		this.edgeCount=0;
		this.MC=0;
	}

	public DGraph(DGraph Graph) {
		this.nodeMap.putAll(Graph.nodeMap);
		this.edgeMap.putAll(Graph.edgeMap);
		this.edgeCount=Graph.edgeCount;
		this.MC=Graph.MC;
	}
	
	/* get / set / Overrides */
	
	@Override
	public node_data getNode(int key) {return (this.nodeMap.get(key));}

	@Override
	public edge_data getEdge(int src, int dest) {
		
		if((this.edgeMap.get(src)!=null) && (this.edgeMap.get(src).get(dest)!=null))
			return(edge_data)(this.edgeMap.get(src).get(dest));
		else
			return null;
	}
	
	@Override
	public int getMC() {return (MC);}

	@Override
	public void addNode(node_data n) {
		
		this.nodeMap.put(n.getKey(), (node)n);
		this.MC++;	
	}
	
	@Override
	public void connect(int src, int dest, double w) {
		
		if((this.nodeMap.get(src))==null || (this.nodeMap.get(dest)==null))
			System.out.println("cant connect this nodes");
		else {
				edge tempEdge = new edge(src,dest,w);
				if(this.edgeMap.get(src)==null){
					this.edgeMap.put(src, new HashMap<Integer,edge_data>());
					this.edgeMap.get(src).put(dest, tempEdge);
					this.MC++;
					edgeCount++;
				}
				else {
					this.edgeMap.get(src).put(dest, tempEdge);
					this.MC++;
					edgeCount++;
			}
		}
		
	}
	
	@Override
	public Collection<node_data> getV() {
		if(this.nodeMap.isEmpty())
			return null;
		else
			return this.nodeMap.values();
	}
	
	@Override
	public Collection<edge_data> getE(int node_id) {
		if((this.edgeMap.get(node_id)==null) || (this.edgeMap.isEmpty()))
			return null;
		else
			return this.edgeMap.get(node_id).values();
	}
	@Override
	public node_data removeNode(int key) {
		
		node_data data = new node((node)nodeMap.get(key));
		ArrayList<Integer> delete = new ArrayList<Integer>();
		
		if (this.nodeMap.get(key)==null) 
			return null;
		
		/* remove edges */
		this.edgeMap.forEach((numKey, val) ->
		{
			if(val.get(key)!=null) {
				val.remove(key);
				this.MC++;
				edgeCount--;
				
				if (val.isEmpty())
					delete.add(numKey);
			}
		});
		
		for (int i : delete)
			this.edgeMap.remove(i);
		edgeCount -= (this.edgeMap.get(key).size());
		this.edgeMap.remove(key);
		this.nodeMap.remove(key);
		this.MC++;

		return data;	
	}

	@Override
	public edge_data removeEdge(int src, int dest) {
		
		edge_data ed = new edge((edge)this.edgeMap.get(src).get(dest));
		
		if((this.edgeMap.get(src).get(dest)==null))
			return null;
		
		this.edgeMap.get(src).remove(dest);
		this.MC++;
		edgeCount--;
		return ed;
	}
	
	@Override
	public int nodeSize() {return(this.nodeMap.size());}
	
	@Override
	public int edgeSize() {return(edgeCount);}
	
	public boolean containNode(int key) {return (this.nodeMap.containsKey(key));}

	public boolean containEdge(int src, int dest) {
		
		if((this.edgeMap.containsKey(src)) && (this.edgeMap.get(src).containsKey(dest)))
				return true;
		else 
			return false;
		
	}
	


}
