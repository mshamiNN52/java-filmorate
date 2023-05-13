package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.time.LocalDate;

@Data
public class User {

    @NotNull
    @Positive
    private final Integer id;
    @Email(message = "некорректная форма email")
    private final String email;
    @NotNull(message = "Log cannot be null")
    private final String login;
    @NotNull (message = "Name cannot be null")
    private  String name;
    private final LocalDate birthday;

    public User(Integer id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
