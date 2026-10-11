package gym.dao;
import gym.model.Platnosc;
import gym.model.enums.MetodaPlatnosci;
import gym.util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class PlatnoscDAO {

    //Zapisuje Płatność
    public void save(Platnosc platnosc) throws SQLException {
        String sql = "INSERT INTO Platnosc (rezerwacjaId, kwota, dataPlatnosci, metodaPlatnosci, statusPlatnosci, typPlatnosci) " + "VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, platnosc.getRezerwacjaId());
            pstmt.setDouble(2, platnosc.getKwota());
            pstmt.setString(3, platnosc.getDataPlatnosci().toString());
            pstmt.setString(4, platnosc.getMetodaPlatnosci().name());
            pstmt.setString(5, platnosc.getStatusPlatnosci());
            pstmt.setString(6, platnosc.getTypPlatnosci());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Tworzenie płatności nie powiodło się");
            }

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()");

            if (rs.next()) {
                platnosc.setId(rs.getInt(1));
                System.out.println("✅ Płatność zapisana z ID: " + platnosc.getId());
            }

            rs.close();
            stmt.close();

        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    //Pobieranie płatność dla rezerwacji (KOMPOZYCJA - 1:1)
    public Platnosc findByRezerwacjaId(int rezerwacjaId) throws SQLException {
        String sql = "SELECT * FROM Platnosc WHERE rezerwacjaId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, rezerwacjaId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapRowToPlatnosc(rs);
            }
        }

        return null;
    }

    public List<Platnosc> findAll() throws SQLException {
        List<Platnosc> platnosci = new ArrayList<>();
        String sql = "SELECT * FROM Platnosc";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                platnosci.add(mapRowToPlatnosc(rs));
            }
        }

        return platnosci;
    }


    public void updateStatus(int id, String nowyStatus) throws SQLException {
        String sql = "UPDATE Platnosc SET statusPlatnosci = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nowyStatus);
            pstmt.setInt(2, id);

            pstmt.executeUpdate();
            System.out.println("✅ Zaktualizowano status płatności ID: " + id + " na: " + nowyStatus);
        }
    }

    //Mapowanie SQL
    private Platnosc mapRowToPlatnosc(ResultSet rs) throws SQLException {
        Platnosc platnosc = new Platnosc();
        platnosc.setId(rs.getInt("id"));
        platnosc.setRezerwacjaId(rs.getInt("rezerwacjaId"));
        platnosc.setKwota(rs.getDouble("kwota"));
        platnosc.setDataPlatnosci(LocalDateTime.parse(rs.getString("dataPlatnosci")));
        platnosc.setMetodaPlatnosci(MetodaPlatnosci.valueOf(rs.getString("metodaPlatnosci")));
        platnosc.setStatusPlatnosci(rs.getString("statusPlatnosci"));
        platnosc.setTypPlatnosci(rs.getString("typPlatnosci"));

        return platnosc;
    }
}