package it.collectors.controller;

import com.mysql.cj.PreparedQuery;
import it.collectors.business.BusinessFactory;
import it.collectors.business.jdbc.Query_JDBC;
import it.collectors.model.*;
import it.collectors.view.Pages;
import it.collectors.view.ViewDispatcher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ProfiloController implements Initializable, DataInitializable<Collezionista>{

    @FXML
    private VBox vBox;

    @FXML
    private Label nick;

    @FXML
    private Label email;

    @FXML
    private Label collezioniTotali;

    @FXML
    private Label minuti;

    @FXML
    private TableView<GenereNumeroDischi> table;

    @FXML
    private TableView<DiscoDuplicati> dischiDuplicati;

    @FXML
    private TableView<AutoreWrapper> minutiTotali;

    @FXML
    private TableColumn<AutoreWrapper, String> autore;

    @FXML
    private TableColumn<AutoreWrapper, String> numeroMinuti;




    @FXML
    private TableColumn<GenereNumeroDischi, String> genere;

    @FXML
    private TableColumn<GenereNumeroDischi, String> numero;

    @FXML
    private TableColumn<DiscoDuplicati, String> nomeDisco;

    @FXML
    private TableColumn<DiscoDuplicati, String> duplicatiDisco;


    Collezionista collezionista;
    private Query_JDBC queryJdbc = BusinessFactory.getImplementation();

    protected class GenereNumeroDischi{
        private Genere genere;
        private int numero;

        public GenereNumeroDischi(Genere genere, int numeroDischi) {
            this.genere = genere;
            this.numero = numeroDischi;
        }
        public String getGenere() {
            return genere.getNome();
        }

        public int getNumero() {
            return numero;
        }

    }
    protected class DiscoDuplicati{
        private Disco disco;
        private int numeroDuplicati;

        public DiscoDuplicati(Disco disco, int numeroDuplicati) {
            this.disco = disco;
            this.numeroDuplicati = numeroDuplicati;
        }

        public String getNomeDisco() {
            return disco.getTitolo();
        }

        public int getNumeroDuplicati() {
            return numeroDuplicati;
        }
    }
    protected class AutoreWrapper{
        Autore autore;
        Integer minuti;

        public AutoreWrapper(Autore autore, Integer minuti){
            this.autore = autore;
            this.minuti = minuti;
        }

        public String getNomeDAutore() {
            return autore.getNomeDAutore();
        }
        public Integer getMinuti() {
            return minuti;
        }
    }

    public int minutiTotaliSistema(){
        int minuti=0;
        List<Autore> autori = queryJdbc.getAutori();
        for(Autore a:autori){
            minuti = minuti + queryJdbc.getMinutiTotaliMusicaPerAutore(a.getId());
        }
        return minuti;
    }


    public List<GenereNumeroDischi> getGenereNumeroDischi(){
        Map<Genere, Integer> statistiche = queryJdbc.getStatisticheDischiPerGenere();
        List<GenereNumeroDischi> genereNumeroDischi = new ArrayList<>();

        for (Map.Entry<Genere, Integer> entry : statistiche.entrySet()) {
            genereNumeroDischi.add(new GenereNumeroDischi(entry.getKey(), entry.getValue()));
        }
        return genereNumeroDischi;
    }

    public List<DiscoDuplicati> getNumeroDischiDuplicati(){
        Map<Disco,Integer> dischi = queryJdbc.getNumeroDuplicatiDischi(collezionista.getId());
        List<DiscoDuplicati> discoDuplicati = new ArrayList<>();

        for(Map.Entry<Disco,Integer> entry : dischi.entrySet()){
            discoDuplicati.add(new DiscoDuplicati(entry.getKey(),entry.getValue()));
        }
        return discoDuplicati;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        genere.setReorderable(false);
        numero.setReorderable(false);
        nomeDisco.setReorderable(false);
        duplicatiDisco.setReorderable(false);
        System.gc();
    }

    @Override
    public void initializeData(Collezionista data) {
        this.collezionista = data;
        nick.setText(collezionista.getNickname());
        email.setText(collezionista.getEmail());
        minuti.setText(Integer.toString(minutiTotaliSistema()));
        collezioniTotali.setText(Integer.toString(queryJdbc.numeroCollezioniCollezionista(collezionista.getId())));

        genere.setCellValueFactory(new PropertyValueFactory<>("genere"));
        numero.setCellValueFactory(new PropertyValueFactory<>("numero"));

        nomeDisco.setCellValueFactory(new PropertyValueFactory<>("nomeDisco"));
        duplicatiDisco.setCellValueFactory(new PropertyValueFactory<>("numeroDuplicati"));

        autore.setCellValueFactory(new PropertyValueFactory<>("NomeDAutore"));
        numeroMinuti.setCellValueFactory(new PropertyValueFactory<>("minuti"));

        for (GenereNumeroDischi gnd : getGenereNumeroDischi()) {
            table.getItems().add(gnd);
        }

        for (DiscoDuplicati dd : getNumeroDischiDuplicati()) {
            dischiDuplicati.getItems().add(dd);
        }

        for(Autore a : queryJdbc.getAutori()){
            minutiTotali.getItems().add(new AutoreWrapper(a,queryJdbc.getMinutiTotaliMusicaPerAutore(a.getId())));
        }


    }


    @FXML
    public void goToHome() {
        ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();
        try {
            viewDispatcher.changeStage(nick.getScene(), vBox, "Home", "home.fxml", this.collezionista);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
