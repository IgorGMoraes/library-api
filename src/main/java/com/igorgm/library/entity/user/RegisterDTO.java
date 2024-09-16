package com.igorgm.library.entity.user;

public record RegisterDTO(String login, String password, UserRole role) {
}
