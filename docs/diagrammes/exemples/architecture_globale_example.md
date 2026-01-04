# Exemple : Architecture Globale du Système

## 📋 Description du Diagramme

Ce diagramme illustre l'architecture complète de l'application d'agence de recrutement, montrant l'intégration entre Spring Boot (backend) et JavaFX (interface desktop).

## 🏗️ Structure du Diagramme

### Couche Présentation (JavaFX 21)
```
┌─────────────────────────────────────────────────────────────┐
│                    INTERFACE UTILISATEUR                   │
│                   (JavaFX 21)                         │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────────┬─────────────────┬─────────────────┐   │
│  │   Admin UI     │  Entreprise UI  │ Demandeur UI    │   │
│  │   Controller    │   Controller    │   Controller    │   │
│  │                 │                 │                 │   │
│  │ • Gestion users│ • Mes offres    │ • Offres dispo  │   │
│  │ • Journaux     │ • Abonnements   │ • Mes candidats │   │
│  │ • Statistiques │ • Candidatures  │ • Journaux      │   │
│  │ • Rapports     │ • Recrutements  │ • Profil        │   │
│  └─────────────────┴─────────────────┴─────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

### Couche Métier (Spring Services)
```
┌─────────────────────────────────────────────────────────────┐
│                    LOGIQUE MÉTIER                         │
│                 (Spring Services)                       │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────────┬─────────────────┬─────────────────┐   │
│  │ Authentification│  Offre Service  │Candidature Svc │   │
│  │    Service     │                 │                 │   │
│  │                 │                 │                 │   │
│  │ • Login/Logout │ • CRUD offres   │ • Postuler      │   │
│  │ • Hashage MDP  │ • Validation    │ • Validation    │   │
│  │ • Sessions     │ • Publication   │ • Notifications │   │
│  └─────────────────┴─────────────────┴─────────────────┘   │
│  ┌─────────────────┬─────────────────┬─────────────────┐   │
│  │Entreprise Svc  │Document Service │Journal Service │   │
│  │                 │                 │                 │   │
│  │ • CRUD entreprise│ • Upload CV     │ • CRUD journaux │   │
│  │ • Abonnements   │ • Validation    │ • Éditions      │   │
│  │ • Validation    │ • Stockage      │ • Publications  │   │
│  └─────────────────┴─────────────────┴─────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

### Couche Accès Données (Spring Data JPA)
```
┌─────────────────────────────────────────────────────────────┐
│                    ACCÈS AUX DONNÉES                       │
│              (Spring Data JPA)                         │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────────┬─────────────────┬─────────────────┐   │
│  │ User Repository │ Offre Repository│Candidature Repo │   │
│  │                 │                 │                 │   │
│  │ • findByLogin   │ • findByEntreprise│ • findByDemandeur│   │
│  │ • findByRole    │ • findByEtat    │ • findByOffre    │   │
│  │ • save/delete   │ • save/delete   │ • save/delete    │   │
│  └─────────────────┴─────────────────┴─────────────────┘   │
│  ┌─────────────────┬─────────────────┬─────────────────┐   │
│  │Journal Repository│Doc Repository  │Abonnement Repo │   │
│  │                 │                 │                 │   │
│  │ • findAll()     │ • findByDemandeur│ • findByEntreprise│   │
│  │ • findByCode    │ • save/delete   │ • findActifs    │   │
│  │ • save/delete   │                 │ • save/delete   │   │
│  └─────────────────┴─────────────────┴─────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

### Couche Stockage (MySQL 8.0+)
```
┌─────────────────────────────────────────────────────────────┐
│                    BASE DE DONNÉES                       │
│                  (MySQL 8.0+)                          │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────────┬─────────────────┬─────────────────┐   │
│  │   utilisateurs │      offres     │  candidatures  │   │
│  │                 │                 │                 │   │
│  │ • id_utilisateur│ • id_offre      │ • id_candidature│   │
│  │ • login         │ • titre         │ • date_candid   │   │
│  │ • mot_de_passe  │ • competences   │ • statut        │   │
│  │ • type_user     │ • experience    │ • id_demandeur  │   │
│  │ • ...           │ • ...           │ • ...           │   │
│  └─────────────────┴─────────────────┴─────────────────┘   │
│  ┌─────────────────┬─────────────────┬─────────────────┐   │
│  │     journaux    │    documents    │  abonnements   │   │
│  │                 │                 │                 │   │
│  │ • code_journal  │ • id_document  │ • id_abonnement│   │
│  │ • nom           │ • nom_fichier   │ • date_debut    │   │
│  │ • ...           │ • chemin_stock  │ • date_fin      │   │
│  │                 │ • ...           │ • ...           │   │
│  └─────────────────┴─────────────────┴─────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

## 🔄 Flux de Données

### Flux de Connexion
```
Utilisateur → LoginController → AuthentificationService → UserRepository → MySQL
     ↓              ↓                    ↓                    ↓         ↓
  Credentials   Validation         BCrypt Check        Query    Response
     ↓              ↓                    ↓                    ↓         ↓
   Success    Session Start        User Object        Fetch    User Data
     ↓              ↓                    ↓                    ↓         ↓
   Main UI    Role Detection      Interface Load      Cache    Dashboard
```

### Flux de Candidature
```
Demandeur → CandidatureDialog → CandidatureService → Multiple Repos → MySQL
    ↓              ↓                    ↓                    ↓         ↓
 Form Fill    UI Validation      Business Rules        Queries   Transaction
    ↓              ↓                    ↓                    ↓         ↓
 Submit      Dialog Close       Candidature Creation   Inserts   New Record
    ↓              ↓                    ↓                    ↓         ↓
 Success    Notification      Interface Refresh      Commit    User Notified
```

## 🔧 Points Techniques Clés

### 1. Intégration Spring Boot + JavaFX
- **Point d'entrée hybride** : `AgenceRecrutementApp` étend `JavafxApplication`
- **Injection de dépendances** : `@Autowired` dans les contrôleurs JavaFX
- **Cycle de vie** : Spring initialise l'application JavaFX

### 2. Architecture en Couches
- **Séparation claire** : Présentation ↔ Métier ↔ Données
- **Dépendances unidirectionnelles** : Couches supérieures dépendent des inférieures
- **Interface standard** : Spring Data JPA pour l'accès aux données

### 3. Gestion des Transactions
- **@Transactional** : Sur les méthodes de service
- **Rollback automatique** : En cas d'exception
- **Isolation des données** : Par transaction

### 4. Sécurité Intégrée
- **Hashage BCrypt** : Pour les mots de passe
- **Validation par rôle** : Dans chaque contrôleur
- **Isolation des données** : Par utilisateur connecté

## 🎯 Avantages de cette Architecture

### 1. **Maintenabilité**
- **Couches indépendantes** : Modifications localisées
- **Interfaces claires** : Contrats bien définis
- **Tests unitaires** : Possibles par couche

### 2. **Évolutivité**
- **Ajout de fonctionnalités** : Sans impact sur les autres couches
- **Changement de base de données** : Via JPA
- **Nouveaux rôles** : Extension du modèle

### 3. **Performance**
- **Lazy loading** : Optimisation mémoire
- **Cache Spring** : Réduction requêtes
- **Connexions poolées** : Gestion efficace

### 4. **Sécurité**
- **Validation centralisée** : Dans les services
- **Contrôle d'accès** : Par rôle
- **Hashage sécurisé** : BCrypt

## 📊 Métriques de l'Architecture

| Composant | Nombre | Responsabilité |
|-----------|--------|----------------|
| Controllers | 3 | UI par rôle |
| Services | 7 | Logique métier |
| Repositories | 9 | Accès données |
| Entités | 11 | Modèle de données |
| Tables SQL | 8 | Persistance |

## 🚀 Évolutions Possibles

### Court Terme
- **API REST** : Pour clients mobiles/web
- **WebSocket** : Notifications temps réel
- **Cache Redis** : Performance accrue

### Moyen Terme
- **Microservices** : Découpage par domaine
- **Message Queue** : Traitement asynchrone
- **Search Engine** : Recherche avancée

### Long Terme
- **Cloud Native** : Conteneurisation
- **Multi-tenant** : Plusieurs agences
- **AI/ML** : Matching intelligent

---

## 📝 Notes de Création

### Outils Recommandés
- **Draw.io** : Pour ce type de diagramme d'architecture
- **Lucidchart** : Pour des versions plus détaillées
- **PlantUML** : Pour génération automatique

### Conventions Visuelles
- **Bleu** : Composants techniques
- **Vert** : Données et persistance
- **Orange** : Flux et interactions
- **Gris** : Infrastructure

### Éléments à Inclure
- [x] Couches principales
- [x] Flux de données
- [x] Points techniques clés
- [x] Avantages et métriques
- [ ] Échelle de performance
- [ ] Points de monitoring

---

**Version de l'exemple** : 1.0  
**Date de création** : Janvier 2024  
**Compatible avec** : Architecture réelle du projet
