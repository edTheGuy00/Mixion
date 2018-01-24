package com.taskail.mixion.utils;

import android.os.Build;
import android.text.Html;
import android.util.Log;

import com.taskail.mixion.data.models.FullDiscussion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**Created by ed on 10/2/17.
 *
 * what a mess, i get a headache reading this
 */

public class StringUtils {
    private static final String TAG = "StringUtils";

    private static boolean containsCharacter(char c, String s) {
        return s.indexOf(c) > -1;
    }
    private static boolean containsText(String match, String s){
        return s.contains(match);
    }
    private static int stringLocation(String match, String s){
        return s.indexOf(match);
    }
    private static int locationOfLast(char c, String s){
        return s.indexOf(c);
    }
    public static String shortenString(int begin, int lenght, String s) throws IOException, IndexOutOfBoundsException{
        return s.substring(begin, Math.min(s.length(), lenght));
    }

    public String getFirstImageFromJsonMetaData(String jsonMetaData){
        JSONObject mainObject = null;
        try {
            mainObject = new JSONObject(jsonMetaData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray imgObject = null;
        try {
            if (mainObject != null) {
                imgObject = mainObject.getJSONArray("image");
            } else {
                imgObject = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String firstImg = null;
        try {
            if (imgObject != null) {
                firstImg = imgObject.get(0).toString();
            } else {
                firstImg = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return firstImg;
    }

    /**
     * shorten the entire body to 200 characters to display in the feed list
     * @param body is the entire response from the server
     * @return a body of 200 characters
     */
    public static String getShorterBody(String body){

        String stringFromHtml;

        if (Build.VERSION.SDK_INT > 23) {
             stringFromHtml = Html.fromHtml(body, Html.FROM_HTML_MODE_COMPACT).toString();
        } else {
            stringFromHtml = Html.fromHtml(body).toString();
        }

        String shorterBody = null;
        try {
            shorterBody = shortenString(0, 200, stringFromHtml);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (containsCharacter('[', shorterBody)){
            String whitoutCharacter =  null;
            int location = locationOfLast(')', shorterBody);
            try {
                whitoutCharacter = shortenString(location + 1, 400, stringFromHtml);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (containsText("http", shortenString(0, 10, whitoutCharacter))){

                    Log.d(TAG, "getShorterBody: there's a link");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return  whitoutCharacter;
        }
        try {
            if (containsText("http", shortenString(0, 10, shorterBody))){
                String withoutLink = removeUrl(shorterBody);
                int location = stringLocation(withoutLink, stringFromHtml);
                String shorterString = null;
                try {
                    shorterString = shortenString(location, 400, stringFromHtml);
                } catch (StringIndexOutOfBoundsException e){
                    Log.e(TAG, "getShorterBody: unable to shorten string " + e.getMessage());
                }
                return shorterString;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return shorterBody;
    }

    /**
     * Removes urls so they won't be displayed.
     * @param commentstr the body containing urls
     * @return a body without urls
     */

    private static String removeUrl(String commentstr)
    {
        String urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern p = Pattern.compile(urlPattern,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(commentstr);
        int i = 0;
        while (m.find()) {
            commentstr = commentstr.replaceAll(Pattern.quote(m.group(i)),"").trim();
            i++;
        }
        return commentstr;
    }


    public List createArrayOfImages(String jsonMetaData){

        List imagesArray = new ArrayList();

        JSONObject mainObject = null;
        try {
            mainObject = new JSONObject(jsonMetaData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray imgObject = null;
        try {
            if (mainObject != null) {
                imgObject = mainObject.getJSONArray("image");

                for (int i = 0; i < imgObject.length(); i++){
                    imagesArray.add(imgObject.get(i));
                }

            } else {
                imgObject = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "createArrayOfImages: " + imagesArray.get(0));

        return imagesArray;
    }

    public static List<String> extractLinksFromContent(String content) {

        List<String> containedUrls = new ArrayList<>();

        Pattern pattern = Pattern.compile(
                "\\b((https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|])",
                Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(content);

        while (urlMatcher.find()) {
            containedUrls.add(content.substring(urlMatcher.start(0), urlMatcher.end(0)));
        }

        return containedUrls;
    }

    /**
     * Get a list of user names that the given <code>content</code> contains.
     *
     * @param content
     *            The content to extract the user names from.
     * @return A list of user names.
     */
    public static List<String> extractUsersFromContent(String content) {
        List<String> containedUrls = new ArrayList<>();
        Pattern pattern = Pattern.compile("(@{1})([a-z0-9\\.-]{3,16})", Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(content);

        while (urlMatcher.find()) {
            containedUrls.add(content.substring(urlMatcher.start(2), urlMatcher.end(2)));
        }

        return containedUrls;
    }
}
