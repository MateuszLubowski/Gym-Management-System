package gym.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class Harmonogram {
    private int id;
    private Strefa strefa;
    private DayOfWeek dzienTygodnia;
    private LocalTime godzinaOtwarcia;
    private LocalTime godzinaZamkniecia;

    //Tymczasowe pole pomocnicze (tylko do inicjalizacji)
    private transient int strefaIdTemp;

    public Harmonogram() {
    }

    public Harmonogram(int id, Strefa strefa, DayOfWeek dzienTygodnia, LocalTime godzinaOtwarcia, LocalTime godzinaZamkniecia) {
        this.id = id;
        this.strefa = strefa;
        this.dzienTygodnia = dzienTygodnia;
        this.godzinaOtwarcia = godzinaOtwarcia;
        this.godzinaZamkniecia = godzinaZamkniecia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Strefa getStrefa() {
        return strefa;
    }

    public void setStrefa(Strefa strefa) {
        this.strefa = strefa;
    }

    //Pomocnicze metody dla tymczasowego ID
    public void setStrefaIdTemp(int id) {
        this.strefaIdTemp = id;
    }

    public int getStrefaIdTemp() {
        return strefaIdTemp;
    }

    //Pomocnicza metoda dla DAO (do zapisu w bazie)
    public int getStrefaId() {
        return strefa != null ? strefa.getId() : 0;
    }

    public DayOfWeek getDzienTygodnia() {
        return dzienTygodnia;
    }

    public void setDzienTygodnia(DayOfWeek dzienTygodnia) {
        this.dzienTygodnia = dzienTygodnia;
    }

    public LocalTime getGodzinaOtwarcia() {
        return godzinaOtwarcia;
    }

    public void setGodzinaOtwarcia(LocalTime godzinaOtwarcia) {
        this.godzinaOtwarcia = godzinaOtwarcia;
    }

    public LocalTime getGodzinaZamkniecia() {
        return godzinaZamkniecia;
    }

    public void setGodzinaZamkniecia(LocalTime godzinaZamkniecia) {
        this.godzinaZamkniecia = godzinaZamkniecia;
    }

    @Override
    public String toString() {
        return dzienTygodnia + ": " + godzinaOtwarcia + " - " + godzinaZamkniecia;
    }
}