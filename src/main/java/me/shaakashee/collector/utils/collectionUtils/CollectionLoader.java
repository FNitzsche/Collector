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
            File myObj = new File(path+".pclf");
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
            e.getString("fam"),
            e.getString("gattung"),
            e.getString("art"),
            e.getString("autor"),
            e.getString("name"),
            e.getString("fundort"),
            e.getString("standort"),
            e.getString("leg"),
            e.getString("det"),
            e.getString("date"),
            e.getString("text"),
            e.getString("url")
            );

            ret.add(etikett);
        }

        return ret;
    }

}
