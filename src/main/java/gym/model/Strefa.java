package gym.model;

import gym.model.enums.TypStrefy;
import java.util.ArrayList;
import java.util.List;

public class Strefa {
    private int id;
    private String nazwa;
    private TypStrefy typ;
    private double cenaZaGodzine;
    private int maksymalnaLiczbaOsob;
    private String opis;

    private List<Harmonogram> harmonogramy = new ArrayList<>();

    public Strefa() {
    }

    public Strefa(int id, String nazwa, TypStrefy typ, double cenaZaGodzine) {
        this.id = id;
        this.nazwa = nazwa;
        this.typ = typ;
        this.cenaZaGodzine = cenaZaGodzine;
    }

    public Strefa(int id, String nazwa, TypStrefy typ, double cenaZaGodzine, int maksymalnaLiczbaOsob, String opis) {
        this.id = id;
        this.nazwa = nazwa;
        this.typ = typ;
        this.cenaZaGodzine = cenaZaGodzine;
        this.maksymalnaLiczbaOsob = maksymalnaLiczbaOsob;
        this.opis = opis;
    }

    public List<Harmonogram> getHarmonogramy() {
        return harmonogramy;
    }

    public void setHarmonogramy(List<Harmonogram> harmonogramy) {
        this.harmonogramy = harmonogramy;
    }

    public void dodajHarmonogram(Harmonogram harmonogram) {
        if (!harmonogramy.contains(harmonogram)) {
            harmonogramy.add(harmonogram);
        }
    }

    public void usunHarmonogram(Harmonogram harmonogram) {
        harmonogramy.remove(harmonogram);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public TypStrefy getTyp() {
        return typ;
    }

    public void setTyp(TypStrefy typ) {
        this.typ = typ;
    }

    public double getCenaZaGodzine() {
        return cenaZaGodzine;
    }

    public void setCenaZaGodzine(double cenaZaGodzine) {
        this.cenaZaGodzine = cenaZaGodzine;
    }

    public int getMaksymalnaLiczbaOsob() {
        return maksymalnaLiczbaOsob;
    }

    public void setMaksymalnaLiczbaOsob(int maksymalnaLiczbaOsob) {
        this.maksymalnaLiczbaOsob = maksymalnaLiczbaOsob;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    @Override
    public String toString() {
        return nazwa + " (" + typ + ") - " + cenaZaGodzine + " PLN/2h";
    }
}