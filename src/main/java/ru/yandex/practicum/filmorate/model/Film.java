package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film{

    @Positive (message = "id cannot be negative")
    private final Integer id;
    @NotNull (message = "Name cannot be null")
    private final String name ;
    @Size(min = 1, max = 200)
    private final String description ;
    private final LocalDate releaseDate;
    @Positive(message = "duration cannot be negative")
    private final Duration duration;
}
