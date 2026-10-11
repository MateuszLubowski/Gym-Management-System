package gym.model;

import java.time.LocalDate;

public class Promocja {
    private int id;
    private String kod;
    private double znizka;
    private LocalDate dataWaznosci;
    private String opis;
    private String status;

    public Promocja() {
    }

    public Promocja(int id, String kod, double znizka, LocalDate dataWaznosci, String status) {
        this.id = id;
        this.kod = kod;
        this.znizka = znizka;
        this.dataWaznosci = dataWaznosci;
        this.status = status;
    }

    public boolean isAktywna() {
        return "AKTYWNA".equals(status) && (dataWaznosci == null || !dataWaznosci.isBefore(LocalDate.now()));
    }

    public double obliczCenePoZnizce(double cenaOryginalna) {
        return cenaOryginalna * (1 - znizka / 100.0);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKod() {
        return kod;
    }

    public void setKod(String kod) {
        this.kod = kod;
    }

    public double getZnizka() {
        return znizka;
    }

    public void setZnizka(double znizka) {
        this.znizka = znizka;
    }

    public LocalDate getDataWaznosci() {
        return dataWaznosci;
    }

    public void setDataWaznosci(LocalDate dataWaznosci) {
        this.dataWaznosci = dataWaznosci;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return kod + " - " + znizka + "% (ważna do: " + dataWaznosci + ")";
    }
}
