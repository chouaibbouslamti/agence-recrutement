# 📋 Comptes Utilisateurs - Agence de Recrutement

## 🔐 Mot de Passe Universel
**Tous les comptes utilisent le même mot de passe :** `password123`

---

## 👑 Administrateur
| Login | Rôle | Nom Complet | Mot de passe |
|-------|------|-------------|--------------|
| `admin` | ADMINISTRATEUR | Administrateur | `password123` |

---

## 🏢 Entreprises (4 comptes)

| Login | Entreprise | Secteur d'activité | Contact | Mot de passe |
|-------|------------|-------------------|---------|--------------|
| `techcorp` | TechCorp Solutions | Développement logiciel et solutions cloud | 01 40 12 34 56 | `password123` |
| `innovent` | InnoVent Group | Intelligence artificielle et machine learning | 04 78 12 34 56 | `password123` |
| `servicesplus` | Services Plus | Services informatiques et consulting | 04 91 12 34 56 | `password123` |
| `retailmaster` | Retail Master France | Chaîne de magasins et e-commerce | 05 56 12 34 56 | `password123` |

---

## 👥 Demandeurs d'Emploi (6 comptes)

| Login | Nom | Prénom | Expérience | Salaire souhaité | Diplôme | Mot de passe |
|-------|-----|--------|------------|------------------|---------|--------------|
| `jdupont` | Dupont | Jean | 3 ans | 45 000€ | Master Informatique | `password123` |
| `mmartin` | Martin | Marie | 2 ans | 38 000€ | Licence Marketing Digital | `password123` |
| `plegrand` | Legrand | Pierre | 5 ans | 42 000€ | DUT Réseaux et Télécoms | `password123` |
| `sbernard` | Bernard | Sophie | 4 ans | 35 000€ | BTS Comptabilité et Gestion | `password123` |
| `tleroi` | Leroi | Thomas | 1 an | 32 000€ | Master Management | `password123` |
| `cfabre` | Fabre | Claire | 6 ans | 48 000€ | Licence Communication | `password123` |

---

## 📊 Statistiques des Données de Test

- **Utilisateurs totaux :** 11 (1 admin + 4 entreprises + 6 demandeurs)
- **Offres d'emploi :** 10 postes disponibles
- **Journaux :** 7 publications différentes
- **Éditions publiées :** 15 éditions
- **Abonnements actifs :** 9 abonnements entreprise
- **Publications d'offres :** 13 publications
- **Candidatures :** 13 candidatures déposées

---

## 🎯 Scénarios de Test Suggérés

### 1. Test Administrateur
- **Connexion :** `admin` / `password123`
- **Actions possibles :** Gestion des utilisateurs, consultation des statistiques

### 2. Test Entreprise (ex: TechCorp)
- **Connexion :** `techcorp` / `password123`
- **Actions possibles :** 
  - Publier de nouvelles offres
  - Gérer les abonnements aux journaux
  - Consulter les candidatures

### 3. Test Demandeur d'Emploi (ex: Jean Dupont)
- **Connexion :** `jdupont` / `password123`
- **Actions possibles :**
  - Consulter les offres disponibles
  - Déposer des candidatures
  - Voir ses candidatures en cours

---

## 📁 Fichiers Connexes

- **`donnees_test.sql`** : Script SQL complet pour remplir la base de données
- **`COMPTES_UTILISATEURS.md`** : Ce fichier récapitulatif

---

## 🔧 Instructions d'Utilisation

1. **Exécuter le script SQL** dans votre base de données pour créer toutes les données de test
2. **Utiliser les comptes** ci-dessus pour tester les différentes fonctionnalités
3. **Explorer les scénarios** pour vérifier le bon fonctionnement de l'application

*Tous les mots de passe sont hashés avec BCrypt pour la sécurité.*
