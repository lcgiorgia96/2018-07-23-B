package it.polito.tdp.newufosightings.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;

public class Model {

	Graph<State,DefaultWeightedEdge> grafo;
	NewUfoSightingsDAO dao;
	List<State> stati;
	Map<String,State> idMap;
	
	public Model() {
		dao = new NewUfoSightingsDAO();
		
	}
	public void creaGrafo(int giorni, int anno) {
	
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		stati = dao.getStati(anno);
		idMap = new HashMap<>();
		for (State s: stati) {
			idMap.put(s.getId(), s);
		}
		
		Graphs.addAllVertices(this.grafo, stati);
		
		System.out.println(grafo.vertexSet().size());
		
		List<Vicini> vicini = dao.getVicini(idMap,giorni,anno);
		
		for (Vicini v: vicini) {
			 State s1 = v.getS1();
			 State s2 = v.getS2();
			 int peso = v.getPeso();
			 
			 Graphs.addEdge(this.grafo, s1, s2, peso);
		}
		
		System.out.println(grafo.edgeSet().size());
	}

	public List<State> getStati() {
		
		return stati;
	}

	public int getTotPesi(State s) {
		int somma = 0;
		List<State> adiacenti = Graphs.neighborListOf(this.grafo, s);
		
		for (State s1: adiacenti) {
			somma+=grafo.getEdgeWeight(grafo.getEdge(s, s1));
		}
		return somma;
	}

}
