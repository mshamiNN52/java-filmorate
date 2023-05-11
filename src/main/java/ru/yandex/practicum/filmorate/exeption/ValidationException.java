package ru.yandex.practicum.filmorate.exeption;

import org.springframework.http.HttpStatus;

public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }


}
