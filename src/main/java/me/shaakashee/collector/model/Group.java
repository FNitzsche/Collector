package me.shaakashee.collector.model;

import me.shaakashee.collector.utils.wikiUtils.WikiConnector;
import me.shaakashee.collector.utils.wikiUtils.WikiParser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Group {

    public static final String ROOT = "root";
    public static final String FAM = "fam";
    public static final String GATTUNG = "gattung";

    public String type;
    public String name;
    public String url = null;
    public String pagedate = null;
    public String pageid;
    public String text;
    public HashMap<String, Group> children = new HashMap<>();
    public ArrayList<Etikett> leaves = new ArrayList<>();


    public ArrayList<Group> getSortedChildren(){
        return children.values().stream().sorted(Comparator.comparing(g -> g.name)).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Etikett> getSortedLeaves(){
        return leaves.stream().sorted(Comparator.comparing(Etikett::getName)).collect(Collectors.toCollection(ArrayList::new));
    }

    public void requestText(){
        if (url != null){
            pageid = WikiConnector.getIdforURL(url);
            WikiParser.pageStruct page = WikiParser.parseIntroPage(WikiConnector.getPageIntroText(new String[]{pageid})).get(0);
            pagedate = page.date;
            text = page.text;
        }
    }
}
