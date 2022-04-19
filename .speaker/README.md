Ce dossier contient l'ensemble des ressources utiles pour les organisateurs ou orateurs du lab.

## Affichage de la Keynote

```bash
npm install && npm run keynote
```

## Génération de la Keynote au format HTML

```bash
npm install && npm run html
```
Les fichiers sont générés dans le répertoire `generatedKeynote.tmp` à la racine du projet.
Seul le fichier `generatedKeynote.tmp/README.html` est à déplacer à la racine du projet, et certaines adaptations restent nécessaires
(liens vers les ressource statiques, se référer aux ressources modifiés via `git status`).
Le reste des fichiers générés peuvent être supprimés.

## Pour imprimer la Keynote en PDF

```bash
npm install && npm run pdf
```

Si besoin de mieux contrôler les paramètres d'impression : aller sur l'URL :
[http://localhost:1948/README.md?print-pdf](http://localhost:1948/README.md?print-pdf)

NB : le paramètre `?print-pdf` active le mode de présentation en "continue" indispensable pour l'impression.

## Ressources complémentaires

* [Template présentation DevoxxFR 2022](https://github.com/quantixx/template-presentation)
