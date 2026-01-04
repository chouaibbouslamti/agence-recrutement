# Liste Complète des Diagrammes du Projet

## 📋 Index des Diagrammes par Catégorie

---

## 🏗️ ARCHITECTURE SYSTÈME

| # | Nom du Fichier | Type | Description | Priorité |
|---|----------------|------|-------------|----------|
| A1 | `architecture_globale.png` | Vue d'ensemble | Architecture complète avec couches Spring Boot + JavaFX | 🔴 Critique |
| A2 | `integration_spring_javafx.png` | Détail technique | Point d'entrée hybride et injection de dépendances | 🔴 Critique |
| A3 | `patterns_architecture.png` | Patterns | MVC, Repository, Service Layer implémentés | 🟡 Moyenne |
| A4 | `flux_donnees_systeme.png` | Flux | Circulation des données entre composants | 🟡 Moyenne |

---

## 📊 BASE DE DONNÉES

| # | Nom du Fichier | Type | Description | Priorité |
|---|----------------|------|-------------|----------|
| B1 | `schema_complet_bdd.png` | ERD | Schéma complet avec toutes les tables et relations | 🔴 Critique |
| B2 | `heritage_utilisateurs.png` | Héritage | Stratégie SINGLE_TABLE pour les utilisateurs | 🔴 Critique |
| B3 | `relations_entites.png` | Relations | OneToMany, ManyToOne, cardinalités | 🔴 Critique |
| B4 | `index_optimisation.png` | Performance | Index et optimisations des requêtes | 🟡 Moyenne |
| B5 | `mapping_jpa.png` | Mapping | Correspondance JPA ↔ SQL | 🟡 Moyenne |

---

## 🔄 FLUX ET SÉQUENCES

| # | Nom du Fichier | Type | Description | Priorité |
|---|----------------|------|-------------|----------|
| F1 | `sequence_authentification.png` | Séquence | Login → Validation → Session | 🔴 Critique |
| F2 | `sequence_inscription.png` | Séquence | Formulaire → Validation → Création compte | 🔴 Critique |
| F3 | `sequence_candidature.png` | Séquence | Consultation → Postulation → Validation | 🔴 Critique |
| F4 | `sequence_publication_offre.png` | Séquence | Création → Abonnement → Publication | 🔴 Critique |
| F5 | `sequence_upload_cv.png` | Séquence | Sélection → Validation → Stockage | 🟡 Moyenne |
| F6 | `sequence_notification.png` | Séquence | Changement statut → Notification → Marquage | 🟢 Faible |

---

## 🎯 CAS D'UTILISATION

| # | Nom du Fichier | Type | Description | Priorité |
|---|----------------|------|-------------|----------|
| U1 | `use_case_administrateur.png` | Use Case | Gestion utilisateurs, journaux, statistiques | 🔴 Critique |
| U2 | `use_case_entreprise.png` | Use Case | Offres, abonnements, candidatures, recrutements | 🔴 Critique |
| U3 | `use_case_demandeur.png` | Use Case | Consultation offres, candidatures, profil | 🔴 Critique |
| U4 | `use_case_global.png` | Vue globale | Interactions entre tous les acteurs | 🟡 Moyenne |

---

## 🖥️ INTERFACE UTILISATEUR

| # | Nom du Fichier | Type | Description | Priorité |
|---|----------------|------|-------------|----------|
| I1 | `interface_administrateur.png` | Wireframe | Écran principal admin avec tableaux de bord | 🔴 Critique |
| I2 | `interface_entreprise.png` | Wireframe | Onglets offres, abonnements, candidatures | 🔴 Critique |
| I3 | `interface_demandeur.png` | Wireframe | Navigation journaux/éditions/offres | 🔴 Critique |
| I4 | `interface_login.png` | Wireframe | Écran de connexion et inscription | 🟡 Moyenne |
| I5 | `interface_dialogues.png` | Wireframe | Boîtes de dialogue (candidature, inscription) | 🟡 Moyenne |

---

## 🔒 SÉCURITÉ

| # | Nom du Fichier | Type | Description | Priorité |
|---|----------------|------|-------------|----------|
| S1 | `securite_mots_de_passe.png` | Flux | Hashage BCrypt et vulnérabilités | 🔴 Critique |
| S2 | `controle_acces_roles.png` | Flux | Validation permissions par rôle | 🔴 Critique |
| S3 | `isolation_donnees.png` | Architecture | Séparation des données par utilisateur | 🟡 Moyenne |
| S4 | `validation_inputs.png` | Flux | Validation des entrées utilisateur | 🟡 Moyenne |

---

## ⚡ PERFORMANCE

| # | Nom du Fichier | Type | Description | Priorité |
|---|----------------|------|-------------|----------|
| P1 | `optimisation_requetes.png` | Technique | Solutions N+1 queries avec JOIN FETCH | 🔴 Critique |
| P2 | `lazy_loading.png` | Technique | Gestion du lazy loading et eager loading | 🟡 Moyenne |
| P3 | `cache_strategies.png` | Architecture | Stratégies de cache possibles | 🟢 Faible |
| P4 | `upload_performance.png` | Flux | Optimisation upload de fichiers | 🟡 Moyenne |

---

## 📈 DÉPLOIEMENT

| # | Nom du Fichier | Type | Description | Priorité |
|---|----------------|------|-------------|----------|
| D1 | `architecture_production.png` | Déploiement | Serveurs, base de données, stockage | 🟡 Moyenne |
| D2 | `process_build_maven.png` | Processus | Compilation, packaging, déploiement | 🟡 Moyenne |
| D3 | `configuration_environnement.png` | Configuration | Variables d'environnement et properties | 🟢 Faible |

---

## 🎨 CONCEPTION D'INTERFACE

| # | Nom du Fichier | Type | Description | Priorité |
|---|----------------|------|-------------|----------|
| UI1 | `maquette_complete.png` | Maquette | Vue complète de l'application | 🟡 Moyenne |
| UI2 | `navigation_principale.png` | Navigation | Structure des menus et onglets | 🟡 Moyenne |
| UI3 | **formulaires_saisie.png** | Formulaire | Design des formulaires de saisie | 🟢 Faible |
| UI4 | **tableaux_affichage.png** | Tableaux | Design des TableView et listes | 🟢 Faible |

---

## 📊 STATISTIQUES ET RAPPORTS

| # | Nom du Fichier | Type | Description | Priorité |
|---|----------------|------|-------------|----------|
| R1 | `tableau_bord_admin.png` | Dashboard | Vue des statistiques pour l'admin | 🟡 Moyenne |
| R2 | `rapports_recrutement.png` | Rapport | Format des rapports de recrutement | 🟢 Faible |
| R3 | **indicateurs_performance.png** | KPI | Indicateurs clés de performance | 🟢 Faible |

---

## 🔄 DIAGRAMMES TECHNIQUES SPÉCIFIQUES

| # | Nom du Fichier | Type | Description | Priorité |
|---|----------------|------|-------------|----------|
| T1 | `flux_navigation_journaux.png` | Séquence | Navigation Journal → Édition → Offre | 🔴 Critique |
| T2 | `gestion_etats_offre.png` | États | Cycle de vie des états d'offre | 🟡 Moyenne |
| T3 | `workflow_validation.png` | Workflow | Processus de validation des candidatures | 🟡 Moyenne |
| T4 | **systeme_notifications.png** | Architecture | Architecture des notifications | 🟢 Faible |

---

## 📝 LÉGENDE DES PRIORITÉS

- 🔴 **Critique** : Essentiel pour comprendre l'architecture et le fonctionnement
- 🟡 **Moyenne** : Important pour la documentation et la maintenance
- 🟢 **Faible** : Complémentaire pour une documentation exhaustive

---

## 🎯 DIAGRAMMES PRIORITAIRES (À CRÉER EN PREMIER)

### Phase 1 - Critiques (Semaine 1)
1. **A1** - `architecture_globale.png`
2. **B1** - `schema_complet_bdd.png`
3. **F1** - `sequence_authentification.png`
4. **F3** - `sequence_candidature.png`
5. **U1** - `use_case_administrateur.png`
6. **U2** - `use_case_entreprise.png`
7. **U3** - `use_case_demandeur.png`
8. **S1** - `securite_mots_de_passe.png`

### Phase 2 - Importants (Semaine 2)
9. **A2** - `integration_spring_javafx.png`
10. **B2** - `heritage_utilisateurs.png`
11. **F2** - `sequence_inscription.png`
12. **F4** - `sequence_publication_offre.png`
13. **I1** - `interface_administrateur.png`
14. **I2** - `interface_entreprise.png`
15. **I3** - `interface_demandeur.png`

### Phase 3 - Complémentaires (Semaine 3)
16. **P1** - `optimisation_requetes.png`
17. **T1** - `flux_navigation_journaux.png`
18. **D1** - `architecture_production.png`
19. **R1** - `tableau_bord_admin.png`

---

## 🛠️ OUTILS RECOMMANDÉS PAR TYPE

### Architecture Système
- **Draw.io** : Pour les vues d'ensemble
- **Lucidchart** : Pour les diagrammes complexes
- **PlantUML** : Pour les diagrammes UML standardisés

### Base de Données
- **MySQL Workbench** : Pour les schémas ERD
- **dbdiagram.io** : Pour les diagrammes en ligne
- **Draw.io** : Pour les relations simples

### Séquences et Flux
- **PlantUML** : Syntaxe standardisée
- **SequenceDiagram.org** : Génération rapide
- **Draw.io** : Interface visuelle

### Interfaces Utilisateur
- **Figma** : Wireframes et maquettes
- **Balsamiq** : Prototypage rapide
- **Draw.io** : Schémas simples

---

## 📋 CHECKLIST DE CRÉATION

### Pour chaque diagramme :
- [ ] **Nom standardisé** selon la nomenclature
- [ ] **Format PNG** haute qualité
- [ ] **Version SVG** pour modifications
- [ ] **Légende** explicative
- [ ] **Référence** dans la documentation
- [ ] **Métadonnées** (auteur, date, version)

### Validation qualité :
- [ ] **Lisibilité** à 100% et 200%
- [ ] **Cohérence** avec les autres diagrammes
- [ ] **Exactitude** technique
- [ ] **Complétude** des éléments
- [ ] **Standardisation** visuelle

---

## 📊 STATISTIQUES DU RÉPERTOIRE

- **Total diagrammes** : 42
- **Diagrammes critiques** : 8
- **Diagrammes importants** : 7
- **Diagrammes complémentaires** : 27
- **Formats supportés** : PNG, SVG, PDF
- **Outils principaux** : Draw.io, PlantUML, MySQL Workbench

---

**Version de la liste** : 1.0  
**Date de création** : Janvier 2024  
**Dernière mise à jour** : Janvier 2024  
**Prochaine révision** : Février 2024
