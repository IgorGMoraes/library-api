package com.igorgm.library.repository.user;

import com.igorgm.library.entity.user.User;
import com.igorgm.library.entity.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {
	
	@Autowired
	private UserRepository userRepository;
	
	@BeforeEach
	void setUp() {
		userRepository.saveAll(List.of(
				User.builder()
						.login("admin")
						.password("pass123")
						.role(UserRole.ADMIN)
						.build(),
				
				User.builder()
						.login("user")
						.password("pass123")
						.role(UserRole.USER)
						.build()
		));
	}
	
	@Test
	void findByLogin_ReturnsUser_WhenLoginExistsForAdmin() {
		UserDetails user = userRepository.findByLogin("admin");
		assertThat(user).isNotNull();
		assertThat(user.getUsername()).isEqualTo("admin");
		assertThat(user.getAuthorities()).extracting("authority")
				.containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_USER");
	}
	
	@Test
	void findByLogin_ReturnsUser_WhenLoginExistsForNormalUser() {
		UserDetails user = userRepository.findByLogin("user");
		assertThat(user).isNotNull();
		assertThat(user.getUsername()).isEqualTo("user");
		assertThat(user.getAuthorities()).extracting("authority")
				.containsExactly("ROLE_USER");
	}
	
	@Test
	void findByLogin_ReturnsNull_WhenLoginDoesNotExist() {
		UserDetails user = userRepository.findByLogin("unknownUser");
		assertThat(user).isNull();
	}
}
