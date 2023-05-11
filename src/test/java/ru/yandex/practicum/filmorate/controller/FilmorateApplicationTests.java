package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.DurationTypeAdapter;
import ru.yandex.practicum.filmorate.LocalDateTypeAdapter;
import ru.yandex.practicum.filmorate.model.Film;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FilmorateApplicationTests {

	FilmController filmController;
	Film film;
	private Gson gson =  new GsonBuilder()
    .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
			.registerTypeAdapter(Duration.class, new DurationTypeAdapter())
			.create();

	@BeforeEach
	void setUp () {
		filmController = new FilmController();
		film = new Film(
				1,
				"dodo",
				"1234hkjhmnb",
				LocalDate.of(2023,12,23),
				Duration.ofMinutes(170));
	}

	@Test
	void create() throws IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:8080/film"))
				.header("Content-Type","application/json")
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(film)))
				.build();
		HttpResponse <String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		Film filmServ = gson.fromJson(response.body(), Film.class);
		assertEquals(200, response.statusCode());
		assertEquals(film, filmServ);
	}

	@Test
	void get() throws IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:8080/film"))
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
				.uri(URI.create("http://localhost:8080/film"))
				.header("Content-Type","application/json")
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(film)))
				.build();
		HttpResponse <String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		Film filmServ = gson.fromJson(response.body(), Film.class);
		assertEquals(200, response.statusCode());
		assertEquals(film, filmServ);
	}

}
