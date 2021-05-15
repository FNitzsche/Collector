package me.shaakashee.collector;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import me.shaakashee.collector.model.Collection;
import me.shaakashee.collector.utils.collectionUtils.CollectionLoader;
import me.shaakashee.collector.utils.collectionUtils.CollectionSaver;

import java.io.File;

public class MainScreenCon {

    public AppStart appStart;
    FileChooser collectionChooser = new FileChooser();

    @FXML
    Button nCol;
    @FXML
    Button openCol;
    @FXML
    ListView collectionList;
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
    }

    public void newCollection(){
        File cFile = collectionChooser.showSaveDialog(appStart.mainStage);
        if (cFile != null) {
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    if (appStart.activeCollection != null) {
                        CollectionSaver.writeCollectionToFile(appStart.activeCollection.collection, appStart.activeCollection.saveFile.getAbsolutePath());
                        System.out.println("saved");
                    }
                    appStart.activeCollection = new Collection();
                    appStart.activeCollection.saveFile = cFile;
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
                    if (appStart.activeCollection != null) {
                        CollectionSaver.writeCollectionToFile(appStart.activeCollection.collection, appStart.activeCollection.saveFile.getAbsolutePath());
                        System.out.println("saved");
                    }
                    appStart.activeCollection = new Collection();
                    appStart.activeCollection.saveFile = cFile;
                    appStart.activeCollection.collection = CollectionLoader.loadCollection(cFile.getAbsolutePath());
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


}
