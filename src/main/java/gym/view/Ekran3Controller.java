package gym.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;


public class Ekran3Controller {

    @FXML private Label lblNumer;
    @FXML private Label lblStrefa;
    @FXML private Label lblData;
    @FXML private Label lblGodziny;
    @FXML private Label lblTrener;
    @FXML private Label lblKosztStrefy;
    @FXML private Label lblKosztTrenera;
    @FXML private Label lblRazem;
    @FXML private Label lblStatus;
    @FXML private Label lblEmail;

    private RezerwacjaController mainController;
    private String numerRezerwacji;

    public void setMainController(RezerwacjaController controller) {
        this.mainController = controller;
    }

    public void setNumerRezerwacji(String numer) {
        this.numerRezerwacji = numer;
        loadData();
    }

    private void loadData() {
        try {
            System.out.println("DEBUG: Ładowanie danych Ekran3...");

            // Numer
            lblNumer.setText(numerRezerwacji);

            // Szczegóły
            lblStrefa.setText(mainController.getWybranaStrefa().getNazwa());
            lblData.setText(formatData(mainController.getWybranaData()));
            lblGodziny.setText(mainController.getWybranyPrzedzial());

            if (mainController.getWybranyTrener() != null) {
                lblTrener.setText(mainController.getWybranyTrener().getImie() + " " + mainController.getWybranyTrener().getNazwisko() + " (" + mainController.getWybranyTrener().getTypTrenera() + ")");
            } else {
                lblTrener.setText("Brak");
            }

            //Koszty
            double kosztStrefy = mainController.getWybranaStrefa().getCenaZaGodzine();
            if (mainController.getKarnet() != null) {
                kosztStrefy = kosztStrefy * 0.5;
                lblKosztStrefy.setText(kosztStrefy + " PLN (karnet -50%)");
            } else {
                lblKosztStrefy.setText(kosztStrefy + " PLN");
            }

            double kosztTrenera = 0;
            if (mainController.getWybranyTrener() != null) {
                kosztTrenera = mainController.getWybranyTrener().getStawkaZaGodzine() * 2;
            }
            lblKosztTrenera.setText(kosztTrenera + " PLN");

            double razem = kosztStrefy + kosztTrenera;
            lblRazem.setText(razem + " PLN");

            //Status
            lblStatus.setText("Płatność: Zrealizowana (" + mainController.getMetodaPlatnosci() + ")");

            //Email
            lblEmail.setText("Potwierdzenie wysłano na email: " + mainController.getKlient().getEmail());

            System.out.println("✅ Ekran3 załadowany pomyślnie!");

        } catch (Exception e) {
            System.err.println("BŁĄD w loadData Ekran3: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePDF() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Pobieranie PDF");
        alert.setContentText("Funkcja niedostępna");
        alert.showAndWait();
    }

    @FXML
    private void handleMojeRezerwacje() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Moje rezerwacje");
        alert.setContentText("Funkcja niedostępna");
        alert.showAndWait();
    }

    @FXML
    private void handleZamknij() {
        Stage stage = (Stage) lblNumer.getScene().getWindow();
        stage.close();
    }

    private String formatData(String data) {
        LocalDate date = LocalDate.parse(data);
        String dzien = date.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("pl"));
        return date.getDayOfMonth() + " " + date.getMonth().getDisplayName(TextStyle.FULL, new Locale("pl")) + " " + date.getYear() + " (" + dzien + ")";
    }
}
