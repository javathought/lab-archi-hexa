package devoxx.lab.archihexa.courtage.archunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.junit.AnalyzeClasses;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.library.Architectures.onionArchitecture;

@AnalyzeClasses(packages = "devoxx.lab.archihexa")
public class ArchitectureTest {
	@Test
	public void test() {
		JavaClasses jc = new ClassFileImporter()
			.importPackages("devoxx.lab.archihexa");
		onionArchitecture()
			.domainModels("devoxx.lab.archihexa.courtage.domain.model..")
			.domainServices("devoxx.lab.archihexa.courtage.domain.port.primaire..")

			.applicationServices("devoxx.lab.archihexa.courtage.application.springboot.controller..")
			.adapter("persistence", "devoxx.lab.archihexa.courtage.application.springboot.adapters.persistence..")
			.adapter("rest", "devoxx.lab.archihexa.courtage.application.springboot.adapters.rest..")
			.check(jc);
	}
}