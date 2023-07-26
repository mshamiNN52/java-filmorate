package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final UserService userService;

    @Autowired
    public FilmController(FilmService filmService, UserService userService) {
        this.filmService = filmService;
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @GetMapping
    public List<Film> findAll() {
        return filmService.getAllFilms();
    }

    @PutMapping
    public Film saveFilm(@Valid @RequestBody Film film) {
        return filmService.editFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        filmService.findFilmById(filmId);
        userService.findUserById(userId);
        filmService.likeFilm(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        filmService.findFilmById(filmId);
        userService.findUserById(userId);
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> filmRate(@RequestParam(value = "count", defaultValue = "10") Integer count,
                               @RequestParam(value = "genreId", defaultValue = "0") Integer genreId,
                               @RequestParam(value = "year", defaultValue = "0") Integer year) {
        return filmService.filmRate(year, genreId, count);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable("id") int id) {
        return filmService.findFilmById(id);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> filmDirector(@PathVariable("directorId") int directorId,
                                   @RequestParam(value = "sortBy", defaultValue = "likes") String sortBy) {
        return filmService.filmDirector(directorId, sortBy);
    }

    @GetMapping("/common")
    public List<Film> commonFilms(@RequestParam int userId, @RequestParam int friendId) {
        return filmService.commonFilms(1, 2);
    }

    @DeleteMapping("/{filmId}")
    public void deleteFilmById(@PathVariable int filmId) {
        log.info("Удаление фильма по id:" + filmId + "...");
        filmService.deleteFilm(filmId);
        log.info("Фильм удален");
    }

    @GetMapping("/{id}/recommendations")
    public Collection<Film> getRecommendations(@Valid @PathVariable int id) {
        log.info("Получение рекомендаций пользователя " + id);
        return userService.getRecommendations(id);
    }

}
