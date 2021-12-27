package me.shaakashee.collector;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ExportScreenCon {

    MainScreenCon mainScreenCon;
    Stage exportStage;

    @FXML
    ChoiceBox<String> eSize;
    @FXML
    ChoiceBox<String> eOrient;

    @FXML
    TextField bL;
    @FXML
    TextField bO;
    @FXML
    TextField bR;
    @FXML
    TextField bU;

    @FXML
    CheckBox iGen;
    @FXML
    CheckBox qGen;
    @FXML
    CheckBox famGen;
    @FXML
    CheckBox gatGen;
    @FXML
    CheckBox plantGen;
    @FXML
    CheckBox numGen;

    @FXML
    Button abb;
    @FXML
    Button ok;


    public void initialize(){
        eSize.getItems().addAll("a3paper", "a4paper", "a5paper", "a6paper");
        eOrient.getItems().addAll("landscape", "portrait");

        eSize.getSelectionModel().select(2);
        eOrient.getSelectionModel().select(0);

        bL.setText("2");
        bO.setText("1");
        bR.setText("1");
        bU.setText("1.5");

        iGen.setSelected(true);
        qGen.setSelected(true);
        famGen.setSelected(true);
        gatGen.setSelected(true);
        plantGen.setSelected(true);
        numGen.setSelected(true);

        ok.setOnAction((e) -> callExport());
        abb.setOnAction(e -> exportStage.hide());
    }

    public void callExport(){
        String documentclass = eSize.getValue() + ", " + eOrient.getValue();
        String borders = "left=" + bL.getText() + "cm,right=" + bR.getText() + "cm,top=" + bO.getText() + "cm,bottom=" + bU.getText() + "cm";
        mainScreenCon.exportCollection(documentclass, borders, iGen.isSelected(), qGen.isSelected(),
                famGen.isSelected(), gatGen.isSelected(), plantGen.isSelected(), numGen.isSelected());
    }

}
