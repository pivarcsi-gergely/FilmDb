package hu.petrik.filmdb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FilmDB {
    Connection DBconn;

    public FilmDB() throws SQLException{
            DBconn = DriverManager.getConnection("jdbc:mysql://localhost:3306/filmdb", "root", "");
    }

    public List<Film> getFilmek() throws SQLException {
        List<Film> filmek = new ArrayList<>();
        Statement stmt = DBconn.createStatement();
        String sql = "SELECT * FROM filmek";
        ResultSet result = stmt.executeQuery(sql);
        while (result.next()){
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
}
