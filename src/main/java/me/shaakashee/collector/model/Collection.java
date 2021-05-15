package me.shaakashee.collector.model;

import javafx.collections.ObservableList;

import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.ArrayList;

public class Collection {

    public static final String ETIKETT = "etikett";
    public static final String ACTIVE = "active";

    public PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    private ArrayList<Etikett> collection = new ArrayList<>();

    private Etikett activeEtikett;

    public File saveFile;


    public ArrayList<Etikett> getCollection() {
        return collection;
    }

    public void silentsetCollection(ArrayList<Etikett> collection) {
        this.collection = collection;
    }

    public void addEtikett(Etikett etikett) {
        collection.add(etikett);
        propertyChangeSupport.firePropertyChange(ETIKETT, null, etikett);
    }

    public void removeEtikett(Etikett etikett){
        collection.remove(etikett);
        propertyChangeSupport.firePropertyChange(ETIKETT, etikett, null);
    }


    public Etikett getActiveEtikett() {
        return activeEtikett;
    }

    public void setActiveEtikett(Etikett activeEtikett) {
        Etikett old = this.activeEtikett;
        this.activeEtikett = activeEtikett;
        propertyChangeSupport.firePropertyChange(ACTIVE, old, this.activeEtikett);
    }
}
