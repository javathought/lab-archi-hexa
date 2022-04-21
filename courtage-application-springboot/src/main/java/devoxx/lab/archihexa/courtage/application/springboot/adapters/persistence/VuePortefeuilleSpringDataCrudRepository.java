package devoxx.lab.archihexa.courtage.application.springboot.adapters.persistence;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface VuePortefeuilleSpringDataCrudRepository
	extends CrudRepository<VuePortefeuille, String>,
	JpaSpecificationExecutor<VuePortefeuille> {


}
