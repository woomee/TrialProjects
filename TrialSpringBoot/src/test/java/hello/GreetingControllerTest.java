package hello;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

public class GreetingControllerTest {

	private WebTestClient client;
	@Before
	public void setUp() throws Exception {
		client = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
	}

	@Test
	public void testGreeting() {
		//fail("まだ実装されていません");
		GreetingController controller = new GreetingController();
		//Assert.assertEquals("Hello, World", controller.greeting("World"));
		assertEquals("Hello, World!", controller.greeting("World").getContent());
	}

	@Test
	public void testGreetingByJSON() {
		client.post().uri("/greeting")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.content").isEqualTo("Hello, World!");

		client.get().uri("/greeting")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.content").isEqualTo("Hello, World!");
	}


	@Test
	public void testGreetingByJSON_NG() {
		client.post().uri("/greetingAA")
				.exchange()
				.expectStatus().is4xxClientError();
	}
}
