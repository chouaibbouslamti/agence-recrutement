# Documentation Architecture Technique

## Vue d'Ensemble

L'application d'agence de recrutement suit une **architecture en couches classique** avec une particularité : l'intégration de Spring Boot (backend) avec JavaFX (interface desktop).

## 1. Architecture Globale

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION                       │
│                   (JavaFX 21)                         │
│  ┌─────────────────┬─────────────────┬─────────────────┐ │
│  │   Admin UI     │  Entreprise UI  │ Demandeur UI    │ │
│  │   Controller    │   Controller    │   Controller    │ │
│  └─────────────────┴─────────────────┴─────────────────┘ │
├─────────────────────────────────────────────────────────────┤
│                    BUSINESS LOGIC                        │
│                 (Spring Services)                       │
│  ┌─────────────────┬─────────────────┬─────────────────┐ │
│  │ Authentification│  Offre Service  │Candidature Svc │ │
│  │    Service     │                 │                 │ │
│  └─────────────────┴─────────────────┴─────────────────┘ │
├─────────────────────────────────────────────────────────────┤
│                    DATA ACCESS                           │
│              (Spring Data JPA)                         │
│  ┌─────────────────┬─────────────────┬─────────────────┐ │
│  │ User Repository │ Offre Repository│Candidature Repo │ │
│  └─────────────────┴─────────────────┴─────────────────┘ │
├─────────────────────────────────────────────────────────────┤
│                    DATA STORAGE                          │
│                  (MySQL 8.0+)                          │
└─────────────────────────────────────────────────────────────┘
```

## 2. Patterns Architecturaux

### 2.1 MVC (Model-View-Controller)

#### Model Layer
```java
@Entity
public class Offre {
    @Id
    private Long idOffre;
    private String titre;
    private Integer experienceRequise;
    
    @ManyToOne
    private Entreprise entreprise;
}
```

#### View Layer (JavaFX)
```java
public class MainController {
    private TableView<Offre> offreTable;
    private TableColumn<Offre, String> titreCol;
    
    private void createOffreTable() {
        titreCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
    }
}
```

#### Controller Layer
```java
@Service
public class OffreService {
    public List<Offre> getOffresByEntreprise(Long idEntreprise) {
        return offreRepository.findByEntrepriseIdUtilisateur(idEntreprise);
    }
}
```

### 2.2 Repository Pattern

```java
@Repository
public interface OffreRepository extends JpaRepository<Offre, Long> {
    List<Offre> findByEntrepriseIdUtilisateur(Long idEntreprise);
    List<Offre> findByEtat(EtatOffre etat);
    
    @Query("SELECT o FROM Offre o JOIN FETCH o.entreprise WHERE o.idOffre = :id")
    Optional<Offre> findByIdWithEntreprise(@Param("id") Long id);
}
```

### 2.3 Service Layer Pattern

```java
@Service
@Transactional
public class CandidatureService {
    
    private final CandidatureRepository candidatureRepository;
    private final OffreRepository offreRepository;
    
    public Candidature postuler(Long idDemandeur, Long idOffre, Long idEdition) {
        // Validation métier
        // Coordination des repositories
        // Gestion transactionnelle
    }
}
```

## 3. Intégration Spring Boot + JavaFX

### 3.1 Point d'Entrée Hybride

```java
// Application Spring Boot
@SpringBootApplication
public class AgencerecrutementApplication {
    public static void main(String[] args) {
        Application.launch(AgenceRecrutementApp.class, args);
    }
}

// Application JavaFX
public class AgenceRecrutementApp extends JavafxApplication {
    @Override
    public void start(Stage primaryStage) throws Exception {
        LoginController loginController = applicationContext.getBean(LoginController.class);
        // Configuration UI avec injection Spring
    }
}
```

### 3.2 Classe Abstraite d'Intégration

```java
public abstract class JavafxApplication extends Application {
    protected ConfigurableApplicationContext applicationContext;
    
    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);
        this.applicationContext = new SpringApplicationBuilder()
            .sources(AgencerecrutementApplication.class)
            .run(args);
    }
}
```

## 4. Architecture de Persistance

### 4.1 Stratégie d'Héritage

```java
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_utilisateur", discriminatorType = DiscriminatorType.STRING)
public abstract class Utilisateur {
    @Id
    private Long idUtilisateur;
    private String login;
    private String motDePasse;
    
    @Enumerated(EnumType.STRING)
    private Role role;
}

@Entity
@DiscriminatorValue("ENTREPRISE")
public class Entreprise extends Utilisateur {
    private String raisonSociale;
    private String adresse;
}

@Entity
@DiscriminatorValue("DEMANDEUR_EMPLOI")
public class DemandeurEmploi extends Utilisateur {
    private String nom;
    private String prenom;
    private Integer experience;
}
```

### 4.2 Relations JPA

```java
@Entity
public class Offre {
    @OneToMany(mappedBy = "offre", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Candidature> candidatures = new ArrayList<>();
    
    @OneToMany(mappedBy = "offre", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recrutement> recrutements = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_entreprise")
    private Entreprise entreprise;
}
```

## 5. Architecture de Sécurité

### 5.1 Hashage des Mots de Passe

```java
@Service
public class AuthentificationService {
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    public String hasherMotDePasse(String motDePasse) {
        return passwordEncoder.encode(motDePasse);
    }
    
    public boolean verifierMotDePasse(String motDePasse, String hash) {
        return passwordEncoder.matches(motDePasse, hash);
    }
}
```

### 5.2 Contrôle d'Accès par Rôle

```java
@Component
public class MainController {
    private void updateView() {
        switch (utilisateurConnecte.getRole()) {
            case ADMINISTRATEUR:
                content.getChildren().add(createAdminPane());
                break;
            case ENTREPRISE:
                content.getChildren().add(createEntreprisePane());
                break;
            case DEMANDEUR_EMPLOI:
                content.getChildren().add(createDemandeurPane());
                break;
        }
    }
}
```

## 6. Architecture des Flux

### 6.1 Flux d'Authentification

```
User Input → LoginController → AuthentificationService → UserRepository → MySQL
     ↓              ↓                    ↓                    ↓         ↓
  Credentials   Validation         BCrypt Check        Query    Response
     ↓              ↓                    ↓                    ↓         ↓
   Success    Session Start        User Object        Fetch    User Data
```

### 6.2 Flux de Candidature

```
Demandeur → CandidatureDialog → CandidatureService → Multiple Repositories → MySQL
    ↓              ↓                    ↓                    ↓         ↓
 Form Fill    UI Validation      Business Rules        Queries   Transaction
    ↓              ↓                    ↓                    ↓         ↓
 Submit      Dialog Close       Candidature Creation   Inserts   New Record
```

## 7. Architecture de Performance

### 7.1 Stratégies de Cache

```java
// Lazy Loading pour optimiser la mémoire
@ManyToOne(fetch = FetchType.LAZY)
private Entreprise entreprise;

// Gestion manuelle du lazy loading
public int getNbPostesDisponibles() {
    if (recrutements == null || !Hibernate.isInitialized(recrutements)) {
        return nbPostes != null ? nbPostes : 0;
    }
    return nbPostes - recrutements.size();
}
```

### 7.2 Requêtes Optimisées

```java
// Éviter les N+1 queries
@Query("SELECT c FROM Candidature c " +
       "JOIN FETCH c.offre o " +
       "JOIN FETCH o.entreprise e " +
       "WHERE c.demandeur.idUtilisateur = :idDemandeur")
List<Candidature> findByDemandeurIdUtilisateurWithRelations(@Param("idDemandeur") Long idDemandeur);
```

## 8. Architecture de Configuration

### 8.1 Configuration Spring

```java
@Configuration
public class SecurityConfig {
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSize(10485760); // 10MB
        return resolver;
    }
}
```

### 8.2 Configuration Externe

```properties
# application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/agence_recrutement
spring.datasource.username=root
spring.datasource.password=platformerecrut

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

app.upload.dir=./uploads
app.max.file.size=10485760
```

## 9. Architecture des Tests

### 9.1 Structure de Tests

```
src/test/java/
└── com/example/agencerecrutement/
    ├── service/
    │   ├── AuthentificationServiceTest.java
    │   ├── CandidatureServiceTest.java
    │   └── OffreServiceTest.java
    ├── repository/
    │   ├── UtilisateurRepositoryTest.java
    │   └── OffreRepositoryTest.java
    └── integration/
        ├── CandidatureIntegrationTest.java
        └── AuthentificationIntegrationTest.java
```

### 9.2 Configuration de Tests

```java
@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class CandidatureServiceTest {
    
    @Autowired
    private CandidatureService candidatureService;
    
    @MockBean
    private EmailService emailService;
}
```

## 10. Architecture de Déploiement

### 10.1 Structure de Build

```
target/
├── classes/              # Classes compilées
├── test-classes/         # Classes de test
├── agencerecrutement-0.0.1-SNAPSHOT.jar  # JAR exécutable
└── lib/                  # Dépendances
```

### 10.2 Configuration de Packaging

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <mainClass>com.example.agencerecrutement.AgenceRecrutementApp</mainClass>
            </configuration>
        </plugin>
    </plugins>
</build>
```

## 11. Bonnes Pratiques Architecturales

### 11.1 Principes SOLID

- **S**ingle Responsibility : Chaque classe a une seule responsabilité
- **O**pen/Closed : Ouvert à l'extension, fermé à la modification
- **L**iskov Substitution : Les sous-classes peuvent remplacer leurs parents
- **I**nterface Segregation : Interfaces spécifiques et cohérentes
- **D**ependency Inversion : Dépendance aux abstractions, pas aux implémentations

### 11.2 Patterns Utilisés

- **Repository Pattern** : Abstraction de l'accès aux données
- **Service Layer Pattern** : Encapsulation de la logique métier
- **DAO Pattern** (via Spring Data) : Mapping objet-relationnel
- **Observer Pattern** (JavaFX) : Réactivité de l'interface
- **Factory Pattern** : Création des boîtes de dialogue

### 11.3 Principes de Conception

- **Convention over Configuration** : Spring Boot
- **Dependency Injection** : Injection via constructeur
- **Transaction Management** : @Transactional
- **Separation of Concerns** : Couches distinctes
- **Don't Repeat Yourself** : Réutilisation des composants

---

Cette architecture assure une base solide, maintenable et évolutive pour l'application d'agence de recrutement.
