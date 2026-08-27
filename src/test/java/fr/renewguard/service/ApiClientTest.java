package fr.renewguard.service;

import com.fasterxml.jackson.core.type.TypeReference;
import okhttp3.*;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class ApiClientTest {

	private MockWebServer mockWebServer;
	private ApiClient apiClient;

	@BeforeEach
	void setUp() throws Exception {
		mockWebServer = new MockWebServer();
		mockWebServer.start();

		String baseUrl = mockWebServer.url("").toString();
		apiClient = ApiClient.getInstance();
		apiClient.setBaseUrl(baseUrl);
	}

	@AfterEach
	void tearDown() throws Exception {
		mockWebServer.shutdown();
	}

	@Test
	void testGetWithClass() throws Exception {
		mockWebServer.enqueue(new MockResponse()
			.setBody("{\"id\": 1, \"name\": \"Test\"}")
			.setResponseCode(200));

		CompletableFuture<TestDto> future = apiClient.get("/test", TestDto.class);
		TestDto result = future.get();

		assertNotNull(result);
		assertEquals(1, result.id);
		assertEquals("Test", result.name);
	}

	@Test
	void testGetWithTypeReference() throws Exception {
		mockWebServer.enqueue(new MockResponse()
			.setBody("[{\"id\": 1, \"name\": \"Item1\"}, {\"id\": 2, \"name\": \"Item2\"}]")
			.setResponseCode(200));

		CompletableFuture<List<TestDto>> future = apiClient.get(
			"/items", new TypeReference<List<TestDto>>() {}
		);
		List<TestDto> result = future.get();

		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals("Item1", result.get(0).name);
		assertEquals("Item2", result.get(1).name);
	}

	@Test
	void testPost() throws Exception {
		mockWebServer.enqueue(new MockResponse()
			.setBody("{\"id\": 1, \"name\": \"Created\"}")
			.setResponseCode(200));

		TestDto request = new TestDto(0, "New Item");
		CompletableFuture<TestDto> future = apiClient.post("/test", request, TestDto.class);
		TestDto result = future.get();

		assertNotNull(result);
		assertEquals(1, result.id);
		assertEquals("Created", result.name);

		RecordedRequest recordedRequest = mockWebServer.takeRequest();
		assertEquals("POST", recordedRequest.getMethod());
		assertTrue(recordedRequest.getBody().readUtf8().contains("New Item"));
	}

	@Test
	void testPut() throws Exception {
		mockWebServer.enqueue(new MockResponse()
			.setBody("{\"id\": 1, \"name\": \"Updated\"}")
			.setResponseCode(200));

		TestDto request = new TestDto(1, "Updated Item");
		CompletableFuture<TestDto> future = apiClient.put("/test/1", request, TestDto.class);
		TestDto result = future.get();

		assertNotNull(result);
		assertEquals("Updated", result.name);

		RecordedRequest recordedRequest = mockWebServer.takeRequest();
		assertEquals("PUT", recordedRequest.getMethod());
	}

	@Test
	void testPatch() throws Exception {
		mockWebServer.enqueue(new MockResponse()
			.setBody("{\"id\": 1, \"name\": \"Patched\"}")
			.setResponseCode(200));

		Map<String, String> request = Map.of("name", "Patched");
		CompletableFuture<TestDto> future = apiClient.patch("/test/1", request, TestDto.class);
		TestDto result = future.get();

		assertNotNull(result);
		assertEquals("Patched", result.name);

		RecordedRequest recordedRequest = mockWebServer.takeRequest();
		assertEquals("PATCH", recordedRequest.getMethod());
	}

	@Test
	void testDelete() throws Exception {
		mockWebServer.enqueue(new MockResponse().setResponseCode(204));

		CompletableFuture<Void> future = apiClient.delete("/test/1");
		future.get();

		RecordedRequest recordedRequest = mockWebServer.takeRequest();
		assertEquals("DELETE", recordedRequest.getMethod());
	}

	@Test
	void testHttpError401() throws Exception {
		mockWebServer.enqueue(new MockResponse().setResponseCode(401));

		CompletableFuture<TestDto> future = apiClient.get("/test", TestDto.class);

		assertThrows(Exception.class, () -> future.get());
	}

	@Test
	void testHttpError404() throws Exception {
		mockWebServer.enqueue(new MockResponse().setResponseCode(404));

		CompletableFuture<TestDto> future = apiClient.get("/notfound", TestDto.class);

		assertThrows(Exception.class, () -> future.get());
	}

	@Test
	void testHttpError500() throws Exception {
		mockWebServer.enqueue(new MockResponse().setResponseCode(500));

		CompletableFuture<TestDto> future = apiClient.get("/test", TestDto.class);

		assertThrows(Exception.class, () -> future.get());
	}

	static class TestDto {
		public int id;
		public String name;

		public TestDto() {}
		public TestDto(int id, String name) {
			this.id = id;
			this.name = name;
		}
	}

}
