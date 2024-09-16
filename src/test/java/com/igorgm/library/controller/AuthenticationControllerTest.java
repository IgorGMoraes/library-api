package com.igorgm.library.controller;

import com.igorgm.library.entity.user.AuthenticationDTO;
import com.igorgm.library.entity.user.LoginResponseDTO;
import com.igorgm.library.entity.user.RegisterDTO;
import com.igorgm.library.entity.user.UserRole;
import com.igorgm.library.service.security.AuthenticationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
class AuthenticationControllerTest {
	
	@InjectMocks
	private AuthenticationController authenticationController;
	
	@Mock
	private AuthenticationService authenticationServiceMock;
	
	private AuthenticationDTO validAuthDTO;
	private RegisterDTO validRegisterDTO;
	
	@BeforeEach
	void setUp() {
		validAuthDTO = new AuthenticationDTO("testUser", "password123");
		validRegisterDTO = new RegisterDTO("testUser", "password123",  UserRole.USER);
		LoginResponseDTO validLoginResponse = new LoginResponseDTO("token123");
		
		// Mocking the service layer behavior for login
		BDDMockito.when(authenticationServiceMock.performLogin(any(AuthenticationDTO.class)))
				.thenReturn(ResponseEntity.ok(validLoginResponse));
		
		// Mocking the service layer behavior for registration
		BDDMockito.when(authenticationServiceMock.registerUser(any(RegisterDTO.class)))
				.thenReturn(ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully"));
	}
	
	@Test
	@DisplayName("login_ReturnsToken_WhenSuccessful")
	void login_ReturnsToken_WhenSuccessful() {
		// Act: Call the login endpoint
		ResponseEntity<LoginResponseDTO> response = authenticationController.login(validAuthDTO);
		
		// Assert: Check the returned token
		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody()).isNotNull();
		Assertions.assertThat(response.getBody().token().equals("token123"));
	}
	
	@Test
	@DisplayName("register_CreatesUser_WhenSuccessful")
	void register_CreatesUser_WhenSuccessful() {
		// Act: Call the register endpoint
		ResponseEntity<String> response = authenticationController.register(validRegisterDTO);
		
		// Assert: Check the registration response
		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		Assertions.assertThat(response.getBody()).isEqualTo("User registered successfully");
	}
}
