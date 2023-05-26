package ru.yandex.practicum.filmorate.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;


    @Test
    void testAddFilm() throws Exception {
        Film film = Film.builder()
                .id(1)
                .name("Test")
                .description("test")
                .duration(150)
                .releaseDate(LocalDate.of(2018, 5, 14))
                .build();
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andReturn();
    }

    @Test
    void testAddNullNameFilm() throws Exception {
        Film film = Film.builder()
                .id(1)
                .name("")
                .description("test2")
                .duration(100)
                .releaseDate(LocalDate.of(1991, 7, 30))
                .build();
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();
    }

    @Test
    void testAddMaxDescrFilm() throws Exception {
        Film film = Film.builder()
                .id(1)
                .name("Test1")
                .description("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                        "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                        "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                        "11111111111111111111111111")
                .duration(100)
                .releaseDate(LocalDate.of(2012, 5, 14))
                .build();
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();
    }

    @Test
    void testAddUpdateFilm() throws Exception {
        Film film = Film.builder()
                .id(1)
                .name("Test1")
                .description("Test1")
                .duration(100)
                .releaseDate(LocalDate.of(2012, 5, 14))
                .build();
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();
    }

    @Test
    void testAddZeroDurationFilm() throws Exception {
        Film film = Film.builder()
                .id(1)
                .name("")
                .description("test")
                .duration(0)
                .releaseDate(LocalDate.of(2012, 5, 14))
                .build();
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();
    }

    @Test
    void testAddBadDurationFilm() throws Exception {
        Film film = Film.builder()
                .id(1)
                .name("Go")
                .description("test")
                .duration(-1)
                .releaseDate(LocalDate.of(2012, 5, 14))
                .build();
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();
    }

}