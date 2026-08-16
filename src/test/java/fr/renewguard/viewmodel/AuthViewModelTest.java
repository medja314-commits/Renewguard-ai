package fr.renewguard.viewmodel;

import fr.renewguard.model.dto.AuthResponseDto;
import fr.renewguard.service.ApiClient;
import fr.renewguard.viewmodel.shared.SessionViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AuthViewModelTest {

	@Mock
	private ApiClient apiClient;

	@Mock
	private SessionViewModel sessionViewModel;

	private AuthViewModel viewModel;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		viewModel = new AuthViewModel(apiClient);

		try {
			var sessionField = SessionViewModel.class.getDeclaredField("INSTANCE");
			sessionField.setAccessible(true);
			sessionField.set(null, sessionViewModel);
		} catch (Exception e) {
			// Ignore if can't mock singleton
		}
	}

	@Test
	void testInitialState() {
		assertEquals("", viewModel.getEmail());
		assertEquals("", viewModel.getPassword());
		assertTrue(viewModel.isRememberMe());
		assertFalse(viewModel.isLoading());
		assertNull(viewModel.getErrorMessage());
	}

	@Test
	void testLoginSuccess() throws Exception {
		AuthResponseDto response = createMockAuthResponse("test-jwt-token", "testuser", "Test Site");

		when(apiClient.post(
			eq("/auth/token"),
			any(),
			eq(AuthResponseDto.class)
		)).thenReturn(CompletableFuture.completedFuture(response));

		viewModel.emailProperty().set("user@example.com");
		viewModel.passwordProperty().set("password123");
		viewModel.rememberMeProperty().set(true);

		boolean callbackExecuted[] = {false};
		viewModel.setOnSuccess(() -> callbackExecuted[0] = true);

		viewModel.login();
		Thread.sleep(100);

		assertFalse(viewModel.isLoading());
		assertNull(viewModel.getErrorMessage());
		assertTrue(callbackExecuted[0]);
	}

	@Test
	void testLoginEmptyEmail() {
		viewModel.emailProperty().set("");
		viewModel.passwordProperty().set("password123");

		viewModel.login();

		assertEquals("Veuillez renseigner tous les champs.", viewModel.getErrorMessage());
		assertFalse(viewModel.isLoading());
	}

	@Test
	void testLoginEmptyPassword() {
		viewModel.emailProperty().set("user@example.com");
		viewModel.passwordProperty().set("");

		viewModel.login();

		assertEquals("Veuillez renseigner tous les champs.", viewModel.getErrorMessage());
		assertFalse(viewModel.isLoading());
	}

	@Test
	void testLoginBothEmpty() {
		viewModel.emailProperty().set("");
		viewModel.passwordProperty().set("");

		viewModel.login();

		assertEquals("Veuillez renseigner tous les champs.", viewModel.getErrorMessage());
		assertFalse(viewModel.isLoading());
	}

	@Test
	void testLoginApiFailure() throws Exception {
		Exception testError = new RuntimeException("Network error");

		when(apiClient.post(
			eq("/auth/token"),
			any(),
			eq(AuthResponseDto.class)
		)).thenReturn(CompletableFuture.failedFuture(testError));


		viewModel.emailProperty().set("user@example.com");
		viewModel.passwordProperty().set("password123");

		viewModel.login();
		Thread.sleep(100);

		assertEquals("Identifiants incorrects ou serveur indisponible.", viewModel.getErrorMessage());
		assertFalse(viewModel.isLoading());
	}

	@Test
	void testLoginUnauthorized() throws Exception {
		Exception unauthorizedException = new ApiClient.ApiException(401, "Invalid credentials");

		when(apiClient.post(
			eq("/auth/token"),
			any(),
			eq(AuthResponseDto.class)
		)).thenReturn(CompletableFuture.failedFuture(unauthorizedException));


		viewModel.emailProperty().set("user@example.com");
		viewModel.passwordProperty().set("wrongpassword");

		viewModel.login();
		Thread.sleep(100);

		assertEquals("Identifiants incorrects ou serveur indisponible.", viewModel.getErrorMessage());
		assertFalse(viewModel.isLoading());
	}

	@Test
	void testLoginWithRememberMe() throws Exception {
		AuthResponseDto response = createMockAuthResponse("test-jwt-token", "testuser", "Test Site");

		when(apiClient.post(
			eq("/auth/token"),
			any(),
			eq(AuthResponseDto.class)
		)).thenReturn(CompletableFuture.completedFuture(response));


		viewModel.emailProperty().set("user@example.com");
		viewModel.passwordProperty().set("password123");
		viewModel.rememberMeProperty().set(true);

		viewModel.login();
		Thread.sleep(100);

		assertNull(viewModel.getErrorMessage());
		assertFalse(viewModel.isLoading());
	}

	@Test
	void testLoginWithoutRememberMe() throws Exception {
		AuthResponseDto response = createMockAuthResponse("test-jwt-token", "testuser", "Test Site");

		when(apiClient.post(
			eq("/auth/token"),
			any(),
			eq(AuthResponseDto.class)
		)).thenReturn(CompletableFuture.completedFuture(response));


		viewModel.emailProperty().set("user@example.com");
		viewModel.passwordProperty().set("password123");
		viewModel.rememberMeProperty().set(false);

		viewModel.login();
		Thread.sleep(100);

		assertNull(viewModel.getErrorMessage());
		assertFalse(viewModel.isLoading());
	}

	@Test
	void testErrorMessageClearing() {
		viewModel.errorMessageProperty().set("Previous error");

		viewModel.emailProperty().set("user@example.com");
		viewModel.passwordProperty().set("password123");

		viewModel.login();

		assertNull(viewModel.getErrorMessage());
	}

	@Test
	void testLoadingStateChange() throws Exception {
		CompletableFuture<AuthResponseDto> slowFuture = new CompletableFuture<>();

		when(apiClient.post(
			eq("/auth/token"),
			any(),
			eq(AuthResponseDto.class)
		)).thenReturn(slowFuture);


		viewModel.emailProperty().set("user@example.com");
		viewModel.passwordProperty().set("password123");

		viewModel.login();
		assertTrue(viewModel.isLoading());

		AuthResponseDto response = createMockAuthResponse("token", "user", "site");

		slowFuture.complete(response);
		Thread.sleep(100);

		assertFalse(viewModel.isLoading());
	}

	@Test
	void testPropertyBindings() {
		viewModel.emailProperty().set("email@test.com");
		assertEquals("email@test.com", viewModel.getEmail());

		viewModel.passwordProperty().set("mypassword");
		assertEquals("mypassword", viewModel.getPassword());

		viewModel.rememberMeProperty().set(false);
		assertFalse(viewModel.isRememberMe());
	}

	private AuthResponseDto createMockAuthResponse(String accessToken, String username, String siteName) {
		AuthResponseDto response = new AuthResponseDto();
		try {
			setFieldViaReflection(response, "accessToken", accessToken);
			setFieldViaReflection(response, "username", username);
			setFieldViaReflection(response, "siteName", siteName);
		} catch (Exception e) {
			throw new RuntimeException("Failed to create mock response", e);
		}
		return response;
	}

	private void setFieldViaReflection(Object obj, String fieldName, Object value) throws Exception {
		var field = obj.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(obj, value);
	}

}
