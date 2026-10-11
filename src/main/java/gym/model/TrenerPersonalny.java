package gym.model;

import gym.model.enums.TypTrenera;

public class TrenerPersonalny extends Osoba {
    private TypTrenera typTrenera;
    private double stawkaZaGodzine;
    private int maksymalnaLiczbaKlientow;

    public TrenerPersonalny() {
        super();
    }

    public TrenerPersonalny(int id, String imie, String nazwisko, String email, String telefon, TypTrenera typTrenera, double stawkaZaGodzine) {
        super(id, imie, nazwisko, email, telefon);
        this.typTrenera = typTrenera;
        this.stawkaZaGodzine = stawkaZaGodzine;
    }

    @Override
    public String getUniqueIdentifier() {
        return "TRENER-" + id;
    }

    public TypTrenera getTypTrenera() {
        return typTrenera;
    }

    public void setTypTrenera(TypTrenera typTrenera) {
        this.typTrenera = typTrenera;
    }

    public double getStawkaZaGodzine() {
        return stawkaZaGodzine;
    }

    public void setStawkaZaGodzine(double stawkaZaGodzine) {
        this.stawkaZaGodzine = stawkaZaGodzine;
    }

    public int getMaksymalnaLiczbaKlientow() {
        return maksymalnaLiczbaKlientow;
    }

    public void setMaksymalnaLiczbaKlientow(int maksymalnaLiczbaKlientow) {
        this.maksymalnaLiczbaKlientow = maksymalnaLiczbaKlientow;
    }

    @Override
    public String toString() {
        return super.toString() + " (" + typTrenera + ") - " + stawkaZaGodzine + " PLN/h";
    }
}