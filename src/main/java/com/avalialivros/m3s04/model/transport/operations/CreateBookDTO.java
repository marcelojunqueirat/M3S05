package com.avalialivros.m3s04.model.transport.operations;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateBookDTO(@NotBlank String title,
                            @NotNull Integer yearOfPublication) {
}
