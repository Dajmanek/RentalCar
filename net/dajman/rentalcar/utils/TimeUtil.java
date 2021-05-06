package net.dajman.rentalcar.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeUtil {


    public static String getDate(long time){
        return new SimpleDateFormat("dd.MM.yyyy").format(new Date(time).getTime());
    }

    public static String getHour(final long time){
        return new SimpleDateFormat("HH:mm:ss").format(new Date(time).getTime());
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
            sb.append(days == 1L ? " dzien " : " dni ");
        }
        if (hours > 0L) {
            sb.append(hours);
            final long i = hours % 10L;
            sb.append(hours == 1L ? " godzina " : i <= 1 || i >= 5 || (hours >= 12 && hours <= 14) ? " godzin " : " godziny ");
        }
        if (minutes > 0L) {
            sb.append(minutes);
            final long i = minutes % 10L;
            sb.append(minutes == 1L ? " minuta " : i <= 1 ||  i >= 5 || (minutes >= 12 && minutes <= 14) ? " minut " : " minuty ");
        }
        if (seconds > 0L) {
            sb.append(seconds);
            final long i = seconds % 10L;
            sb.append(seconds == 1L ? " sekunda " : i <= 1 || i >= 5 || (seconds >= 12 && seconds <= 14)? " sekund " : " sekundy ");
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
            sb.append(days).append("d ");
        }
        if (hours > 0L) {
            sb.append(hours).append("h ");
        }
        if (minutes > 0L) {
            sb.append(minutes).append("m ");
        }
        if (seconds > 0L) {
            sb.append(seconds).append("s ");
        }
        return sb.toString();
    }

}
