package gym.view;

import gym.model.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RezerwacjaController {
    private Stage stage;

    private Strefa wybranaStrefa;
    private Harmonogram wybranyHarmonogram;
    private String wybranaData;
    private String wybranyPrzedzial;
    private Klient klient;
    private TrenerPersonalny wybranyTrener;
    private Karnet karnet;
    private String metodaPlatnosci;

    public RezerwacjaController(Stage stage) {
        this.stage = stage;
    }

    public void pokazEkran1() {
        try {
            System.out.println("DEBUG: Ładowanie Ekran1.fxml...");

            java.net.URL fxmlUrl = RezerwacjaController.class.getResource("/gym/view/Ekran1.fxml");

            if (fxmlUrl == null) {
                System.err.println("NIE ZNALEZIONO /gym/view/Ekran1.fxml");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            Ekran1Controller controller = loader.getController();
            controller.setMainController(this);

            stage.setScene(new Scene(root, 600, 750));
            stage.setTitle("Rezerwacja - Krok 1/3");

            System.out.println("Ekran1 załadowany!");

        } catch (Exception e) {
            System.err.println("Błąd ładowania Ekran1: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void pokazEkran2() {
        try {
            System.out.println("DEBUG: Ładowanie Ekran2.fxml...");

            java.net.URL fxmlUrl = RezerwacjaController.class.getResource("/gym/view/Ekran2.fxml");

            if (fxmlUrl == null) {
                System.err.println("NIE ZNALEZIONO /gym/view/Ekran2.fxml");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            Ekran2Controller controller = loader.getController();
            controller.setMainController(this);

            stage.setScene(new Scene(root, 600, 850));
            stage.setTitle("Rezerwacja - Krok 2/3");

            System.out.println("Ekran2 załadowany!");

        } catch (Exception e) {
            System.err.println("Błąd ładowania Ekran2: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void pokazEkran3(String numerRezerwacji) {
        try {
            System.out.println("DEBUG: Ładowanie Ekran3.fxml...");

            java.net.URL fxmlUrl = RezerwacjaController.class.getResource("/gym/view/Ekran3.fxml");

            if (fxmlUrl == null) {
                System.err.println("NIE ZNALEZIONO /gym/view/Ekran3.fxml");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            Ekran3Controller controller = loader.getController();
            controller.setMainController(this);
            controller.setNumerRezerwacji(numerRezerwacji);

            stage.setScene(new Scene(root, 600, 800));
            stage.setTitle("Rezerwacja - Krok 3/3");

            System.out.println("Ekran3 załadowany!");

        } catch (Exception e) {
            System.err.println("Błąd ładowania Ekran3: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Stage getStage() {
        return stage;
    }

    public Strefa getWybranaStrefa() {
        return wybranaStrefa;
    }

    public void setWybranaStrefa(Strefa wybranaStrefa) {
        this.wybranaStrefa = wybranaStrefa;
        System.out.println("DEBUG: Ustawiono strefę: " + wybranaStrefa.getNazwa());
    }

    public Harmonogram getWybranyHarmonogram() {
        return wybranyHarmonogram;
    }

    public void setWybranyHarmonogram(Harmonogram wybranyHarmonogram) {
        this.wybranyHarmonogram = wybranyHarmonogram;
    }

    public String getWybranaData() {
        return wybranaData;
    }

    public void setWybranaData(String wybranaData) {
        this.wybranaData = wybranaData;
        System.out.println("DEBUG: Ustawiono datę: " + wybranaData);
    }

    public String getWybranyPrzedzial() {
        return wybranyPrzedzial;
    }

    public void setWybranyPrzedzial(String wybranyPrzedzial) {
        this.wybranyPrzedzial = wybranyPrzedzial;
        System.out.println("DEBUG: Ustawiono przedział: " + wybranyPrzedzial);
    }

    public Klient getKlient() {
        return klient;
    }

    public void setKlient(Klient klient) {
        this.klient = klient;
        System.out.println("DEBUG: Ustawiono klienta: " + klient.getEmail());
    }

    public TrenerPersonalny getWybranyTrener() {
        return wybranyTrener;
    }

    public void setWybranyTrener(TrenerPersonalny wybranyTrener) {
        this.wybranyTrener = wybranyTrener;
        if (wybranyTrener != null) {
            System.out.println("DEBUG: Ustawiono trenera: " + wybranyTrener.getImie() + " " + wybranyTrener.getNazwisko());
        } else {
            System.out.println("DEBUG: Usunięto trenera");
        }
    }

    public Karnet getKarnet() {
        return karnet;
    }

    public void setKarnet(Karnet karnet) {
        this.karnet = karnet;
        if (karnet != null) {
            System.out.println("DEBUG: Ustawiono karnet: " + karnet.getTyp());
        } else {
            System.out.println("DEBUG: Usunięto karnet");
        }
    }

    public String getMetodaPlatnosci() {
        return metodaPlatnosci;
    }

    public void setMetodaPlatnosci(String metodaPlatnosci) {
        this.metodaPlatnosci = metodaPlatnosci;
        System.out.println("DEBUG: Ustawiono metodę płatności: " + metodaPlatnosci);
    }
}
