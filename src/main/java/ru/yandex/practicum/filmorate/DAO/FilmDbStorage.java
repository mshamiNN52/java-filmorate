package ru.yandex.practicum.filmorate.DAO;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmDbStorage {
    Film findFilmById(int id);

    Film createFilm(Film film);

    List<Film> getAllFilms();

    Film editFilm(Film film);
    
    List<Film> filmRate(int count);

    void delete(int id);

    void likeFilm(int filmId, int userId);

    void deleteLike(int filmId, int userId);
}
