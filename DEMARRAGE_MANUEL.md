# Guide de Démarrage Manuel du Projet

## 📋 Prérequis

Avant de démarrer, assurez-vous que :
- ✅ Java 17+ est installé
- ✅ Maven est installé
- ✅ MySQL est installé et démarré
- ✅ La base de données est configurée

---

## 🚀 Méthode 1 : Démarrage Standard (Recommandé)

### Étape 1 : Ouvrir un terminal dans le dossier du projet

```bash
cd "C:\Users\mr chou\Desktop\agencerecrutement"
```

### Étape 2 : Vérifier que MySQL est démarré (Optionnel mais recommandé)

```bash
mysql -u root -p
```

Entrez votre mot de passe MySQL, puis :

```sql
SHOW DATABASES;
EXIT;
```

### Étape 3 : Lancer l'application

```bash
mvn spring-boot:run
```

**C'est tout !** L'application va :
- Compiler automatiquement si nécessaire
- Se connecter à MySQL
- Créer les tables automatiquement
- Démarrer l'interface JavaFX

---

## 🔧 Méthode 2 : Compilation puis Exécution

### Étape 1 : Nettoyer et compiler

```bash
cd "C:\Users\mr chou\Desktop\agencerecrutement"
mvn clean compile
```

### Étape 2 : Créer le JAR (Optionnel)

```bash
mvn package
```

### Étape 3 : Exécuter le JAR

```bash
java -jar target/agencerecrutement-0.0.1-SNAPSHOT.jar
```

**OU** directement avec Maven :

```bash
mvn spring-boot:run
```

---

## 💻 Méthode 3 : Avec PowerShell

### Ouvrir PowerShell dans le dossier du projet

```powershell
cd "C:\Users\mr chou\Desktop\agencerecrutement"
```

### Lancer l'application

```powershell
mvn spring-boot:run
```

---

## 🖥️ Méthode 4 : Avec CMD (Invite de commande Windows)

### Ouvrir CMD dans le dossier du projet

```cmd
cd "C:\Users\mr chou\Desktop\agencerecrutement"
```

### Lancer l'application

```cmd
mvn spring-boot:run
```

---

## ⚙️ Configuration Avant Démarrage

### Vérifier la configuration MySQL

Ouvrez le fichier `src/main/resources/application.properties` et vérifiez :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/agence_recrutement?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=VOTRE_MOT_DE_PASSE
```

Remplacez `VOTRE_MOT_DE_PASSE` par votre mot de passe MySQL.

### Créer la base de données manuellement (Optionnel)

Si vous voulez créer la base de données manuellement avant de lancer l'application :

```bash
mysql -u root -p
```

```sql
CREATE DATABASE IF NOT EXISTS agence_recrutement;
SHOW DATABASES;
EXIT;
```

L'application peut aussi la créer automatiquement grâce à `createDatabaseIfNotExist=true`.

---

## 🔍 Vérification du Démarrage

### Messages attendus dans la console :

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.0)

2025-XX-XX ... Starting AgencerecrutementApplication ...
2025-XX-XX ... Hibernate: create table utilisateurs ...
2025-XX-XX ... Started AgencerecrutementApplication in X.XXX seconds
```

### Fenêtre JavaFX attendue :

Une fenêtre devrait s'afficher avec :
- Titre : "Agence de Recrutement - Connexion"
- Champs : Login et Mot de passe
- Boutons : "Se connecter" et "S'inscrire"

---

## 🛑 Arrêter l'Application

### Méthode 1 : Fermer la fenêtre JavaFX
- Fermez simplement la fenêtre de l'application

### Méthode 2 : Utiliser Ctrl+C
- Dans le terminal, appuyez sur `Ctrl + C`
- Confirmez l'arrêt si demandé

### Méthode 3 : Tuer le processus Java

**Windows PowerShell :**
```powershell
Get-Process -Name java | Stop-Process
```

**Windows CMD :**
```cmd
taskkill /F /IM java.exe
```

---

## 🐛 Résolution de Problèmes

### Erreur : "MySQL connection refused"

**Solution :**
1. Vérifier que MySQL est démarré :
   ```bash
   netstat -ano | findstr :3306
   ```
   Si rien n'apparaît, MySQL n'est pas démarré.

2. Démarrer MySQL :
   - Via XAMPP : Ouvrir XAMPP Control Panel → Start MySQL
   - Via Service Windows : `net start MySQL80` (en tant qu'administrateur)
   - Ou redémarrer votre ordinateur

### Erreur : "Access denied for user 'root'@'localhost'"

**Solution :**
- Vérifier le mot de passe dans `application.properties`
- Vérifier que l'utilisateur root existe :
  ```bash
  mysql -u root -p
  ```

### Erreur : "Port 3306 already in use"

**Solution :**
1. Identifier le processus utilisant le port :
   ```bash
   netstat -ano | findstr :3306
   ```
2. Tuer le processus si nécessaire (attention, cela arrêtera MySQL)

### Erreur : "JavaFX runtime components are missing"

**Solution :**
- Vérifier que JavaFX est dans le pom.xml
- Recompiler : `mvn clean compile`
- Vérifier la version de Java (doit être 17+)

### Erreur : "Maven not found"

**Solution :**
- Vérifier que Maven est installé : `mvn --version`
- Si non installé, téléchargez depuis : https://maven.apache.org/download.cgi
- Ajoutez Maven au PATH système

---

## 📝 Commandes Utiles

### Vérifier la version de Java
```bash
java -version
```

### Vérifier la version de Maven
```bash
mvn --version
```

### Vérifier que MySQL fonctionne
```bash
mysql -u root -p
```

### Nettoyer le projet
```bash
mvn clean
```

### Compiler sans lancer
```bash
mvn compile
```

### Voir les logs en détail
```bash
mvn spring-boot:run -X
```

### Lancer avec un profil spécifique
```bash
mvn spring-boot:run -Dspring.profiles.active=dev
```

### Vérifier les processus Java en cours
```bash
jps
```

### Voir les ports utilisés
```bash
netstat -ano | findstr :3306
```

---

## 🎯 Démarrage Rapide (Résumé)

```bash
# 1. Aller dans le dossier du projet
cd "C:\Users\mr chou\Desktop\agencerecrutement"

# 2. Vérifier MySQL (optionnel)
mysql -u root -p

# 3. Lancer l'application
mvn spring-boot:run
```

**C'est tout !** La fenêtre JavaFX devrait s'afficher dans quelques secondes.

---

## 📚 Commandes Additionnelles

### Créer un JAR exécutable
```bash
mvn clean package
```

Le JAR sera créé dans : `target/agencerecrutement-0.0.1-SNAPSHOT.jar`

### Exécuter le JAR
```bash
java -jar target/agencerecrutement-0.0.1-SNAPSHOT.jar
```

### Compiler avec les tests
```bash
mvn clean install
```

### Compiler sans les tests
```bash
mvn clean install -DskipTests
```

### Voir toutes les dépendances
```bash
mvn dependency:tree
```

---

## 🔐 Compte Administrateur par Défaut

Une fois l'application démarrée, vous pouvez vous connecter avec :

- **Login :** `admin`
- **Password :** `admin`

⚠️ **Important :** Changez ce mot de passe après la première connexion en production !

---

## 📞 Support

Si vous rencontrez des problèmes :

1. Vérifiez les logs dans la console
2. Vérifiez que tous les prérequis sont installés
3. Consultez les fichiers de documentation :
   - `INSTALL_MYSQL.md` pour l'installation de MySQL
   - `TEST_PROJET.md` pour les tests
   - `README.md` pour la documentation générale

---

## ✅ Checklist de Démarrage

- [ ] Java 17+ installé
- [ ] Maven installé
- [ ] MySQL installé et démarré
- [ ] Base de données configurée dans `application.properties`
- [ ] Terminal ouvert dans le dossier du projet
- [ ] Commande `mvn spring-boot:run` exécutée
- [ ] Fenêtre JavaFX affichée
- [ ] Pas d'erreurs dans la console

