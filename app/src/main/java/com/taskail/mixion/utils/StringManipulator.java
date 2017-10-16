package com.taskail.mixion.utils;

import android.os.Build;
import android.text.Html;
import android.util.Log;

import com.taskail.mixion.models.FullDiscussion;

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

public class StringManipulator {
    private static final String TAG = "StringManipulator";

    private boolean containsCharacter(char c, String s) {
        return s.indexOf(c) > -1;
    }
    private boolean containsText(String match, String s){
        return s.contains(match);
    }
    private int stringLocation(String match, String s){
        return s.indexOf(match);
    }
    private int locationOfLast(char c, String s){
        return s.indexOf(c);
    }
    public String shortenString(int begin, int lenght, String s) throws IOException, IndexOutOfBoundsException{
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

    public String getFirstFewCharacters(String body){

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

                    Log.d(TAG, "getFirstFewCharacters: there's a link");
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
                    Log.e(TAG, "getFirstFewCharacters: unable to shorten string " + e.getMessage());
                }
                return shorterString;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return shorterBody;
    }

    private String removeUrl(String commentstr)
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

    public void parseBody(String jsonMetaData, String body){

        Log.d(TAG, "parseBody: Entire body " + body);
        Log.d(TAG, "parseBody: the jSon Meta data " + jsonMetaData);

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
                Log.d(TAG, "parseBody: image " + imgObject.length());
            } else {
                imgObject = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {

            for (int i = 0; i < imgObject.length(); i++) {

                Log.d(TAG, "parseBody: Location of images " + stringLocation(imgObject.get(i).toString(), body));

            }

        } catch(JSONException e){
            e.printStackTrace();
        }

        try {
            createArrayofTexts(imgObject.length(), imgObject, body);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

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

    public List createArrayofTexts(int numOfImages, JSONArray images, String body) throws JSONException, IOException {
        List stringArray = new ArrayList();
        int startAt = 0;

        for (int i =0; i < numOfImages; i++){

            int imageLocation = stringLocation(images.get(i).toString(), body);

            stringArray.add(shortenString(startAt, imageLocation, body));

            startAt = (imageLocation + images.get(i).toString().length());

        }

        return stringArray;
    }

    public List<FullDiscussion> createArrayOfImagesAndText(String body, String jsonMetaData){
        List <FullDiscussion> listToLoad = new ArrayList<>();
        String contentFromHtml = Html.fromHtml(body).toString();
        int startAt = 0;

        Log.d(TAG, "createArrayOfImagesAndText: content from html " + contentFromHtml);

        try {
            if (containsCharacter('[', shortenString(0, 20, contentFromHtml)));

            Log.d(TAG, "createArrayOfImagesAndText: Find the end of ] ");

            Log.d(TAG, "createArrayOfImagesAndText: " + locationOfLast(']', shortenString(0, 200, contentFromHtml)));

        } catch (IOException e) {
            e.printStackTrace();
        }

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
                if (imgObject != null) {

                    for (int i =0; i < imgObject.length(); i++){
                        int imageLocation = stringLocation(imgObject.get(i).toString(), body);

                        try {
                            listToLoad.add(new FullDiscussion(
                                    shortenString(startAt, imageLocation, body),
                                    true
                            ));
                        }catch (StringIndexOutOfBoundsException e){
                            Log.e(TAG, "createArrayOfImagesAndText: " + e.getMessage() );
                        }

                        listToLoad.add(new FullDiscussion(
                                imgObject.get(i).toString(),
                                false
                        ));

                        if (i == (imgObject.length() - 1)){

                            try {
                                listToLoad.add(new FullDiscussion(
                                        shortenString(startAt, imageLocation, body),
                                        true
                                ));

                                Log.d(TAG, "createArrayOfImagesAndText: added last part " + shortenString(imageLocation, body.length(), body));
                            }catch (StringIndexOutOfBoundsException e){
                                Log.e(TAG, "createArrayOfImagesAndText: " + e.getMessage() );

                                listToLoad.add(new FullDiscussion(
                                        shortenString(startAt, body.length(), body),
                                        true
                                ));

                                Log.d(TAG, "createArrayOfImagesAndText: added last part " + shortenString(startAt, body.length(), body));

                            }


                        }

                        startAt = (imageLocation + imgObject.get(i).toString().length());

                    }
                }
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "createArrayOfImagesAndText: First " + listToLoad.get(0).getmString());

        Log.d(TAG, "createArrayOfImagesAndText: second " + listToLoad.get(1).getmString());

        return listToLoad;

    }

    public void findText(String[] args) {
        Pattern p = Pattern.compile(
                "<row><column>(.*)</column></row>",
                Pattern.DOTALL
        );

        Matcher matcher = p.matcher(
                "<row><column>Header\n\n\ntext</column></row>"
        );

        if(matcher.matches()){
            System.out.println(matcher.group(1));
        }
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
