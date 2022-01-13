package hu.petrik.filmdb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FilmDB {
    Connection DBconn;

    public FilmDB() throws SQLException {
        DBconn = DriverManager.getConnection("jdbc:mysql://localhost:3306/filmdb", "root", "");
    }

    public List<Film> getFilmek() throws SQLException {
        List<Film> filmek = new ArrayList<>();
        Statement stmt = DBconn.createStatement();
        String sql = "SELECT * FROM filmek";
        ResultSet result = stmt.executeQuery(sql);
        while (result.next()) {
            int id = result.getInt("id");
            String cim = result.getString("cim");
            String kategoria = result.getString("kategoria");
            int hossz = result.getInt("hossz");
            int ertekeles = result.getInt("ertekeles");
            Film film = new Film(id, cim, kategoria, hossz, ertekeles);
            filmek.add(film);
        }
        return filmek;
    }

    public int filmHozzaadasa(String cim, String kategoria, int hossz, int ertekeles) throws SQLException {
        String sql = "INSERT INTO filmek(cim, kategoria, hossz, ertekeles) VALUES (?,?,?,?)";
        PreparedStatement pStmt = DBconn.prepareStatement(sql);
        pStmt.setString(1, cim);
        pStmt.setString(2, kategoria);
        pStmt.setInt(3, hossz);
        pStmt.setInt(4, ertekeles);
        return pStmt.executeUpdate();
    }

    public boolean filmTorlese(int id) throws SQLException {
        String sql = "DELETE FROM filmek WHERE id = ?";
        PreparedStatement pStmt = DBconn.prepareStatement(sql);
        pStmt.setInt(1, id);
        int erintettSorok = pStmt.executeUpdate();
        return erintettSorok == 1;
    }

    public boolean filmModositasa(Film modositando) throws SQLException {
        String sql = "UPDATE filmek SET cim = ?, kategoria = ?, hossz = ?, ertekeles = ? WHERE id = ?";
        PreparedStatement pStmt = DBconn.prepareStatement(sql);
        pStmt.setString(1, modositando.getCim());
        pStmt.setString(2, modositando.getKategoria());
        pStmt.setInt(3, modositando.getHossz());
        pStmt.setInt(4, modositando.getErtekeles());
        pStmt.setInt(5, modositando.getId());
        int erintettSorok = pStmt.executeUpdate();
        return erintettSorok == 1;
    }
}
