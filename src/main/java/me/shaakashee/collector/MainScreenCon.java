package me.shaakashee.collector;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import me.shaakashee.collector.model.Collection;
import me.shaakashee.collector.model.Etikett;
import me.shaakashee.collector.utils.collectionUtils.CollectionLoader;
import me.shaakashee.collector.utils.collectionUtils.CollectionSaver;
import me.shaakashee.collector.utils.texUtils.TexCaller;
import me.shaakashee.collector.utils.texUtils.TexWriter;
import me.shaakashee.collector.utils.wikiUtils.WikiConnector;
import me.shaakashee.collector.utils.wikiUtils.WikiParser;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MainScreenCon {

    public AppStart appStart;
    FileChooser collectionChooser = new FileChooser();
    FileChooser texChooser = new FileChooser();

    public ExportScreenCon exportScreenCon = new ExportScreenCon();
    public Stage exportStage;

    @FXML
    Button nCol;
    @FXML
    Button openCol;
    @FXML
    ListView<Etikett> collectionList;
    @FXML
    Button nEtikett;
    @FXML
    Button delEtikett;
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
    ChoiceBox<String> pageList;
    @FXML
    Button refresh;
    @FXML
    TextArea pageText;
    @FXML
    Button save;
    @FXML
    Button export;

    public void initialize(){
        exportScreenCon.mainScreenCon = this;
        collectionChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PlantCollectionFile", "*.pclf"));
        texChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tex-File", "*.tex"));

        nCol.setOnAction((e) -> newCollection());
        openCol.setOnAction(e -> openCollection());

        appStart.propertyChangeSupport.addPropertyChangeListener(AppStart.COLLECTION, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                Platform.runLater(() -> listAllEtiketts());
            }
        });
        collectionList.setOnMouseClicked(e -> appStart.getActiveCollection().setActiveEtikett(collectionList.getSelectionModel().getSelectedItem()));

        nEtikett.setOnAction(e -> addEtikett());
        delEtikett.setOnAction(e -> removeEtikett());

        initializeTextFields();
        initializeWikiChooser();

        refresh.setOnAction(e -> refreshButton());

        save.setOnAction(e -> saveCollection());

        export.setOnAction(e -> {
            exportStage.show();
            exportStage.toFront();
        });
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
            appStart.getActiveCollection().setActiveEtikett((Etikett) event.getNewValue());
            collectionList.getSelectionModel().selectLast();
        } else if (event.getOldValue() != null && event.getNewValue() == null){
            collectionList.getSelectionModel().selectFirst();
            appStart.getActiveCollection().setActiveEtikett(collectionList.getSelectionModel().getSelectedItem());
            collectionList.getItems().remove(event.getOldValue());
        }
    }

    public void addEtikett(){
        Etikett etikett = new Etikett();
        appStart.getActiveCollection().addEtikett(etikett);
    }

    public void removeEtikett(){
        if (collectionList.getSelectionModel().getSelectedItem() != null) {
            appStart.getActiveCollection().removeEtikett(collectionList.getSelectionModel().getSelectedItem());
        }
    }

    public void initializeTextFields(){
        fam.setOnKeyTyped(e -> appStart.getActiveCollection().getActiveEtikett().setFam(fam.getText()));
        gattung.setOnKeyTyped(e -> appStart.getActiveCollection().getActiveEtikett().setGattung(gattung.getText()));
        art.setOnKeyTyped(e -> appStart.getActiveCollection().getActiveEtikett().setArt(art.getText()));
        autor.setOnKeyTyped(e -> appStart.getActiveCollection().getActiveEtikett().setAutor(autor.getText()));
        name.setOnKeyTyped(e -> {
            appStart.getActiveCollection().getActiveEtikett().setName(name.getText());
            collectionList.refresh();
        });
        fort.setOnKeyTyped(e -> appStart.getActiveCollection().getActiveEtikett().setFundort(fort.getText()));
        leg.setOnKeyTyped(e -> appStart.getActiveCollection().getActiveEtikett().setLeg(leg.getText()));
        sort.setOnKeyTyped(e -> appStart.getActiveCollection().getActiveEtikett().setStandort(sort.getText()));
        det.setOnKeyTyped(e -> appStart.getActiveCollection().getActiveEtikett().setDet(det.getText()));
        date.setOnKeyTyped(e -> {
            appStart.getActiveCollection().getActiveEtikett().setDate(date.getText());
            collectionList.refresh();
        });

        appStart.propertyChangeSupport.addPropertyChangeListener(AppStart.COLLECTION, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                appStart.getActiveCollection().propertyChangeSupport.addPropertyChangeListener(Collection.ACTIVE, new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                        Platform.runLater(() -> {
                            Etikett e = (Etikett) propertyChangeEvent.getNewValue();
                            fam.setText((e.getFam()!= null? e.getFam():""));
                            gattung.setText((e.getGattung()!= null? e.getGattung():""));
                            art.setText((e.getArt()!= null? e.getArt():""));
                            autor.setText((e.getAutor()!= null? e.getAutor():""));
                            name.setText((e.getName()!= null? e.getName():""));
                            fort.setText((e.getFundort()!= null? e.getFundort():""));
                            leg.setText((e.getLeg()!= null? e.getLeg():""));
                            sort.setText((e.getStandort()!= null? e.getStandort():""));
                            det.setText((e.getDet()!= null? e.getDet():""));
                            date.setText((e.getDate()!= null? e.getDate():""));
                            pageText.setText((e.getText()!= null? e.getText():""));
                        });
                    }
                });
            }
        });
    }

    public void initializeWikiChooser(){
        appStart.propertyChangeSupport.addPropertyChangeListener(AppStart.COLLECTION, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                Platform.runLater(() -> pageList.getItems().clear());
                appStart.getActiveCollection().propertyChangeSupport.addPropertyChangeListener(Collection.ACTIVE, new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                        refreshPages();
                    }
                });
            }
        });

        pageList.setOnAction(e -> {
            if (appStart.getActiveCollection() != null && appStart.getActiveCollection().getActiveEtikett() != null) {
                appStart.getActiveCollection().getActiveEtikett().setPageTitle(pageList.getValue());
                appStart.getActiveCollection().getActiveEtikett().setPageID(appStart.getActiveCollection().getActiveEtikett().getIdForTitle(pageList.getValue()));
                Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        Etikett etikett = appStart.getActiveCollection().getActiveEtikett();
                        String page = WikiConnector.getPageIntroText(new String[]{etikett.getPageID()});
                        ArrayList<WikiParser.pageStruct> p = WikiParser.parseIntroPage(page);
                        if (p.size() > 0) {
                            etikett.setUrl(p.get(0).url);
                            etikett.setText(p.get(0).text);
                            etikett.setPageDate(p.get(0).date);
                            Platform.runLater(() -> pageText.setText(p.get(0).text));

                            HashMap<String, String> parsed = WikiConnector.getPageHtml(etikett.getUrl());
                            etikett.setName(parsed.get("name"));
                            etikett.setFam(parsed.get("fam"));
                            etikett.setGattung(parsed.get("gattung"));
                            etikett.setAutor(parsed.get("person"));
                            etikett.setArt(parsed.get("wName"));
                            etikett.setFamRef(parsed.get("famRef"));
                            etikett.setgRef(parsed.get("gRef"));

                            Platform.runLater(() -> {
                                fam.setText(etikett.getFam());
                                name.setText(etikett.getName());
                                gattung.setText(etikett.getGattung());
                                autor.setText(etikett.getAutor());
                                art.setText(etikett.getArt());
                            });
                        }
                    }
                };

                appStart.exe.execute(run);
            }
        });
    }

    public void refreshPages(){
        Platform.runLater(() -> {
            pageList.getItems().clear();
            for (String title: appStart.getActiveCollection().getActiveEtikett().getPageTitles()){
                pageList.getItems().add(title);
            }

            if (appStart.getActiveCollection().getActiveEtikett().getPageTitle() != null) {
                if (pageList.getItems().contains(appStart.getActiveCollection().getActiveEtikett().getPageTitle())) {
                    pageList.getSelectionModel().select(appStart.getActiveCollection().getActiveEtikett().getPageTitle());
                }
            }
        });
    }

    public void refreshButton(){
        Runnable run = new Runnable() {
            @Override
            public void run() {
                Etikett etikett = appStart.getActiveCollection().getActiveEtikett();
                String result = WikiConnector.getSearchResults(etikett.getArt());
                ArrayList<String[]> pgs = WikiParser.parsePageSearch(result);
                for (String[] p: pgs){
                    etikett.addPPage(p[0], p[1]);
                }
                refreshPages();
            }
        };
        appStart.exe.execute(run);
    }

    public void saveCollection(){
        Runnable run = new Runnable() {
            @Override
            public void run() {
                CollectionSaver.writeCollectionToFile(appStart.getActiveCollection().getCollection(), appStart.getActiveCollection().saveFile.getAbsolutePath());
            }
        };
        appStart.exe.execute(run);
    }

    public void exportCollection(String documentclass, String borders,
                                   Boolean showTableOfContents, Boolean generateQRCodes, Boolean showFamilie, Boolean showGattung,
                                   Boolean showPlantDescription, Boolean showPageNumbers){
        File texFile = texChooser.showSaveDialog(appStart.mainStage);
        Runnable run = new Runnable() {
            @Override
            public void run() {
                TexWriter.writeCollectionToTex(appStart.getActiveCollection().getCollection(), texFile.getAbsolutePath(),
                        documentclass, borders, showTableOfContents, generateQRCodes, showFamilie, showGattung, showPlantDescription, showPageNumbers);
                TexCaller.callLatex(texFile.getAbsolutePath());
            }
        };
        appStart.exe.execute(run);
    }
}
