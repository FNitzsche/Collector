package me.shaakashee.collector.utils.collectionUtils;

import me.shaakashee.collector.model.Etikett;
import me.shaakashee.collector.model.Group;

import java.util.ArrayList;

public class CollectionSorter {

    public static Group sortCollection(ArrayList<Etikett> etiketts){
        Group root = new Group();
        root.type = Group.ROOT;

        for (Etikett e: etiketts){

            System.out.println((e.getFamRef()!= null?"FRef: " + e.getFamRef():""));

            if (root.children.containsKey((e.getFam()!= null? e.getFam(): "Keine Familie"))){
                Group fam = root.children.get((e.getFam()!= null? e.getFam(): "Keine Familie"));
                if (fam.children.containsKey((e.getGattung()!= null? e.getGattung(): "Keine Gattung"))){
                    Group gattung = fam.children.get((e.getGattung()!= null? e.getGattung(): "Keine Gattung"));
                    if (!gattung.name.equals("Keine Gattung") && gattung.url == null){
                        if (e.getgRef() != null){
                            gattung.url = e.getgRef();
                        }
                    }
                    if (!fam.name.equals("Keine Familie") && fam.url == null){
                        if (e.getFamRef() != null){
                            fam.url = e.getFamRef();
                        }
                    }
                    gattung.leaves.add(e);
                } else {
                    Group gattung = new Group();
                    gattung.type = Group.GATTUNG;
                    gattung.name = (e.getGattung()!= null? e.getGattung(): "Keine Gattung");
                    gattung.leaves.add(e);
                    fam.children.put((e.getGattung()!= null? e.getGattung(): "Keine Gattung"), gattung);

                    if (!gattung.name.equals("Keine Gattung") && gattung.url == null){
                        if (e.getgRef() != null){
                            gattung.url = e.getgRef();
                        }
                    }
                    if (!fam.name.equals("Keine Familie") && fam.url == null){
                        if (e.getFamRef() != null){
                            fam.url = e.getFamRef();
                        }
                    }
                }
            } else {
                Group fam = new Group();
                fam.type = Group.FAM;
                fam.name = (e.getFam()!= null? e.getFam(): "Keine Familie");
                root.children.put((e.getFam()!= null? e.getFam(): "Keine Familie"), fam);

                Group gattung = new Group();
                gattung.type = Group.GATTUNG;
                gattung.name = (e.getGattung()!= null? e.getGattung(): "Keine Gattung");
                gattung.leaves.add(e);
                fam.children.put((e.getGattung()!= null? e.getGattung(): "Keine Gattung"), gattung);

                if (!gattung.name.equals("Keine Gattung") && gattung.url == null){
                    if (e.getgRef() != null){
                        gattung.url = e.getgRef();
                    }
                }
                if (!fam.name.equals("Keine Familie") && fam.url == null){
                    if (e.getFamRef() != null){
                        fam.url = e.getFamRef();
                    }
                }
            }
        }

        return root;
    }

}
