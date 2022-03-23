package dk.plexhost.core.message;

import dk.plexhost.core.utils.ColorUtils;
import dk.plexhost.core.utils.NumberUtils;

public class Rainbow {

    static String[] COLORS = "4c6e2ab9d5".split("");

    public static String letters(String text){
        StringBuilder sb = new StringBuilder();
        int current = NumberUtils.generateRandomNumber(0, COLORS.length);
        for(String s : text.split("")){
            if(!s.equalsIgnoreCase(" "))
                current = current < COLORS.length-1 ? current + 1 : 0;
            sb.append(ColorUtils.getColored("§"+COLORS[current])).append(s);
        }
        return sb.toString();
    }

    public static String words(String text){
        StringBuilder sb = new StringBuilder();
        int current = NumberUtils.generateRandomNumber(0, COLORS.length-1);
        for(String s : text.split(" ")){
            current = current < COLORS.length-1 ? current + 1 : 0;
            sb.append(ColorUtils.getColored("§"+COLORS[current])).append(s).append(" ");
        }
        return sb.toString();
    }

}
