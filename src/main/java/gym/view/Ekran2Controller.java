package gym.view;

import gym.dao.*;
import gym.model.*;
import gym.model.enums.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class Ekran2Controller {

    @FXML private Label lblStrefa;
    @FXML private Label lblData;
    @FXML private Label lblGodziny;
    @FXML private Label lblKosztStrefy;

    @FXML private CheckBox chkTrener;
    @FXML private ComboBox<TrenerPersonalny> cmbTrener;
    @FXML private Label lblKosztTrenera;

    @FXML private CheckBox chkKarnet;
    @FXML private Label lblKarnetInfo;
    @FXML private Label lblNowyKoszt;

    @FXML private Label lblStrefaKoszt;
    @FXML private Label lblTrenerKoszt;
    @FXML private Label lblRazem;

    @FXML private RadioButton rbKarta;
    @FXML private RadioButton rbPrzelew;
    @FXML private ToggleGroup metodaGroup;

    @FXML private Label lblError;

    private RezerwacjaController mainController;
    private double kosztStrefy;
    private double kosztTrenera = 0;
    private boolean maKarnet = false;
    private Karnet karnet;

    @FXML
    public void initialize() {
        System.out.println("=== Ekran2Controller ===");

        //checkbox trenera
        chkTrener.selectedProperty().addListener((obs, oldVal, newVal) -> {
            cmbTrener.setDisable(!newVal);
            if (!newVal) {
                cmbTrener.getSelectionModel().clearSelection();
                kosztTrenera = 0;
                lblKosztTrenera.setVisible(false);
            }
            updateKoszt();
        });

        //Wybór trenera
        cmbTrener.setOnAction(e -> {
            TrenerPersonalny trener = cmbTrener.getValue();
            if (trener != null) {
                kosztTrenera = trener.getStawkaZaGodzine() * 2;
                lblKosztTrenera.setText("Koszt trenera: " + kosztTrenera + " PLN (2h × " + trener.getStawkaZaGodzine() + " PLN/h)");
                lblKosztTrenera.setVisible(true);
                mainController.setWybranyTrener(trener);
            } else {
                kosztTrenera = 0;
                lblKosztTrenera.setVisible(false);
                mainController.setWybranyTrener(null);
            }
            updateKoszt();
        });

        //checkbox karnetu
        chkKarnet.selectedProperty().addListener((obs, oldVal, newVal) -> {
            maKarnet = newVal;
            if (newVal && karnet != null) {
                double znizka = kosztStrefy * 0.5;
                lblNowyKoszt.setText("Nowy koszt strefy: " + (kosztStrefy - znizka) + " PLN (-50%)");
                lblNowyKoszt.setVisible(true);
                mainController.setKarnet(karnet);
            } else {
                lblNowyKoszt.setVisible(false);
                mainController.setKarnet(null);
            }
            updateKoszt();
        });
    }

    public void setMainController(RezerwacjaController controller) {
        this.mainController = controller;
        loadData();
    }

    private void loadData() {
        try {
            System.out.println("DEBUG: Ładowanie danych Ekran2...");

            //Podsumowanie
            Strefa strefa = mainController.getWybranaStrefa();
            String data = mainController.getWybranaData();
            String przedzial = mainController.getWybranyPrzedzial();

            lblStrefa.setText(strefa.getNazwa());
            lblData.setText(formatData(data));
            lblGodziny.setText(przedzial);

            kosztStrefy = strefa.getCenaZaGodzine();
            lblKosztStrefy.setText(kosztStrefy + " PLN");

            //Załadowanie trenerów
            TrenerPersonalnyDAO trenerDAO = new TrenerPersonalnyDAO();
            List<TrenerPersonalny> trenerzy = trenerDAO.findAll();
            cmbTrener.getItems().addAll(trenerzy);

            //Sprawdzenie karnetow
            KarnetDAO karnetDAO = new KarnetDAO();
            Klient klient = mainController.getKlient();
            karnet = karnetDAO.findAktywnyByKlientId(klient.getId());

            if (karnet != null) {
                chkKarnet.setDisable(false);
                lblKarnetInfo.setText("Posiadasz aktywny karnet!\n" + "Typ: " + karnet.getTyp() + "\n" + "Ważny do: " + karnet.getDataWaznosci());
                lblKarnetInfo.setStyle("-fx-background-color: #d4edda; -fx-padding: 10; " + "-fx-background-radius: 5; -fx-text-fill: #155724;");
            } else {
                lblKarnetInfo.setText("Nie posiadasz aktywnego karnetu.");
                lblKarnetInfo.setStyle("-fx-text-fill: #666;");
            }

            //Początkowy koszt
            updateKoszt();

            System.out.println("DEBUG: Dane załadowane pomyślnie");

        } catch (Exception e) {
            System.err.println("BŁĄD w loadData: " + e.getMessage());
            e.printStackTrace();
            showError("Błąd ładowania danych: " + e.getMessage());
        }
    }

    private void updateKoszt() {
        double strefaKoszt = maKarnet ? kosztStrefy * 0.5 : kosztStrefy;

        String strefaTekst = strefaKoszt + " PLN";
        if (maKarnet) strefaTekst += " (karnet -50%)";

        lblStrefaKoszt.setText(strefaTekst);
        lblTrenerKoszt.setText(kosztTrenera + " PLN");

        double razem = strefaKoszt + kosztTrenera;
        lblRazem.setText(razem + " PLN");

        System.out.println("DEBUG: Koszt zaktualizowany - RAZEM: " + razem + " PLN");
    }

//Przycisk Wstecz
    @FXML
    private void handleWstecz() {
        mainController.pokazEkran1();
    }

//Przycisk Anuluj
    @FXML
    private void handleAnuluj() {
        Stage stage = (Stage) lblStrefa.getScene().getWindow();
        stage.close();
    }


 //Przycisk Zapłać - ZAPIS DO BAZY + WALIDACJA
    @FXML
    private void handleZaplac() {
        try {
            System.out.println("DEBUG: handleZaplac - START");

            //Walidacja metody płatności
            if (metodaGroup.getSelectedToggle() == null) {
                showError("Wybierz metodę płatności!");
                return;
            }

            String przedzial = mainController.getWybranyPrzedzial();
            String[] godziny = przedzial.split(" - ");
            LocalTime start = LocalTime.parse(godziny[0]);
            LocalTime end = LocalTime.parse(godziny[1]);

            System.out.println("DEBUG: Przedział: " + start + " - " + end);

            RezerwacjaDAO rezerwacjaDAO = new RezerwacjaDAO();

            //Sprawdzenie dostępnosci strefy
            boolean dostepna = rezerwacjaDAO.isStrefaDostepna(
                    mainController.getWybranaStrefa().getId(),
                    LocalDate.parse(mainController.getWybranaData()),
                    start,
                    end
            );

            if (!dostepna) {
                showError("Strefa jest już zarezerwowana w tym terminie! Wybierz inny przedział czasowy.");
                System.out.println("Strefa niedostępna - konflikt rezerwacji!");
                return;
            }

            System.out.println("Strefa dostępna!");

            //Sprawdzenie dostępnosci trenera
            if (mainController.getWybranyTrener() != null) {
                boolean trenerDostepny = rezerwacjaDAO.isTrenerDostepny(
                        mainController.getWybranyTrener().getId(),
                        LocalDate.parse(mainController.getWybranaData()),
                        start,
                        end
                );

                if (!trenerDostepny) {
                    showError("Wybrany trener jest już zajęty w tym terminie! Wybierz innego trenera lub inny przedział.");
                    System.out.println("Trener niedostępny - konflikt rezerwacji!");
                    return;
                }

                System.out.println("Trener dostępny!");
            }

            //Stworzenie rezerwacji
            Rezerwacja rezerwacja = new Rezerwacja();

            rezerwacja.setNumer(rezerwacjaDAO.generateNumerRezerwacji());
            rezerwacja.setKlientId(mainController.getKlient().getId());
            rezerwacja.setStrefaId(mainController.getWybranaStrefa().getId());

            if (mainController.getWybranyTrener() != null) {
                rezerwacja.setTrenerId(mainController.getWybranyTrener().getId());
            }

            if (mainController.getKarnet() != null) {
                rezerwacja.setKarnetId(mainController.getKarnet().getId());
            }

            rezerwacja.setDataRezerwacji(LocalDate.parse(mainController.getWybranaData()));
            rezerwacja.setGodzinaRozpoczecia(start);
            rezerwacja.setGodzinaZakonczenia(end);
            rezerwacja.setStatus(StatusRezerwacji.ZAAKCEPTOWANA);

            double strefaKoszt = maKarnet ? kosztStrefy * 0.5 : kosztStrefy;
            rezerwacja.setKosztStrefy(strefaKoszt);
            rezerwacja.setKosztTrenera(kosztTrenera);

            System.out.println("DEBUG: Zapisuję rezerwację...");
            rezerwacjaDAO.save(rezerwacja);
            System.out.println("✅ Rezerwacja zapisana: " + rezerwacja.getNumer());

            //Stowrzenie płatnosci
            PlatnoscDAO platnoscDAO = new PlatnoscDAO();
            Platnosc platnosc = new Platnosc();

            platnosc.setRezerwacjaId(rezerwacja.getId());
            platnosc.setKwota(rezerwacja.obliczCalkowyKoszt());
            platnosc.setDataPlatnosci(LocalDateTime.now());

            RadioButton selected = (RadioButton) metodaGroup.getSelectedToggle();
            String metoda = selected == rbKarta ? "KARTA" : "PRZELEW";
            platnosc.setMetodaPlatnosci(MetodaPlatnosci.valueOf(metoda));
            platnosc.setStatusPlatnosci("ZREALIZOWANA");
            platnosc.setTypPlatnosci("REZERWACJA");

            System.out.println("DEBUG: Zapisuję płatność...");
            platnoscDAO.save(platnosc);
            System.out.println("✅ Płatność zapisana: " + platnosc.getKwota() + " PLN");

            //Zapisanie metody
            mainController.setMetodaPlatnosci(metoda);

            //Przejscie do ekranu 3ciego
            System.out.println("DEBUG: Przechodzę do Ekranu 3...");
            mainController.pokazEkran3(rezerwacja.getNumer());

        } catch (Exception e) {
            System.err.println("BŁĄD w handleZaplac: " + e.getMessage());
            e.printStackTrace();
            showError("Błąd zapisu: " + e.getMessage());
        }
    }

    private String formatData(String data) {
        LocalDate date = LocalDate.parse(data);
        String dzien = date.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("pl"));
        return date.getDayOfMonth() + " " + date.getMonth().getDisplayName(TextStyle.FULL, new Locale("pl")) + " " + date.getYear() + " (" + dzien + ")";
    }

    private void showError(String message) {
        lblError.setText(message);
        lblError.setVisible(true);
    }
}