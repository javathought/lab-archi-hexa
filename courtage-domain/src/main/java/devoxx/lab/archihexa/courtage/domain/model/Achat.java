package devoxx.lab.archihexa.courtage.domain.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public record Achat(
	@NotNull
	@Pattern(regexp = "^[\\p{IsLatin}0-9]{2,5}$", message = "doit être composé de 2 à 5 caractères latins")
	String action,

	@Min(1)
	int nombre
) {
}
