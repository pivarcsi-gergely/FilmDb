package hu.petrik.filmdb;

import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.sql.SQLException;

public class HozzaadController {
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
        if (kategoria.isEmpty()){
            alert("Kategória megadása kötelező.");
        }
        try{
            hossz = inputHossz.getValue();
        }
        catch (NullPointerException e){
            alert("Hossz kiválasztása kötelező.");
            return;
        }
        catch (Exception e) {
            alert("A hossz csak 1 és 999 közötti szám lehet.");
            return;
        }

        if (ertekelesIndex == -1) {
            alert("Értékelés kiválasztása kötelező.");
            return;
        }

        int ertekeles = inputErtekeles.getValue();

        try{
            FilmDB db = new FilmDB();
            boolean siker = db.filmHozzaadasa(cim, kategoria, hossz, ertekeles);
            if (siker) {
                alert("Film hozzáadása sikeres!");
            }
            else {
                alert("Film hozzáadása sikertelen!");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }

    private void alert(String uzenet) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setContentText(uzenet);
        alert.getButtonTypes().add(ButtonType.OK);
        alert.show();
    }
}
