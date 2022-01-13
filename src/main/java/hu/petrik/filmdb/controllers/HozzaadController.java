package hu.petrik.filmdb.controllers;

import hu.petrik.filmdb.Controller;
import hu.petrik.filmdb.FilmDB;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

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
            FilmDB db = new FilmDB();
            int siker = db.filmHozzaadasa(cim, kategoria, hossz, ertekeles);
            if (siker == 1) {
                alert("Film hozzáadása sikeres!");
            } else {
                alert("Film hozzáadása sikertelen!");
            }
        } catch (Exception e) {
            hibaKiir(e);
        }
    }
}
