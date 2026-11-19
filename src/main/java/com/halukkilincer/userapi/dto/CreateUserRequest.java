package com.halukkilincer.userapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank @Size(min = 2, max = 80) String name,
        @NotBlank @Email @Size(max = 120) String email
) {
    public CreateUserRequest {
        name = name == null ? null : name.trim();
        email = email == null ? null : email.trim().toLowerCase();
    }
}
