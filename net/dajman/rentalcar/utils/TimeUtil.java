package net.dajman.rentalcar.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeUtil {


    public static String getDate(long time){
        return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(time).getTime());
    }

    public static String getDurationBreakdown(long millis) {
        if (millis == 0L) return "0";

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        if (days > 0L) millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        if (hours > 0L) millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        if (minutes > 0L) millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        if (seconds > 0L) millis -= TimeUnit.SECONDS.toMillis(seconds);

        StringBuilder sb = new StringBuilder();
        if (days > 0L) {
            sb.append(days);
            if (days == 1)sb.append(" dzien ");
            else sb.append(" dni ");
        }
        if (hours > 0L) {
            sb.append(hours);
            long i = hours % 10L;
            if (hours == 1L) sb.append(" godzina ");
            else if (hours > 10 && hours < 15) sb.append(" godzin ");
            else if (i < 5L) sb.append(" godziny ");
            else sb.append(" godzin ");
        }
        if (minutes > 0L) {
            sb.append(minutes);
            long i = minutes % 10L;
            if (minutes == 1L) sb.append(" minuta ");
            else if (minutes > 10 && minutes < 15) sb.append(" minut ");
            else if (i < 5L) sb.append(" minuty ");
            else sb.append(" minut ");
        }
        if (seconds > 0L) {
            sb.append(seconds);
            long i = seconds % 10L;
            if (seconds == 1L) sb.append(" sekunda ");
            else if (seconds > 10 && seconds < 15) sb.append(" sekund ");
            else if (i < 5L) sb.append(" sekundy ");
            else sb.append(" sekund ");
        }
        return sb.toString();
    }

    public static String getDurationBreakdownShort(long millis) {
        if (millis == 0L) return "0";

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        if (days > 0L) millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        if (hours > 0L) millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        if (minutes > 0L) millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        if (seconds > 0L) millis -= TimeUnit.SECONDS.toMillis(seconds);

        StringBuilder sb = new StringBuilder();
        if (days > 0L) {
            sb.append(days);
            sb.append("d ");
        }
        if (hours > 0L) {
            sb.append(hours);
            sb.append("h ");
        }
        if (minutes > 0L) {
            sb.append(minutes);
            sb.append("m ");
        }
        if (seconds > 0L) {
            sb.append(seconds);
            sb.append("s ");
        }
        return sb.toString();
    }

}
