package gym.model;

import gym.model.enums.StadiumZaawansowania;

public class Klient extends Osoba {
    private StadiumZaawansowania stadiumZaawansowania;
    private String nazwaInstytucji;

    public Klient() {
        super();
    }

    public Klient(int id, String imie, String nazwisko, String email, String telefon, StadiumZaawansowania stadiumZaawansowania, String nazwaInstytucji) {
        super(id, imie, nazwisko, email, telefon);
        this.stadiumZaawansowania = stadiumZaawansowania;
        this.nazwaInstytucji = nazwaInstytucji;
    }

    @Override
    public String getUniqueIdentifier() {
        return "KLIENT-" + id;
    }

    //Sprawdzanie czy klient jest instytucjonalny
    public boolean isInstytucjonalny() {
        return nazwaInstytucji != null && !nazwaInstytucji.trim().isEmpty();
    }

    public StadiumZaawansowania getStadiumZaawansowania() {
        return stadiumZaawansowania;
    }

    public void setStadiumZaawansowania(StadiumZaawansowania stadiumZaawansowania) {
        this.stadiumZaawansowania = stadiumZaawansowania;
    }

    public String getNazwaInstytucji() {
        return nazwaInstytucji;
    }

    public void setNazwaInstytucji(String nazwaInstytucji) {
        this.nazwaInstytucji = nazwaInstytucji;
    }

    @Override
    public String toString() {
        if (isInstytucjonalny()) {
            return nazwaInstytucji;
        }
        return super.toString() + " (" + stadiumZaawansowania + ")";
    }
}