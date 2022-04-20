package devoxx.lab.archihexa.courtage.domain.model;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;
import static java.util.Optional.ofNullable;

public class Portefeuille {
	private String nom;
	private final Map<String, Integer> actions = new HashMap<>();

	/**
	 * Constructeur utilisé pour le mapping JPA.
	 */
	Portefeuille() {
	}

	public Portefeuille(String nom) {
		this.nom = nom;
	}

	public String getNom() {
		return nom;
	}

	public void ajouterAction(Achat achat) {
		actions.compute(
			achat.action(),
			(action, ancienNbActions) -> ofNullable(ancienNbActions).orElse(0) + achat.nombre()
		);
	}

	public Map<String, Integer> getActions() {
		return unmodifiableMap(actions);
	}
}
