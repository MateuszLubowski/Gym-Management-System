package gym.model;

import java.time.LocalDateTime;

public class ImprezaSportowa {
    private int id;
    private int strefaId;
    private String nazwa;
    private LocalDateTime dataRozpoczecia;
    private LocalDateTime dataZakonczenia;
    private String opis;
    private String status;

    public ImprezaSportowa() {
    }
    public ImprezaSportowa(int id, int strefaId, String nazwa, LocalDateTime dataRozpoczecia, LocalDateTime dataZakonczenia, String status) {
        this.id = id;
        this.strefaId = strefaId;
        this.nazwa = nazwa;
        this.dataRozpoczecia = dataRozpoczecia;
        this.dataZakonczenia = dataZakonczenia;
        this.status = status;
    }

    public boolean isTrwa() {
        LocalDateTime teraz = LocalDateTime.now();
        return "PLANOWANA".equals(status) && !teraz.isBefore(dataRozpoczecia) && !teraz.isAfter(dataZakonczenia);
    }

    //Sprawdzanie czy impreza koliduje z danym czasem
    public boolean kolidujeZCzasem(LocalDateTime start, LocalDateTime koniec) {
        return !(koniec.isBefore(dataRozpoczecia) || start.isAfter(dataZakonczenia));
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStrefaId() {
        return strefaId;
    }

    public void setStrefaId(int strefaId) {
        this.strefaId = strefaId;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public LocalDateTime getDataRozpoczecia() {
        return dataRozpoczecia;
    }

    public void setDataRozpoczecia(LocalDateTime dataRozpoczecia) {
        this.dataRozpoczecia = dataRozpoczecia;
    }

    public LocalDateTime getDataZakonczenia() {
        return dataZakonczenia;
    }

    public void setDataZakonczenia(LocalDateTime dataZakonczenia) {
        this.dataZakonczenia = dataZakonczenia;
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
        return nazwa + " (" + status + ") - " + dataRozpoczecia.toLocalDate() + " " + dataRozpoczecia.toLocalTime();
    }
}
