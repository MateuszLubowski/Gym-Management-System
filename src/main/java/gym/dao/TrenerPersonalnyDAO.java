package gym.dao;

import gym.model.TrenerPersonalny;
import gym.model.enums.TypTrenera;
import gym.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class TrenerPersonalnyDAO {

    //Wyświetlanie trenerów
    public List<TrenerPersonalny> findAll() throws SQLException {
        List<TrenerPersonalny> trenerzy = new ArrayList<>();
        String sql = "SELECT * FROM TrenerPersonalny";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                trenerzy.add(mapRowToTrener(rs));
            }
        }

        System.out.println("✅ Pobrano " + trenerzy.size() + " trenerów z bazy");
        return trenerzy;
    }

   //Pobieranie po ID
    public TrenerPersonalny findById(int id) throws SQLException {
        String sql = "SELECT * FROM TrenerPersonalny WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapRowToTrener(rs);
            }
        }

        return null;
    }

    //Zapisywanie nowego trenera
    public void save(TrenerPersonalny trener) throws SQLException {
        String sql = "INSERT INTO TrenerPersonalny (imie, nazwisko, email, telefon, " + "typTrenera, stawkaZaGodzine, maksymalnaLiczbaKlientow) " + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, trener.getImie());
            pstmt.setString(2, trener.getNazwisko());
            pstmt.setString(3, trener.getEmail());
            pstmt.setString(4, trener.getTelefon());
            pstmt.setString(5, trener.getTypTrenera().name());
            pstmt.setDouble(6, trener.getStawkaZaGodzine());
            pstmt.setInt(7, trener.getMaksymalnaLiczbaKlientow());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    trener.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    private TrenerPersonalny mapRowToTrener(ResultSet rs) throws SQLException {
        TrenerPersonalny trener = new TrenerPersonalny();
        trener.setId(rs.getInt("id"));
        trener.setImie(rs.getString("imie"));
        trener.setNazwisko(rs.getString("nazwisko"));
        trener.setEmail(rs.getString("email"));
        trener.setTelefon(rs.getString("telefon"));
        trener.setTypTrenera(TypTrenera.valueOf(rs.getString("typTrenera")));
        trener.setStawkaZaGodzine(rs.getDouble("stawkaZaGodzine"));
        trener.setMaksymalnaLiczbaKlientow(rs.getInt("maksymalnaLiczbaKlientow"));

        return trener;
    }
}