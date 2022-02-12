package dk.plexhost.core.utils;

import java.util.Collection;
import java.util.Random;

public class NumberUtils {

    public static int generateRandomNumber(int minimum, int maximum){
        Random rand = new Random();
        return minimum + rand.nextInt((maximum - minimum) + 1);
    }

    public static int sum(int... ints){
        int sum = 0;
        for(int i : ints) sum += i;
        return sum;
    }

    public static int sum(Collection<Integer> integers){
        int sum = 0;
        for(int i : integers) sum += i;
        return sum;
    }

    public static boolean isFloat(String value){
        if(value == null || value.equals("")) return false;
        try{
            Float.parseFloat(value);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    public static boolean isDouble(String value){
        if(value == null || value.equals("")) return false;
        try{
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    public static boolean isInteger(String value){
        if(value == null || value.equals("")) return false;
        try{
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

}
