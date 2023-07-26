package ru.yandex.practicum.filmorate.DAO;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface UserDbStorage {

    User findUserById(int id);

    User createUser(User user);

    User editUser(User user);

    ArrayList<User> getAllUsers();

    Collection<Film> getRecommendations(int id);

    List<User> getCommonFriends(int id, int id1);

    void delete(int id);
}
