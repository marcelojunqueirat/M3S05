package com.avalialivros.m3s04.model.transport.operations;

import jakarta.validation.constraints.NotBlank;

public record CreatePersonDTO(@NotBlank String name,
                              @NotBlank String email,
                              @NotBlank String password) {
}
