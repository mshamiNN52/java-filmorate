package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.DAO.FilmDbStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Service
public class FilmService {
    private final FilmDbStorage filmDbStorage;

    @Autowired
    public FilmService(FilmDbStorage filmDbStorage) {
        this.filmDbStorage = filmDbStorage;
    }

    public Film createFilm(Film film) {
        return filmDbStorage.createFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmDbStorage.getAllFilms();
    }

    public Film editFilm(Film film) {
        return filmDbStorage.editFilm(film);
    }

    public void likeFilm(int filmId, int userId) {
        filmDbStorage.likeFilm(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        filmDbStorage.deleteLike(filmId, userId);
    }

    public List<Film> filmRate(int count) {
        return filmDbStorage.filmRate(count);
    }

    public Film findFilmById(int id) {
        return filmDbStorage.findFilmById(id);
    }

    public void deleteFilm(int id) {filmDbStorage.delete(id);}
}
