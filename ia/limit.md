# limit.md — Limitations / Contraintes (J2EE / Jakarta EE)

## 1) Interdictions
- ❌ Pas d’accès DB depuis JSP.
- ❌ Pas de logique métier dans JSP.
- ❌ Pas d’invention de :
    - tables
    - colonnes
    - endpoints
    - champs formulaire
    - classes métier
      Sans preuve dans le projet (code/énoncé).

## 2) Gestion HTTP (contraintes)
- GET = affichage, POST = modification.
- Toujours PRG après POST (redirect) pour éviter double soumission.
- Ne pas utiliser `doGet` pour supprimer (si possible). Préférer POST/DELETE simulé.

## 3) Sécurité minimale
- Toujours valider les entrées côté serveur.
- Toujours parser les nombres avec try/catch (`Integer.parseInt`) et gérer les erreurs.
- Ne jamais afficher une stacktrace au client.
- Échapper l’affichage de contenu utilisateur (EL/JSTL le fait souvent, mais attention aux cas).
- Mot de passe :
    - ❌ pas en clair en DB (hash si demandé). Si non demandé par cours, au minimum le signaler.

## 4) Transactions & EntityManager
- Toute écriture DB doit être transactionnelle.
- Gérer correctement la fermeture/gestion de l’EntityManager selon le pattern utilisé :
    - `EntityManagerFactory` unique
    - `EntityManager` par requête ou par DAO

## 5) Compatibilité Jakarta vs Java EE
- Tomcat 10+ = `jakarta.servlet.*` / `jakarta.persistence.*`
- Tomcat 9 = `javax.servlet.*` / `javax.persistence.*`
  ➡️ Ne pas mélanger les imports.

## 6) UI / JSP
- Si JSTL disponible : l’utiliser.
- Si le cours impose scriptlets :
    - tolérées uniquement pour lecture d’attributs simples
    - ❌ pas de boucles/conditions complexes en scriptlets si JSTL possible

## 7) Performance / bonne pratique
- Pas de `findAll()` sans pagination si dataset gros (si concerné)
- Eviter les N+1 sur relations (fetch strategy si nécessaire)

## 8) Tests
- Si tests demandés : privilégier tests service (unit) + tests DAO (intégration)
- Sinon : fournir au moins une checklist de tests manuels (création, update, delete, erreurs)

## 9) Livrables attendus
- Code complet + structure MVC respectée
- JSP dans `/WEB-INF/views/`
- Routes documentées
- Instructions de lancement (Tomcat, DB, config)