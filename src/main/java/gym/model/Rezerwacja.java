package gym.model;

import gym.model.enums.StatusRezerwacji;
import java.time.LocalDate;
import java.time.LocalTime;

public class Rezerwacja {
    private int id;
    private String numer;
    private int klientId;
    private int strefaId;
    private Integer trenerId;
    private Integer karnetId;
    private LocalDate dataRezerwacji;
    private LocalTime godzinaRozpoczecia;
    private LocalTime godzinaZakonczenia;
    private StatusRezerwacji status;
    private double kosztStrefy;
    private double kosztTrenera;

    private Platnosc platnosc;

    public Rezerwacja() {
    }

    public Rezerwacja(int id, String numer, int klientId, int strefaId, LocalDate dataRezerwacji, LocalTime godzinaRozpoczecia, LocalTime godzinaZakonczenia, StatusRezerwacji status) {
        this.id = id;
        this.numer = numer;
        this.klientId = klientId;
        this.strefaId = strefaId;
        this.dataRezerwacji = dataRezerwacji;
        this.godzinaRozpoczecia = godzinaRozpoczecia;
        this.godzinaZakonczenia = godzinaZakonczenia;
        this.status = status;
    }

    public double obliczCalkowyKoszt() {
        return kosztStrefy + kosztTrenera;
    }

    public int getLiczbaGodzin() {
        if (godzinaRozpoczecia != null && godzinaZakonczenia != null) {
            return (int) java.time.Duration.between(godzinaRozpoczecia, godzinaZakonczenia).toHours();
        }
        return 0;
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

    public int getStrefaId() {
        return strefaId;
    }

    public void setStrefaId(int strefaId) {
        this.strefaId = strefaId;
    }

    public Integer getTrenerId() {
        return trenerId;
    }

    public void setTrenerId(Integer trenerId) {
        this.trenerId = trenerId;
    }

    public Integer getKarnetId() {
        return karnetId;
    }

    public void setKarnetId(Integer karnetId) {
        this.karnetId = karnetId;
    }

    public LocalDate getDataRezerwacji() {
        return dataRezerwacji;
    }

    public void setDataRezerwacji(LocalDate dataRezerwacji) {
        this.dataRezerwacji = dataRezerwacji;
    }

    public LocalTime getGodzinaRozpoczecia() {
        return godzinaRozpoczecia;
    }

    public void setGodzinaRozpoczecia(LocalTime godzinaRozpoczecia) {
        this.godzinaRozpoczecia = godzinaRozpoczecia;
    }

    public LocalTime getGodzinaZakonczenia() {
        return godzinaZakonczenia;
    }

    public void setGodzinaZakonczenia(LocalTime godzinaZakonczenia) {
        this.godzinaZakonczenia = godzinaZakonczenia;
    }

    public StatusRezerwacji getStatus() {
        return status;
    }

    public void setStatus(StatusRezerwacji status) {
        this.status = status;
    }

    public double getKosztStrefy() {
        return kosztStrefy;
    }

    public void setKosztStrefy(double kosztStrefy) {
        this.kosztStrefy = kosztStrefy;
    }

    public double getKosztTrenera() {
        return kosztTrenera;
    }

    public void setKosztTrenera(double kosztTrenera) {
        this.kosztTrenera = kosztTrenera;
    }

    public Platnosc getPlatnosc() {
        return platnosc;
    }

    public void setPlatnosc(Platnosc platnosc) {
        this.platnosc = platnosc;
    }

    @Override
    public String toString() {
        return numer + " - " + dataRezerwacji + " " + godzinaRozpoczecia + "-" + godzinaZakonczenia;
    }
}