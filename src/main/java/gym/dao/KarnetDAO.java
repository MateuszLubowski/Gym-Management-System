package gym.dao;
import gym.model.Karnet;
import gym.util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//Sprawdzenie czy Klient ma aktywny karnet dla zniżki 50%
public class KarnetDAO {

    //Sprawdzenie czy Klient ma karnet
    public Karnet findAktywnyByKlientId(int klientId) throws SQLException {
        String sql = "SELECT * FROM Karnet WHERE klientId = ? AND status = 'AKTYWNY' " +
                "AND (dataWaznosci IS NULL OR dataWaznosci >= date('now'))";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, klientId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapRowToKarnet(rs);
            }
        }

        return null;
    }

    //Pobieram wszytkie karnety klienta
    public List<Karnet> findByKlientId(int klientId) throws SQLException {
        List<Karnet> karnety = new ArrayList<>();
        String sql = "SELECT * FROM Karnet WHERE klientId = ? ORDER BY dataWydania DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, klientId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                karnety.add(mapRowToKarnet(rs));
            }
        }

        return karnety;
    }

    //Zapisuje nowy karnet
    public void save(Karnet karnet) throws SQLException {
        String sql = "INSERT INTO Karnet (numer, klientId, dataWydania, dataWaznosci, " + "typ, liczbaDostepnychWejsc, status) " + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, karnet.getNumer());
            pstmt.setInt(2, karnet.getKlientId());
            pstmt.setString(3, karnet.getDataWydania().toString());
            pstmt.setString(4, karnet.getDataWaznosci() != null ? karnet.getDataWaznosci().toString() : null);
            pstmt.setString(5, karnet.getTyp());

            if (karnet.getLiczbaDostepnychWejsc() != null) {
                pstmt.setInt(6, karnet.getLiczbaDostepnychWejsc());
            } else {
                pstmt.setNull(6, Types.INTEGER);
            }

            pstmt.setString(7, karnet.getStatus());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    karnet.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    //Mapuje wiersz SQL na obiekt Karnet
    private Karnet mapRowToKarnet(ResultSet rs) throws SQLException {
        Karnet karnet = new Karnet();
        karnet.setId(rs.getInt("id"));
        karnet.setNumer(rs.getString("numer"));
        karnet.setKlientId(rs.getInt("klientId"));
        karnet.setDataWydania(LocalDate.parse(rs.getString("dataWydania")));

        String dataWaznosci = rs.getString("dataWaznosci");
        if (dataWaznosci != null) {
            karnet.setDataWaznosci(LocalDate.parse(dataWaznosci));
        }

        karnet.setTyp(rs.getString("typ"));

        int liczbaDostepnychWejsc = rs.getInt("liczbaDostepnychWejsc");
        if (!rs.wasNull()) {
            karnet.setLiczbaDostepnychWejsc(liczbaDostepnychWejsc);
        }

        karnet.setStatus(rs.getString("status"));

        return karnet;
    }
}