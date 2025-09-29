package com.example.overlook_hotel.service.auth;

import com.example.overlook_hotel.model.entity.PasswordResetToken;
import com.example.overlook_hotel.model.entity.User;
import com.example.overlook_hotel.repository.auth.PasswordResetTokenRepository;
import com.example.overlook_hotel.repository.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
	private final UserRepository userRepository;
	private final PasswordResetTokenRepository tokenRepository;
	private final PasswordEncoder passwordEncoder;

	private final SecureRandom secureRandom = new SecureRandom();

	public void requestReset(String email) {
		userRepository.findByEmail(email).ifPresent(user -> {
			String token = generateToken();
			PasswordResetToken prt = PasswordResetToken.builder()
					.token(token)
					.user(user)
					.expiresAt(Instant.now().plus(30, ChronoUnit.MINUTES))
					.build();
			tokenRepository.save(prt);
			// TODO: integrate email service. For now just log token (developer use)
			System.out.println("Password reset token for " + email + ": " + token);
		});
	}

	public void resetPassword(String token, String newPassword) {
		PasswordResetToken prt = tokenRepository.findByToken(token)
				.orElseThrow(() -> new IllegalArgumentException("Invalid token"));

		if (prt.getExpiresAt().isBefore(Instant.now())) {
			tokenRepository.delete(prt);
			throw new IllegalArgumentException("Token expired");
		}

		User user = prt.getUser();
		user.setPasswordHash(passwordEncoder.encode(newPassword));
		userRepository.save(user);
		tokenRepository.delete(prt);
	}

	private String generateToken() {
		byte[] bytes = new byte[36];
		secureRandom.nextBytes(bytes);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
	}
}
