package devoxx.lab.archihexa.courtage.domain.port.primaire;

import devoxx.lab.archihexa.courtage.domain.exception.PortefeuilleDejaExistantException;
import devoxx.lab.archihexa.courtage.domain.exception.PortefeuilleNonGereException;
import devoxx.lab.archihexa.courtage.domain.model.Achat;
import devoxx.lab.archihexa.courtage.domain.model.Portefeuille;
import devoxx.lab.archihexa.courtage.domain.port.secondaire.PortefeuilleRepository;
import devoxx.lab.archihexa.courtage.domain.port.secondaire.PortefeuilleRepositoryMock;
import devoxx.lab.archihexa.courtage.domain.port.secondaire.ServiceBourse;
import devoxx.lab.archihexa.courtage.domain.port.secondaire.ServiceBourseMock;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.DataTableEntryDefinitionBody;
import io.cucumber.java8.Fr;
import org.assertj.core.groups.Tuple;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.assertThat;

public class CourtageStepDefinitions implements Fr {
	static {
		// Pour s'assurer des messages BeanValidation en Fr
		Locale.setDefault(Locale.FRANCE);
	}

	private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	// Afin de se rapprocher au mieux d'un repository persistant, celui-ci doit être réutilisé entre les différents scénarios
	private static final PortefeuilleRepository portefeuilleRepository = new PortefeuilleRepositoryMock();

	// Afin de se rapprocher au mieux d'un service distant, celui-ci doit être réutilisé entre les différents scénarios
	private static final ServiceBourse serviceBourse = new ServiceBourseMock();

	private final ServiceCourtage serviceCourtage = new Courtage(portefeuilleRepository, serviceBourse);
	private Portefeuille portefeuilleCree;
	private Exception thrownException = null;
	private BigDecimal valeurPortefeuille = null;
	private BigDecimal valeurAction = null;

	private Achat achat;

	public CourtageStepDefinitions() {
		// étape 1
		Quand("on demande au service de courtage la création du portefeuille {string}", (String nomPortefeuille) -> {
			try {
				portefeuilleCree = serviceCourtage.creerPortefeuille(nomPortefeuille);
			} catch (PortefeuilleDejaExistantException e) {
				thrownException = e;
			}
		});
		Alors("l'id du portefeuille créé doit être {string}", (String nomPortefeuille) ->
			assertThat(portefeuilleCree.getNom()).isEqualTo(nomPortefeuille));
		Alors("le portefeuille {string} est géré par le service de courtage", (String nomPortefeuille) ->
			assertThat(serviceCourtage.gere(nomPortefeuille)).isTrue());

		// étape 2
		Alors("le portefeuille {string} n'est pas géré par le service de courtage", (String nomPortefeuille) -> assertThat(serviceCourtage.gere(nomPortefeuille)).isFalse());
		Alors("une exception est levée : Portefeuille déjà géré", () ->
			assertThat(thrownException).isInstanceOf(PortefeuilleDejaExistantException.class));
		Quand("on demande le calcul de la valeur du portefeuille {string}", (String nomPortefeuille) -> {
			try {
				valeurPortefeuille = serviceCourtage.calculerValeurPortefeuille(nomPortefeuille);
			} catch (PortefeuilleNonGereException e) {
				thrownException = e;
			}
		});
		Alors("la valeur du portefeuille est {bigdecimal}", (BigDecimal valeur) ->
			assertThat(valeurPortefeuille).isEqualByComparingTo(valeur));
		Alors("une exception est levée : Portefeuille non géré", () ->
			assertThat(thrownException).isInstanceOf(PortefeuilleNonGereException.class));

		// étape 3
		DataTableType(CoursBourse.CONVERTER);
		Quand("(si )les cours de bourse suivants/sont/deviennent :", (DataTable dataTable) ->
			dataTable.asList(CoursBourse.class).forEach(coursBourse ->
				((ServiceBourseMock) serviceBourse).setCours(coursBourse.action, coursBourse.valeur)
			)
		);
		Quand("on demande au service de bourse la valeur de l'action {string}", (String nomAction) ->
			this.valeurAction = serviceBourse.recupererCours(nomAction));

		Alors("la valeur récupérée pour l'action est {bigdecimal}", (BigDecimal valeurAction) ->
			assertThat(this.valeurAction).isEqualByComparingTo(valeurAction));

		DataTableType(AjoutAction.CONVERTER);
		Quand("^on demande au service de courtage d'ajouter (?:l'|les )actions? suivantes? :$", (DataTable dataTable) ->
			dataTable.asList(AjoutAction.class).forEach(ajoutAction -> {
				try {
					serviceCourtage.ajouteAction(ajoutAction.portefeuille, new Achat(ajoutAction.action, ajoutAction.nombre));
				} catch (PortefeuilleNonGereException e) {
					thrownException = e;
				}
			})
		);

		// étape 4
		Quand("on demande au service de courtage le calcul de la valeur de tous les portefeuilles", () -> {
			throw new io.cucumber.java8.PendingException();
		});
		Alors("la valeur pour l'ensemble des portefeuilles est {bigdecimal}", (BigDecimal valeurPortefeuilles) -> {
			throw new io.cucumber.java8.PendingException();
		});

		// étape 8
		DataTableType((Map<String, String> data) -> new Achat(data.get("action"), Integer.parseInt(data.get("nombre"))));
		Soit("l'achat", (Achat achat) ->
			this.achat = achat);
		Alors("l'achat est invalide avec l'erreur", (DataTable expected) -> {
			Set<ConstraintViolation<Achat>> violations = validator.validate(achat);
			assertThat(violations)
				.extracting("interpolatedMessage", "propertyPath.currentLeafNode.name")
				.containsExactlyInAnyOrderElementsOf(
					expected.asMaps().stream()
						.map(e -> new Tuple(e.get("message"), e.get("propriété")))
						.collect(Collectors.toList()));
		});
	}

	private static class CoursBourse {
		private static final NumberFormat NF = NumberFormat.getInstance(Locale.FRENCH);
		static final DataTableEntryDefinitionBody<CoursBourse> CONVERTER = (Map<String, String> row) -> new CoursBourse(
			row.get("Action"),
			new BigDecimal(NF.parse(row.get("Valeur")).toString())
		);
		final String action;
		final BigDecimal valeur;

		private CoursBourse(String action, BigDecimal valeur) {
			this.action = action;
			this.valeur = valeur;
		}
	}

	private static class AjoutAction {
		static final DataTableEntryDefinitionBody<AjoutAction> CONVERTER = (Map<String, String> row) -> new AjoutAction(
			row.get("Portefeuille"),
			row.get("Action"),
			Integer.parseInt(row.get("Nombre"))
		);
		final String portefeuille;
		final String action;
		final int nombre;

		private AjoutAction(String portefeuille, String action, int nombre) throws ParseException {
			this.portefeuille = portefeuille;
			this.action = action;
			this.nombre = nombre;
		}
	}
}
