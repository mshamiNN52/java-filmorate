package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@Validated
@Slf4j
public class UserController {

    Map<Integer,User> users = new HashMap<>();

    @GetMapping("/user")
    public List<User> getUsers() {
        log.debug("Запрос списка пользователей");
        return new ArrayList<>(users.values());
    }

    @PostMapping("/user")
    public User create(@RequestBody @Validated User user)  {
        if(user.getName().isEmpty()){
            log.debug("Добавление пользователя");
            user.setName(user.getLogin());
            users.put(user.getId(), user);
        }else{
            users.put(user.getId(), user);
        }
        return user;
    }

    @PutMapping("/user")
    public User update(@RequestBody @Validated  User user) {
        for (User users1 : users.values()) {
            if (user.getId().equals(users1.getId())) {
                log.debug("Обновление данных пользователя");
                users.replace(user.getId(), user);
            } else {
                log.debug("пользователь  не найден");
                System.out.println("No Find");
            }
        }
        return user;
    }
}
