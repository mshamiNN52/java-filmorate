package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    private final LocalDate date =LocalDate.of(1985,12,28);
    Map<Integer, Film> films = new HashMap<>();

    @GetMapping("/film")
    public List<Film> getUsers() {
        log.debug("Запрос списка фильмов");
        List<Film> list;
        return list = new ArrayList<Film>(films.values());
    }

    @PostMapping("/film")
    public Film create(@RequestBody Film film) throws ValidationException {
        log.debug("Добавление фильма в список");
        if (film.getName() != null && film.getReleaseDate().isBefore(date) && film.getDescription().length() <= 200 && film.getDuration().isPositive()) {
           films.put(film.getId(), film);
        }
        return film;
    }

    @PutMapping("/film")
    public Film update(@RequestBody Film film) throws ValidationException {
       if (films.containsKey(film.getId())) {
           log.debug("обновление фильма");
           films.replace(film.getId(), film);
       } else{
           log.debug("Такого фильма нет");
           System.out.println("Такого фильма нет");
       }
        return film;
    }
}
