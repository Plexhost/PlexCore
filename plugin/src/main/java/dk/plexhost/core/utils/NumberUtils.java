package dk.plexhost.core.utils;

import java.util.ArrayList;
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


}
