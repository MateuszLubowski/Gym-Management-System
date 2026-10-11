package gym.model;

public abstract class Osoba {
    protected int id;
    protected String imie;
    protected String nazwisko;
    protected String email;
    protected String telefon;

    public Osoba() {
    }

    public Osoba(int id, String imie, String nazwisko, String email, String telefon) {
        this.id = id;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.email = email;
        this.telefon = telefon;
    }

    //Każda osoba ma unikalny identyfikator tekstowy
    public abstract String getUniqueIdentifier();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    @Override
    public String toString() {
        return imie + " " + nazwisko;
    }
}