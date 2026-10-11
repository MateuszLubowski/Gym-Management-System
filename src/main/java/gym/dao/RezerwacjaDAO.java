package gym.dao;

import gym.model.Rezerwacja;
import gym.model.enums.StatusRezerwacji;
import gym.util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class RezerwacjaDAO {

    //Zapisuje nowa rezerwacje do bazy
    public void save(Rezerwacja rezerwacja) throws SQLException {
        String sql = "INSERT INTO Rezerwacja (numer, klientId, strefaId, trenerId, karnetId, " + "dataRezerwacji, godzinaRozpoczecia, godzinaZakonczenia, status, kosztStrefy, kosztTrenera) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, rezerwacja.getNumer());
            pstmt.setInt(2, rezerwacja.getKlientId());
            pstmt.setInt(3, rezerwacja.getStrefaId());

            if (rezerwacja.getTrenerId() != null) {
                pstmt.setInt(4, rezerwacja.getTrenerId());
            } else {
                pstmt.setNull(4, Types.INTEGER);
            }

            if (rezerwacja.getKarnetId() != null) {
                pstmt.setInt(5, rezerwacja.getKarnetId());
            } else {
                pstmt.setNull(5, Types.INTEGER);
            }

            pstmt.setString(6, rezerwacja.getDataRezerwacji().toString());
            pstmt.setString(7, rezerwacja.getGodzinaRozpoczecia().toString());
            pstmt.setString(8, rezerwacja.getGodzinaZakonczenia().toString());
            pstmt.setString(9, rezerwacja.getStatus().name());
            pstmt.setDouble(10, rezerwacja.getKosztStrefy());
            pstmt.setDouble(11, rezerwacja.getKosztTrenera());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Tworzenie rezerwacji nie powiodło się - brak wierszy");
            }

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()");

            if (rs.next()) {
                rezerwacja.setId(rs.getInt(1));
                System.out.println("Rezerwacja zapisana z ID: " + rezerwacja.getId());
            } else {
                throw new SQLException("Nie udało się pobrać ID rezerwacji");
            }

            rs.close();
            stmt.close();

        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    //Generowanie unikalnego ID dla Rezerwacji
    public String generateNumerRezerwacji() throws SQLException {
        String dzis = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = "REZ-" + dzis + "-";

        String sql = "SELECT COUNT(*) as cnt FROM Rezerwacja WHERE numer LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, prefix + "%");
            ResultSet rs = pstmt.executeQuery();

            int count = 0;
            if (rs.next()) {
                count = rs.getInt("cnt");
            }

            String numer = prefix + String.format("%04d", count + 1);
            System.out.println("DEBUG: Wygenerowano numer: " + numer);
            return numer;
        }
    }


    //Sprawdzanie czy strefa jest dostępna w danym przedziale czasowym.
    public boolean isStrefaDostepna(int strefaId, LocalDate data, LocalTime start, LocalTime end) throws SQLException {
        String sql = "SELECT COUNT(*) as cnt FROM Rezerwacja " + "WHERE strefaId = ? AND dataRezerwacji = ? " + "AND status != 'ANULOWANA' " + "AND ((godzinaRozpoczecia < ? AND godzinaZakonczenia > ?) " + "  OR (godzinaRozpoczecia < ? AND godzinaZakonczenia > ?))";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, strefaId);
            pstmt.setString(2, data.toString());
            pstmt.setString(3, end.toString());
            pstmt.setString(4, start.toString());
            pstmt.setString(5, start.toString());
            pstmt.setString(6, start.toString());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt("cnt");
                return count == 0; // Dostępna jeśli brak konfliktów
            }

            return true;
        }
    }


     //Pobiera wszystkie rezerwacje.

    public List<Rezerwacja> findAll() throws SQLException {
        List<Rezerwacja> rezerwacje = new ArrayList<>();
        String sql = "SELECT * FROM Rezerwacja ORDER BY dataRezerwacji DESC, godzinaRozpoczecia DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                rezerwacje.add(mapRowToRezerwacja(rs));
            }
        }

        return rezerwacje;
    }

   //Pobiera po ID
    public Rezerwacja findById(int id) throws SQLException {
        String sql = "SELECT * FROM Rezerwacja WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapRowToRezerwacja(rs);
            }
        }

        return null;
    }


    public List<Rezerwacja> findByKlientId(int klientId) throws SQLException {
        List<Rezerwacja> rezerwacje = new ArrayList<>();
        String sql = "SELECT * FROM Rezerwacja WHERE klientId = ? ORDER BY dataRezerwacji DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, klientId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                rezerwacje.add(mapRowToRezerwacja(rs));
            }
        }

        return rezerwacje;
    }


    public void updateStatus(int id, StatusRezerwacji nowyStatus) throws SQLException {
        String sql = "UPDATE Rezerwacja SET status = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nowyStatus.name());
            pstmt.setInt(2, id);

            pstmt.executeUpdate();
        }
    }

    private Rezerwacja mapRowToRezerwacja(ResultSet rs) throws SQLException {
        Rezerwacja rezerwacja = new Rezerwacja();

        rezerwacja.setId(rs.getInt("id"));
        rezerwacja.setNumer(rs.getString("numer"));
        rezerwacja.setKlientId(rs.getInt("klientId"));
        rezerwacja.setStrefaId(rs.getInt("strefaId"));

        int trenerId = rs.getInt("trenerId");
        if (!rs.wasNull()) {
            rezerwacja.setTrenerId(trenerId);
        }

        int karnetId = rs.getInt("karnetId");
        if (!rs.wasNull()) {
            rezerwacja.setKarnetId(karnetId);
        }

        rezerwacja.setDataRezerwacji(LocalDate.parse(rs.getString("dataRezerwacji")));
        rezerwacja.setGodzinaRozpoczecia(LocalTime.parse(rs.getString("godzinaRozpoczecia")));
        rezerwacja.setGodzinaZakonczenia(LocalTime.parse(rs.getString("godzinaZakonczenia")));
        rezerwacja.setStatus(StatusRezerwacji.valueOf(rs.getString("status")));
        rezerwacja.setKosztStrefy(rs.getDouble("kosztStrefy"));
        rezerwacja.setKosztTrenera(rs.getDouble("kosztTrenera"));

        return rezerwacja;
    }
    public boolean isTrenerDostepny(int trenerId, LocalDate data, LocalTime start, LocalTime end) throws SQLException {
        String sql = "SELECT COUNT(*) as cnt FROM Rezerwacja " + "WHERE trenerId = ? AND dataRezerwacji = ? " + "AND status != 'ANULOWANA' " + "AND ((godzinaRozpoczecia < ? AND godzinaZakonczenia > ?) " + "  OR (godzinaRozpoczecia < ? AND godzinaZakonczenia > ?))";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, trenerId);
            pstmt.setString(2, data.toString());
            pstmt.setString(3, end.toString());
            pstmt.setString(4, start.toString());
            pstmt.setString(5, start.toString());
            pstmt.setString(6, start.toString());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt("cnt");
                boolean dostepny = count == 0;
                System.out.println("DEBUG isTrenerDostepny: trenerId=" + trenerId + ", data=" + data + ", konflikty=" + count + ", dostępny=" + dostepny);
                return dostepny;
            }

            return true;
        }
    }
}