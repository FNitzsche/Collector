package me.shaakashee.collector;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class WikiParser {

    public static ArrayList<pageStruct> parseIntroPage(String json){


        JSONObject root = new JSONObject(replaceUmlaute(json));

        JSONObject pages = root.getJSONObject("query").getJSONObject("pages");
        ArrayList<pageStruct> pageList = new ArrayList<>();
        for (Iterator<String> it = pages.keys(); it.hasNext(); ) {
            String page = it.next();
            pageStruct ps = new pageStruct();

            try {
                ps.url = new String(pages.getJSONObject(page).getString("fullurl"));
                ps.title = new String(pages.getJSONObject(page).getString("title").getBytes(), "UTF-8");
                ps.text = new String(pages.getJSONObject(page).getString("extract").getBytes(), StandardCharsets.UTF_8);

                System.out.println(ps.text);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            ps.title = pages.getJSONObject(page).getString("title");
            ps.text = pages.getJSONObject(page).getString("extract");
            pageList.add(ps);
        }
        return pageList;
    }


    public static class pageStruct{
        public String title;
        public String url;
        public String text;
    }

    private static String replaceUmlaute(String output) {
        return output.replace("\\u00fc", "ue")
                .replace("\\u00f6", "oe")
                .replace("\\u00e4", "ae")
                .replace("\\u00df", "ss")
                .replaceAll("\u00dc(?=[a-z\u00e4\u00f6\u00fc\u00df ])", "Ue")
                .replaceAll("\u00d6(?=[a-z\u00e4\u00f6\u00fc\u00df ])", "Oe")
                .replaceAll("\u00c4(?=[a-z\u00e4\u00f6\u00fc\u00df ])", "Ae")
                .replace("\\u00dc", "UE")
                .replace("\\u00d6", "OE")
                .replace("\\u00c4", "AE");
    }

}
