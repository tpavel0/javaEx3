package algorithms;

import java.io.Serializable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import dataStructure.*;

/**
 * This empty class represents the set of graph-theory algorithms
 * which should be implemented as part of Ex2 - Do edit this class.
 *
 */
public class Graph_Algo implements graph_algorithms, Serializable{

	private static final long serialVersionUID = 1L;
	
	private DGraph graph;

	public Graph_Algo() {
		this.graph=new DGraph();
		}

	public Graph_Algo(graph gr) {
		this.graph=(DGraph)gr;
		}
	
	@Override
	public void init(graph g) {
		if(g instanceof DGraph)
			this.graph=(DGraph)g;
		else  
			throw new RuntimeException("Error initialzing the graph");	
	}

	@Override
	public void init(String file_name) {
		try {
			ObjectInputStream input=new ObjectInputStream(new FileInputStream(file_name));
			graph ngraph = (graph) input.readObject(); 
			init(ngraph);
			input.close();
		}
		catch(Exception e) {throw new RuntimeException("Error loading from the file");}	
	}

	@Override
	public void save(String file_name) {
		try {
			OutputStream streamOut = new FileOutputStream(file_name);
			ObjectOutputStream fileObjectOut = new ObjectOutputStream(streamOut);
			fileObjectOut.writeObject(graph);
			fileObjectOut.close();
			streamOut.close();
		}
		catch(Exception e) {
			throw new RuntimeException("Error in save");
		}	
	}

	public boolean isConnected() {
		DGraph grtemp = (DGraph) this.copy();
		
		if ((grtemp.nodeMap.size()<2) || (grtemp.edgeSize()<2) ) {return false;}
		
		for(int numkey:grtemp.nodeMap.keySet()) 
			grtemp.nodeMap.get(numkey).setTag(0);
		
		Collection<node_data> nodes = grtemp.getV();
		for(node_data d1:nodes) {
			DFSalgo(d1); 
			break;
		}
		
		for (node_data d2 : nodes) {
			if (d2.getTag() == 0)
				return false;
		}
		
		viceVersa(grtemp);
		
		for(int numkey:grtemp.nodeMap.keySet())
			grtemp.nodeMap.get(numkey).setTag(0);
		
		Collection<node_data> nodes2 = grtemp.getV();
		for(node_data d3:nodes2) {
			DFSalgo(d3); 
			break;
		}
		
		for (node_data node1 : nodes2) {
			if (node1.getTag() == 0)
				return false;
		}
		
		return true;
	}

	@Override
	public double shortestPathDist(int src, int dest) {
		try {
			dijakstraAlgo(src);
			return graph.getNode(dest).getWeight();
		} 
		catch (NullPointerException e) {return -1;}
	}
	
	@Override
	public List<node_data> shortestPath(int src, int dest) {
		dijakstraAlgo(src);

		List<node_data> ds = new ArrayList<node_data>();
		node_data temp = graph.getNode(dest);
		while((temp.getInfo().isEmpty()==false)||(temp.getKey()==graph.getNode(src).getKey()))	{
			ds.add(0, temp);
			temp = graph.getNode(Integer.parseInt(temp.getInfo()));
			if(temp.getKey()==graph.getNode(src).getKey())
				break;
		}
		ds.add(0, temp);
		return ds;
	}

	@Override
	public List<node_data> TSP(List<Integer> targets) { 
		if(targets.size()==0)
			throw new RuntimeException("Empty list of targets");
		
		ArrayList<node_data> ds = new ArrayList<node_data>();
		if(targets.size()==1) {
			ds.add(graph.getNode(targets.get(0)));
			return ds;
		}
		/* all nodes in the graph */
		Collection<node_data> grNodes = graph.getV(); 
		/* ArrayList target nodes  */
		ArrayList<node_data> targetNodes = new ArrayList<node_data>();
		/* all nodes */
		ArrayList<node_data> allNodes = new ArrayList<node_data>();
		
		for(node_data nd:grNodes) {
			nd.setTag(0);
			allNodes.add(nd);
			if(targets.contains(nd.getKey()))
				targetNodes.add(nd);
		}

		for(int i=1;i<Integer.MAX_VALUE;i++) {
			for(int j=1;j<targetNodes.size();j++) {
				ds.addAll(isPathExist(targetNodes.get(j), targetNodes.get(j+1)));
				targetNodes.get(j).setTag(1);
				targetNodes.get(j+1).setTag(1);
			}
			if(nodesVisted(targetNodes)) break;
			Collections.shuffle(targetNodes);
		}
		if(ds.size()<2) return null;
		RemoveDuplicate(ds);
		return ds;	
	}

	@Override
	public graph copy() {return new DGraph(this.graph);}
	
	/**
	 * famous DFS algorithm.
	 * used for traversing a graph.
	 * for further reading:
	 * https://en.wikipedia.org/wiki/Depth-first_search
	 * @param node
	 */
	private void DFSalgo(node_data n) {
		n.setTag(1);
		Collection<edge_data> edgeC = this.graph.getE(n.getKey());
		if (edgeC != null) {
			for (edge_data edge : edgeC) {
				if ((graph.getNode(edge.getDest()).getTag() == 0)
					 &&(graph.getNode(edge.getDest()) != null)
					 &&(graph.getE(edge.getDest()) != null))
					DFSalgo(graph.getNode(edge.getDest()));	
			}
		}
	}
	/* replace dest with source and vase versa for each edge*/
	public void viceVersa (graph g) {
		Collection<node_data> d = graph.getV();
		for(node_data srcNode:d) {
			
			Collection<edge_data> de = graph.getE(srcNode.getKey());
			for(edge_data edge:de) {
				
				edge e = new edge((edge)edge);
				int edgetag = e.getTag();
				de.remove(e);
				graph.connect(edge.getDest(), edge.getSrc(), edge.getWeight());
				graph.getEdge(edge.getDest(), edge.getSrc()).setTag(edgetag);
			}
		}	
	}
	/**
	 * Dijkstra algo for calculating paths from selected node to all other nodes in graph
	 * https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
	 */
	public void dijakstraAlgo(int src) {

		List<node_data>  notvisited = new LinkedList<node_data>();
		Collection<node_data> n = graph.getV(); 
		for(node_data ndata:n) notvisited.add(ndata);
		List<node_data>  visited = new LinkedList<node_data>();

		for(int key:this.graph.nodeMap.keySet())	{
			this.graph.nodeMap.get(key).setWeight(Double.MAX_VALUE);		
		}

		this.graph.nodeMap.get(src).setWeight(0);

		node_data tempNode = graph.nodeMap.get(src);

		while(!notvisited.isEmpty()||IsInfi(graph)||tempNode!=null) {

			visited.add(tempNode);

			notvisited.remove(tempNode);

			/* edges start in tempNode */ 
			Collection<edge_data> e = graph.getE(tempNode.getKey());
			
			/*no edges */
			if(e==null) {
				tempNode = NotVisitedMin(notvisited);
				if(tempNode==null) break;
				continue;
			}
			for(edge_data edge:e) {
				/*neighbor of the tempNode */
				node_data destn = graph.getNode(edge.getDest());
				if(tempNode.getWeight()+edge.getWeight()<=graph.nodeMap.get(destn.getKey()).getWeight()) { 
					graph.nodeMap.get(destn.getKey()).setWeight(edge.getWeight()+tempNode.getWeight()); 
					destn.setInfo(""+tempNode.getKey());
				}
			}
			tempNode = NotVisitedMin(notvisited);	
			if(tempNode==null) break;
		}
		return;
	}
	
	public node_data NotVisitedMin(List<node_data> ds) {
		if(ds.isEmpty())
			return null;
	
		node_data d=null;
		for(int i=0; i<ds.size();i++) {
			if(ds.get(i).getWeight()<Double.MAX_VALUE) {d=ds.get(i);}
		}
		return d;
	}
	
	public boolean IsInfi(graph gr) {
		Collection<node_data> nodes = gr.getV();
		for(node_data d : nodes) 
			if(d.getWeight()==Double.MAX_VALUE) return true;
		return false;
	}
	
	private boolean nodesVisted(List<node_data> list) {
		for(node_data d:list)
			if(d.getTag()==0) {return false;}
		return true;
	}
	
	private List<node_data> isPathExist(node_data d, node_data d2) {return (shortestPath(d.getKey(), d2.getKey()));}
	
	private void RemoveDuplicate(ArrayList<node_data> ds) {
		for(int i=1;i<ds.size();i++) 
			if(ds.get(i-1)==ds.get(i)) {ds.remove(i-1);}	
	}
	
	

}
