package com.example.blutalkoholwert;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

public record PersonForm(
        @Positive(message = "Gewicht muss positiv sein") Double weight,
        @Positive(message = "Menge muss positiv sein")Double menge,
        String alkoholart,
        String geschlecht) {}
