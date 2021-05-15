package me.shaakashee.collector;

import javafx.application.Application;
import javafx.stage.Stage;
import me.shaakashee.collector.model.Collection;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppStart extends Application {

    FXMLLoad mainScreen = new FXMLLoad("/me/shaakashee/collector/mainScreen.fxml", new MainScreenCon());

    public Collection activeCollection;
    public Stage mainStage;
    public ExecutorService exe = Executors.newCachedThreadPool();

    @Override
    public void start(Stage stage) throws Exception {
        mainStage = stage;
        stage.setScene(mainScreen.getScene());
        mainScreen.getController(MainScreenCon.class).appStart = this;
        stage.show();
    }
}
