package devoxx.lab.archihexa.courtage.application.springboot.adapters.persistence;

import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;

@NamedNativeQuery(
	name="vuePorefeuille",
	query = "select p.nom, sum(a.nombre) as quantite from portefeuille p\n" +
		"                left join actions a where a.portefeuille_nom = p.nom\n" +
		"                group by p.nom"
)
public class VuePortefeuille {

	@Id
	private String nom;

	private int quantite;

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getQuantite() {
		return quantite;
	}

	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}

}
