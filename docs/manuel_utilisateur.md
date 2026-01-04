# Manuel d'Utilisation - Agence de Recrutement

## Table des Matières

1. [Première Connexion](#première-connexion)
2. [Espace Administrateur](#espace-administrateur)
3. [Espace Entreprise](#espace-entreprise)
4. [Espace Demandeur d'Emploi](#espace-demandeur-demploi)
5. [Guide de Dépannage](#guide-de-dépannage)

---

## Première Connexion

### 1. Lancement de l'Application

1. **Double-cliquez** sur le fichier JAR exécutable
2. **Ou utilisez la commande** :
   ```bash
   java -jar agencerecrutement-0.0.1-SNAPSHOT.jar
   ```

### 2. Écran de Connexion

L'interface de connexion s'affiche avec :
- **Champ Login** : Avec autocomplétion des comptes existants
- **Champ Mot de passe** : Champ sécurisé
- **Bouton "Se connecter"** : Pour accéder à votre espace
- **Bouton "S'inscrire"** : Pour créer un nouveau compte
- **Bouton "Mot de passe oublié"** : Pour récupérer l'accès

### 3. Connexion par Défaut

Pour la première utilisation :
- **Login** : `admin`
- **Mot de passe** : `1234567890`

⚠️ **Important** : Changez immédiatement ce mot de passe après la première connexion !

---

## Espace Administrateur

### Tableau de Bord Principal

L'espace administrateur offre un accès complet à la gestion de la plateforme :

#### Boutons Principaux
- **👥 Gérer les utilisateurs** : Administration des comptes
- **📰 Gérer les journaux** : Configuration des publications
- **📊 Gérer les offres** : Supervision des offres d'emploi
- **📈 Statistiques** : Tableaux de bord et indicateurs
- **📋 Rapports de recrutement** : Historique complet

### Gestion des Utilisateurs

1. **Cliquez sur "Gérer les utilisateurs"**
2. **Liste des utilisateurs** affichée avec :
   - Nom d'utilisateur
   - Rôle (Administrateur/Entreprise/Demandeur)
   - Statut (Actif/Désactivé)
3. **Actions disponibles** :
   - ✅ Activer/Désactiver un compte
   - 🔄 Réinitialiser le mot de passe
   - 🗑️ Supprimer un utilisateur

### Gestion des Journaux

1. **Cliquez sur "Gérer les journaux"**
2. **Interface de gestion** :
   - ➕ Ajouter un nouveau journal
   - ✏️ Modifier un journal existant
   - 📅 Gérer les éditions
   - 📂 Catégoriser les publications

### Statistiques et Rapports

#### Tableau de Bord
- **Nombre total d'utilisateurs** par type
- **Offres publiées** par période
- **Candidatures** par statut
- **Taux de recrutement** global

#### Rapports Détaillés
- **Historique des recrutements** : Avec détails entreprises/candidats
- **Activité des journaux** : Publications et abonnements
- **Performance des offres** : Temps de recrutement moyen

---

## Espace Entreprise

### Tableau de Bord Entreprise

L'espace entreprise est organisé en 4 onglets principaux :

#### 📝 Mes Coordonnées
- **Modifier les informations** de l'entreprise
- **Mettre à jour** les coordonnées
- **Changer le mot de passe**

#### 💼 Mes Offres
- **Liste des offres** créées
- **États des offres** : Active/Désactivée
- **Postes disponibles** : Calcul automatique

##### Créer une Nouvelle Offre
1. **Cliquez sur "Nouvelle offre"**
2. **Remplissez le formulaire** :
   - Titre de l'offre
   - Compétences requises
   - Expérience minimale (années)
   - Nombre de postes
3. **Validez** pour créer l'offre

##### Publier une Offre
1. **Sélectionnez une offre** active
2. **Cliquez sur "Publier l'offre sélectionnée"**
3. **Choisissez le journal** et l'édition
4. **Confirmez** la publication

#### 📰 Mes Abonnements
- **Liste des abonnements** actifs
- **Dates d'expiration** visibles
- **État** : Actif/Expiré

##### Souscrire un Abonnement
1. **Cliquez sur "Souscrire un abonnement"**
2. **Sélectionnez le journal** souhaité
3. **Définissez la durée** (date d'expiration)
4. **Validez** l'abonnement

#### 📋 Candidatures Reçues
- **Liste des candidatures** pour vos offres
- **Informations du candidat** : Nom, expérience, CV
- **Statut de la candidature** : En attente/Approuvée/Rejetée

##### Traitement des Candidatures
1. **Consultez le CV** du candidat
2. **Évaluez l'adéquation** avec l'offre
3. **Prenez une décision** :
   - ✅ **Approuver** : Candidat retenu pour entretien
   - ❌ **Rejeter** : Candidat non retenu
   - 🎯 **Recruter** : Finaliser l'embauche

#### 👥 Mes Recrutements
- **Historique des recrutements** effectués
- **Détails** : Candidat, offre, date de recrutement
- **Suivi** : État du processus

---

## Espace Demandeur d'Emploi

### Tableau de Bord Demandeur

L'espace demandeur est organisé en 3 onglets :

#### 👤 Mes Coordonnées
- **Modifier les informations** personnelles
- **Mettre à jour** le profil
- **Gérer le CV** : Upload et validation

#### 🔍 Offres Disponibles
- **Consultation des offres** actives
- **Filtres** : Par expérience, compétences
- **Détails complets** : Entreprise, postes, exigences

##### Processus de Candidature
1. **Naviguez vers l'onglet "Journaux & éditions"**
2. **Sélectionnez un journal** : Ex: "Journal Emploi"
3. **Choisissez une édition** : Numéro et date de parution
4. **Consultez les offres** publiées dans cette édition
5. **Sélectionnez une offre** et cliquez "Postuler"

##### Conditions de Candidature
- ✅ **Expérience suffisante** : ≥ expérience requise
- ✅ **CV valide** : Format PNG et validé
- ✅ **Première candidature** : Uniquement par offre

#### 📊 Mes Candidatures
- **Suivi en temps réel** de vos candidatures
- **Statuts visuels** : Colorés selon l'état
- **Notifications** : Pour nouvelles approbations

##### Statuts de Candidature
- 🟡 **En attente** : Candidature soumise, en cours de traitement
- 🟢 **Approuvée** : Candidat retenu pour entretien
- 🔴 **Rejetée** : Candidature non retenue
- 🔵 **Recrutée** : Candidat embauché

##### Tableau de Bord des Candidatures
```
📈 Statistiques de vos candidatures :
├── En attente : 3
├── Approuvées : 2  
├── Rejetées : 1
└── Recrutées : 0
```

#### 📰 Journaux & Éditions

##### Navigation Hiérarchique
1. **Journaux disponibles** : Liste complète des journaux
2. **Éditions du journal** : Numéros et dates de parution
3. **Offres de l'édition** : Annonces publiées

##### Exemple de Navigation
```
Journal Emploi
├── Édition #150 (15/01/2024)
│   ├── Développeur Java Senior
│   ├── Chef de Projet IT
│   └── Analyste Programmation
└── Édition #151 (01/02/2024)
    ├── Data Engineer
    └── DevOps Engineer
```

---

## Guide de Dépannage

### Problèmes Connexion

#### ❌ "Login ou mot de passe incorrect"
**Causes possibles** :
- Mot de passe erroné
- Compte désactivé
- Login incorrect

**Solutions** :
1. **Vérifiez la casse** : Admin ≠ admin
2. **Réinitialisez le mot de passe** : Contactez administrateur
3. **Vérifiez le statut** du compte

#### ❌ "Compte désactivé"
**Solution** : Contactez l'administrateur pour réactiver votre compte

### Problèmes d'Inscription

#### ❌ "Ce login existe déjà"
**Solution** : Choisissez un login différent

#### ❌ "Format de fichier non autorisé"
**Pour les CV** : Uniquement les fichiers PNG sont acceptés
**Solution** : Convertissez votre CV en PNG avant l'upload

### Problèmes de Candidature

#### ❌ "Expérience insuffisante"
**Cause** : Votre expérience < expérience requise
**Solution** : Postulez aux offres correspondant à votre niveau

#### ❌ "Vous avez déjà postulé à cette offre"
**Solution** : Une seule candidature par offre est autorisée

#### ❌ "Aucune édition disponible"
**Cause** : Aucune édition de journal configurée
**Solution** : Contactez l'administrateur

### Problèmes Techniques

#### ❌ L'application ne se lance pas
**Vérifications** :
1. **Java 17+** installé ?
   ```bash
   java -version
   ```
2. **Mémoire suffisante** : Minimum 512MB disponible
3. **Permissions** : Droits d'écriture dans le répertoire

#### ❌ Erreur de connexion à la base
**Vérifications** :
1. **MySQL en cours d'exécution**
2. **Base de données créée** : `agence_recrutement`
3. **Identifiants corrects** dans `application.properties`

### Performance

#### 🐌 Application lente
**Optimisations** :
1. **Redémarrez l'application**
2. **Nettoyez les fichiers temporaires**
3. **Vérifiez l'espace disque** disponible

#### 📱 Interface non responsive
**Conseils** :
1. **Redimensionnez** la fenêtre manuellement
2. **Utilisez la résolution** recommandée : 1920x1080
3. **Évitez** les zooms extrêmes

---

## Support Technique

### Contacts du Support

Pour toute assistance technique :
- **📧 Hiba Zouitina** : HibaZouitina@gmail.com
- **📧 Imane Taleb** : Imanetaleb@gmail.com
- **📧 Saïda Stifi** : Saida27stifi@gmail.com
- **📧 Chouaib Bouslamti** : chouaibbouslamti7@gmail.com

### Informations à Fournir

Pour une assistance efficace, fournissez :
- **Version de l'application** : 0.0.1-SNAPSHOT
- **Système d'exploitation** : Windows/Linux/macOS
- **Version de Java** : `java -version`
- **Description détaillée** du problème
- **Copie d'écran** de l'erreur (si possible)

### Ressources Additionnelles

- **📖 Documentation technique** : `docs/architecture.md`
- **📊 Rapport de projet** : `docs/rapport_projet.md`
- **🔧 Configuration** : `src/main/resources/application.properties`

---

**Version du manuel** : 1.0  
**Dernière mise à jour** : Janvier 2024  
**Compatible avec** : Application v0.0.1-SNAPSHOT
