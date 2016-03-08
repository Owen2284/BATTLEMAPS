import java.util.Random;
import java.util.ArrayList;

public class Test {

    public static void main(String[] args) {

        Random x = new Random();
        NameGenerator name = new NameGenerator();

        System.out.println("--- " + args[0] + " ---");

        // Test code for the City class.
        if (args[0].equals("City")) {

            City c1 = new City("C001",40,50);
            City c2 = new City("C002",10,0,"Southtown");
            City c3 = new City("C003",30,20,"Eastburg",20,20);

            // City data and structure tests.
            System.out.println(c1.getID());
            System.out.println(c2.getX());
            System.out.println(c3.getY());
            System.out.println(c1.getName());
            System.out.println(c2.getName());
            System.out.println(c3.getName());
            System.out.println(c3.getLength());
            System.out.println(c3.getWidth());
            System.out.println(c2.getLength());
            System.out.println(c1.getBlock("B015"));
            System.out.println(c3.getBlock(4,3));
            ArrayList<Block> testStore = c2.getGrid();

            c2.setID("C004");
            c2.setX(-123);
            c2.setY(123);
            System.out.println(c2);
            c1.setName("Northcity");
            c1.setLength(10);
            c1.setWidth(20);
            System.out.println(c1);
            System.out.println(c1.getLength());
            System.out.println(c1.getWidth());

            // City stat value tests.


        // Test code for the Map class.
        } else if (args[0].equals("Map")) {

            Map newMap = new Map(10, 1000, 1000);
            System.out.println(newMap.toString());

        // Test code for the Route class.
        } else if (args[0].equals("Route")) {

            Route r1 = new Route("City 1", "City 2");
            Route r2 = new Route("City 3", "City 2");
            Route r3 = new Route("City 2", "City 4");

            System.out.println(r2.getPoints()[0] + " to " + r2.getPoints()[1]);
            System.out.println(r1.getStart());
            System.out.println(r3.getEnd());
            System.out.println(r1.getDestination("City 1"));
            System.out.println(r1.getDestination("City 2"));
            System.out.println(r1.getDestination("City 3"));
            System.out.println(r2.isConnected("City 3"));
            System.out.println(r2.isConnected("City 4"));
            System.out.println(r3.isConnected("City 4", "City 2"));
            System.out.println(r3.isConnected("City 2", "City 4"));
            System.out.println(r3.isConnected("City 2", "City 1"));

            System.out.println(r1.getType());
            r1.setType("Embargo'd");
            System.out.println(r1.getType());

        // Test code for the Names.
        } else if (args[0].equals("Names")) {

            for (int i = 0; i < 10; ++i) {
                System.out.println(name.newName(2));
            }

        // Building tests.
        } else if (args[0].equals("Building")) {

            int[] s1 = {10,5,0,3,2,1};
            int[] s2 = {0,3,4,5,6,0};
            int[] s3 = {9,8,7,6,5,4};

            Building bld1 = new Building("Peaceful Manor", IDCreate.generateID("BLD", 237), "Small House", "Residential", "", "1x1:1", -1, 10, s1);
            Building bld2 = new Building("Large Barracks", IDCreate.generateID("BLD", 94), "Large Barracks", "Military", "", "3x3:111101101", -3, 50, s2);
            Building bld3 = new Building("Unstable-Brand Science Labs", IDCreate.generateID("BLD", 8), "Small Lab", "Science", "", "1x2:11", -3, 5, s3);

            System.out.println(bld1 + "\n");
            System.out.println(bld2 + "\n");
            System.out.println(bld3 + "\n");

        } else if (args[0].equals("BuildingFactory")) {

            BuildingFactory bf = new BuildingFactory();

            System.out.println(bf);
            System.out.println(bf.getBuilding("Small Estate"));
            System.out.println(bf.getCategory("RES"));
            System.out.println(bf.getCategory("HAP"));

            Building t1 = bf.getBuilding("Bunker");
            t1.setName("Test 1");
            Building t2 = new Building(t1);
            t2.setName("Test 2");
            Building t3 = bf.getBuilding("Bunker");
            t3.setName("Test 3");

            System.out.println(bf.getBuilding("Bunker"));
            System.out.println(t1);
            System.out.println(t2);
            System.out.println(t3);

        }

    }

}