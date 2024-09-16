package com.igorgm.library.controller;

import com.igorgm.library.entity.user.AuthenticationDTO;
import com.igorgm.library.entity.user.LoginResponseDTO;
import com.igorgm.library.entity.user.RegisterDTO;
import com.igorgm.library.service.security.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthenticationController {
	private final AuthenticationService authenticationService;
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
		return authenticationService.performLogin(data);
	}
	
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody @Valid RegisterDTO data) {
		return authenticationService.registerUser(data);
	}
	
}