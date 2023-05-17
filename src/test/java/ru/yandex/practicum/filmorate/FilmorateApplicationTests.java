package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class FilmorateApplicationTests {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;


    @Test
    void addFilm() throws Exception {
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
    void addNullNameFilm() throws Exception {
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
    void addMaxDescrFilm() throws Exception {
        Film film = Film.builder()
                .id(1)
                .name("Test1")
                .description("111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111")
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
    void addEarlyFilm() throws Exception {
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
    void addZeroDurationFilm() throws Exception {
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
    void addBadDurationFilm() throws Exception {
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

    @Test
    void addUser() throws Exception {
        User user = User.builder()
                .id(1)
                .name("mikhail")
                .email("m.shamin52@yandex.ru")
                .login("shamin")
                .birthday(LocalDate.of(1993, 3, 18))
                .build();
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andReturn();
    }

    @Test
    void addBadEmailUser() throws Exception {
        User user = User.builder()
                .id(1)
                .name("mikhail")
                .email("m.shamin52yandex.ru")
                .login("shamin")
                .birthday(LocalDate.of(1993, 3, 18))
                .build();
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();
    }

    @Test
    void addNullLoginUser() throws Exception {
        User user = User.builder()
                .id(1)
                .name("mikhail")
                .email("m.shamin52@yandex.ru")
                .login("")
                .birthday(LocalDate.of(1993, 3, 18))
                .build();
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();
    }

    @Test
    void addBadLoginUser() throws Exception {
        User user = User.builder()
                .id(1)
                .name("mikhail")
                .email("m.shamin52@yandex.ru")
                .login("sha  min")
                .birthday(LocalDate.of(1993, 3, 18))
                .build();
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();
    }

    @Test
    void addFutureBDUser() throws Exception {
        User user = User.builder()
                .id(1)
                .name("mikhail")
                .email("m.shamin52@yandex.ru")
                .login("shamin")
                .birthday(LocalDate.of(2993, 3, 18))
                .build();
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();
    }

}
