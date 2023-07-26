package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@Builder
public class Director {
    private int id;
    @NotBlank
    @NotEmpty
    @Pattern(regexp = "^\\S*")
    private String name;

    public Director(int id, String name) {
        this.id = id;
        this.name = name;

    }
}
