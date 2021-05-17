package main;

import me.shaakashee.collector.utils.wikiUtils.WikiConnector;
import me.shaakashee.collector.utils.wikiUtils.WikiParser;
import org.junit.Test;

public class htmlTest {

    @Test
    public void htmlPersonTest(){
        WikiConnector.getPageHtml("https://de.wikipedia.org/wiki/Wei%C3%9Fe_Lichtnelke");
    }

    @Test
    public void idGetTest(){
        String id = WikiConnector.getIdforURL("/wiki/Weiße_Lichtnelke");
        System.out.println(id);
        System.out.println(WikiParser.parseIntroPage(WikiConnector.getPageIntroText(new String[]{id})).get(0).text);
    }
}
