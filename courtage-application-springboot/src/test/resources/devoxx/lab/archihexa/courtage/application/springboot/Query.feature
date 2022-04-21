#language: fr
#noinspection NonAsciiCharacters

Fonctionnalité: Interrogation (CQRS)

    # Ce scénario dépend de l'exécution des scénarios précédents
	@E9
	Scénario: Recherche des portefeuilles de gros clients
		Quand on recherche les portefeuilles de plus de 10 actions
		Alors la réponse contient les portefeuilles :
			| myCard  |
			| eWallet |
