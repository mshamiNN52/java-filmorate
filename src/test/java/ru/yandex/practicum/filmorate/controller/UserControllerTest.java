package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testAddUser() throws Exception {
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
    void testAddBadEmailUser() throws Exception {
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
    void testAddNullLoginUser() throws Exception {
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
    void testAddBadLoginUser() throws Exception {
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
    void testAddFutureBDUser() throws Exception {
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