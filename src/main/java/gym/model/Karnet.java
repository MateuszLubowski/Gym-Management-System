package gym.model;

import java.time.LocalDate;

public class Karnet {
    private int id;
    private String numer;
    private int klientId;
    private LocalDate dataWydania;
    private LocalDate dataWaznosci;
    private String typ;
    private Integer liczbaDostepnychWejsc;
    private String status;

    public Karnet() {
    }

    public Karnet(int id, String numer, int klientId, LocalDate dataWydania, LocalDate dataWaznosci, String typ, String status) {
        this.id = id;
        this.numer = numer;
        this.klientId = klientId;
        this.dataWydania = dataWydania;
        this.dataWaznosci = dataWaznosci;
        this.typ = typ;
        this.status = status;
    }
    //Sprawdzanie czy karnet jest aktywny
    public boolean isAktywny() {
        return "AKTYWNY".equals(status) && (dataWaznosci == null || !dataWaznosci.isBefore(LocalDate.now()));
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumer() {
        return numer;
    }

    public void setNumer(String numer) {
        this.numer = numer;
    }

    public int getKlientId() {
        return klientId;
    }

    public void setKlientId(int klientId) {
        this.klientId = klientId;
    }

    public LocalDate getDataWydania() {
        return dataWydania;
    }

    public void setDataWydania(LocalDate dataWydania) {
        this.dataWydania = dataWydania;
    }

    public LocalDate getDataWaznosci() {
        return dataWaznosci;
    }

    public void setDataWaznosci(LocalDate dataWaznosci) {
        this.dataWaznosci = dataWaznosci;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public Integer getLiczbaDostepnychWejsc() {
        return liczbaDostepnychWejsc;
    }

    public void setLiczbaDostepnychWejsc(Integer liczbaDostepnychWejsc) {
        this.liczbaDostepnychWejsc = liczbaDostepnychWejsc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return numer + " (" + typ + ") - ważny do: " + dataWaznosci;
    }
}