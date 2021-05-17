package me.shaakashee.collector.utils.wikiUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class WikiConnector {

    static final String wikiURL = "https://de.wikipedia.org/w/api.php";
    static final String searchPart = "?action=query&format=json&list=search&srsearch=intitle:";
    static final String pagePart = "?format=json&action=query&prop=info%7Cextracts&inprop=url&explaintext&exintro&redirects=1&pageids=";
    static final String pageFull = "?format=json&action=query&prop=info%7Cextracts&inprop=url&explaintext&redirects=1&pageids=";

    public static String getSearchResults(String term){
        URI uri = URI.create(wikiURL+searchPart+term.replace(" ", "%20"));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET().build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert response != null;
        return response.body();
    }

    public static String getPageIntroText(String[] pageIds){
        if (pageIds.length < 1){
            return null;
        }
        String ids = pageIds[0];

        for (int i = 1; i < pageIds.length; i++) {
            ids += "%7C" + pageIds[i];
        }

        URI uri = URI.create(wikiURL+pagePart+ids);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET().build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert response != null;
        return response.body();
    }

    public static String getPageFullText(String[] pageIds){
        if (pageIds.length < 1){
            return null;
        }
        String ids = pageIds[0];

        for (int i = 1; i < pageIds.length; i++) {
            ids += "%7C" + pageIds[i];
        }

        URI uri = URI.create(wikiURL+pagePart+ids);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET().build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert response != null;
        return response.body();
    }

    public static HashMap<String, String> getPageHtml(String url){
        Document wikiPage = null;
        try {
            wikiPage = Jsoup.connect(url).url(new URL(url)).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        HashMap<String, String> ret = new HashMap<>();

            if (wikiPage != null && wikiPage.getElementById("Vorlage_Taxobox") != null) {
                Element taxobox = wikiPage.getElementById("Vorlage_Taxobox");

                StringBuilder person = new StringBuilder();
                StringBuilder fam = new StringBuilder();
                StringBuilder gattung = new StringBuilder();
                StringBuilder name = new StringBuilder();
                StringBuilder wName = new StringBuilder();

                for (Element block : taxobox.getElementsByClass("toptextcells").select("tr")) {
                    if (block.select("i").size() > 0 && block.select("i").first().select("a") != null) {
                        String type = block.select("i").first().select("a").first().ownText();
                        switch (type) {
                            case "Familie":
                                fam.append(block.select("td").get(1).ownText());
                                break;
                            case "Gattung":
                                gattung.append(block.select("td").get(1).select("i").first().ownText());
                                break;
                        }
                    }
                }

                if (taxobox.select("th").first().ownText() != null) {
                    name.append(taxobox.select("th").first().ownText());
                }

                if (taxobox.getElementsByClass("Person").select("a").size() > 0) {
                    Elements personCell = taxobox.getElementsByClass("Person");
                    person.append(personCell.select("a").first().ownText());
                    for (int i = 1; i < personCell.select("a").size(); i++) {
                        person.append(", " + personCell.select("a").get(i).ownText());
                    }
                }

                if (taxobox.getElementsByClass("taxo-name").select("i").size() > 0){
                    wName.append(taxobox.getElementsByClass("taxo-name").select("i").first().ownText());
                }

                System.out.println(person);
                System.out.println(fam.toString().replace("(", "").replace(")", ""));
                System.out.println(gattung);
                System.out.println(name);
                System.out.println(wName);

                ret.put("person", person.toString());
                ret.put("fam", fam.toString().replace("(", "").replace(")", ""));
                ret.put("gattung", gattung.toString());
                ret.put("name", name.toString());
                ret.put("wName", wName.toString());

            }

        return  ret;
    }

}
