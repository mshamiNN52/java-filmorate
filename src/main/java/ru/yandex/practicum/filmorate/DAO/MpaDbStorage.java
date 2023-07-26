package ru.yandex.practicum.filmorate.DAO;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaDbStorage {
    List<Mpa> findAllRating();

    Mpa getRatingById(int id);
}
