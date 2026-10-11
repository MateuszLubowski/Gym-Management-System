package gym.model;

import java.time.LocalDate;

public class PlanTreningowy {
    private int id;
    private int trenerId;
    private int klientId;
    private LocalDate dataUtworzenia;
    private int okresWaznosci;
    private String opis;
    private String cel;

    public PlanTreningowy() {
    }

    public PlanTreningowy(int id, int trenerId, int klientId, LocalDate dataUtworzenia, int okresWaznosci) {
        this.id = id;
        this.trenerId = trenerId;
        this.klientId = klientId;
        this.dataUtworzenia = dataUtworzenia;
        this.okresWaznosci = okresWaznosci;
    }

    //Sprawdza czy plan jest jeszcze ważny
    public boolean isWazny() {
        if (dataUtworzenia == null) return false;
        LocalDate dataWygasniecia = dataUtworzenia.plusDays(okresWaznosci);
        return !dataWygasniecia.isBefore(LocalDate.now());
    }

    //Obliczanie datę wygaśnięcia
    public LocalDate getDataWygasniecia() {
        if (dataUtworzenia == null) return null;
        return dataUtworzenia.plusDays(okresWaznosci);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTrenerId() {
        return trenerId;
    }

    public void setTrenerId(int trenerId) {
        this.trenerId = trenerId;
    }

    public int getKlientId() {
        return klientId;
    }

    public void setKlientId(int klientId) {
        this.klientId = klientId;
    }

    public LocalDate getDataUtworzenia() {
        return dataUtworzenia;
    }

    public void setDataUtworzenia(LocalDate dataUtworzenia) {
        this.dataUtworzenia = dataUtworzenia;
    }

    public int getOkresWaznosci() {
        return okresWaznosci;
    }

    public void setOkresWaznosci(int okresWaznosci) {
        this.okresWaznosci = okresWaznosci;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getCel() {
        return cel;
    }

    public void setCel(String cel) {
        this.cel = cel;
    }

    @Override
    public String toString() {
        return "Plan treningowy (ważny do: " + getDataWygasniecia() + ")";
    }
}
