package myData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyArrays {
	
	// toString function for Object arrays.
	public static String arrayToString(Object[] inArray, String delimeter, String leftBracket, String rightBracket) {
		String returnString = leftBracket;
		for (Object obj : inArray) {
			returnString += obj + delimeter;
		}
		return (returnString.substring(0, returnString.length() - delimeter.length()) + rightBracket);
	}
	
	public static Object arrayToString(Object[] inArray, String delimeter) {
		String returnString = "";
		for (Object obj : inArray) {
			returnString += obj + delimeter;
		}
		return (returnString.substring(0, returnString.length() - delimeter.length()));
	}
	
	// toString function for int arrays.
	public static String arrayToString(int[] inArray, String delimeter, String leftBracket, String rightBracket) {
		String returnString = leftBracket;
		for (Object obj : inArray) {
			returnString += obj + delimeter;
		}
		return (returnString.substring(0, returnString.length() - delimeter.length()) + rightBracket);
	}
	
	public static Object arrayToString(int[] inArray, String delimeter) {
		String returnString = "";
		for (Object obj : inArray) {
			returnString += obj + delimeter;
		}
		return (returnString.substring(0, returnString.length() - delimeter.length()));
	}
	
	public static <T> List<T> union(List<T> list1, List<T> list2) {
        Set<T> set = new HashSet<T>();

        set.addAll(list1);
        set.addAll(list2);

        return new ArrayList<T>(set);
    }

    public static <T> List<T> intersection(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<T>();

        for (T t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }
	
}
