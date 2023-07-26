package ru.yandex.practicum.filmorate.DAO.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.DAO.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.validators.NotFoundException;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Component
public class FilmDbStorageImpl implements FilmDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreDbStorage genreDbStorage;
    private final MpaDbStorage mpaDbStorage;
    private final UserDbStorage userDbStorage;
    private final DirectorDbStorage directorDbStorage;

    public FilmDbStorageImpl(JdbcTemplate jdbcTemplate,
                             GenreDbStorage genreDbStorage,
                             MpaDbStorage mpaDbStorage,
                             UserDbStorage userDbStorage,
                             DirectorDbStorage directorDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDbStorage = genreDbStorage;
        this.mpaDbStorage = mpaDbStorage;
        this.userDbStorage = userDbStorage;
        this.directorDbStorage = directorDbStorage;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder().id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .duration(resultSet.getLong("duration"))
                .releaseDate(resultSet.getDate("release_Date").toLocalDate())
                .mpa(mpaDbStorage.getRatingById(resultSet.getInt("rating_id"))).build();
    }

    @Override
    public Film findFilmById(int id) {
        String sqlQuery = "select id, name, description, duration, release_date, rating_id "
                + "from films where id = ?";
        try {
            jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Фильм не найден");
        }
        Film film = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);

        String sqlQuery2 = "select genre_id from film_genre where film_id = ?";
        List<Integer> listGenre = jdbcTemplate.queryForList(sqlQuery2, Integer.class, film.getId());
        Set<Genre> genres = film.getGenres();
        for (int genre : listGenre) {
            genres.add(genreDbStorage.getGenreById(genre));
        }
        film.setGenres(genres);

        String sqlQuery3 = "select director_id from film_director where film_id = ?";
        List<Integer> listDirector = jdbcTemplate.queryForList(sqlQuery3, Integer.class, film.getId());
        Set<Director> directors = film.getDirectors();
        for (int directorId : listDirector) {
            directors.add(directorDbStorage.getDirectorById(directorId));
        }
        film.setDirectors(directors);
        return film;

    }

    @Override
    public Film createFilm(Film film) {
        String sqlQuery = "insert into films(name, description, release_date, duration, rating_id) "
                + "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId((int) keyHolder.getKey().longValue());
        Set<Genre> genreList = film.getGenres();
        if (!genreList.isEmpty()) {
            for (Genre genre : genreList) {
                String sqlQuery2 = "merge into film_genre(film_id, genre_id) key (film_id) " + "values (?, ?)";
                jdbcTemplate.update(sqlQuery2, film.getId(), genre.getId());
            }
        }
        Set<Director> directorList = film.getDirectors();
        if (!directorList.isEmpty()) {
            for (Director director : directorList) {
                String sqlQuery3 = "merge into film_director(film_id, director_id) key (film_id) " + "values (?, ?)";
                jdbcTemplate.update(sqlQuery3, film.getId(), director.getId());
            }
        }
        return findFilmById(film.getId());
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "select id, name, description, duration, release_date, rating_id from films";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
        for (Film film : films) {
            String sqlQuery2 = "select genre_id from film_genre where film_id = ?";
            List<Integer> listGenre = jdbcTemplate.queryForList(sqlQuery2, Integer.class, film.getId());
            Set<Genre> genres = film.getGenres();
            for (int genre : listGenre) {
                genres.add(genreDbStorage.getGenreById(genre));
            }
            String sqlQuery3 = "select director_id from film_director where film_id = ?";
            List<Integer> listDirector = jdbcTemplate.queryForList(sqlQuery3, Integer.class, film.getId());
            Set<Director> directors = film.getDirectors();
            for (int dir : listDirector) {
                directors.add(directorDbStorage.getDirectorById(dir));
            }
        }
        return films;
    }

    @Override
    public Film editFilm(Film film) {
        findFilmById(film.getId());
        String sqlQuery = "update films set "
                + "name = ?, description = ?, duration = ?, release_date = ?, rating_id =? "
                + "where id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                film.getId());
        Set<Genre> genreList = film.getGenres();
        String sqlQuery3 = "delete from film_genre where film_id =?";
        jdbcTemplate.update(sqlQuery3, film.getId());
        if (!genreList.isEmpty()) {
            for (Genre genre : genreList) {
                String sqlQuery2 = "insert into film_genre(film_id, genre_id) " + "values (?, ?)";
                jdbcTemplate.update(sqlQuery2, film.getId(), genre.getId());
            }
        }
        Set<Director> directorList = film.getDirectors();
        String sqlQuery4 = "delete from film_director where film_id =?";
        jdbcTemplate.update(sqlQuery4, film.getId());
        if (!directorList.isEmpty()) {
            for (Director director : directorList) {
                String sqlQuery2 = "insert into film_director(film_id, director_id) " + "values (?, ?)";
                jdbcTemplate.update(sqlQuery2, film.getId(), director.getId());
            }

        }
        return findFilmById(film.getId());
    }

    @Override
    public void likeFilm(int filmId, int userId) {
        findFilmById(filmId);
        userDbStorage.findUserById(userId);
        String sqlQuery = "merge into film_likes (film_id, user_id, date_like)" + "values (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId, LocalDateTime.now());
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        findFilmById(filmId);
        userDbStorage.findUserById(userId);
        String sqlQuery = "delete from film_likes " + "where film_id = ? and user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public List<Film> filmRate(int year, int genreId, int count) {
        String sqlQuery;
        List<Film> films;
        if (genreId == 0 && year == 0) {
            sqlQuery = "select id, name, description, duration, release_date, rating_id from films as f " +
                    "LEFT join film_likes as fl on fl.film_id = f.id " +
                    "LEFT join film_genre as fg on f.id = fg.film_id "
                    + "group by f.name "
                    + "order by count(fl.film_id) desc "
                    + "limit ?;";
            films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);

        } else if (year == 0) {
            sqlQuery = "select id, name, description, duration, release_date, rating_id from films as f " +
                    "LEFT join film_likes as fl on fl.film_id = f.id " +
                    "LEFT join film_genre as fg on f.id = fg.film_id " +
                    "WHERE fg.genre_id = ? " +
                    "group by f.name "
                    + "order by count(fl.film_id) desc "
                    + "limit ?;";
            films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm, genreId, count);
            System.out.println(films);

        } else if (genreId == 0) {
            sqlQuery = "select id, name, description, duration, release_date, rating_id from films as f " +
                    "LEFT join film_likes as fl on fl.film_id = f.id " +
                    "LEFT join film_genre as fg on f.id = fg.film_id " +
                    "WHERE EXTRACT(YEAR FROM release_date) = ? "
                    + "group by f.name "
                    + "order by count(fl.film_id) desc "
                    + "limit ?;";
            films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm, year, count);
            System.out.println(films);


        } else {
            sqlQuery = "select id, name, description, duration, release_date, rating_id from films as f " +
                    "LEFT join film_likes as fl on fl.film_id = f.id " +
                    "LEFT join film_genre as fg on f.id = fg.film_id " +
                    "WHERE EXTRACT(YEAR FROM release_date) = ? " +
                    "AND fg.genre_id = ? "
                    + "group by f.name "
                    + "order by count(fl.film_id) desc "
                    + "limit ?;";
            films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm, year, genreId, count);
            System.out.println(films);

        }
        for (Film film : films) {
            String sqlQuery2 = "select genre_id from film_genre where film_id = ?";
            List<Integer> listGenre = jdbcTemplate.queryForList(sqlQuery2, Integer.class, film.getId());
            Set<Genre> genres = film.getGenres();
            for (int genre : listGenre) {
                genres.add(genreDbStorage.getGenreById(genre));
            }

            sqlQuery2 = "select director_id from film_director where film_id = ?";
            List<Integer> listDirector = jdbcTemplate.queryForList(sqlQuery2, Integer.class, film.getId());
            Set<Director> directors = film.getDirectors();
            for (int dir : listDirector) {
                directors.add(directorDbStorage.getDirectorById(dir));
            }
        }
        return films;
    }

    @Override
    public List<Film> filmDirector(int directorId, String sortBy) {
        String sqlQuery;
        List<Film> films;
        if (sortBy.equals("likes")) {
            sqlQuery = "select id, name, description, duration, release_date, rating_id from films as f " +
                    "LEFT join film_likes as fl on fl.film_id = f.id " +
                    "LEFT join film_director as fd on f.id = fd.film_id " +
                    "WHERE fd.director_id = ? "
                    + "group by f.name "
                    + "order by count(fl.film_id) desc;";
            films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm, directorId);
        } else {
            sqlQuery = "select id, name, description, duration, release_date, rating_id from films as f " +
                    "LEFT join film_likes as fl on fl.film_id = f.id " +
                    "LEFT join film_director as fd on f.id = fd.film_id " +
                    "WHERE fd.director_id = ? "
                    + "group by f.name "
                    + "order by EXTRACT(YEAR FROM release_date);";
            films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm, directorId);
        }
        for (Film film : films) {
            selectGenre(film);
            selectDirector(film);
        }
        if (films.isEmpty()) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Фильмы не найден");
        }
        return films;
    }

    private void selectGenre(Film film) {
        String sqlQuery2 = "select genre_id from film_genre where film_id = ?";
        List<Integer> listGenre = jdbcTemplate.queryForList(sqlQuery2, Integer.class, film.getId());
        Set<Genre> genres = film.getGenres();
        for (int genre : listGenre) {
            genres.add(genreDbStorage.getGenreById(genre));
        }
    }

    private void selectDirector(Film film) {
        String sqlQuery2 = "select director_id from film_director where film_id = ?";
        List<Integer> listDirector = jdbcTemplate.queryForList(sqlQuery2, Integer.class, film.getId());
        Set<Director> directors = film.getDirectors();
        for (int dir : listDirector) {
            directors.add(directorDbStorage.getDirectorById(dir));
        }
    }

    @Override
    public List<Film> commonFilms(int userId, int friendId) {
        String sql = "SELECT f.*, M.* " +
                "FROM FILM_LIKES " +
                "JOIN FILM_LIKES fl ON fl.FILM_ID = FILM_LIKES.FILM_ID " +
                "JOIN FILMS f on f.id = fl.FILM_id " +
                "JOIN MPA M on f.RATING_ID = M.ID " +
                "WHERE fl.USER_ID = ? AND FILM_LIKES.USER_ID = ?";

        return jdbcTemplate.query(sql, this::mapRowToFilm, userId, friendId);
    }

    @Override
    public void delete(int id) {
        String sqlQueryDelete = "DELETE FROM films WHERE id = ?";
        jdbcTemplate.update(sqlQueryDelete, id);
    }
}
