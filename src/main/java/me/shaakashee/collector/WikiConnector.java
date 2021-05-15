package me.shaakashee.collector;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WikiConnector {

    static final String wikiURL = "https://de.wikipedia.org/w/api.php";
    static final String searchPart = "?action=query&format=json&list=search&srsearch=intitle:";
    static final String pagePart = "?format=json&action=query&prop=info%7Cextracts&inprop=url&explaintext&exintro&redirects=1&pageids=";
    static final String pageFull = "?format=json&action=query&prop=info%7Cextracts&inprop=url&explaintext&redirects=1&pageids=";

    public static String getSearchResults(String term){
        URI uri = URI.create(wikiURL+searchPart+term);

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

}
