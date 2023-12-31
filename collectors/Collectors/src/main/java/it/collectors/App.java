package it.collectors;

import it.collectors.business.BusinessFactory;
import it.collectors.business.jdbc.Connect_JDBC;
import it.collectors.business.jdbc.DatabaseImpl;
import it.collectors.business.jdbc.Query_JDBC;
import it.collectors.model.Collezionista;
import it.collectors.model.Disco;
import it.collectors.view.Pages;
import it.collectors.view.ViewDispatcher;
import it.collectors.view.ViewDispatcherException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.util.ArrayList;

public class App extends Application {

    private Stage stage;

    private static final String COLLECTORS_ICON = "ui/images/logo.png";


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        boolean loginVeloce = false; // mettere true per skippare la pagina di login (only for test purpose)
        try {
            ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();

            viewDispatcher.setStage(stage);
            if(loginVeloce) {
                try {
                    viewDispatcher.navigateTo(Pages.HOME, new Collezionista(1,"rossidiana@gmail.com","Diana"));
                } catch (ViewDispatcherException e) {
                    throw new RuntimeException(e);
                }
            }
            else viewDispatcher.showLogin();
            stage.setResizable(false);
            stage.getIcons().add(new Image(getClass().getResource(COLLECTORS_ICON).toString()));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
