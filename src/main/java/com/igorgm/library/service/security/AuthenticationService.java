package com.igorgm.library.service.security;


import com.igorgm.library.entity.user.AuthenticationDTO;
import com.igorgm.library.entity.user.LoginResponseDTO;
import com.igorgm.library.entity.user.RegisterDTO;
import com.igorgm.library.entity.user.User;
import com.igorgm.library.infra.security.TokenService;
import com.igorgm.library.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private final AuthenticationManager authenticationManager;
	private final UserRepository repository;
	private final TokenService tokenService;
	
	public ResponseEntity<String> registerUser(RegisterDTO data) {
		if (this.repository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();
		
		String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
		User newUser = new User(data.login(), encryptedPassword, data.role());
		
		this.repository.save(newUser);
		
		return ResponseEntity.ok("User registered successfully.");
	}
	
	public ResponseEntity<LoginResponseDTO> performLogin(AuthenticationDTO data) {
		var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
		var auth = this.authenticationManager.authenticate(usernamePassword);
		
		var token = tokenService.generateToken((User) auth.getPrincipal());
		
		return ResponseEntity.ok(new LoginResponseDTO(token));
	}
}
