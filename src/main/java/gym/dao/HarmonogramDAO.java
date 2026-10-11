package gym.dao;

import gym.model.Harmonogram;
import gym.util.DatabaseConnection;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class HarmonogramDAO {
    //Pobiera wszystkie harmonogramy z bazy (bez filtrowania) Używane tylko przez DataLoader przy starcie aplikacji
    public List<Harmonogram> findAll() throws SQLException {
        List<Harmonogram> harmonogramy = new ArrayList<>();
        String sql = "SELECT * FROM Harmonogram ORDER BY " + "CASE dzienTygodnia " + "WHEN 'MONDAY' THEN 1 " + "WHEN 'TUESDAY' THEN 2 " + "WHEN 'WEDNESDAY' THEN 3 " + "WHEN 'THURSDAY' THEN 4 " + "WHEN 'FRIDAY' THEN 5 " + "WHEN 'SATURDAY' THEN 6 " + "WHEN 'SUNDAY' THEN 7 " + "END";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Harmonogram harmonogram = new Harmonogram();
                harmonogram.setId(rs.getInt("id"));
                harmonogram.setStrefaIdTemp(rs.getInt("strefaId"));
                harmonogram.setDzienTygodnia(DayOfWeek.valueOf(rs.getString("dzienTygodnia")));
                harmonogram.setGodzinaOtwarcia(LocalTime.parse(rs.getString("godzinaOtwarcia")));
                harmonogram.setGodzinaZamkniecia(LocalTime.parse(rs.getString("godzinaZamkniecia")));

                harmonogramy.add(harmonogram);
            }
        }

        return harmonogramy;
    }


//Zapisuje nowy harmonogram do bazy
    public void save(Harmonogram harmonogram) throws SQLException {
        String sql = "INSERT INTO Harmonogram (strefaId, dzienTygodnia, godzinaOtwarcia, godzinaZamkniecia) " + "VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, harmonogram.getStrefaId());
            pstmt.setString(2, harmonogram.getDzienTygodnia().name());
            pstmt.setString(3, harmonogram.getGodzinaOtwarcia().toString());
            pstmt.setString(4, harmonogram.getGodzinaZamkniecia().toString());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    harmonogram.setId(generatedKeys.getInt(1));
                }
            }
        }
    }
}