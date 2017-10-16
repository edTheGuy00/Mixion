package com.taskail.mixion.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**Created by ed on 10/2/17.
 */

public class GetTimeAgo {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    /**
     * This class converts a dateTime string in yyyy-MM-dd'T'HH:mm:ss format
     * into a 'time ago' String
     * @param dateTime the createdAt string returned from the server.
     * @return the timeAgo string returned.
     */
    public static String getlongtoago(String dateTime) {

        Long createdAt = null;

        try {
            createdAt = transformStringToTimeStamp(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (createdAt != null) {
            if (createdAt < 1000000000000L) {
                // if timestamp given in seconds, convert to millis
                createdAt *= 1000;
            }

            long now = System.currentTimeMillis();
            if (createdAt > now || createdAt <= 0) {
                return null;
            }

            // TODO: localize
            final long diff = now - createdAt;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " minutes ago";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " hours ago";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " days ago";
            }
        } else {
            return dateTime;
        }
    }

    /**
     * This method transforms a String into a timestamp.
     *
     * @param dateCreated
     *            The date to transform.
     * @return The timestamp representation of the given String.
     * @throws ParseException
     *             If the String could not be transformed.
     */
    private static Long transformStringToTimeStamp(String dateCreated) throws ParseException{
        Calendar calendarToMil = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        calendarToMil.setTime(simpleDateFormat.parse(dateCreated + "GMT"));
        return calendarToMil.getTimeInMillis();
    }
}
