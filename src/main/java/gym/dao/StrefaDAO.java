package gym.dao;

import gym.model.Strefa;
import gym.model.enums.TypStrefy;
import gym.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StrefaDAO {

//Pobiera wszystkie strefy z bazy danych.
    public List<Strefa> findAll() throws SQLException {
        List<Strefa> strefy = new ArrayList<>();
        String sql = "SELECT * FROM Strefa";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Strefa strefa = new Strefa(
                        rs.getInt("id"),
                        rs.getString("nazwa"),
                        TypStrefy.valueOf(rs.getString("typ")),
                        rs.getDouble("cenaZaGodzine")
                );
                strefa.setMaksymalnaLiczbaOsob(rs.getInt("maksymalnaLiczbaOsob"));
                strefa.setOpis(rs.getString("opis"));

                strefy.add(strefa);
            }
        }

        return strefy;
    }


//Pobiera strefę po ID.
    public Strefa findById(int id) throws SQLException {
        String sql = "SELECT * FROM Strefa WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Strefa strefa = new Strefa(
                        rs.getInt("id"),
                        rs.getString("nazwa"),
                        TypStrefy.valueOf(rs.getString("typ")),
                        rs.getDouble("cenaZaGodzine")
                );
                strefa.setMaksymalnaLiczbaOsob(rs.getInt("maksymalnaLiczbaOsob"));
                strefa.setOpis(rs.getString("opis"));

                return strefa;
            }
        }

        return null;
    }


//Zapisuje nową strefę do bazy.
    public void save(Strefa strefa) throws SQLException {
        String sql = "INSERT INTO Strefa (nazwa, typ, cenaZaGodzine, maksymalnaLiczbaOsob, opis) " + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, strefa.getNazwa());
            pstmt.setString(2, strefa.getTyp().name());
            pstmt.setDouble(3, strefa.getCenaZaGodzine());
            pstmt.setInt(4, strefa.getMaksymalnaLiczbaOsob());
            pstmt.setString(5, strefa.getOpis());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        strefa.setId(generatedKeys.getInt(1));
                    }
                }
            }
        }
    }

 //Aktualizuje istniejącą strefę.
    public void update(Strefa strefa) throws SQLException {
        String sql = "UPDATE Strefa SET nazwa = ?, typ = ?, cenaZaGodzine = ?, " + "maksymalnaLiczbaOsob = ?, opis = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, strefa.getNazwa());
            pstmt.setString(2, strefa.getTyp().name());
            pstmt.setDouble(3, strefa.getCenaZaGodzine());
            pstmt.setInt(4, strefa.getMaksymalnaLiczbaOsob());
            pstmt.setString(5, strefa.getOpis());
            pstmt.setInt(6, strefa.getId());

            pstmt.executeUpdate();
        }
    }


//Usuwa strefę z bazy.
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Strefa WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}