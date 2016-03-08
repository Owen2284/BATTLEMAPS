/*
 *  IDCreate.java
 *
 *  Class used to create IDs for other objects.
 *
 */

package myGame;

public class IDCreate {

    public static String generateID(String start, int n) {

        String tempID = start;
        if (n <= 99) {
            tempID += "0";
        }
        if (n <= 9) {
            tempID += "0";
        }
        tempID += Integer.toString(n);

        return tempID;

    }

}