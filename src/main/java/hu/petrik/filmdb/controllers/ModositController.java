package hu.petrik.filmdb.controllers;

import hu.petrik.filmdb.Controller;
import hu.petrik.filmdb.Film;
import hu.petrik.filmdb.FilmDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class ModositController extends Controller {
    @FXML
    private TextField inputCim;
    @FXML
    private TextField inputKategoria;
    @FXML
    private Spinner<Integer> inputHossz;
    @FXML
    private ChoiceBox<Integer> inputErtekeles;
    private Film modositando;

    public Film getModositando() {
        return modositando;
    }

    public void setModositando(Film modositando) {
        this.modositando = modositando;
        ertekekBeallitasa();
    }

    private void ertekekBeallitasa() {
        inputCim.setText(modositando.getCim());
        inputKategoria.setText(modositando.getKategoria());
        inputHossz.getValueFactory().setValue(modositando.getHossz());
        inputErtekeles.setValue(modositando.getErtekeles());
    }

    @FXML
    public void onModositButtonClick(ActionEvent actionEvent) {
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

        modositando.setCim(cim);
        modositando.setKategoria(kategoria);
        modositando.setHossz(hossz);
        modositando.setErtekeles(ertekeles);

        try {
            FilmDB db = new FilmDB();
            if (db.filmModositasa(modositando)) {
                alertWait("Sikeres módosítás");
                this.stage.close();
            }
            else {
                alert("Sikertelen módosítás");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}
