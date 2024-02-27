package com.avalialivros.m3s04.model.transport.operations;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateRatingDTO(@NotNull @Min(1) @Max(5) Integer grade) {
}
