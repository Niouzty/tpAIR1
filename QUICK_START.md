# TP3 - Démarrage Rapide

## 🚀 Commandes essentielles

### 1. Compiler le projet
```bash
cd /home/etudiants/info/epembelefuala/IdeaProjects/tpAIR1
mvn clean compile
```

### 2. Packager en WAR
```bash
mvn clean package -DskipTests
```

Le fichier WAR sera créé dans `target/tpAIR1-1.0-SNAPSHOT.war`

### 3. Déployer sur Tomcat
```bash
# Copier le WAR dans le répertoire webapps de Tomcat
cp target/tpAIR1-1.0-SNAPSHOT.war $CATALINA_HOME/webapps/

# Redémarrer Tomcat
$CATALINA_HOME/bin/shutdown.sh
$CATALINA_HOME/bin/startup.sh
```

### 4. Vérifier que l'API fonctionne
```bash
curl -X GET "http://localhost:8080/tpAIR1/api/helloWorld"
```

Vous devriez voir :
```json
{
  "message": "Hello World!"
}
```

---

## 🧪 Tester l'API

### Avec le script bash
```bash
chmod +x test_api.sh
./test_api.sh
```

### Avec Postman
1. Importer `postman_collection.json` dans Postman
2. Cliquer sur "Run"
3. Configurer les variables d'environnement si nécessaire

### Avec curl manuelle
```bash
# 1. Login
TOKEN=$(curl -s -X POST "http://localhost:8080/tpAIR1/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' | jq -r '.token')

echo "Token: $TOKEN"

# 2. Lister les annonces
curl -X GET "http://localhost:8080/tpAIR1/api/annonces" \
  -H "Authorization: Bearer $TOKEN"

# 3. Créer une annonce
curl -X POST "http://localhost:8080/tpAIR1/api/annonces" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "titre": "Test",
    "description": "Une description",
    "categorie": "Test"
  }'
```

---

## 📚 Documentation complète

Voir `README_TP3.md` pour la documentation complète.

---

## ✅ Checklist Déploiement

- [ ] JDK 11+ installé (`java -version`)
- [ ] Maven 3.6+ installé (`mvn -v`)
- [ ] Tomcat 10+ installé et `$CATALINA_HOME` défini
- [ ] Base de données PostgreSQL configurée
- [ ] `mvn clean package` réussit
- [ ] WAR copié dans `$CATALINA_HOME/webapps/`
- [ ] Tomcat redémarré
- [ ] `http://localhost:8080/tpAIR1/api/helloWorld` accessible
- [ ] Login fonctionne (`/api/auth/login`)
- [ ] Token obtenu et valide
- [ ] Endpoints protégés accessibles avec token

---

## 🐛 Dépannage

### Erreur: "Impossible de trouver le WAR"
```bash
# Vérifier que le WAR a bien été créé
ls -lh target/tpAIR1-1.0-SNAPSHOT.war
```

### Erreur: "404 Not Found"
```bash
# Vérifier l'URL complète
curl -X GET "http://localhost:8080/tpAIR1/api/helloWorld"
#                           ^^^^^^ host
#                                  ^^^^^^ context-path
#                                         ^^^^^^^^^^^^ endpoint
```

### Erreur: "401 Unauthorized"
```bash
# Vérifier le token
curl -s -X POST "http://localhost:8080/tpAIR1/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' | jq .

# Utiliser le token dans le header Authorization
```

### Erreur: "Compilation échouée"
```bash
# Nettoyer et recompiler
mvn clean compile

# Ou avec plus de détails
mvn clean compile -X
```

### Tomcat ne démarre pas
```bash
# Vérifier les logs
tail -f $CATALINA_HOME/logs/catalina.out

# Ou
cat $CATALINA_HOME/logs/catalina.out | grep ERROR
```

---

## 📝 Notes importantes

1. **TokenManager en mémoire** : Les tokens sont perdus à chaque redémarrage de Tomcat
2. **Authentification simplifiée** : Accepte tous les logins (pas de vérification en base)
3. **Base de données** : S'assurer que PostgreSQL est configurée dans `persistence.xml`
4. **HTTPS** : Non configuré par défaut (à ajouter pour production)

---

## 🎯 Endpoints clés

| Méthode | Endpoint | Auth | Description |
|---------|----------|------|-------------|
| GET | `/api/helloWorld` | ❌ | Test simple |
| POST | `/api/auth/login` | ❌ | Login |
| GET | `/api/annonces` | ✅ | Lister |
| POST | `/api/annonces` | ✅ | Créer |
| GET | `/api/annonces/{id}` | ✅ | Détail |
| PUT | `/api/annonces/{id}` | ✅ | Mettre à jour |
| DELETE | `/api/annonces/{id}` | ✅ | Supprimer |
| POST | `/api/auth/logout` | ✅ | Logout |

---

**Besoin d'aide ?** Voir `README_TP3.md` ou `FILES_CREATED.md`
