package gym.dao;
import gym.model.Klient;
import gym.model.enums.StadiumZaawansowania;
import gym.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KlientDAO {

    //Pobieranie Klientów
    public List<Klient> findAll() throws SQLException {
        List<Klient> klienci = new ArrayList<>();
        String sql = "SELECT * FROM Klient";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Klient klient = mapRowToKlient(rs);
                klienci.add(klient);
            }
        }

        System.out.println("✅ Pobrano " + klienci.size() + " klientów z bazy");
        return klienci;
    }

    //Pobiera po ID
    public Klient findById(int id) throws SQLException {
        String sql = "SELECT * FROM Klient WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapRowToKlient(rs);
            }
        }

        return null;
    }

   //Pobiera przez Email
    public Klient findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM Klient WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapRowToKlient(rs);
            }
        }

        return null;
    }

    //Zapisuje nowego Klienta
    public void save(Klient klient) throws SQLException {
        String sql = "INSERT INTO Klient (imie, nazwisko, email, telefon, stadiumZaawansowania, nazwaInstytucji) " + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, klient.getImie());
            pstmt.setString(2, klient.getNazwisko());
            pstmt.setString(3, klient.getEmail());
            pstmt.setString(4, klient.getTelefon());
            pstmt.setString(5, klient.getStadiumZaawansowania() != null ?
                    klient.getStadiumZaawansowania().name() : null);
            pstmt.setString(6, klient.getNazwaInstytucji());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    klient.setId(generatedKeys.getInt(1));
                    System.out.println("✅ Zapisano klienta z ID: " + klient.getId());
                }
            }
        }
    }

    //Aktualizuje Klienta
    public void update(Klient klient) throws SQLException {
        String sql = "UPDATE Klient SET imie = ?, nazwisko = ?, email = ?, telefon = ?, " + "stadiumZaawansowania = ?, nazwaInstytucji = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, klient.getImie());
            pstmt.setString(2, klient.getNazwisko());
            pstmt.setString(3, klient.getEmail());
            pstmt.setString(4, klient.getTelefon());
            pstmt.setString(5, klient.getStadiumZaawansowania() != null ?
                    klient.getStadiumZaawansowania().name() : null);
            pstmt.setString(6, klient.getNazwaInstytucji());
            pstmt.setInt(7, klient.getId());

            int affectedRows = pstmt.executeUpdate();
            System.out.println("✅ Zaktualizowano klienta ID: " + klient.getId());
        }
    }

    //Usuwa Klienta
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Klient WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("✅ Usunięto klienta ID: " + id);
        }
    }

    //Mapuje wiersz SQL na obiekt Klient
    private Klient mapRowToKlient(ResultSet rs) throws SQLException {
        Klient klient = new Klient();
        klient.setId(rs.getInt("id"));
        klient.setImie(rs.getString("imie"));
        klient.setNazwisko(rs.getString("nazwisko"));
        klient.setEmail(rs.getString("email"));
        klient.setTelefon(rs.getString("telefon"));

        String stadium = rs.getString("stadiumZaawansowania");
        if (stadium != null && !stadium.isEmpty()) {
            klient.setStadiumZaawansowania(StadiumZaawansowania.valueOf(stadium));
        }

        klient.setNazwaInstytucji(rs.getString("nazwaInstytucji"));

        return klient;
    }
}