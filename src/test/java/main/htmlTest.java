package main;

import me.shaakashee.collector.utils.wikiUtils.WikiConnector;
import org.junit.Test;

public class htmlTest {

    @Test
    public void htmlPersonTest(){
        WikiConnector.getPageHtml("https://de.wikipedia.org/wiki/Wei%C3%9Fe_Lichtnelke");
    }
}
