package me.shaakashee.collector.utils.texUtils;

import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class TexCaller {

    public static void callLatex(String path){

        if(Desktop.isDesktopSupported()){
            Desktop desktop = Desktop.getDesktop();
            try {
                File file = new File(path);
                desktop.open(file);
            } catch (IOException e) {
                Platform.runLater(new Runnable() {
                    public void run() {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("tex-File not Found");
                        alert.setHeaderText("Please generate first.");
                        alert.showAndWait();
                    }
                });
            }
        }

    }

}
