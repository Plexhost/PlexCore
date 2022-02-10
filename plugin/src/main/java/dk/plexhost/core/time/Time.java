package dk.plexhost.core.time;
import dk.plexhost.core.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Time {


    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    public static long currentUnixInSeconds(){
        return System.currentTimeMillis()/1000;
    }
    public static String getFormattedTime(long n){
        return getFormattedTime((int)n, true);
    }
    public static String getFormattedTime(long n, boolean withSeconds){
        return getFormattedTime((int)n, withSeconds);
    }
    public static String getFormattedTime(int n){
        return getFormattedTime(n, true);
    }

    public static String getFormattedTime(int n, boolean withSeconds){
        if(n < 0){
            return "0 sekunder";
        } else{
            int days, hours, minutes, seconds;
            days = (int) Math.floor( n / 86400 );
            hours = (int) Math.floor( ( n / 3600 ) - ( days * 24 ) );
            minutes = (int) Math.floor( ( n / 60 ) - ( ( hours + ( days * 24 ) ) * 60 ) );
            seconds = (int) Math.floor( n - ( ( days * 86400 ) + ( hours * 3600 ) + ( minutes * 60 ) ) );
            List<String> stringList = new ArrayList<>();
            if(days > 0)
                stringList.add(days + " " + (days == 1 ? "dag" : "dage"));
            if(hours > 0)
                stringList.add(hours + " " + (hours == 1 ? "time" : "timer"));
            if(minutes > 0)
                stringList.add(minutes + " " + (minutes == 1 ? "minut" : "minutter"));
            if(withSeconds && seconds > 0)
                stringList.add(seconds + " " + (seconds == 1 ? "sekund" : "sekunder"));
            else if((days+hours+minutes) <= 0 && seconds > 0)
                stringList.add(seconds + " " + (seconds == 1 ? "sekund" : "sekunder"));
            return StringUtils.formatList(stringList, "");
        }
    }

    public static String formatTimestampAsDate(long paramLong){
        return SIMPLE_DATE_FORMAT.format(paramLong);
    }

}
