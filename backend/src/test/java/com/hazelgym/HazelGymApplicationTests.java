package com.hazelgym;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import com.hazelgym.model.Role;
import com.hazelgym.model.RoleName;
import com.hazelgym.repository.RoleRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HazelGymApplicationTests {

	@Autowired
	private RoleRepository roleRepository;

	@LocalServerPort
	private int port;

	@Test
	void contextLoads() {
	}

	@Test
	void registerTokenCanAccessMeEndpoint() throws Exception {
		ensureRoleExists(RoleName.CLIENT);

		String email = "test-" + System.currentTimeMillis() + "@hazelgym.com";
		String registerBody = """
				{
				  "nombre": "Test Client",
				  "email": "%s",
				  "password": "hazel123"
				}
				""".formatted(email);

		HttpClient httpClient = HttpClient.newHttpClient();
		HttpResponse<String> registerResponse = httpClient.send(
				HttpRequest.newBuilder()
						.uri(URI.create(baseUrl() + "/api/auth/register"))
						.header(HttpHeaders.CONTENT_TYPE, "application/json")
						.POST(HttpRequest.BodyPublishers.ofString(registerBody))
						.build(),
				HttpResponse.BodyHandlers.ofString()
		);

		assertThat(registerResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		String response = registerResponse.body();

		String token = extractJsonValue(response, "token");
		assertThat(token).isNotBlank();

		HttpResponse<String> meResponse = httpClient.send(
				HttpRequest.newBuilder()
						.uri(URI.create(baseUrl() + "/api/auth/me"))
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
						.GET()
						.build(),
				HttpResponse.BodyHandlers.ofString()
		);

		assertThat(meResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(meResponse.body()).contains("\"email\":\"" + email + "\"");
		assertThat(meResponse.body()).contains("\"role\":\"CLIENT\"");
	}

	private void ensureRoleExists(RoleName roleName) {
		if (roleRepository.findByName(roleName).isEmpty()) {
			Role role = new Role();
			role.setName(roleName);
			roleRepository.save(role);
		}
	}

	private String extractJsonValue(String json, String field) {
		String pattern = "\"" + field + "\":\"";
		int start = json.indexOf(pattern);
		if (start < 0) {
			return "";
		}
		start += pattern.length();
		int end = json.indexOf('"', start);
		if (end < 0) {
			return "";
		}
		return json.substring(start, end);
	}

	private String baseUrl() {
		return "http://localhost:" + port;
	}
}
