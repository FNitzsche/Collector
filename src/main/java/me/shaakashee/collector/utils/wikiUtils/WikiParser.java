package me.shaakashee.collector.utils.wikiUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class WikiParser {

    public static ArrayList<pageStruct> parseIntroPage(String json){
        ArrayList<pageStruct> pageList = new ArrayList<>();
        //JSONObject root = new JSONObject(replaceUmlaute(json));
        JSONObject root = new JSONObject((json));

        if (root.has("query")) {
            JSONObject pages = root.getJSONObject("query").getJSONObject("pages");
            for (Iterator<String> it = pages.keys(); it.hasNext(); ) {
                String page = it.next();
                pageStruct ps = new pageStruct();

                ps.url = new String(pages.getJSONObject(page).getString("fullurl"));
                ps.title = pages.getJSONObject(page).getString("title");
                ps.text = pages.getJSONObject(page).getString("extract");
                ps.date = pages.getJSONObject(page).getString("touched");

                //System.out.println(ps.text);

                pageList.add(ps);
            }
        }
        return pageList;
    }

    public static ArrayList<String[]> parsePageSearch(String json){
        //JSONObject root = new JSONObject(replaceUmlaute(json));
        JSONObject root = new JSONObject((json));

        JSONArray pages = root.getJSONObject("query").getJSONArray("search");
        ArrayList<String[]> pageList = new ArrayList<>();
        for (Iterator<Object> it = pages.iterator(); it.hasNext(); ) {
            JSONObject p = (JSONObject) it.next();
            String title = p.getString("title");
            String id = "" + p.getInt("pageid");
            pageList.add(new String[]{title, id});
        }
        return pageList;
    }


    public static class pageStruct{
        public String title;
        public String url;
        public String text;
        public String date;
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
                .replace("\\u00c4", "AE")
                .replace("\\u201e", "\"")
                .replace("\\u201c", "\"")
                .replace("\\u2013", "-");
    }

}
