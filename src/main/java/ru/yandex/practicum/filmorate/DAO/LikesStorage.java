package ru.yandex.practicum.filmorate.DAO;

import java.util.Set;

public interface LikesStorage {
    Integer getAmountOfLikes(int filmId, int userId);

    Set<Long> getTopFilmLikes();

    void removeLike(Long idFilm, Long delIdUser);

    void addLike(Long id, Long userId);

    Set<Integer> getLikesByFilmId(Long filmId);
}
