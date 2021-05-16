package me.shaakashee.collector.utils.collectionUtils;

import me.shaakashee.collector.model.Etikett;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class CollectionLoader {

    public static String loadFile(String path){
        StringBuilder ret = new StringBuilder();
        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                ret.append(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return ret.toString();
    }

    public static ArrayList<Etikett> loadCollection(String path){
        JSONArray coll = new JSONArray(loadFile(path));

        ArrayList<Etikett> ret = new ArrayList<>();

        for (Iterator<Object> it = coll.iterator(); it.hasNext(); ) {
            JSONObject e = (JSONObject) it.next();

            Etikett etikett = Etikett.build(
                    (e.has("fam")? e.getString("fam"):null),
                    (e.has("gattung")?e.getString("gattung"):null),
                    (e.has("art")?e.getString("art"):null),
                    (e.has("autor")?e.getString("autor"):null),
                    (e.has("name")?e.getString("name"):null),
                    (e.has("fundort")?e.getString("fundort"):null),
                    (e.has("standort")?e.getString("standort"):null),
                    (e.has("leg")?e.getString("leg"):null),
                    (e.has("det")?e.getString("det"):null),
                    (e.has("date")?e.getString("date"):null),
                    (e.has("text")?e.getString("text"):null),
                    (e.has("url")?e.getString("url"):null)
            );

            if (e.has("pageid")){
                etikett.setPageID(e.getString("pageid"));
            }
            if (e.has("pagetitle")){
                etikett.setPageTitle(e.getString("pagetitle"));
            }

            ret.add(etikett);
        }

        return ret;
    }

}
