package hu.petrik.filmdb.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import hu.petrik.filmdb.*;
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
import java.lang.reflect.Type;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static javafx.scene.input.KeyCode.T;

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
        filmListaFeltolt();
    }

    @FXML
    public void onAddButtonClick(ActionEvent actionEvent) {
        try {
            Controller hozzaadas = ujAblak("hozzaad-viev.fxml", "Film hozzáadása", 320, 400);
            hozzaadas.getStage().setOnCloseRequest(event -> filmListaFeltolt());
            hozzaadas.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onEditButtonClick(ActionEvent actionEvent) {
        int selectedIndex = filmTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            alert("A módosításhoz előbb válasszon ki egy elemet a táblázatból");
            return;
        }
        Film modositandoFilm = filmTable.getSelectionModel().getSelectedItem();
        try {
            ModositController modositas = (ModositController) ujAblak("modosit-view.fxml", "Film módosítása", 320, 400);
            modositas.setModositando(modositandoFilm);
            modositas.getStage().setOnHiding(event -> filmTable.refresh());
            modositas.getStage().setOnCloseRequest(event -> filmListaFeltolt());
            modositas.getStage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        } else {
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
        try {
            Response response = RequestHandler.get("http://localhost/14s/pgm/film_db_rest/api/film");
            String json = response.getContent();
            if (response.getResponseCode() >= 400) {
                System.out.println(json);
                return;
            }
            else {
                Gson Gayson = new Gson();
                Type type = new TypeToken<List<Film>>(){}.getType();
                List<Film> filmList = Gayson.fromJson(json, type);
                filmTable.getItems().clear();
                for (Film film : filmList) {
                    filmTable.getItems().add(film);
                }
            }
        } catch (IOException e) {
            hibaKiir(e);
        }
    }
}