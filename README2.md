# Répertoire des Diagrammes - Agence de Recrutement

## 📋 Vue d'Ensemble des Diagrammes

Ce répertoire contient l'ensemble des diagrammes techniques et fonctionnels relatifs au projet d'agence de recrutement. Chaque diagramme illustre un aspect spécifique de l'application pour faciliter la compréhension et la documentation.

---

## 🏗️ Diagrammes d'Architecture

### 1. architecture_globale.png
**Description** : Vue d'ensemble de l'architecture système
**Contenu** :
- Couches applicatives (Présentation, Métier, Données)
- Intégration Spring Boot + JavaFX
- Flux de données entre composants
- Positionnement de la base de données MySQL

**Utilité** : Comprendre l'organisation générale du système

### 2. architecture_spring_javafx.png
**Description** : Détail de l'intégration Spring Boot avec JavaFX
**Contenu** :
- Point d'entrée hybride
- Injection de dépendances dans les contrôleurs
- Cycle de vie de l'application
- Configuration des beans

**Utilité** : Comprendre la particularité technique de l'architecture

### 3. pattern_architecture.png
**Description** : Patterns architecturaux implémentés
**Contenu** :
- MVC (Model-View-Controller)
- Repository Pattern
- Service Layer Pattern
- DAO Pattern (via Spring Data)

**Utilité** : Visualiser les patterns de conception utilisés

---

## 📊 Diagrammes de Base de Données

### 4. schema_bdd.png
**Description** : Schéma complet de la base de données
**Contenu** :
- Tables principales (utilisateurs, offres, candidatures)
- Relations et clés étrangères
- Stratégie d'héritage SINGLE_TABLE
- Index et contraintes

**Utilité** : Comprendre la structure de persistance

### 5. heritage_utilisateur.png
**Description** : Stratégie d'héritage des utilisateurs
**Contenu** :
- Table `utilisateurs` avec discriminator
- Spécialisation (Administrateur, Entreprise, Demandeur)
- Champs spécifiques par type
- Mapping JPA correspondant

**Utilité** : Visualiser la gestion des rôles et types d'utilisateurs

### 6. relations_entites.png
**Description** : Relations entre les entités principales
**Contenu** :
- Relations OneToMany/ManyToOne
- Cascades et orphanRemoval
- Lazy loading vs Eager loading
- Cardinalités des relations

**Utilité** : Comprendre les interactions entre entités

---

## 🔄 Diagrammes de Flux et Séquences

### 7. flux_authentification.png
**Description** : Séquence d'authentification complète
**Contenu** :
- LoginController → AuthentificationService → Repository
- Validation BCrypt
- Gestion des erreurs
- Initialisation de session

**Utilité** : Suivre le processus de connexion

### 8. flux_inscription.png
**Description** : Processus d'inscription des utilisateurs
**Contenu** :
- InscriptionDialog → Services → Base de données
- Validation des formulaires
- Upload de documents (CV)
- Création des comptes par type

**Utilité** : Comprendre la création de comptes

### 9. flux_candidature.png
**Description** : Cycle de vie d'une candidature
**Contenu** :
- Consultation des offres → Postulation → Validation
- Vérifications métier (expérience, unicité)
- États de la candidature
- Notifications automatiques

**Utilité** : Visualiser le processus de recrutement

### 10. flux_publication_offre.png
**Description** : Publication d'une offre par une entreprise
**Contenu** :
- Création offre → Abonnement → Publication
- Validation des abonnements actifs
- Sélection journal/édition
- Mise à jour des états

**Utilité** : Comprendre le cycle de publication

---

## 🎯 Diagrammes Fonctionnels

### 11. cas_utilisation_administrateur.png
**Description** : Cas d'utilisation pour l'administrateur
**Contenu** :
- Gestion des utilisateurs
- Administration des journaux
- Consultation des statistiques
- Rapports de recrutement

**Utilité** : Visualiser les fonctionnalités administrateur

### 12. cas_utilisation_entreprise.png
**Description** : Cas d'utilisation pour l'entreprise
**Contenu** :
- Gestion des offres
- Abonnements aux journaux
- Traitement des candidatures
- Recrutement des candidats

**Utilité** : Comprendre le workflow entreprise

### 13. cas_utilisation_demandeur.png
**Description** : Cas d'utilisation pour le demandeur d'emploi
**Contenu** :
- Consultation des offres
- Processus de candidature
- Suivi des candidatures
- Gestion des documents

**Utilité** : Visualiser le parcours demandeur

---

## 🖥️ Diagrammes d'Interface Utilisateur

### 14. interface_administrateur.png
**Description** : Maquette interface administrateur
**Contenu** :
- Tableau de bord principal
- Boutons de gestion
- Vues des statistiques
- Interface de rapports

**Utilité** : Comprendre l'expérience utilisateur admin

### 15. interface_entreprise.png
**Description** : Maquette interface entreprise
**Contenu** :
- Onglets fonctionnels
- Gestion des offres
- Vue des candidatures
- Interface d'abonnement

**Utilité** : Visualiser l'interface entreprise

### 16. interface_demandeur.png
**Description** : Maquette interface demandeur
**Contenu** :
- Navigation journaux/éditions
- Consultation des offres
- Suivi des candidatures
- Gestion du profil

**Utilité** : Comprendre l'expérience utilisateur demandeur

---

## 🔒 Diagrammes de Sécurité

### 17. securite_mots_de_passe.png
**Description** : Gestion de la sécurité des mots de passe
**Contenu** :
- Hashage BCrypt
- Validation en clair (vulnérabilité)
- Flux de vérification
- Recommandations de sécurité

**Utilité** : Comprendre les mécanismes de sécurité

### 18. controle_acces.png
**Description** : Contrôle d'accès par rôle
**Contenu** :
- Validation des permissions
- Isolation des données
- Accès par fonctionnalité
- Restrictions d'interface

**Utilité** : Visualiser la sécurité applicative

---

## ⚡ Diagrammes de Performance

### 19. performance_requetes.png
**Description** : Optimisation des requêtes SQL
**Contenu** :
- Problème N+1 queries
- Solutions avec JOIN FETCH
- Lazy loading
- Indexation des tables

**Utilité** : Comprendre les optimisations de performance

### 20. gestion_fichiers.png
**Description** : Gestion des uploads de fichiers
**Contenu** :
- Processus d'upload CV
- Validation taille et format
- Stockage avec UUID
- Gestion des métadonnées

**Utilité** : Visualiser la gestion des documents

---

## 📈 Diagrammes de Déploiement

### 21. deploiement_production.png
**Description** : Architecture de déploiement
**Contenu** :
- Serveur d'application
- Base de données MySQL
- Stockage des fichiers
- Configuration environnement

**Utilité** : Comprendre l'architecture de production

### 22. process_build.png
**Description** : Processus de build et packaging
**Contenu** :
- Compilation Maven
- Création du JAR exécutable
- Dépendances et librairies
- Configuration de déploiement

**Utilité** : Visualiser le processus de build

---

## 🎨 Conventions des Diagrammes

### Légende des Couleurs
- 🔵 **Bleu** : Composants techniques (Spring, JavaFX)
- 🟢 **Vert** : Données et persistance
- 🟡 **Jaune** : Logique métier
- 🔴 **Rouge** : Sécurité et validation
- 🟣 **Violet** : Interface utilisateur
- ⚫ **Noir** : Flux et séquences

### Types de Diagrammes
- **Architecture** : Vue structurelle du système
- **Base de données** : Schéma et relations
- **Séquence** : Flux temporels des interactions
- **Cas d'utilisation** : Fonctionnalités par rôle
- **Interface** : Maquettes et wireframes
- **Déploiement** : Infrastructure et environnement

### Format des Fichiers
- **PNG** : Diagrammes haute qualité pour la documentation
- **SVG** : Format vectoriel pour modifications
- **PDF** : Version imprimable des diagrammes complexes

---

## 🔧 Outils de Création

### Logiciels Recommandés
- **Draw.io** : Diagrammes techniques et flux
- **PlantUML** : Diagrammes de séquence et architecture
- **Lucidchart** : Cas d'utilisation et wireframes
- **MySQL Workbench** : Schéma de base de données
- **StarUML** : Diagrammes UML complets

### Templates et Modèles
- **Template architecture** : Standardisé pour tous les diagrammes système
- **Template base de données** : Conventions de notation ERD
- **Template flux** : Style uniforme pour les séquences
- **Template interface** : Wireframes standardisés

---

## 📚 Utilisation des Diagrammes

### Dans la Documentation
- **Rapport technique** : Références aux diagrammes pertinents
- **Présentation** : Supports visuels pour les explications
- **Formation** : Supports pédagogiques pour les nouveaux développeurs

### Pour le Développement
- **Onboarding** : Compréhension rapide de l'architecture
- **Maintenance** : Visualisation des impacts des modifications
- **Évolution** : Base pour les futures améliorations

### Pour la Communication
- **Réunions techniques** : Supports visuels pour les discussions
- **Présentations clients** : Illustration des fonctionnalités
- **Documentation utilisateur** : Guides visuels

---

## 📝 Historique des Versions

### Version 1.0 (Janvier 2024)
- Création initiale du répertoire
- 22 diagrammes fondamentaux
- Documentation complète

### Évolutions Prévues
- **Version 1.1** : Ajout diagrammes de tests
- **Version 1.2** : Diagrammes de monitoring
- **Version 2.0** : Architecture microservices

---

## 📞 Contact et Support

Pour toute question sur les diagrammes :
- **📧 Hiba Zouitina** : HibaZouitina@gmail.com
- **📧 Imane Taleb** : Imanetaleb@gmail.com
- **📧 Saïda Stifi** : Saida27stifi@gmail.com
- **📧 Chouaib Bouslamti** : chouaibbouslamti7@gmail.com

---

**Version du document** : 1.0  
**Date de création** : Janvier 2024  
**Nombre de diagrammes** : 22  
**Formats supportés** : PNG, SVG, PDF
