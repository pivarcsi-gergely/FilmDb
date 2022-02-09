package hu.petrik.filmdb;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class FilmApi {
    private static final String BASE_URL = "http://localhost:8000";
    public static final String FILM_API_URL = BASE_URL + "/api/film";

    public static List<Film> getFilmek() throws IOException {
        Response response = RequestHandler.get(FILM_API_URL);
        String json = response.getContent();
        Gson Gayson = new Gson();
        if (response.getResponseCode() >= 400) {
            System.out.println(json);
            String message = Gayson.fromJson(json, ApiError.class).getMessage();
            throw new IOException(message);
        } else {
            Type type = new TypeToken<List<Film>>() {
            }.getType();
            List<Film> filmList = Gayson.fromJson(json, type);
            return filmList;
        }
    }

    public static Film filmHozzaadasa(Film ujFilm) throws IOException {
        Gson GaysonAdd = new Gson();
        String filmJson = GaysonAdd.toJson(ujFilm);
        Response response = RequestHandler.post(FILM_API_URL, filmJson);

        String json = response.getContent();
        if (response.getResponseCode() >= 400) {
            String message = GaysonAdd.fromJson(json, ApiError.class).getMessage();
            throw new IOException(message);
        }
        return GaysonAdd.fromJson(json, Film.class);
    }

    public static Film filmModositasa(Film modositando) throws IOException {
        Gson GaysonEdit = new Gson();
        String filmJson = GaysonEdit.toJson(modositando);
        Response response = RequestHandler.put(FILM_API_URL + "/" + modositando.getId(), filmJson);

        String json = response.getContent();
        if (response.getResponseCode() >= 400) {
            String message = GaysonEdit.fromJson(json, ApiError.class).getMessage();
            throw new IOException(message);
        }
        return GaysonEdit.fromJson(json, Film.class);
    }

    public static boolean filmTorlese(int id) throws IOException {
        Response response = RequestHandler.delete(FILM_API_URL+"/"+id);

        Gson GaysonDel = new Gson();
        String json = response.getContent();
        if (response.getResponseCode() >= 400) {
            String message = GaysonDel.fromJson(json, ApiError.class).getMessage();
            throw new IOException(message);
        }
        return response.getResponseCode() == 204;
    }
}
