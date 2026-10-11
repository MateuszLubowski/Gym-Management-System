package gym.view;

import gym.dao.KlientDAO;
import gym.dao.StrefaDAO;
import gym.model.Harmonogram;
import gym.model.Klient;
import gym.model.Strefa;
import gym.util.DataLoader;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Ekran1Controller {

    @FXML private ComboBox<Strefa> cmbStrefa;
    @FXML private DatePicker datePicker;
    @FXML private Label lblDataInfo;
    @FXML private ComboBox<String> cmbPrzedzial;
    @FXML private Label lblError;
    @FXML private Button btnDalej;

    private List<Strefa> strefy;
    private RezerwacjaController mainController;

    @FXML
    public void initialize() {
        System.out.println("=== Ekran1Controller ===");

        loadStrefy();

        //Ustawianie domyślnej daty jako jutro
        datePicker.setValue(LocalDate.now().plusDays(1));

        //Blokujemy przeszłosc
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });

        cmbStrefa.setOnAction(e -> {
            System.out.println("DEBUG: Wybrano strefę");
            updatePrzedzialy();
        });

        datePicker.setOnAction(e -> {
            System.out.println("DEBUG: Wybrano datę");
            try {
                updateDataInfo();
                updatePrzedzialy();
            } catch (Exception ex) {
                System.err.println("BŁĄD w datePicker event: " + ex.getMessage());
                ex.printStackTrace();
                showError("Błąd: " + ex.getMessage());
            }
        });

        cmbPrzedzial.setOnAction(e -> {
            btnDalej.setDisable(cmbPrzedzial.getValue() == null);
        });

        updateDataInfo();

        System.out.println("=== ZAKOŃCZONY ===");
    }
//Zapisanie referencji do głównego kontrolera
    public void setMainController(RezerwacjaController controller) {

        this.mainController = controller;
    }


    private void loadStrefy() {
        try {
            //ładuje strefy Z harmonogramami
            strefy = DataLoader.loadStrefyWithHarmonogramy();

            //Dodajemy do ComboBox
            cmbStrefa.getItems().addAll(strefy);

        } catch (Exception e) {
            showError("Błąd ładowania stref: " + e.getMessage());
            e.printStackTrace();
        }
    }


    //Aktualizuje info o wybranej dacie
    private void updateDataInfo() {
        if (datePicker.getValue() != null) {
            LocalDate data = datePicker.getValue();
            String dzien = data.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("pl"));

            lblDataInfo.setText("Wybrano: " + data.getDayOfMonth() + " " + data.getMonth().getDisplayName(TextStyle.FULL, new Locale("pl")) + " " + data.getYear() + " (" + dzien + ")");
            lblDataInfo.setVisible(true);
        }
    }


    //Aktualizuje przedziały czasowe
    private void updatePrzedzialy() {
        try {
            System.out.println("=== UPDATE PRZEDZIAŁY ===");

            cmbPrzedzial.getItems().clear();
            btnDalej.setDisable(true);
            lblError.setVisible(false);

            Strefa strefa = cmbStrefa.getValue(); //pobiera aktualna wybrana strefe z comboboxa i zwraca caly obiekt Strefa z danymi i lista harmonogramow
            LocalDate data = datePicker.getValue(); // pobiera wybrana date

            System.out.println("DEBUG: Strefa = " + (strefa != null ? strefa.getNazwa() : "NULL"));
            System.out.println("DEBUG: Data = " + (data != null ? data : "NULL"));

            if (strefa == null || data == null) {
                System.out.println("DEBUG: Brak strefy lub daty - RETURN");
                return;
            }

            DayOfWeek dzien = data.getDayOfWeek();
            System.out.println("DEBUG: Dzień tygodnia: " + dzien);
            //Asocjacja
            List<Harmonogram> harmonogramy = strefa.getHarmonogramy();
            System.out.println("DEBUG: Liczba harmonogramów: " + harmonogramy.size());

            Harmonogram harmonogram = harmonogramy.stream()
                    .filter(h -> h.getDzienTygodnia() == dzien)
                    .findFirst()
                    .orElse(null);

            if (harmonogram == null) {
                System.out.println("DEBUG: Brak harmonogramu dla dnia: " + dzien);
                showError("Strefa zamknięta w tym dniu");
                return;
            }

            //Sprawdzenie godzin
            LocalTime otwarcie = harmonogram.getGodzinaOtwarcia();
            LocalTime zamkniecie = harmonogram.getGodzinaZamkniecia();

            System.out.println("DEBUG: Godziny: " + otwarcie + " - " + zamkniecie);

            if (otwarcie == null || zamkniecie == null) {
                showError("Błąd: brak godzin otwarcia");
                return;
            }

            if (otwarcie.isAfter(zamkniecie) || otwarcie.equals(zamkniecie)) {
                showError("Błąd: nieprawidłowe godziny");
                return;
            }

            //Generowanie przedziałów
            List<String> przedzialy = generatePrzedzialy(otwarcie, zamkniecie);

            if (przedzialy.isEmpty()) {
                showError("Nie można wygenerować przedziałów");
                return;
            }

            System.out.println("DEBUG: Dodaję " + przedzialy.size() + " przedziałów do ComboBox");
            cmbPrzedzial.getItems().addAll(przedzialy);

            System.out.println("=== UPDATE PRZEDZIAŁY ZAKOŃCZONY ===");

        } catch (Exception e) {
            System.err.println("BŁĄD w updatePrzedzialy: " + e.getMessage());
            e.printStackTrace();
            showError("Błąd: " + e.getMessage());
        }
    }

    private List<String> generatePrzedzialy(LocalTime start, LocalTime end) {
        List<String> przedzialy = new ArrayList<>();

        if (start == null || end == null) {
            System.err.println("BŁĄD: start lub end is NULL!");
            return przedzialy;
        }

        if (start.isAfter(end) || start.equals(end)) {
            System.err.println("BŁĄD: start >= end! start=" + start + ", end=" + end);
            return przedzialy;
        }

        LocalTime current = start;
        int maxIterations = 20; //Bezpiecznik - max 20 przedziałów (40h) 1 przedział to 2h
        int count = 0;

        System.out.println("DEBUG generatePrzedzialy: start=" + start + ", end=" + end);

        while (count < maxIterations) {
            LocalTime next = current.plusHours(2);

            System.out.println("DEBUG: Iteracja " + count + ": current=" + current + ", next=" + next + ", end=" + end);

            //Sprawdzenie czy next przekracza end
            if (next.isAfter(end)) {
                System.out.println("DEBUG: next > end, BREAK");
                break;
            }

            String przedzial = current.toString() + " - " + next.toString();
            przedzialy.add(przedzial);
            System.out.println("DEBUG: Dodano przedział: " + przedzial);

            current = next;
            count++;

            //Dodatkowy bezpiecznik - jeśli osiągnęliśmy koniec
            if (current.equals(end) || current.isAfter(end)) {
                System.out.println("DEBUG: current >= end, BREAK");
                break;
            }
        }

        if (count >= maxIterations) {
            System.err.println("OSTRZEŻENIE: Osiągnięto limit iteracji!");
        }

        System.out.println("DEBUG: Wygenerowano " + przedzialy.size() + " przedziałów");
        return przedzialy;
    }

    //Przycisk Anuluj
    @FXML
    private void handleAnuluj() {
        Stage stage = (Stage) btnDalej.getScene().getWindow();
        stage.close();
    }


    //Przycisk Dalej
    @FXML
    private void handleDalej() {
        try {
            System.out.println("DEBUG: handleDalej - START");

            if (cmbStrefa.getValue() == null || datePicker.getValue() == null || cmbPrzedzial.getValue() == null) {
                showError("Wypełnij wszystkie pola!");
                return;
            }

            //Zapisanie danych w głównym kontrolerze
            mainController.setWybranaStrefa(cmbStrefa.getValue());
            mainController.setWybranaData(datePicker.getValue().toString());
            mainController.setWybranyPrzedzial(cmbPrzedzial.getValue());

            System.out.println("DEBUG: Zapisano wybór - strefa: " + cmbStrefa.getValue().getNazwa() + ", data: " + datePicker.getValue() + ", przedział: " + cmbPrzedzial.getValue());

            //Załadowanie testowego klienta
            KlientDAO klientDAO = new KlientDAO();
            Klient klient = klientDAO.findById(1);
            mainController.setKlient(klient);

            System.out.println("DEBUG: Załadowano klienta: " + klient.getEmail());

            //Przejście do Ekranu 2
            System.out.println("DEBUG: Przechodzę do Ekranu 2...");
            mainController.pokazEkran2();

        } catch (Exception e) {
            System.err.println("BŁĄD w handleDalej: " + e.getMessage());
            showError("Błąd: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        lblError.setText("Error " + message);
        lblError.setVisible(true);
    }
}
