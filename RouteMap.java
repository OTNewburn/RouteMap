/**
 *
 *title: RouteMap.java
 *description:
 *purpose:
 *
 *
 *author: Owen
 *created: 6 Mar 2015 2015
 *
 */

import java.util.ArrayList;
import java.util.List;

class JunctionNotInRouteMapException extends Exception {}
class NoRoadToGivenJunctionException extends Exception {}
class DuplicateJunctionNameException extends Exception {}

/**
 * Contains all info about the Map including route finding methods
 * stores junctions as an arraylist
 */
public class RouteMap {
	
	/**
	 * Describes a juction as an object with a name and roads to other junctions
	 *
	 */
	class Junction{
		
		/**
		 * points to a junction and has a length property 
		 */
		
		class Road{
			Junction to;
			int length;
			
			Road(Junction to, int length){
				this.to = to;
				this.length = length;
			}
		}
		
			List<Road> roads = new ArrayList<Road>();
			char name;
			int index;
		
			Junction(char name, int index){
				this.name = name;
				this.index=index;
			}
		
			public void addRoad(Junction j, int l){
				roads.add(new Road(j,l));
			}
		
			public Road getRoad(char name) throws NoRoadToGivenJunctionException{
				for (int i=0; i<roads.size(); i++){
					if (roads.get(i).to.name == name){
						return roads.get(i);
					}
				}
				throw new NoRoadToGivenJunctionException();
			}
		}
	
	//This class is used to store data of for a given path whislt working out the shortest
	class Path {
		int length;
		String junctions;
		
		Path(int length, String junctions){
			this.length=length;
			this.junctions = junctions;
		}
	}
	
	List<Junction> junctions;
	
	RouteMap(char[] junctions) throws DuplicateJunctionNameException{
		this.junctions = new ArrayList<Junction>();
		for (int i=0; i<junctions.length; i++){
			for (int j=0; j<i; j++){
				if (junctions[i]==junctions[j]){
					throw new DuplicateJunctionNameException();
				}
			}
			this.junctions.add(new Junction(junctions[i], i));
		}
	}
	
	/**
	 * @param name
	 * @return junction with given name if exists, else throws error
	 * @throws JunctionNotInRouteMap
	 */
	public Junction getJunction(char name) throws JunctionNotInRouteMapException {
		for (int i=0; i<junctions.size(); i++){
			if (junctions.get(i).name == name){
				return junctions.get(i);
			}
		}
		throw new JunctionNotInRouteMapException();
	}
	
	/**
	 * 
	 * @param junctions
	 * @return the length of a specified route
	 * @throws JunctionNotInRouteMapException 
	 * @throws NoRoadToGivenJunctionException 
	 * @throws JunctionNotInRouteMap
	 * @throws NoRoadToGivenJunction
	 */
	public int distanceOfRoute(String junctions) throws NoRoadToGivenJunctionException, JunctionNotInRouteMapException {
		if (junctions.length()==1){
			return 0;
		} else {
			return this.getJunction(junctions.charAt(0)).getRoad(junctions.charAt(1)).length + distanceOfRoute(junctions.substring(1,junctions.length()));
		}
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @return the shortest route, as a path class, between the junctions represented by start and end
	 */
	public Path shortestRoute(char start, char end) {
		Path[] routes = new Path[junctions.size()];
		List<Integer> current = new ArrayList<Integer>();
		try {
			current.add(new Integer(getJunction(start).index));
			return dijkstra(routes, current, getJunction(end).index);
		} catch (JunctionNotInRouteMapException e){
			System.out.println("Tried to access a junction that does not exist");
			return null;
		}
	}
	
	/**
	 * @description my implementation of dijksra's algorithm
	 * @param routes, an a array of paths storing the shortest route to each junction so far
	 * @param current, a list of Integers representing the junctions that where changed in the last iteration. When called current should only contain the index of the start junction
	 * @param end, index of the end junction
	 * @return the path of the shortest route from the specified start and end junctions 
	 */
	public Path dijkstra(Path[] routes, List<Integer> current, int end){
		List<Integer> next = new ArrayList<Integer>();
		
		for(int i=0; i<current.size(); i++){
			
			Junction curJunction = junctions.get((int) current.get(i));
			
			for (int j=0; j<curJunction.roads.size(); j++){
				
				Junction nextJunction = curJunction.roads.get(j).to;
				
				if(routes[nextJunction.index]==null){
					if(routes[curJunction.index]==null){
						routes[nextJunction.index]=new Path(curJunction.roads.get(j).length, "" + curJunction.name + nextJunction.name);
					} else{
						routes[nextJunction.index]=new Path(routes[curJunction.index].length + curJunction.roads.get(j).length, routes[curJunction.index].junctions + nextJunction.name);
					}
					next.add(new Integer(junctions.get((int) current.get(i)).roads.get(j).to.index));
				} else {
					if (routes[nextJunction.index].length > routes[(int) current.get(i)].length + curJunction.roads.get(j).length){
						routes[nextJunction.index].length = routes[(int) current.get(i)].length + curJunction.roads.get(j).length;
						routes[nextJunction.index].junctions = routes[curJunction.index].junctions + nextJunction.name;
						next.add(new Integer(nextJunction.index));
					}
				}
			}
		}
			if((notNull(routes) && isShortest(routes[end], routes) || next.size() == 0)){
				return routes[end];
			} else {
				return dijkstra(routes, next, end);
			}
	}
	
	//Test that no elements of array are null, separated for neatness
	public boolean notNull(Path[] routes){
		boolean test = true;
		for (int i=0; i<routes.length; i++){
			if (routes[i]==null){
				test=false;
			}
		}
		return test;
	}
	
	//checks if path p is shorter than all the pathes in array of paths
	public boolean isShortest(Path p, Path[] routes){
		boolean test = true;
		for (int i=0; i<routes.length; i++){
			if (routes[i].length<p.length){
				test=false;
			}
		}
		return test;
	}
	
	/**
	 * 
	 * @param start, represents starting junction
	 * @param end, represents finishing junciton
	 * @param n, number of junctions allowed in route
	 * @param exact, if true, route must have EXACTLY n junctions, if false, route must have n or fewer junctions
	 * @return array of strings representing all allowed routes from start to end 
	 */
	public String[] routeOfNJunctions(char start, char end, int n, boolean exact){
		List<String> correctRoutes = new ArrayList<String>();
		List<String> allRoutes = new ArrayList<String>();
		allRoutes.add(start+"");
		
		for (int i=0; i<n; i++){
			for (int j=0; j<allRoutes.size(); j++){
				if(allRoutes.get(j).length()==i+1){
					Junction curJunction;
					try {
						curJunction = getJunction(allRoutes.get(j).charAt(allRoutes.get(j).length()-1));
					} catch (JunctionNotInRouteMapException e) {
						System.out.println("Junction entered does not exist");
						return null;
					}
					for (int k=0; k<curJunction.roads.size(); k++){
						Junction nextJunction = curJunction.roads.get(k).to;
						allRoutes.add(allRoutes.get(j) + nextJunction.name);
						if (((! exact) || i==n-1) && nextJunction.name == end){
							correctRoutes.add(allRoutes.get(j) + nextJunction.name);
						}
					}
				}
			}
		}
		String[] cr = new String[correctRoutes.size()];
		cr = correctRoutes.toArray(cr);
		return  cr;
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @param length, maximum allowed length of routes
	 * @return array of strings representing all routes from start to end shorter than length
	 * This function can be used with distanceOfRoute(String juncitons) to get lengths of individual routes
	 */
	public String[] routesShorterThan(char start, char end, int length){
		
		List<String> correctRoutes = new ArrayList<String>();
		List<String> allRoutes = new ArrayList<String>();
		allRoutes.add(start+"");
				
		for (int j=0; j<allRoutes.size(); j++){
			
			Junction curJunction;
			try {
				curJunction = getJunction(allRoutes.get(j).charAt(allRoutes.get(j).length()-1));
			} catch (JunctionNotInRouteMapException e) {
				System.out.println("Junction entered does not exist");
				return null;
			}
			
			for (int k=0; k<curJunction.roads.size(); k++){
				Junction nextJunction = curJunction.roads.get(k).to;
				
				try {
					if (distanceOfRoute(allRoutes.get(j) + nextJunction.name) <= length){
						allRoutes.add(allRoutes.get(j) + nextJunction.name);
						if (nextJunction.name == end){
							correctRoutes.add(allRoutes.get(j) + nextJunction.name);
						}
					}
				} catch (NoRoadToGivenJunctionException	| JunctionNotInRouteMapException e) {
					System.out.println("Access to unkown junciton or road, this should NEVER occur");
				}

			}
		}
		String[] cr = new String[correctRoutes.size()];
		cr = correctRoutes.toArray(cr);
		return  cr;
	}
}