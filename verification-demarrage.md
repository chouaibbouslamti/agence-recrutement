# Vérification du Démarrage de l'Application

## 🚀 Application Lancée

L'application a été lancée en arrière-plan. 

### Ce qui devrait se passer :

1. **Démarrage Spring Boot** (5-10 secondes)
   - Chargement des configurations
   - Connexion à MySQL
   - Création des tables si nécessaire
   - Initialisation du contexte Spring

2. **Initialisation JavaFX** (2-3 secondes)
   - Démarrage de la JavaFX Application Thread
   - Chargement des contrôleurs
   - Création de la fenêtre

3. **Fenêtre de Connexion**
   - Une fenêtre JavaFX devrait s'afficher
   - Titre : "Agence de Recrutement - Connexion"
   - Champs : Login et Mot de passe
   - Boutons : "Se connecter" et "S'inscrire"

---

## ✅ Vérifications à Faire

### 1. Vérifier que la fenêtre s'est ouverte

Cherchez une fenêtre intitulée **"Agence de Recrutement - Connexion"**

### 2. Vérifier la Console/Terminal

Dans le terminal où l'application a été lancée, vous devriez voir :

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.0)

...
Hibernate: create table ...
...
Started AgencerecrutementApplication in X.XXX seconds
```

### 3. Vérifier les Erreurs Éventuelles

Si vous voyez des erreurs, voici les plus courantes :

#### Erreur de Connexion MySQL
```
Communications link failure
```
**Solution :** Vérifiez que MySQL est démarré et que le mot de passe est correct

#### Erreur JavaFX
```
JavaFX runtime components are missing
```
**Solution :** JavaFX devrait être inclus dans les dépendances, vérifiez le pom.xml

#### Port déjà utilisé
```
Port 3306 is already in use
```
**Solution :** Un autre processus MySQL est peut-être en cours

---

## 🧪 Test Rapide

Une fois la fenêtre ouverte, testez rapidement :

1. **Connexion Admin :**
   - Login : `admin`
   - Password : `admin`
   - Cliquez sur "Se connecter"
   
   **Résultat attendu :** Tableau de bord administrateur s'affiche

2. **Inscription Test :**
   - Cliquez sur "S'inscrire"
   - Choisissez "Demandeur d'emploi"
   - Remplissez le formulaire
   - Testez l'inscription

---

## 📊 Vérification Base de Données

Si l'application démarre correctement, connectez-vous à MySQL pour vérifier :

```sql
mysql -u root -p
USE agence_recrutement;
SHOW TABLES;
```

Vous devriez voir toutes les tables créées automatiquement :
- utilisateurs
- categories
- journaux
- editions
- abonnements
- offres
- publications_offres
- candidatures
- recrutements

---

## 🛑 Arrêter l'Application

Pour arrêter l'application :
- Fermez la fenêtre JavaFX
- Ou appuyez sur `Ctrl+C` dans le terminal

---

## ⚠️ Si l'Application ne Démarre Pas

1. Vérifiez les logs dans le terminal
2. Vérifiez que MySQL est démarré : `mysql -u root -p`
3. Vérifiez le fichier `application.properties`
4. Consultez les erreurs dans la console

---

## 📝 Notes

- L'application peut prendre 10-20 secondes pour démarrer complètement
- La première exécution peut être plus lente (création des tables)
- Si vous fermez la fenêtre, l'application s'arrête automatiquement

