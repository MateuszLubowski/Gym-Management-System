package gym.model;

import gym.model.enums.MetodaPlatnosci;
import java.time.LocalDateTime;

public class Platnosc {
    private int id;
    private int rezerwacjaId;
    private double kwota;
    private LocalDateTime dataPlatnosci;
    private MetodaPlatnosci metodaPlatnosci;
    private String statusPlatnosci;
    private String typPlatnosci;

    // Konstruktory
    public Platnosc() {
    }

    public Platnosc(int rezerwacjaId, double kwota, MetodaPlatnosci metodaPlatnosci, String typPlatnosci) {
        this.rezerwacjaId = rezerwacjaId;
        this.kwota = kwota;
        this.dataPlatnosci = LocalDateTime.now();
        this.metodaPlatnosci = metodaPlatnosci;
        this.statusPlatnosci = "ZREALIZOWANA";
        this.typPlatnosci = typPlatnosci;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRezerwacjaId() {
        return rezerwacjaId;
    }

    public void setRezerwacjaId(int rezerwacjaId) {
        this.rezerwacjaId = rezerwacjaId;
    }

    public double getKwota() {
        return kwota;
    }

    public void setKwota(double kwota) {
        this.kwota = kwota;
    }

    public LocalDateTime getDataPlatnosci() {
        return dataPlatnosci;
    }

    public void setDataPlatnosci(LocalDateTime dataPlatnosci) {
        this.dataPlatnosci = dataPlatnosci;
    }

    public MetodaPlatnosci getMetodaPlatnosci() {
        return metodaPlatnosci;
    }

    public void setMetodaPlatnosci(MetodaPlatnosci metodaPlatnosci) {
        this.metodaPlatnosci = metodaPlatnosci;
    }

    public String getStatusPlatnosci() {
        return statusPlatnosci;
    }

    public void setStatusPlatnosci(String statusPlatnosci) {
        this.statusPlatnosci = statusPlatnosci;
    }

    public String getTypPlatnosci() {
        return typPlatnosci;
    }

    public void setTypPlatnosci(String typPlatnosci) {
        this.typPlatnosci = typPlatnosci;
    }

    @Override
    public String toString() {
        return kwota + " PLN - " + metodaPlatnosci + " (" + statusPlatnosci + ")";
    }
}