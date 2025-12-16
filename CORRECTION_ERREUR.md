# Correction de l'Erreur Spring Security

## 🔴 Problème Rencontré

```
Caused by: java.lang.ClassNotFoundException: jakarta.servlet.Filter
```

### Cause
Spring Security Web tente de charger des classes servlet qui n'existent pas dans une application JavaFX (qui n'est pas une application web).

## ✅ Solution Appliquée

### 1. Suppression de la dépendance Web Security

**Avant :**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

**Après :** Supprimée (cette dépendance inclut la configuration web)

### 2. Simplification de SecurityConfig

**Avant :**
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configuration web non nécessaire pour JavaFX
    }
}
```

**Après :**
```java
@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

### 3. Conservation de spring-security-crypto

Cette dépendance est conservée car elle est nécessaire pour BCrypt (hachage des mots de passe) sans les dépendances web :

```xml
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-crypto</artifactId>
</dependency>
```

## 📝 Résultat

- ✅ Compilation réussie
- ✅ Plus d'erreur de ClassNotFoundException
- ✅ BCrypt fonctionne toujours pour hasher les mots de passe
- ✅ Application JavaFX peut démarrer sans problèmes

## 🎯 Pourquoi cette Solution ?

Dans une application JavaFX :
- On n'a pas besoin de Spring Security Web (qui gère les filtres HTTP)
- On a seulement besoin de BCrypt pour hasher les mots de passe
- `spring-security-crypto` fournit BCrypt sans les dépendances servlet

L'authentification est gérée manuellement dans `AuthentificationService` via la vérification du mot de passe hashé, ce qui est suffisant pour une application desktop.

