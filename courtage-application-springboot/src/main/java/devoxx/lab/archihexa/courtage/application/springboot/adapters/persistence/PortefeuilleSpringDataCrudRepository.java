package devoxx.lab.archihexa.courtage.application.springboot.adapters.persistence;

import devoxx.lab.archihexa.courtage.domain.model.Portefeuille;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface PortefeuilleSpringDataCrudRepository
	extends CrudRepository<Portefeuille, String>,
	JpaSpecificationExecutor<Portefeuille> {


}
