package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.DurationTypeAdapter;
import ru.yandex.practicum.filmorate.LocalDateTypeAdapter;
import ru.yandex.practicum.filmorate.model.User;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserControllerTest {

    UserController userController;
    User user;
    LocalDate birthDay =  LocalDate.of(2000,12,23);
    private Gson gson =  new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .create();

    @BeforeEach
    void setUp () {
        userController = new UserController();
        user = new User(
                1,
                "dodo@mail.ru",
                "pupsik",
                "tony",
                birthDay
               );
    }

    @Test
    void create() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/user"))
                .header("Content-Type","application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(user)))
                .build();
        HttpResponse <String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        User user1 = gson.fromJson(response.body(), User.class);
        assertEquals(200, response.statusCode());
        assertEquals(user, user1);
    }

    @Test
    void get() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/user"))
                .header("Content-Type","application/json")
                .GET()
                .build();
        HttpResponse <String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

    }

    @Test
    void put() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/user"))
                .header("Content-Type","application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(user)))
                .build();
        HttpResponse <String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        User user2 = gson.fromJson(response.body(), User.class);
        assertEquals(200, response.statusCode());

    }

}