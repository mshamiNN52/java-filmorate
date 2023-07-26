package ru.yandex.practicum.filmorate.DAO.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.DAO.MpaDbStorage;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.validators.NotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MpaDbStorageImpl implements MpaDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Mpa mapRowToRating(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder().id(resultSet.getInt("id")).name(resultSet.getString("name")).build();
    }

    @Override
    public List<Mpa> findAllRating() {
        String sqlQuery = "select id, name from MPA";
        return (ArrayList<Mpa>) jdbcTemplate.query(sqlQuery, this::mapRowToRating);

    }

    @Override
    public Mpa getRatingById(int id) {
        String sqlQuery = "select id, name " + "from MPA where id = ?";
        try {
            jdbcTemplate.queryForObject(sqlQuery, this::mapRowToRating, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Рейтинг не найден");
        }
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToRating, id);

    }
}
