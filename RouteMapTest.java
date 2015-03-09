/**
 *
 *title: RouteMapTest.java
 *description:
 *purpose:
 *
 *
 *author: Owen
 *created: 8 Mar 2015 2015
 *
 */

public class RouteMapTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		char[] junctions = {'A','B','C','D','E'};
		RouteMap map;
		try {
			map = new RouteMap(junctions);
		} catch (DuplicateJunctionNameException e) {
			System.out.println("Tried to add two junctions with the same name");
			return;
		}
		
		try {
			map.getJunction('A').addRoad(map.getJunction('B'),5);
			map.getJunction('A').addRoad(map.getJunction('D'),5);
			map.getJunction('A').addRoad(map.getJunction('E'),7);
			map.getJunction('B').addRoad(map.getJunction('C'),4);
			map.getJunction('C').addRoad(map.getJunction('D'),7);
			map.getJunction('C').addRoad(map.getJunction('E'),2);
			map.getJunction('D').addRoad(map.getJunction('C'),8);
			map.getJunction('D').addRoad(map.getJunction('E'),6);
			map.getJunction('E').addRoad(map.getJunction('B'),3);
		} catch (JunctionNotInRouteMapException e) {
			System.out.println("Tried to access a junction that does not exist");
		}
		
		try {
			System.out.println("The route A-B-C is length "+map.distanceOfRoute("ABC"));
		} catch (NoRoadToGivenJunctionException e) {
			System.out.println("NO SUCH ROUTE");
		} catch (JunctionNotInRouteMapException e) {
			System.out.println("TRIED TO ACCESS JUNCTION WHICH DOES NOT EXIST");
		}

		try {
			System.out.println("The route A-D is length "+map.distanceOfRoute("AD"));
		} catch (NoRoadToGivenJunctionException e) {
			System.out.println("NO SUCH ROUTE");
		} catch (JunctionNotInRouteMapException e) {
			System.out.println("TRIED TO ACCESS JUNCTION WHICH DOES NOT EXIST");
		}

		try {
			System.out.println("The route A-D-C is length "+map.distanceOfRoute("ADC"));
		} catch (NoRoadToGivenJunctionException e) {
			System.out.println("NO SUCH ROUTE");
		} catch (JunctionNotInRouteMapException e) {
			System.out.println("TRIED TO ACCESS JUNCTION WHICH DOES NOT EXIST");
		}

		try {
			System.out.println("The route A-E-B-C-D is length "+map.distanceOfRoute("AEBCD"));
		} catch (NoRoadToGivenJunctionException e) {
			System.out.println("NO SUCH ROUTE");
		} catch (JunctionNotInRouteMapException e) {
			System.out.println("TRIED TO ACCESS JUNCTION WHICH DOES NOT EXIST");
		}

		try {
			System.out.println("The route A-E-D is length "+map.distanceOfRoute("AED"));
		} catch (NoRoadToGivenJunctionException e) {
			System.out.println("NO SUCH ROUTE");
		} catch (JunctionNotInRouteMapException e) {
			System.out.println("TRIED TO ACCESS JUNCTION WHICH DOES NOT EXIST");
		}

		
		String[] routes = map.routeOfNJunctions('C','C',3,false);
		System.out.print("The routes from C-C with max 3 junctions are: ");
		for (String route : routes){
			System.out.print(route + ", ");
		}
		System.out.println("Total: " + routes.length);
		
		routes = map.routeOfNJunctions('A','C',4,true);
		System.out.print("The routes from A-C with exactly 4 junctions are: ");
		for (String route : routes){
			System.out.print(route + ", ");
		}
		System.out.println("Total: " + routes.length);
		
		System.out.println("The Shortest route from A - C is " + map.shortestRoute('A', 'C').junctions + " which is length " + map.shortestRoute('A', 'C').length);
		
		System.out.println("The Shortest route from B - B is " + map.shortestRoute('B', 'B').junctions + " which is length " + map.shortestRoute('B', 'B').length);
		
		routes = map.routesShorterThan('C','C',30);
		System.out.print("The routes from C-C shorter than 30 are: ");
		for (String route : routes){
			System.out.print(route + ", ");
		}
		System.out.println("Total: " + routes.length);
	}

}
