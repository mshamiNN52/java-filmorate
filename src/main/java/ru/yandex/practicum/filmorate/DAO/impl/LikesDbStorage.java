package ru.yandex.practicum.filmorate.DAO.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.DAO.LikesStorage;


@Slf4j
@Component("LikesDbStorage")
@RequiredArgsConstructor
public class LikesDbStorage implements LikesStorage{


    private final JdbcTemplate jdbcTemplate;

    @Override
    public Set<Integer> getLikesByFilmId(Long filmId) {
        String sqlQuery =
                "SELECT userId " +
                        "FROM filmLikes " +
                        "WHERE filmId = ?";
        List<Integer> likes = jdbcTemplate.queryForList(sqlQuery, Integer.class, filmId);
        return new HashSet<>(likes);
    }

    @Override
    public void removeLike(Long idFilm, Long userId) {
        String sqlQueryRemoveLike = "DELETE FROM filmLikes WHERE filmId = ? AND userId = ?";
        jdbcTemplate.update(sqlQueryRemoveLike,
                idFilm,
                userId);
    }

    @Override
    public void addLike(Long id, Long userId) {
        String sqlQueryAdd = "MERGE INTO filmLikes(filmId,userId)" +
                " values(?,?)";
        jdbcTemplate.update(sqlQueryAdd,
                id,
                userId);
    }

    @Override
    public Integer getAmountOfLikes(int filmId, int userId) {
        int amount = 0;
        String sqlAmountOfLikes = "SELECT count(*) " +
                "FROM filmLikes WHERE filmId=? AND userId = ?";
        try {
            amount = jdbcTemplate.queryForObject(sqlAmountOfLikes, Integer.class, filmId, userId);
        } catch (EmptyResultDataAccessException e) {
            log.info("В базе нет информации по запросу {}. filmId={}, userId={}",
                    sqlAmountOfLikes, filmId, userId);
        }
        return amount;
    }

    @Override
    public Set<Long> getTopFilmLikes() {
        String sqlQueryTopFilmLikes =
                "SELECT t.id FROM films t LEFT JOIN " +
                        "(SELECT filmId as id, count(userId) AS count FROM filmLikes GROUP BY filmId) AS cn ON t.id = cn.id " +
                        "ORDER BY (COALESCE(t.RATE, 0) + COALESCE(cn.count, 0)) desc";
        return new LinkedHashSet<>(jdbcTemplate.queryForList(sqlQueryTopFilmLikes, Long.class));
    }
}
