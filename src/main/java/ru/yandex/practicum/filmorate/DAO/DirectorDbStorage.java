package ru.yandex.practicum.filmorate.DAO;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorDbStorage {
    List<Director> findAllDirectors();

    Director getDirectorById(int id);

    Director createDirector(Director director);

    Director updateDirector(Director director);

    void deleteDirector(int directorId);

}
