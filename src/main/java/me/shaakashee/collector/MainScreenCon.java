package me.shaakashee.collector;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import me.shaakashee.collector.model.Collection;
import me.shaakashee.collector.model.Etikett;
import me.shaakashee.collector.utils.collectionUtils.CollectionLoader;
import me.shaakashee.collector.utils.collectionUtils.CollectionSaver;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

public class MainScreenCon {

    public AppStart appStart;
    FileChooser collectionChooser = new FileChooser();

    @FXML
    Button nCol;
    @FXML
    Button openCol;
    @FXML
    ListView<Etikett> collectionList;
    @FXML
    Button nEtikett;
    @FXML
    TextField fam;
    @FXML
    TextField gattung;
    @FXML
    TextField art;
    @FXML
    TextField autor;
    @FXML
    TextField name;
    @FXML
    TextField fort;
    @FXML
    TextField sort;
    @FXML
    TextField leg;
    @FXML
    TextField det;
    @FXML
    TextField date;
    @FXML
    ChoiceBox pageList;
    @FXML
    Button refresh;
    @FXML
    TextArea pageText;
    @FXML
    Button save;
    @FXML
    Button export;

    public void initialize(){
        collectionChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PlantCollectionFile", "*.pclf"));

        nCol.setOnAction((e) -> newCollection());
        openCol.setOnAction(e -> openCollection());

        appStart.propertyChangeSupport.addPropertyChangeListener(AppStart.COLLECTION, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                listAllEtiketts();
            }
        });
        collectionList.setOnMouseClicked(e -> appStart.getActiveCollection().setActiveEtikett(collectionList.getSelectionModel().getSelectedItem()));
    }

    public void newCollection(){
        File cFile = collectionChooser.showSaveDialog(appStart.mainStage);
        if (cFile != null) {
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    if (appStart.getActiveCollection() != null) {
                        CollectionSaver.writeCollectionToFile(appStart.getActiveCollection().getCollection(), appStart.getActiveCollection().saveFile.getAbsolutePath());
                        System.out.println("saved");
                    }
                    Collection nC = new Collection();
                    nC.saveFile = cFile;
                    appStart.setActiveCollection(nC);
                }
            };
            appStart.exe.execute(run);
        } else {
            Platform.runLater(new Runnable() {
                public void run() {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Invalid Savefile");
                    alert.setHeaderText("Enter Filename and click \"Okay\"");
                    alert.showAndWait();
                }
            });
        }
    }

    public void openCollection(){
        File cFile = collectionChooser.showOpenDialog(appStart.mainStage);
        if (cFile != null) {
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    if (appStart.getActiveCollection() != null) {
                        CollectionSaver.writeCollectionToFile(appStart.getActiveCollection().getCollection(), appStart.getActiveCollection().saveFile.getAbsolutePath());
                        System.out.println("saved");
                    }
                    Collection nC = new Collection();
                    nC.saveFile = cFile;
                    nC.silentsetCollection(CollectionLoader.loadCollection(cFile.getAbsolutePath()));
                    appStart.setActiveCollection(nC);
                }
            };
            appStart.exe.execute(run);
        } else {
            Platform.runLater(new Runnable() {
                public void run() {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Invalid PlantCollectionFile");
                    alert.setHeaderText("Select a \"*.pclf\"-File and click \"Okay\"");
                    alert.showAndWait();
                }
            });
        }
    }

    public void listAllEtiketts(){
        collectionList.getItems().clear();
        for (Etikett e: appStart.getActiveCollection().getCollection()){
            collectionList.getItems().add(e);
        }
        appStart.getActiveCollection().propertyChangeSupport.addPropertyChangeListener(Collection.ETIKETT, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                etikettAddDel(propertyChangeEvent);
            }
        });
    }

    public void etikettAddDel(PropertyChangeEvent event){
        if (event.getOldValue() == null && event.getNewValue() != null){
            collectionList.getItems().add((Etikett) event.getNewValue());
        } else if (event.getOldValue() != null && event.getNewValue() == null){
            collectionList.getItems().remove(event.getOldValue());
        }
    }


}
