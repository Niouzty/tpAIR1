# instructions.md — Instructions de génération / dev (J2EE / Jakarta EE)

## 1) Règles générales
- Ne jamais inventer des endpoints / champs / tables : se baser sur le code existant ou les consignes.
- Respecter MVC strict.
- Code propre : lisible, court, commentaires utiles uniquement.
- Chaque fonctionnalité doit être livrée complète :
    - servlet(s) + JSP + service/DAO + entités/DTO + validations + erreurs.

## 2) Checklist pour une nouvelle feature (CRUD typique)
### A. Contrat & données
- Identifier l’entité concernée (ex: `Annonce`, `User`)
- Vérifier :
    - champs exacts
    - contraintes (null, unique, tailles)
    - relations (OneToMany, ManyToOne)

### B. DAO (persistance)
- Fournir des méthodes claires :
    - `findAll()`, `findById(id)`, `create(entity)`, `update(entity)`, `delete(id)`
- Gérer EntityManager correctement :
    - transaction sur create/update/delete
    - fermer les ressources si besoin (selon pattern)

### C. Service (métier)
- Valider les règles métier (ex: champs obligatoires, formats)
- Ne pas mettre de logique HTTP ici.

### D. Servlet (contrôleur)
- `doGet` : afficher formulaire / page / liste
- `doPost` : traiter création / update / actions
- Pattern PRG :
    - après succès en POST : `response.sendRedirect(...)`
    - en cas d’erreur : remettre attributs + forward vers JSP
- Toujours définir :
    - `request.setCharacterEncoding("UTF-8")`
    - `response.setContentType("text/html;charset=UTF-8")`

### E. JSP (vue)
- Pas d’accès DB, pas de logique métier.
- Affichage des erreurs via attributs (ex: `${errors}`)
- Formulaires :
    - conserver les valeurs précédentes si erreur
    - champs `name` cohérents avec servlet
- UI :
    - Bootstrap si utilisé : forms, buttons, tables, alerts

## 3) Gestion d’erreurs standard
- Erreurs de validation :
    - message utilisateur clair
    - conserver input
- Erreurs techniques :
    - log serveur (stacktrace côté serveur)
    - message générique côté UI
- Pages erreurs HTTP (optionnel) :
    - 404.jsp / 500.jsp configurées

## 4) Authentification (si présente)
- Connexion :
    - servlet `/login` (GET form, POST verify)
    - stocker utilisateur en session : `session.setAttribute("user", user)`
- Déconnexion :
    - invalider session
- Protection routes :
    - Filter `AuthFilter` sur `/secure/*`

## 5) Base de données & migrations
- Les entités JPA doivent correspondre aux tables réelles.
- Ne pas “deviner” un schéma : lire `persistence.xml`, scripts SQL, ou entités existantes.
- Si un script SQL est demandé :
    - inclure PK, FK, indexes, contraintes

## 6) Qualité & conventions de code
- Nommage :
    - Classe : `AnnonceCreateServlet`
    - URL : `/annonce/create`
    - JSP : `/WEB-INF/views/annonce/create.jsp` (non accessible directement)
- Eviter la duplication :
    - extraire helpers (validation, parsing id, messages)
- Logs :
    - utiliser `Logger` (ou `System.out` si cours) mais jamais dans JSP.

## 7) “Definition of Done” (DoD)
Une feature est finie si :
- compile + déploiement ok
- navigation cohérente (liens, retours)
- formulaires robustes (erreurs affichées)
- pas de NPE / ClassCastException
- DAO/service/servlet/jsp cohérents