package me.shaakashee.collector;

import me.shaakashee.collector.model.Etikett;

import java.net.http.HttpRequest;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args){
        //System.out.println(WikiConnector.getSearchResults("loewenzahn"));
        String page = WikiConnector.getPageIntroText(new String[]{"18327"});
        ArrayList<WikiParser.pageStruct> p = WikiParser.parseIntroPage(page);
        Etikett test = new Etikett();
        test.setFam("blabla");
        test.setGattung("blubb");
        test.setArt("blubb");
        test.setAutor("blubb");
        test.setName("blubb");
        test.setFundort("blubb");
        test.setStandort("blubb");
        test.setLeg("blubb");
        test.setDet("blubb");
        test.setDate("blubb");
        test.setText(p.get(0).text);
        test.setUrl(p.get(0).url);
        ArrayList<Etikett> e = new ArrayList<>();
        e.add(test);
        TexWriter.writeCollectionToTex(e, "cT");
    }



}
