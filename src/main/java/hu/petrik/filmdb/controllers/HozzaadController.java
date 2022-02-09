package hu.petrik.filmdb.controllers;

import hu.petrik.filmdb.Controller;
import hu.petrik.filmdb.Film;
import hu.petrik.filmdb.FilmApi;
import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

public class HozzaadController extends Controller {
    @javafx.fxml.FXML
    private TextField inputCim;
    @javafx.fxml.FXML
    private TextField inputKategoria;
    @javafx.fxml.FXML
    private Spinner<Integer> inputHossz;
    @javafx.fxml.FXML
    private ChoiceBox<Integer> inputErtekeles;

    @javafx.fxml.FXML
    public void onHozzaadButtonClick(ActionEvent actionEvent) {
        String cim = inputCim.getText().trim();
        String kategoria = inputKategoria.getText().trim();
        int hossz = 0;
        int ertekelesIndex = inputErtekeles.getSelectionModel().getSelectedIndex();
        if (cim.isEmpty()) {
            alert("Cím megadása kötelező.");
            return;
        }
        if (kategoria.isEmpty()) {
            alert("Kategória megadása kötelező.");
        }
        try {
            hossz = inputHossz.getValue();
        } catch (NullPointerException e) {
            alert("Hossz kiválasztása kötelező.");
            return;
        } catch (Exception e) {
            alert("A hossz csak 1 és 999 közötti szám lehet.");
            return;
        }

        if (ertekelesIndex == -1) {
            alert("Értékelés kiválasztása kötelező.");
            return;
        }

        int ertekeles = inputErtekeles.getValue();

        try {
            Film ujFilm = new Film(0, cim, kategoria, hossz, ertekeles);
            Film letrehozott = FilmApi.filmHozzaadasa(ujFilm);
            if (letrehozott != null) {
                alert("Film hozzáadása sikeres!");
                this.stage.close();
            } else {
                alert("Film hozzáadása sikertelen!");
            }
        } catch (Exception e) {
            hibaKiir(e);
        }
    }
}
