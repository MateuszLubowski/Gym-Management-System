package gym.util;

import gym.dao.StrefaDAO;
import gym.model.Harmonogram;
import gym.model.Strefa;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataLoader {

//Ładuje wszystkie strefy wraz z harmonogramami Tworzy asocjacje w pamięci, GUI używa tylko strefa.getHarmonogramy()

    public static List<Strefa>
    loadStrefyWithHarmonogramy() throws SQLException {
        //Pobieramy wszystkie strefy
        StrefaDAO strefaDAO = new StrefaDAO();
        List<Strefa> strefy = strefaDAO.findAll();

        //Tworzymy mapę stref dla szybkiego dostępu
        Map<Integer, Strefa> strefyMap = new HashMap<>();
        for (Strefa s : strefy) {
            strefyMap.put(s.getId(), s);
        }

        //Pobieramy wszystkie harmonogramy
        List<Harmonogram> wszystkieHarmonogramy = loadAllHarmonogramy();

        //Przypisujemy harmonogramy do stref przez asocjację
        for (Harmonogram h : wszystkieHarmonogramy) {
            int strefaId = h.getStrefaIdTemp(); //Do której strefy nalezy
            Strefa strefa = strefyMap.get(strefaId); //Szukane strefy w mapie

            if (strefa != null) {
                h.setStrefa(strefa); //Jedna strona asocjacji harmonogram -> Strefa
                strefa.dodajHarmonogram(h); //2ga strona asocjacji strefa -> haronogram
            }
        }

        return strefy;
    }

    //Pobieramy wszystkie harmonogramy z bazy
    private static List<Harmonogram> loadAllHarmonogramy() throws SQLException {
        List<Harmonogram> harmonogramy = new ArrayList<>();

        String sql = "SELECT * FROM Harmonogram ORDER BY " + "CASE dzienTygodnia " + "WHEN 'MONDAY' THEN 1 " + "WHEN 'TUESDAY' THEN 2 " + "WHEN 'WEDNESDAY' THEN 3 " + "WHEN 'THURSDAY' THEN 4 " + "WHEN 'FRIDAY' THEN 5 " + "WHEN 'SATURDAY' THEN 6 " + "WHEN 'SUNDAY' THEN 7 " + "END";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Harmonogram h = new Harmonogram();
                h.setId(rs.getInt("id"));
                h.setStrefaIdTemp(rs.getInt("strefaId"));
                h.setDzienTygodnia(DayOfWeek.valueOf(rs.getString("dzienTygodnia")));
                h.setGodzinaOtwarcia(LocalTime.parse(rs.getString("godzinaOtwarcia")));
                h.setGodzinaZamkniecia(LocalTime.parse(rs.getString("godzinaZamkniecia")));

                harmonogramy.add(h);
            }
        }

        return harmonogramy;
    }
}