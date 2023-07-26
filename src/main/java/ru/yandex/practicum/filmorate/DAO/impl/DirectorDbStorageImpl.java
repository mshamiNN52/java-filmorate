package ru.yandex.practicum.filmorate.DAO.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.DAO.DirectorDbStorage;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.validators.NotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DirectorDbStorageImpl implements DirectorDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public DirectorDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Director mapRowToDirector(ResultSet resultSet, int rowNum) throws SQLException {
        return Director.builder().id(resultSet.getInt("id")).name(resultSet.getString("name")).build();
    }

    @Override
    public List<Director> findAllDirectors() {
        String sqlQuery = "select id, name from director";
        return (ArrayList<Director>) jdbcTemplate.query(sqlQuery, this::mapRowToDirector);
    }

    @Override
    public Director getDirectorById(int id) {
        String sqlQuery = "select id, name from director where id = ?";
        try {
            jdbcTemplate.queryForObject(sqlQuery, this::mapRowToDirector, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Режиссер не найден");
        }
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToDirector, id);
    }

    @Override
    public Director createDirector(Director director) {
        if (director.getName().isEmpty() || director.getName().isBlank()) {
            throw new ru.yandex.practicum.filmorate.validators.ValidationException(HttpStatus.BAD_REQUEST, "Incorrect name");
        }
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("director")
                .usingGeneratedKeyColumns("id");
        Map<String, String> params = Map.of("name", director.getName());
        Number id = simpleJdbcInsert.executeAndReturnKey(params);
        director.setId(id.intValue());
        return director;
    }

    @Override
    public Director updateDirector(Director director) {
        String sql = "UPDATE director SET name = ? WHERE id = ?";
        if (getDirectorById(director.getId()).getId() == director.getId()) {
            jdbcTemplate.update(sql, director.getName(), director.getId());
        }
        return director;
    }

    @Override
    public void deleteDirector(int directorId) {
        String sql = "DELETE FROM director WHERE id = ?;";
        jdbcTemplate.update(sql, directorId);
    }

}
