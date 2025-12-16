# Guide de Test du Projet Agence de Recrutement

## ✅ Tests Préliminaires

### 1. Vérification de la Compilation

```bash
mvn clean compile
```

**Résultat attendu :** `BUILD SUCCESS`

### 2. Vérification des Dépendances

```bash
mvn dependency:tree
```

Vérifiez que toutes les dépendances sont présentes :
- Spring Boot starters
- JavaFX controls et fxml
- MySQL connector
- Lombok
- Spring Security

---

## 🚀 Test du Démarrage de l'Application

### Prérequis

1. **MySQL doit être installé et démarré**
   - Vérifiez que le service MySQL est en cours d'exécution
   - Le port 3306 doit être disponible

2. **Base de données configurée**
   - Le fichier `application.properties` doit avoir le bon mot de passe MySQL

### Démarrage

```bash
mvn spring-boot:run
```

**Étapes du démarrage :**

1. Spring Boot démarre
2. Connexion à MySQL
3. Création automatique des tables (grâce à `ddl-auto=update`)
4. Initialisation du compte admin (via `InitialisationService`)
5. Lancement de l'interface JavaFX

**Résultat attendu :**
- Une fenêtre JavaFX s'ouvre avec l'écran de connexion
- Pas d'erreurs dans la console
- Messages de log Spring Boot normaux

---

## 🧪 Tests Fonctionnels

### Test 1 : Connexion Administrateur

1. **Compte par défaut :**
   - Login : `admin`
   - Password : `admin`

2. **Actions à effectuer :**
   - Ouvrir l'application
   - Entrer les identifiants admin
   - Cliquer sur "Se connecter"

3. **Résultat attendu :**
   - Connexion réussie
   - Affichage du tableau de bord administrateur
   - Menu "Administration" visible

### Test 2 : Inscription d'une Entreprise

1. **Actions :**
   - Cliquer sur "S'inscrire"
   - Choisir "Entreprise"
   - Remplir le formulaire :
     - Login : `entreprise1`
     - Mot de passe : `test123`
     - Raison sociale : `Tech Solutions`
     - Adresse : `123 Rue Test, Paris`
     - Téléphone : `0123456789`
     - Description : `Entreprise de développement`
   - Cliquer sur "S'inscrire"

2. **Résultat attendu :**
   - Message de succès
   - Possibilité de se connecter avec ce compte

### Test 3 : Inscription d'un Demandeur d'Emploi

1. **Actions :**
   - Cliquer sur "S'inscrire"
   - Choisir "Demandeur d'emploi"
   - Remplir le formulaire :
     - Login : `demandeur1`
     - Mot de passe : `test123`
     - Nom : `Dupont`
     - Prénom : `Jean`
     - Adresse : `456 Avenue Test, Lyon`
     - Téléphone : `0987654321`
     - Fax : `0987654322`
     - Diplôme : `Master Informatique`
     - Expérience : `3` années
     - Salaire souhaité : `35000`
   - Cliquer sur "S'inscrire"

2. **Résultat attendu :**
   - Message de succès
   - Possibilité de se connecter avec ce compte

### Test 4 : Connexion Entreprise

1. **Actions :**
   - Se connecter avec le compte entreprise créé
   - Explorer les onglets disponibles

2. **Résultat attendu :**
   - Tableau de bord entreprise s'affiche
   - Onglets : "Mes Offres", "Mes Abonnements", "Candidatures", "Recrutements"

### Test 5 : Création d'une Offre (Entreprise)

**Prérequis :** Avoir un abonnement actif (voir Test 6)

1. **Actions :**
   - Se connecter en tant qu'entreprise
   - Aller dans l'onglet "Mes Offres"
   - Cliquer sur "Nouvelle offre"
   - Remplir :
     - Titre : `Développeur Java Spring Boot`
     - Compétences : `Java, Spring Boot, MySQL`
     - Expérience requise : `2` années
     - Nombre de postes : `3`
   - Cliquer sur "Créer"

2. **Résultat attendu :**
   - Message de succès
   - L'offre apparaît dans la liste

### Test 6 : Souscription d'un Abonnement (Entreprise)

1. **Actions :**
   - Se connecter en tant qu'entreprise
   - Aller dans l'onglet "Mes Abonnements"
   - Cliquer sur "Souscrire un abonnement"
   - Sélectionner un journal (s'il existe)
   - Choisir une date d'expiration future
   - Cliquer sur "Souscrire"

2. **Résultat attendu :**
   - Message de succès
   - L'abonnement apparaît dans la liste avec état "ACTIF"

**Note :** Pour ce test, vous devez d'abord créer des journaux via l'interface administrateur ou directement en base de données.

### Test 7 : Publication d'une Offre

1. **Actions :**
   - Se connecter en tant qu'entreprise
   - Créer une offre (voir Test 5)
   - Publier l'offre (nécessite un abonnement actif et une édition de journal)

2. **Résultat attendu :**
   - Publication réussie
   - L'offre devient visible pour les demandeurs d'emploi

### Test 8 : Candidature (Demandeur d'Emploi)

**Prérequis :** Une offre active doit exister

1. **Actions :**
   - Se connecter en tant que demandeur d'emploi
   - Aller dans l'onglet "Offres disponibles"
   - Sélectionner une offre
   - Cliquer sur "Postuler"
   - Sélectionner l'édition du journal où l'offre a été vue
   - Cliquer sur "Postuler"

2. **Résultat attendu :**
   - Si l'expérience est suffisante : candidature réussie
   - Si l'expérience est insuffisante : message d'erreur explicite
   - Si déjà candidaté : message d'erreur

### Test 9 : Consultation des Candidatures (Entreprise)

1. **Actions :**
   - Se connecter en tant qu'entreprise
   - Aller dans l'onglet "Candidatures"
   - Voir la liste des candidatures reçues

2. **Résultat attendu :**
   - Liste des candidatures avec détails
   - Possibilité de recruter un candidat

### Test 10 : Recrutement (Entreprise)

1. **Actions :**
   - Se connecter en tant qu'entreprise
   - Aller dans "Candidatures"
   - Sélectionner une candidature
   - Cliquer sur "Recruter le candidat sélectionné"
   - Confirmer

2. **Résultat attendu :**
   - Recrutement enregistré
   - Le nombre de postes disponibles diminue
   - Si tous les postes sont pourvus, l'offre se désactive automatiquement

---

## 🔍 Tests de Validation des Règles Métier

### Règle 1 : Un seul abonnement actif par journal

**Test :**
1. Souscrire un abonnement pour le journal A
2. Essayer de souscrire un autre abonnement pour le même journal A

**Résultat attendu :** Erreur "Un abonnement actif existe déjà pour ce journal"

### Règle 2 : Expérience requise pour candidater

**Test :**
1. Créer une offre nécessitant 5 ans d'expérience
2. Se connecter avec un demandeur ayant 2 ans d'expérience
3. Essayer de postuler

**Résultat attendu :** Erreur "Expérience insuffisante"

### Règle 3 : Une seule candidature par offre

**Test :**
1. Postuler à une offre (succès)
2. Essayer de postuler à nouveau à la même offre

**Résultat attendu :** Erreur "Vous avez déjà postulé à cette offre"

### Règle 4 : Limitation des recrutements

**Test :**
1. Créer une offre avec 2 postes
2. Recruter 2 candidats
3. Essayer de recruter un 3ème candidat

**Résultat attendu :** Erreur "Tous les postes ont été pourvus"
L'offre est automatiquement désactivée

### Règle 5 : Publication nécessite un abonnement actif

**Test :**
1. Créer une offre sans avoir d'abonnement actif
2. Essayer de publier l'offre

**Résultat attendu :** Erreur "L'abonnement doit être actif"

---

## 🐛 Tests de Gestion d'Erreurs

### Test 1 : Connexion avec identifiants incorrects

**Actions :**
- Entrer un login inexistant ou un mauvais mot de passe

**Résultat attendu :** Message d'erreur "Login ou mot de passe incorrect"

### Test 2 : Champs vides dans les formulaires

**Actions :**
- Essayer de créer une offre sans titre

**Résultat attendu :** Message de validation "Le titre est obligatoire"

### Test 3 : Connexion avec compte désactivé

**Actions :**
- Désactiver un compte (via base de données ou interface admin)
- Essayer de se connecter

**Résultat attendu :** Message d'erreur "Compte désactivé"

---

## 📊 Vérification Base de Données

### Après les tests, vérifiez en base :

```sql
-- Se connecter à MySQL
mysql -u root -p
USE agence_recrutement;

-- Vérifier les tables créées
SHOW TABLES;

-- Vérifier les utilisateurs créés
SELECT * FROM utilisateurs;

-- Vérifier les offres
SELECT * FROM offres;

-- Vérifier les candidatures
SELECT * FROM candidatures;

-- Vérifier les recrutements
SELECT * FROM recrutements;
```

**Tables attendues :**
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

## ✅ Checklist de Validation

- [ ] Compilation réussie
- [ ] Application démarre sans erreur
- [ ] Fenêtre JavaFX s'affiche
- [ ] Connexion admin fonctionne
- [ ] Inscription entreprise fonctionne
- [ ] Inscription demandeur fonctionne
- [ ] Création d'offre fonctionne
- [ ] Souscription abonnement fonctionne
- [ ] Candidature fonctionne (avec règles)
- [ ] Recrutement fonctionne (avec limites)
- [ ] Toutes les règles métier sont respectées
- [ ] Base de données contient les données

---

## 🚨 Problèmes Courants

### L'application ne démarre pas

**Causes possibles :**
1. MySQL n'est pas démarré → Démarrer le service MySQL
2. Mauvais mot de passe dans `application.properties`
3. Port 3306 déjà utilisé
4. JavaFX n'est pas disponible → Vérifier JavaFX dans le classpath

### Erreur "Cannot connect to database"

**Solutions :**
1. Vérifier que MySQL est démarré : `mysql -u root -p`
2. Vérifier le mot de passe dans `application.properties`
3. Vérifier que la base de données existe ou peut être créée

### Interface JavaFX ne s'affiche pas

**Solutions :**
1. Vérifier les logs pour voir les erreurs
2. S'assurer que JavaFX est bien dans les dépendances
3. Vérifier que vous utilisez Java 17 ou supérieur

---

## 📝 Notes de Test

Créez ce fichier pour noter vos résultats de test :

```markdown
Date: _______________
Testeur: _______________

Résultats:
- Test 1 (Connexion Admin): [ ] Réussi [ ] Échec
- Test 2 (Inscription Entreprise): [ ] Réussi [ ] Échec
- Test 3 (Inscription Demandeur): [ ] Réussi [ ] Échec
...

Remarques:
_________________________________________________
```

