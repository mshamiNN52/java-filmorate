package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final UserStorage userStorage;

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public List<User> findAll() {
        return userStorage.getAllUsers();
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User findUserById(int id) {
        return userStorage.getUserByID(id);
    }

    public void addFriend(int userId, int friendId) {
        User user = userStorage.getUserByID(userId);
        User friend = userStorage.getUserByID(friendId);
        Set<Integer> userFrList = user.getFriends();
        userFrList.add(friendId);
        user.setFriends(userFrList);
        Set<Integer> friendFrList = friend.getFriends();
        friendFrList.add(userId);
        friend.setFriends(friendFrList);
    }

    public List<User> getFriends(int id) {
        ArrayList<User> friendsList = new ArrayList<>();
        Set<Integer> friends = userStorage.getUserByID(id).getFriends();
        for (int friend : friends) {
            friendsList.add(userStorage.getUserByID(friend));
        }
        return friendsList;
    }

    public void deleteFriend(int userId, int friendId) {
        User user = userStorage.getUserByID(userId);
        User friend = userStorage.getUserByID(friendId);
        Set<Integer> userFriendsList = user.getFriends();
        userFriendsList.remove(friendId);
        user.setFriends(userFriendsList);
        Set<Integer> friendList = friend.getFriends();
        friendList.remove(userId);
        friend.setFriends(friendList);
    }

    public List<User> getCommonFriends(int idUser, int idFriend) {
        ArrayList<User> commonFriendsList = new ArrayList<>();
        User user = userStorage.getUserByID(idUser);
        User friend = userStorage.getUserByID(idFriend);
        Set<Integer> friends = user.getFriends();
        for (int friendID : friends) {
            if (friend.getFriends().contains(friendID)) {
                commonFriendsList.add(userStorage.getUserByID(friendID));
            }
        }
        return commonFriendsList;
    }
}
