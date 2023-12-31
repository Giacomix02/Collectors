package it.collectors.controller;

import it.collectors.business.BusinessFactory;
import it.collectors.business.jdbc.Query_JDBC;
import it.collectors.model.Collezione;
import it.collectors.model.Collezionista;
import it.collectors.model.Disco;
import it.collectors.model.Traccia;
import it.collectors.view.Pages;
import it.collectors.view.ViewDispatcher;
import javafx.beans.property.Property;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TracceController implements Initializable, DataInitializable<Collezionista>{

    ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();

    @FXML
    private VBox vBox;

    @FXML
    private TableView<Traccia> table;

    @FXML
    private TableColumn<Traccia, String> titolo;

    @FXML
    private TableColumn<Traccia, String> durata;


    Collezionista collezionista;
    private Query_JDBC queryJdbc = BusinessFactory.getImplementation();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titolo.setReorderable(false);
        durata.setReorderable(false);

        System.gc();
    }

    @Override
    public void initializeData(Collezionista data) {
        this.collezionista = data;
        List<Disco> dischi = queryJdbc.getDischiUtente(collezionista.getId());
        List<Traccia> tracklist = new ArrayList<>();
        titolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        durata.setCellValueFactory(new PropertyValueFactory<>("durata"));
        for (Disco d: dischi){
            for(Traccia t: queryJdbc.tracklistDisco(d.getId())){
                table.getItems().add(t);
            }
        }
    }


    @FXML
    public void add() {
        try {
            viewDispatcher.changeStage(vBox.getScene(), vBox, "Aggiungi traccia", "aggiuntaTraccia.fxml", this.collezionista);
        }catch (Exception e) {
            e.printStackTrace();
        }


    }

    @FXML
    public void remove() {
        Traccia traccia = table.getSelectionModel().getSelectedItem();
        queryJdbc.removeTraccia(traccia.getId());
        table.getItems().remove(traccia);
    }

    @FXML
    public void goToHome() {
        try {
            viewDispatcher.changeStage(vBox.getScene(), vBox, "Home", "home.fxml", this.collezionista);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
