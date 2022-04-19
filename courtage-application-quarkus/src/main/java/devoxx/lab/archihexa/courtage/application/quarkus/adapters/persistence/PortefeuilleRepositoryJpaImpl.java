package devoxx.lab.archihexa.courtage.application.quarkus.adapters.persistence;

import devoxx.lab.archihexa.courtage.domain.model.Portefeuille;
import devoxx.lab.archihexa.courtage.domain.port.secondaire.PortefeuilleRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

@ApplicationScoped
public class PortefeuilleRepositoryJpaImpl implements PortefeuilleRepository {
	@PersistenceContext
	private final EntityManager em;

	public PortefeuilleRepositoryJpaImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public boolean existe(String nomPortefeuille) {
		return em.find(Portefeuille.class, nomPortefeuille) != null;
	}

	@Override
	public void sauvegarde(Portefeuille portefeuille) {
		em.persist(portefeuille);
	}

	@Override
	public Optional<Portefeuille> recupere(String id) {
		return ofNullable(em.find(Portefeuille.class, id));
	}

	@Override
	public Stream<Portefeuille> recupereTous() {
		Query query = em.createQuery("SELECT p FROM Portefeuille p");
		return ((Stream<Portefeuille>) query.getResultStream())
			// Récupération de l'ensemble de la liste pour éviter la fermerture prématurée du ResultSet
			.toList()
			.stream();
	}
}
