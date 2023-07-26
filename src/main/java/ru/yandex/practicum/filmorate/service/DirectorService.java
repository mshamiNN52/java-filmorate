package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.DAO.DirectorDbStorage;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

@Service
public class DirectorService {
    private final DirectorDbStorage directorDbStorage;

    @Autowired
    public DirectorService(DirectorDbStorage directorDbStorage) {
        this.directorDbStorage = directorDbStorage;
    }

    public List<Director> findAllDirectors() {
        return directorDbStorage.findAllDirectors();
    }

    public Director getDirectorById(int id) {
        return directorDbStorage.getDirectorById(id);
    }

    public Director createDirector(Director director) {
        return directorDbStorage.createDirector(director);
    }

    public Director updateDirector(Director director) {
        return directorDbStorage.updateDirector(director);
    }

    public void deleteDirector(int directorId) {
        directorDbStorage.deleteDirector(directorId);
    }
}
