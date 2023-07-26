package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.DAO.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.DAO.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Slf4j
@Service
public class UserService {
    private final UserDbStorage userDbStorage;
    private final FriendshipDbStorage friendshipDbStorage;

    @Autowired
    public UserService(UserDbStorage userDbStorage, FriendshipDbStorage friendshipDbStorage) {
        this.friendshipDbStorage = friendshipDbStorage;
        this.userDbStorage = userDbStorage;
    }

    public User createUser(User user) {
        return userDbStorage.createUser(user);
    }

    public List<User> findAll() {
        return userDbStorage.getAllUsers();
    }

    public User saveUser(User user) {
        return userDbStorage.editUser(user);
    }

    public User findUserById(int id) {
        return userDbStorage.findUserById(id);
    }

    public void addFriend(int userId, int friendId) {
        User user = userDbStorage.findUserById(userId);
        User friend = userDbStorage.findUserById(friendId);
        friendshipDbStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        User user = userDbStorage.findUserById(userId);
        User friend = userDbStorage.findUserById(friendId);
        friendshipDbStorage.deleteFriend(userId, friendId);
    }

    public List<User> getFriends(int id) {
        User user = userDbStorage.findUserById(id);
        ArrayList<User> friendsList = new ArrayList<>();
        List<Integer> friends = friendshipDbStorage.getFriends(id);
        for (int friend : friends) {
            friendsList.add(userDbStorage.findUserById(friend));
        }
        return friendsList;
    }

    public List<User> getCommonFriends(int idUser, int idFriend) {
        ArrayList<User> commonFriendsList = new ArrayList<>();
        List<Integer> userFriends = friendshipDbStorage.getFriends(idUser);
        List<Integer> friendFriends = friendshipDbStorage.getFriends(idFriend);
        for (int friend : userFriends) {
            if (friendFriends.contains(friend)) {
                commonFriendsList.add(userDbStorage.findUserById(friend));
            }
        }
        return commonFriendsList;
    }

    public Collection<Film> getRecommendations(int id) {
        return userDbStorage.getRecommendations(id);
    }

    private boolean contains(int id) {
        return userDbStorage.getAllUsers().contains(id);
    }

    public void deleteUser(int id) {
        if (contains(id)) {
            findAll()
                    .stream()
                    .forEach(user -> {
                        deleteFriend(user.getId(), id);
                        userDbStorage.getCommonFriends(id, user.getId())
                                .stream()
                                .forEach(userCommon -> {
                                    deleteFriend(user.getId(), id);
                                });
                    });
            userDbStorage.delete(id);
        } else {
            log.info("User с id " + id + " не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
