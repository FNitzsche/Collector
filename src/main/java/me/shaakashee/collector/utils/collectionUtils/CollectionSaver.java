package me.shaakashee.collector.utils.collectionUtils;

import me.shaakashee.collector.model.Etikett;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CollectionSaver {

    public static File writeCollectionToFile(ArrayList<Etikett> etiketts, String path){

        String saveString = createSaveString(etiketts);

        File ret = createFile(path);

        try {
            FileWriter myWriter = new FileWriter(path);
            myWriter.write(saveString);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    private static String createSaveString(ArrayList<Etikett> etiketts) {
        JSONArray array = new JSONArray();

        for (Etikett e: etiketts){

            JSONObject eJson = new JSONObject();

            eJson.put("fam", e.getFam());
            eJson.put("gattung", e.getGattung());
            eJson.put("art", e.getArt());
            eJson.put("autor", e.getAutor());
            eJson.put("name", e.getName());
            eJson.put("fundort", e.getFundort());
            eJson.put("standort", e.getStandort());
            eJson.put("leg", e.getLeg());
            eJson.put("det", e.getDet());
            eJson.put("date", e.getDate());
            eJson.put("text", e.getText());
            eJson.put("url", e.getUrl());
            eJson.put("pageid", e.getPageID());
            eJson.put("pagetitle", e.getPageTitle());

            array.put(eJson);

        }

        return array.toString();
    }

    public static File createFile(String path){
        try {
            File myObj = new File(path);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
            return myObj;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
        }
    }

}
