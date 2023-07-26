package ru.yandex.practicum.filmorate.DAO.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.DAO.LikesStorage;
import ru.yandex.practicum.filmorate.DAO.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.NotFoundException;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Component
public class UserDbStorageImpl implements UserDbStorage {
    private final JdbcTemplate jdbcTemplate;
    FilmDbStorageImpl filmDbStorage;
    LikesStorage likesDbStorage;

    public UserDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate()).build();
    }

    @Override
    public User findUserById(int id) {
        String sqlQuery = "select id, email, login, name, birthday " + "from users where id = ?";
        try {
            jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
    }

    @Override
    public User createUser(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        String sqlQuery = "insert into users(email, login, name, birthday) " + "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId((int) keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public User editUser(User user) {
        findUserById(user.getId());
        String sqlQuery = "update users set " + "email = ?, login = ?, name = ?, birthday = ? " + "where id = ?";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public ArrayList<User> getAllUsers() {
        String sqlQuery = "select id, email, login, name, birthday from users";
        return (ArrayList<User>) jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    public void removeFriend(Long id, Long idRemoveFriend) {
        if (id == null || idRemoveFriend == null || id.equals(idRemoveFriend)) {
            return;
        }
        String sqlQueryRemoveFriend = "DELETE FROM userFriends f WHERE f.userId = ? AND f.friendsId = ?";
        try {
            jdbcTemplate.update(sqlQueryRemoveFriend, id, idRemoveFriend);
        } catch (EmptyResultDataAccessException e) {
            log.info("В базе нет информации по запросу {}.  userId={}, friendsId={}",
                    sqlQueryRemoveFriend, id, idRemoveFriend);
        }
    }

    @Override
    public Collection<Film> getRecommendations(int id) {
        Collection<Film> result = new HashSet<>();
        Collection<Film> films = filmDbStorage.getAllFilms();
        getAllUsers().forEach(user -> {
            if (user.getId() != id) {
                films.forEach(film -> {
                    Integer userLikes = likesDbStorage.getAmountOfLikes(film.getId(), id);
                    Integer otherUserLikes = likesDbStorage.getAmountOfLikes(film.getId(), user.getId());
                    if (userLikes == 0 && otherUserLikes > 0) {
                        result.add(film);
                    }
                });
            }
        });
        return result;
    }

    @Override
    public List<User> getCommonFriends(int id, int otherId) {
        List<User> commonFriends = new ArrayList<>();
        String sqlQueryCommonFriends = "SELECT u.* FROM userFriends f LEFT JOIN users u on f.friendsId = u.id " +
                " WHERE f.userId= ? AND f.friendsId IN (SELECT ff.friendsId FROM userFriends ff WHERE ff.userId=?)";
        try {
            commonFriends = jdbcTemplate.query(sqlQueryCommonFriends, this::mapRowToUser, id, otherId);
        } catch (EmptyResultDataAccessException e) {
            log.info("В базе нет информации по запросу {}.  userId={}, friendsId={}",
                    sqlQueryCommonFriends, id, otherId);
        }
        return commonFriends;
    }

    @Override
    public void delete(int id) {
        String sqlQueryDelete = "DELETE FROM users WHERE id = ?";
        try {
            jdbcTemplate.update(sqlQueryDelete, id);
        } catch (EmptyResultDataAccessException e) {
            log.info("В базе нет информации по запросу {}", sqlQueryDelete);
        }
    }
}
