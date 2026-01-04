# Application de Gestion d'Agence de Recrutement

## 👥 Membres du Groupe

- **Hiba Zouitina** - Développeuse
- **Imane Taleb** - Développeuse  
- **Saïda Stifi** - Développeuse
- **Chouaib Bouslamti** - Développeur

---

## 📋 Description du Projet

Application desktop hybride combinant **Spring Boot 3.2.0** et **JavaFX 21** pour la gestion complète d'une agence de recrutement. Le système permet aux entreprises de publier des offres d'emploi, aux demandeurs d'emploi de postuler, et aux administrateurs de gérer l'ensemble de la plateforme.

### Technologies Utilisées

- **Java 17+** - Langage principal
- **Spring Boot 3.2.0** - Framework backend
- **JavaFX 21** - Interface utilisateur desktop
- **MySQL 8.0+** - Base de données principale
- **H2** - Base de données embarquée (alternative)
- **Spring Data JPA** - Persistance des données
- **Lombok** - Réduction de code boilerplate
- **Spring Security Crypto** - Sécurité des mots de passe
- **Maven** - Gestion des dépendances

---

## 🚀 Instructions d'Installation et d'Exécution

### Prérequis

1. **Java 17** ou supérieur installé
2. **Maven 3.6+** installé et configuré
3. **MySQL 8.0+** installé et en cours d'exécution
4. **MySQL Workbench** ou un autre client MySQL (recommandé)

### Configuration de la Base de Données

1. **Créer la base de données MySQL** :
   ```sql
   CREATE DATABASE agence_recrutement;
   ```

2. **Configurer les paramètres de connexion** dans `src/main/resources/application.properties` :
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/agence_recrutement?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   spring.datasource.username=root
   spring.datasource.password=votre_mot_de_passe
   ```

### Compilation et Exécution

#### Méthode 1 : Via Maven (Recommandée)

1. **Ouvrir un terminal** dans le répertoire racine du projet
2. **Compiler le projet** :
   ```bash
   mvn clean install
   ```
3. **Exécuter l'application** :
   ```bash
   mvn spring-boot:run
   ```

#### Méthode 2 : Via votre IDE

1. **Importer le projet** comme projet Maven dans votre IDE (IntelliJ, Eclipse, etc.)
2. **Attendre la résolution des dépendances** Maven
3. **Exécuter la classe principale** : `AgencerecrutementApplication.java`
4. **L'interface JavaFX** se lancera automatiquement

### Compte par Défaut

À la première exécution, un compte administrateur est créé automatiquement :
- **Login** : `admin`
- **Mot de passe** : `1234567890`

⚠️ **Important** : Changez ce mot de passe après la première connexion !

---

## 📁 Structure des Répertoires

### 📂 Racine du Projet

```
agencerecrutement/
├── 📄 README.md                 # Ce fichier
├── 📄 pom.xml                  # Configuration Maven et dépendances
├── 📄 .gitignore               # Fichiers ignorés par Git
├── 📄 .gitattributes          # Attributs Git
├── 📄 mvw                      # Script Maven pour Linux/macOS
├── 📄 mvw.cmd                  # Script Maven pour Windows
├── 📁 src/                     # Code source du projet
├── 📁 target/                  # Fichiers compilés et builds
└── 📁 uploads/                 # Fichiers uploadés (CVs, documents)
```

### 📂 src/ - Code Source du Projet

```
src/
├── 📁 main/                    # Code principal de l'application
│   ├── 📁 java/               # Code Java
│   │   └── 📁 com/example/agencerecrutement/
│   │       ├── 📄 AgencerecrutementApplication.java  # Point d'entrée Spring Boot
│   │       ├── 📁 config/      # Configuration Spring
│   │       │   └── 📄 SecurityConfig.java
│   │       ├── 📁 javafx/       # Interface utilisateur JavaFX
│   │       │   ├── 📄 AgenceRecrutementApp.java    # Application JavaFX principale
│   │       │   ├── 📄 JavafxApplication.java       # Classe abstraite Spring+JavaFX
│   │       │   ├── 📁 controllers/                # Contrôleurs UI
│   │       │   │   ├── 📄 LoginController.java
│   │       │   │   └── 📄 MainController.java
│   │       │   └── 📁 dialogs/                    # Boîtes de dialogue
│   │       │       ├── 📄 CandidatureDialog.java
│   │       │       ├── 📄 InscriptionDialog.java
│   │       │       ├── 📄 MotDePasseOublieDialog.java
│   │       │       ├── 📄 ModifierMotDePasseDialog.java
│   │       │       ├── 📄 NouvelAbonnementDialog.java
│   │       │       ├── 📄 NouvelleOffreDialog.java
│   │       │       └── 📄 PublierOffreDialog.java
│   │       ├── 📁 model/        # Entités JPA
│   │       │   ├── 📄 Utilisateur.java              # Classe abstraite parent
│   │       │   ├── 📄 Administrateur.java
│   │       │   ├── 📄 Entreprise.java
│   │       │   ├── 📄 DemandeurEmploi.java
│   │       │   ├── 📄 Offre.java
│   │       │   ├── 📄 Candidature.java
│   │       │   ├── 📄 Recrutement.java
│   │       │   ├── 📄 Journal.java
│   │       │   ├── 📄 Edition.java
│   │       │   ├── 📄 Abonnement.java
│   │       │   ├── 📄 PublicationOffre.java
│   │       │   ├── 📄 Document.java
│   │       │   └── 📄 Categorie.java
│   │       ├── 📁 repository/   # Repositories Spring Data JPA
│   │       │   ├── 📄 UtilisateurRepository.java
│   │       │   ├── 📄 EntrepriseRepository.java
│   │       │   ├── 📄 DemandeurEmploiRepository.java
│   │       │   ├── 📄 OffreRepository.java
│   │       │   ├── 📄 CandidatureRepository.java
│   │       │   ├── 📄 RecrutementRepository.java
│   │       │   ├── 📄 JournalRepository.java
│   │       │   ├── 📄 EditionRepository.java
│   │       │   ├── 📄 AbonnementRepository.java
│   │       │   ├── 📄 PublicationOffreRepository.java
│   │       │   └── 📄 DocumentRepository.java
│   │       └── 📁 service/      # Logique métier
│   │           ├── 📄 AuthentificationService.java
│   │           ├── 📄 EntrepriseService.java
│   │           ├── 📄 DemandeurEmploiService.java
│   │           ├── 📄 OffreService.java
│   │           ├── 📄 CandidatureService.java
│   │           ├── 📄 RecrutementService.java
│   │           ├── 📄 JournalService.java
│   │           └── 📄 DocumentService.java
│   └── 📁 resources/           # Fichiers de configuration
│       └── 📄 application.properties
└── 📁 test/                    # Tests unitaires et d'intégration
    └── 📁 java/
        └── 📁 com/example/agencerecrutement/
```

### 📂 docs/ - Documentation du Projet

```
docs/
├── 📄 rapport_projet.md         # Rapport technique détaillé
├── 📄 presentation_projet.pptx # Présentation du projet
├── 📄 architecture.md          # Documentation architecture
├── 📄 api_reference.md         # Référence API (si applicable)
├── 📄 manuel_utilisateur.md    # Guide d'utilisation
└── 📁 images/                 # Images et diagrammes
    ├── 📄 architecture_diagram.png
    ├── 📄 database_schema.png
    └── 📁 user_interface_screenshots/
```

---

## 🎯 Fonctionnalités Principales

### 👤 Pour l'Administrateur
- **Gestion des utilisateurs** : Création, modification, suppression
- **Gestion des journaux et catégories** : Administration des publications
- **Consultation des statistiques** : Tableaux de bord et rapports
- **Historique des recrutements** : Suivi complet des processus

### 🏢 Pour l'Entreprise
- **Souscription d'abonnements** : Aux journaux pour publier des offres
- **Création et publication d'offres** : Gestion complète des postes
- **Consultation des candidatures** : Accès aux profils et CVs
- **Recrutement de candidats** : Validation et embauche

### 👨‍💼 Pour le Demandeur d'Emploi
- **Consultation des offres** : Par journal et par édition
- **Candidature aux offres** : Sous conditions d'expérience
- **Suivi des candidatures** : États en temps réel
- **Gestion des documents** : Upload et validation de CVs

---

## 📊 Règles Métier Implémentées

1. **Abonnements** : Une entreprise ne peut avoir qu'un seul abonnement actif par journal
2. **Publication** : Les offres ne peuvent être publiées que via un abonnement actif
3. **Candidatures** : 
   - Le demandeur doit avoir au moins l'expérience requise
   - Un demandeur ne peut postuler qu'une seule fois à une même offre
4. **Recrutements** : 
   - Limité au nombre de postes disponibles
   - Désactivation automatique de l'offre quand tous les postes sont pourvus

---

## 🔧 Configuration Technique

### Base de Données
- **URL** : `jdbc:mysql://localhost:3306/agence_recrutement`
- **Driver** : `com.mysql.cj.jdbc.Driver`
- **Stratégie DDL** : `update` (création/mise à jour automatique des tables)
- **Dialecte** : `org.hibernate.dialect.MySQLDialect`

### Fichiers Uploadés
- **Répertoire** : `./uploads`
- **Taille maximale** : 10MB par fichier
- **Formats acceptés** : PNG (pour les CVs)

### Logging SQL
- **Activation** : `spring.jpa.show-sql=true`
- **Formatage** : `spring.jpa.properties.hibernate.format_sql=true`

---

## 🐛 Dépannage

### Problèmes Communs

1. **Erreur de connexion à la base de données**
   - Vérifier que MySQL est en cours d'exécution
   - Vérifier les identifiants dans `application.properties`
   - S'assurer que la base de données `agence_recrutement` existe

2. **Erreur JavaFX**
   - Vérifier que JavaFX est bien configuré dans votre IDE
   - S'assurer d'utiliser Java 17 ou supérieur

3. **Problème de dépendances Maven**
   - Exécuter `mvn clean install`
   - Vérifier la connexion internet pour télécharger les dépendances

### Support Technique

Pour toute question ou problème technique, contacter :
- **Hiba Zouitina** : HibaZouitina@gmail.com
- **Imane Taleb** : Imanetaleb@gmail.com  
- **Saïda Stifi** : Saida27stifi@gmail.com
- **Chouaib Bouslamti** : chouaibbouslamti7@gmail.com

---

## 📝 Notes de Développement

- Projet développé dans le cadre d'un mini-projet Spring Boot + JavaFX
- Architecture en couches respectant les bonnes pratiques
- Tests unitaires à compléter
- Interface utilisateur entièrement programmée (pas de FXML)
- Sécurité des mots de passe via BCrypt

---

## 📄 Licence

Ce projet est développé à des fins éducatives dans le cadre du cursus universitaire.

---

**Version** : 0.0.1-SNAPSHOT  
**Dernière mise à jour** : Janvier 2024


