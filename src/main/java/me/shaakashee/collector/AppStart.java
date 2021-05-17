package me.shaakashee.collector;

import javafx.application.Application;
import javafx.stage.Stage;
import me.shaakashee.collector.model.Collection;

import java.beans.PropertyChangeSupport;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppStart extends Application {

    public static final String COLLECTION = "collection";

    public PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    FXMLLoad mainScreen;

    private Collection activeCollection;
    public Stage mainStage;
    public ExecutorService exe = Executors.newCachedThreadPool();

    @Override
    public void start(Stage stage) throws Exception {
        stage.setOnCloseRequest(e -> exe.shutdown());
        mainStage = stage;
        MainScreenCon mainScreenCon = new MainScreenCon();
        mainScreenCon.appStart = this;
        mainScreen = new FXMLLoad("/me/shaakashee/collector/mainScreen.fxml", mainScreenCon);
        stage.setScene(mainScreen.getScene());
        stage.show();
    }

    public Collection getActiveCollection() {
        return activeCollection;
    }

    public void setActiveCollection(Collection activeCollection) {
        Collection old = this.activeCollection;
        this.activeCollection = activeCollection;
        propertyChangeSupport.firePropertyChange(COLLECTION, old, this.activeCollection);
    }

    public static void main(String[] args){
        launch(args);
    }
}
