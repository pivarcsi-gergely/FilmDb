package hu.petrik.filmdb;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainController extends Controller {

    @FXML
    private TableView<Film> filmTable;
    @FXML
    private TableColumn<Film, String> colCim;
    @FXML
    private TableColumn<Film, String> colKategoria;
    @FXML
    private TableColumn<Film, Integer> colHossz;
    @FXML
    private TableColumn<Film, Integer> colErtekeles;
    private FilmDB db;

    public void initialize() {
        colCim.setCellValueFactory(new PropertyValueFactory<>("cim")); //a tárolt objektumban egy getCim függvényt fog keresni.
        colKategoria.setCellValueFactory(new PropertyValueFactory<>("kategoria"));
        colHossz.setCellValueFactory(new PropertyValueFactory<>("hossz"));
        colErtekeles.setCellValueFactory(new PropertyValueFactory<>("ertekeles"));
    }

    @FXML
    public void onAddButtonClick(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(FilmApp.class.getResource("hozzaad-viev.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 320, 400);
            stage.setTitle("FilmDb");
            stage.setScene(scene);
            stage.setOnCloseRequest(event -> filmListaFeltolt());
            stage.show();
        }
        catch (Exception e){
            hibaKiir(e);
        }
    }

    @FXML
    public void onEditButtonClick(ActionEvent event) {
    }

    @FXML
    public void onTorlesButtonClick(ActionEvent event) {
        int selectedIndex = filmTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            alert("A törléshez előbb válasszon ki egy elemet a táblázatból");
            return;
        }
        Film torlendoFilm = filmTable.getSelectionModel().getSelectedItem();
        if (!confirm("Biztos, hogy törölni szeretné az alábbi filmet:" + torlendoFilm.getCim())) {
            return;
        }
        else {
            try {
                db.filmTorlese(torlendoFilm.getId());
                alert("Sikeres törlés!");
                filmListaFeltolt();
            } catch (SQLException e) {
                hibaKiir(e);
            }
        }
    }


    private void filmListaFeltolt() {
        try{
            List<Film> filmList = db.getFilmek();
            filmTable.getItems().clear();
            for (Film film: filmList) {
                filmTable.getItems().add(film);
            }
        }catch (SQLException e) {
            hibaKiir(e);
        }
    }
}