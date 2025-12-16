# Guide d'installation de MySQL pour Windows

## Option 1 : Installation via MySQL Installer (Recommandé)

### Étape 1 : Télécharger MySQL Installer

1. Allez sur le site officiel MySQL : https://dev.mysql.com/downloads/installer/
2. Téléchargez **MySQL Installer for Windows** (choisissez le fichier web ou le fichier complet)
3. Le fichier s'appelle généralement `mysql-installer-community-*.msi`

### Étape 2 : Exécuter l'installateur

1. Double-cliquez sur le fichier `.msi` téléchargé
2. Acceptez les termes de licence
3. Choisissez **"Developer Default"** (par défaut) ou **"Server only"** (juste le serveur)
4. Cliquez sur "Next"

### Étape 3 : Installation des prérequis

1. L'installateur vérifie les prérequis (Visual C++ Redistributables, etc.)
2. Si certains sont manquants, cliquez sur "Execute" pour les installer
3. Une fois tous les prérequis installés, cliquez sur "Next"

### Étape 4 : Installation de MySQL

1. Cliquez sur "Execute" pour installer MySQL Server
2. Attendez que l'installation soit terminée
3. Cliquez sur "Next"

### Étape 5 : Configuration du serveur

1. **Type de configuration** : Choisissez "Standalone MySQL Server / Classic MySQL Replication"
2. **Méthode de connexion** : Utilisez "Use Strong Password Encryption"
3. **Mot de passe root** : 
   - Créez un mot de passe fort (notez-le bien !)
   - Ou laissez-le vide si vous préférez (déconseillé en production)
4. **Compte Windows** : Vous pouvez créer un compte Windows pour MySQL (optionnel)
5. Cliquez sur "Next"

### Étape 6 : Finaliser l'installation

1. L'installation se termine
2. Cliquez sur "Finish"
3. MySQL Server est maintenant installé et démarré

---

## Option 2 : Installation via XAMPP (Plus simple pour débuter)

### Avantages de XAMPP :
- Installation plus simple
- Inclut MySQL, PHP, Apache, phpMyAdmin
- Idéal pour le développement

### Étapes :

1. Téléchargez XAMPP depuis : https://www.apachefriends.org/
2. Installez XAMPP (exécutez le fichier `.exe`)
3. Lors de l'installation :
   - Choisissez MySQL dans les composants à installer
   - Choisissez un répertoire d'installation (par défaut : `C:\xampp`)
4. Une fois installé :
   - Ouvrez le **XAMPP Control Panel**
   - Cliquez sur "Start" pour MySQL
  5. MySQL sera accessible sur le port 3306

---

## Option 3 : Installation via Chocolatey (pour utilisateurs avancés)

Si vous avez Chocolatey installé :

```powershell
choco install mysql
```

---

## Vérification de l'installation

### Méthode 1 : Via la ligne de commande

Ouvrez PowerShell ou l'invite de commande et exécutez :

```bash
mysql --version
```

Vous devriez voir quelque chose comme : `mysql Ver 8.0.xx for Win64`

### Méthode 2 : Vérifier le service Windows

1. Appuyez sur `Win + R`
2. Tapez `services.msc` et appuyez sur Entrée
3. Cherchez "MySQL80" ou "MySQL" dans la liste
4. Le statut doit être "En cours d'exécution"

### Méthode 3 : Se connecter à MySQL

```bash
mysql -u root -p
```

Entrez le mot de passe root que vous avez créé. Vous devriez voir :

```
mysql>
```

---

## Configuration pour le projet

### Étape 1 : Créer la base de données

Connectez-vous à MySQL :

```bash
mysql -u root -p
```

Puis exécutez :

```sql
CREATE DATABASE agence_recrutement;
SHOW DATABASES;
EXIT;
```

### Étape 2 : Configurer application.properties

Ouvrez le fichier `src/main/resources/application.properties` et modifiez :

```properties
# Base de données MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/agence_recrutement?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=VOTRE_MOT_DE_PASSE_ICI
```

Remplacez `VOTRE_MOT_DE_PASSE_ICI` par le mot de passe root que vous avez créé.

### Étape 3 : Tester la connexion

Lancez l'application :

```bash
mvn spring-boot:run
```

Si tout fonctionne, vous verrez que Spring Boot a créé automatiquement les tables dans la base de données.

---

## Outils recommandés

### MySQL Workbench (Interface graphique)

1. Téléchargez depuis : https://dev.mysql.com/downloads/workbench/
2. Installez MySQL Workbench
3. Créez une nouvelle connexion :
   - Hostname : `localhost`
   - Port : `3306`
   - Username : `root`
   - Password : votre mot de passe
4. Connectez-vous et vous pourrez gérer votre base de données visuellement

### phpMyAdmin (via XAMPP)

Si vous avez installé XAMPP :
1. Démarrez Apache et MySQL dans XAMPP Control Panel
2. Allez sur : http://localhost/phpmyadmin
3. Connectez-vous avec :
   - Username : `root`
   - Password : (laissez vide par défaut avec XAMPP)

---

## Résolution des problèmes courants

### MySQL ne démarre pas

1. Vérifiez que le port 3306 n'est pas déjà utilisé :
   ```bash
   netstat -ano | findstr :3306
   ```
2. Vérifiez les logs MySQL dans le dossier d'installation
3. Redémarrez le service MySQL dans services.msc

### Mot de passe oublié

1. Arrêtez le service MySQL
2. Créez un fichier texte avec :
   ```
   ALTER USER 'root'@'localhost' IDENTIFIED BY 'nouveau_mot_de_passe';
   ```
3. Démarrez MySQL avec `--init-file` pointant vers ce fichier
4. Ou réinstallez MySQL

### Erreur "Access denied"

- Vérifiez que le nom d'utilisateur et le mot de passe sont corrects
- Essayez de vous connecter sans mot de passe : `mysql -u root` (si aucun mot de passe n'a été défini)

---

## Commandes MySQL utiles

```sql
-- Se connecter
mysql -u root -p

-- Lister les bases de données
SHOW DATABASES;

-- Utiliser une base de données
USE agence_recrutement;

-- Lister les tables
SHOW TABLES;

-- Voir la structure d'une table
DESCRIBE nom_table;

-- Quitter MySQL
EXIT;
```

---

## Configuration recommandée pour le développement

Pour faciliter le développement, vous pouvez :

1. **Créer un utilisateur dédié** (au lieu d'utiliser root) :
```sql
CREATE USER 'agence_user'@'localhost' IDENTIFIED BY 'password123';
GRANT ALL PRIVILEGES ON agence_recrutement.* TO 'agence_user'@'localhost';
FLUSH PRIVILEGES;
```

2. **Utiliser ce nouvel utilisateur** dans `application.properties` :
```properties
spring.datasource.username=agence_user
spring.datasource.password=password123
```

---

## Support

- Documentation MySQL : https://dev.mysql.com/doc/
- Forum MySQL : https://forums.mysql.com/
- Stack Overflow : Tag [mysql] et [mysql-windows]

