package com.example.agencerecrutement.javafx.controllers;

import com.example.agencerecrutement.javafx.dialogs.*;
import com.example.agencerecrutement.model.*;
import com.example.agencerecrutement.repository.*;
import com.example.agencerecrutement.service.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MainController {
    
    private final EntrepriseService entrepriseService;
    private final OffreService offreService;
    private final CandidatureService candidatureService;
    private final RecrutementService recrutementService;
    private final JournalService journalService;
    private final DemandeurEmploiService demandeurEmploiService;
    private final UtilisateurRepository utilisateurRepository;
    private final EntrepriseRepository entrepriseRepository;
    private final DemandeurEmploiRepository demandeurEmploiRepository;
    private final JournalRepository journalRepository;
    private final OffreRepository offreRepository;
    private final CandidatureRepository candidatureRepository;
    private final RecrutementRepository recrutementRepository;
    private final AbonnementRepository abonnementRepository;
    
    private ConfigurableApplicationContext applicationContext;
    private Utilisateur utilisateurConnecte;
    private BorderPane root;
    
    public MainController(EntrepriseService entrepriseService, OffreService offreService,
                         CandidatureService candidatureService, RecrutementService recrutementService,
                         JournalService journalService, DemandeurEmploiService demandeurEmploiService,
                         UtilisateurRepository utilisateurRepository, EntrepriseRepository entrepriseRepository,
                         DemandeurEmploiRepository demandeurEmploiRepository, JournalRepository journalRepository,
                         OffreRepository offreRepository, CandidatureRepository candidatureRepository,
                         RecrutementRepository recrutementRepository, AbonnementRepository abonnementRepository) {
        this.entrepriseService = entrepriseService;
        this.offreService = offreService;
        this.candidatureService = candidatureService;
        this.recrutementService = recrutementService;
        this.journalService = journalService;
        this.demandeurEmploiService = demandeurEmploiService;
        this.utilisateurRepository = utilisateurRepository;
        this.entrepriseRepository = entrepriseRepository;
        this.demandeurEmploiRepository = demandeurEmploiRepository;
        this.journalRepository = journalRepository;
        this.offreRepository = offreRepository;
        this.candidatureRepository = candidatureRepository;
        this.recrutementRepository = recrutementRepository;
        this.abonnementRepository = abonnementRepository;
        initializeView();
    }
    
    public void setApplicationContext(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    
    public void setUtilisateurConnecte(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
        updateView();
    }
    
    private void initializeView() {
        root = new BorderPane();
        root.setPadding(new Insets(10));
    }
    
    private void updateView() {
        if (utilisateurConnecte == null) return;
        
        // Créer un nouveau root pour éviter l'erreur "setted for another root"
        root = new BorderPane();
        root.setPadding(new Insets(10));
        
        // Créer le menu selon le rôle
        MenuBar menuBar = createMenuBar();
        root.setTop(menuBar);
        
        // Créer le contenu principal selon le rôle
        Pane content = createContentPane();
        root.setCenter(content);
    }
    
    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu menuFichier = new Menu("Fichier");
        MenuItem itemDeconnexion = new MenuItem("Déconnexion");
        itemDeconnexion.setOnAction(e -> {
            // Toujours chercher le stage dans toutes les fenêtres pour plus de fiabilité
            Stage stage = null;
            for (javafx.stage.Window window : javafx.stage.Window.getWindows()) {
                if (window instanceof Stage && window.isShowing()) {
                    stage = (Stage) window;
                    break;
                }
            }
            handleDeconnexion(stage);
        });
        menuFichier.getItems().add(itemDeconnexion);
        menuBar.getMenus().add(menuFichier);
        
        // Ajouter des menus selon le rôle
        if (utilisateurConnecte.getRole() == Utilisateur.Role.ADMINISTRATEUR) {
            Menu menuAdmin = new Menu("Administration");
            MenuItem itemGestionUsers = new MenuItem("Gérer les utilisateurs");
            MenuItem itemGestionJournaux = new MenuItem("Gérer les journaux");
            MenuItem itemStats = new MenuItem("Statistiques");
            
            // Ajouter les actions pour les boutons d'administration
            itemGestionUsers.setOnAction(e -> showGestionUtilisateurs());
            itemGestionJournaux.setOnAction(e -> showGestionJournaux());
            itemStats.setOnAction(e -> showStatistiques());
            
            menuAdmin.getItems().addAll(itemGestionUsers, itemGestionJournaux, itemStats);
            menuBar.getMenus().add(menuAdmin);
        }
        
        return menuBar;
    }
    
    private Pane createContentPane() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        
        // Header avec bienvenue et bouton déconnexion
        HBox headerBox = new HBox();
        headerBox.setSpacing(20);
        headerBox.setAlignment(javafx.geometry.Pos.CENTER);
        headerBox.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10px; -fx-border-radius: 5px;");
        
        Label welcomeLabel = new Label("Bienvenue, " + utilisateurConnecte.getLogin());
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        Button logoutButton = new Button("Déconnexion");
        logoutButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px;");
        logoutButton.setOnAction(e -> {
            // Toujours chercher le stage dans toutes les fenêtres pour plus de fiabilité
            Stage stage = null;
            for (javafx.stage.Window window : javafx.stage.Window.getWindows()) {
                if (window instanceof Stage && window.isShowing()) {
                    stage = (Stage) window;
                    break;
                }
            }
            handleDeconnexion(stage);
        });
        
        headerBox.getChildren().addAll(welcomeLabel, logoutButton);
        content.getChildren().add(headerBox);
        
        // Contenu spécifique selon le rôle
        if (utilisateurConnecte.getRole() == Utilisateur.Role.ENTREPRISE) {
            content.getChildren().add(createEntreprisePane());
        } else if (utilisateurConnecte.getRole() == Utilisateur.Role.DEMANDEUR_EMPLOI) {
            content.getChildren().add(createDemandeurPane());
        } else if (utilisateurConnecte.getRole() == Utilisateur.Role.ADMINISTRATEUR) {
            content.getChildren().add(createAdminPane());
        }
        
        return content;
    }
    
    private Pane createEntreprisePane() {
        VBox pane = new VBox(15);
        pane.setPadding(new Insets(20));
        
        Label title = new Label("Espace Entreprise");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        TabPane tabPane = new TabPane();
        
        Tab tabOffres = new Tab("Mes Offres");
        tabOffres.setContent(createOffresTab());
        tabOffres.setClosable(false);
        
        Tab tabAbonnements = new Tab("Mes Abonnements");
        tabAbonnements.setContent(createAbonnementsTab());
        tabAbonnements.setClosable(false);
        
        Tab tabCandidatures = new Tab("Candidatures");
        tabCandidatures.setContent(createCandidaturesTab());
        tabCandidatures.setClosable(false);
        
        Tab tabRecrutements = new Tab("Recrutements");
        tabRecrutements.setContent(createRecrutementsTab());
        tabRecrutements.setClosable(false);
        
        tabPane.getTabs().addAll(tabOffres, tabAbonnements, tabCandidatures, tabRecrutements);
        
        pane.getChildren().addAll(title, tabPane);
        return pane;
    }
    
    private Pane createDemandeurPane() {
        VBox pane = new VBox(15);
        pane.setPadding(new Insets(20));
        
        Label title = new Label("Espace Demandeur d'Emploi");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        TabPane tabPane = new TabPane();
        
        Tab tabOffres = new Tab("Offres disponibles");
        tabOffres.setContent(createOffresDisponiblesTab());
        tabOffres.setClosable(false);
        
        Tab tabMesCandidatures = new Tab("Mes candidatures");
        tabMesCandidatures.setContent(createMesCandidaturesTab());
        tabMesCandidatures.setClosable(false);
        
        tabPane.getTabs().addAll(tabOffres, tabMesCandidatures);
        
        pane.getChildren().addAll(title, tabPane);
        return pane;
    }
    
    private Pane createAdminPane() {
        VBox pane = new VBox(15);
        pane.setPadding(new Insets(20));
        
        Label title = new Label("Espace Administrateur");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        Label infoLabel = new Label("Interface administrateur - Fonctionnalités à venir");
        pane.getChildren().addAll(title, infoLabel);
        
        return pane;
    }
    
    private Pane createOffresTab() {
        VBox pane = new VBox(10);
        
        Button btnNouvelleOffre = new Button("Nouvelle offre");
        btnNouvelleOffre.setOnAction(e -> showNouvelleOffreDialog());
        
        // TableView pour afficher les offres
        TableView<Offre> tableView = new TableView<>();
        
        TableColumn<Offre, String> titreCol = new TableColumn<>("Titre");
        titreCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
        
        TableColumn<Offre, Integer> expCol = new TableColumn<>("Expérience requise");
        expCol.setCellValueFactory(new PropertyValueFactory<>("experienceRequise"));
        
        TableColumn<Offre, Integer> postesCol = new TableColumn<>("Postes disponibles");
        postesCol.setCellValueFactory(param -> 
            new javafx.beans.property.SimpleIntegerProperty(param.getValue().getNbPostesDisponibles()).asObject());
        
        TableColumn<Offre, String> etatCol = new TableColumn<>("État");
        etatCol.setCellValueFactory(param -> 
            new javafx.beans.property.SimpleStringProperty(
                param.getValue().estActive() ? "Active" : "Désactivée"));
        
        tableView.getColumns().addAll(titreCol, expCol, postesCol, etatCol);
        
        try {
            Entreprise entreprise = (Entreprise) utilisateurConnecte;
            tableView.getItems().addAll(offreService.getOffresByEntreprise(entreprise.getIdUtilisateur()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        pane.getChildren().addAll(btnNouvelleOffre, tableView);
        return pane;
    }
    
    private Pane createAbonnementsTab() {
        VBox pane = new VBox(10);
        
        Button btnNouvelAbonnement = new Button("Souscrire un abonnement");
        btnNouvelAbonnement.setOnAction(e -> showNouvelAbonnementDialog());
        
        TableView<Abonnement> tableView = new TableView<>();
        
        TableColumn<Abonnement, String> journalCol = new TableColumn<>("Journal");
        journalCol.setCellValueFactory(param -> 
            new javafx.beans.property.SimpleStringProperty(param.getValue().getJournal().getNom()));
        
        TableColumn<Abonnement, String> etatCol = new TableColumn<>("État");
        etatCol.setCellValueFactory(param -> 
            new javafx.beans.property.SimpleStringProperty(param.getValue().getEtat().toString()));
        
        TableColumn<Abonnement, String> expirationCol = new TableColumn<>("Date expiration");
        expirationCol.setCellValueFactory(param -> 
            new javafx.beans.property.SimpleStringProperty(param.getValue().getDateExpiration().toString()));
        
        tableView.getColumns().addAll(journalCol, etatCol, expirationCol);
        
        try {
            Entreprise entreprise = (Entreprise) utilisateurConnecte;
            tableView.getItems().addAll(entrepriseService.getAbonnements(entreprise.getIdUtilisateur()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        pane.getChildren().addAll(btnNouvelAbonnement, tableView);
        return pane;
    }
    
    private Pane createCandidaturesTab() {
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(10));
        
        try {
            Entreprise entreprise = (Entreprise) utilisateurConnecte;
            
            // TableView pour afficher les candidatures
            TableView<Candidature> tableView = new TableView<>();
            
            TableColumn<Candidature, String> demandeurCol = new TableColumn<>("Demandeur");
            demandeurCol.setCellValueFactory(param -> {
                DemandeurEmploi d = param.getValue().getDemandeur();
                return new javafx.beans.property.SimpleStringProperty(d.getNom() + " " + d.getPrenom());
            });
            
            TableColumn<Candidature, String> offreCol = new TableColumn<>("Offre");
            offreCol.setCellValueFactory(param -> 
                new javafx.beans.property.SimpleStringProperty(param.getValue().getOffre().getTitre()));
            
            TableColumn<Candidature, String> dateCol = new TableColumn<>("Date");
            dateCol.setCellValueFactory(param -> 
                new javafx.beans.property.SimpleStringProperty(param.getValue().getDateCandidature().toString()));
            
            TableColumn<Candidature, Integer> expCol = new TableColumn<>("Expérience");
            expCol.setCellValueFactory(param -> 
                new javafx.beans.property.SimpleIntegerProperty(param.getValue().getDemandeur().getExperience()).asObject());
            
            tableView.getColumns().addAll(demandeurCol, offreCol, dateCol, expCol);
            
            // Charger les candidatures de toutes les offres de l'entreprise
            offreService.getOffresByEntreprise(entreprise.getIdUtilisateur())
                .forEach(offre -> tableView.getItems().addAll(candidatureService.getCandidaturesByOffre(offre.getIdOffre())));
            
            // Bouton pour recruter
            Button btnRecruter = new Button("Recruter le candidat sélectionné");
            btnRecruter.setOnAction(e -> {
                Candidature selected = tableView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    handleRecrutement(selected);
                    tableView.refresh();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Sélection");
                    alert.setHeaderText("Aucune candidature sélectionnée");
                    alert.setContentText("Veuillez sélectionner une candidature pour recruter");
                    alert.showAndWait();
                }
            });
            
            pane.getChildren().addAll(new Label("Candidatures reçues :"), tableView, btnRecruter);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            pane.getChildren().add(new Label("Erreur : " + ex.getMessage()));
        }
        
        return pane;
    }
    
    private Pane createRecrutementsTab() {
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(10));
        
        try {
            Entreprise entreprise = (Entreprise) utilisateurConnecte;
            
            TableView<Recrutement> tableView = new TableView<>();
            
            TableColumn<Recrutement, String> demandeurCol = new TableColumn<>("Demandeur");
            demandeurCol.setCellValueFactory(param -> {
                DemandeurEmploi d = param.getValue().getDemandeur();
                return new javafx.beans.property.SimpleStringProperty(d.getNom() + " " + d.getPrenom());
            });
            
            TableColumn<Recrutement, String> offreCol = new TableColumn<>("Offre");
            offreCol.setCellValueFactory(param -> 
                new javafx.beans.property.SimpleStringProperty(param.getValue().getOffre().getTitre()));
            
            TableColumn<Recrutement, String> dateCol = new TableColumn<>("Date");
            dateCol.setCellValueFactory(param -> 
                new javafx.beans.property.SimpleStringProperty(param.getValue().getDateRecrutement().toString()));
            
            tableView.getColumns().addAll(demandeurCol, offreCol, dateCol);
            
            tableView.getItems().addAll(recrutementService.getRecrutementsByEntreprise(entreprise.getIdUtilisateur()));
            
            pane.getChildren().addAll(new Label("Historique des recrutements :"), tableView);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            pane.getChildren().add(new Label("Erreur : " + ex.getMessage()));
        }
        
        return pane;
    }
    
    private Pane createOffresDisponiblesTab() {
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(10));
        
        try {
            DemandeurEmploi demandeur = (DemandeurEmploi) utilisateurConnecte;
            
            TableView<Offre> tableView = new TableView<>();
            
            TableColumn<Offre, String> titreCol = new TableColumn<>("Titre");
            titreCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
            
            TableColumn<Offre, String> competencesCol = new TableColumn<>("Compétences");
            competencesCol.setCellValueFactory(new PropertyValueFactory<>("competences"));
            
            TableColumn<Offre, Integer> expCol = new TableColumn<>("Expérience requise");
            expCol.setCellValueFactory(new PropertyValueFactory<>("experienceRequise"));
            
            TableColumn<Offre, Integer> postesCol = new TableColumn<>("Postes disponibles");
            postesCol.setCellValueFactory(param -> 
                new javafx.beans.property.SimpleIntegerProperty(param.getValue().getNbPostesDisponibles()).asObject());
            
            tableView.getColumns().addAll(titreCol, competencesCol, expCol, postesCol);
            
            tableView.getItems().addAll(offreService.getOffresActives());
            
            Button btnPostuler = new Button("Postuler");
            btnPostuler.setOnAction(e -> {
                Offre selected = tableView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    handlePostuler(selected);
                    tableView.refresh();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Sélection");
                    alert.setHeaderText("Aucune offre sélectionnée");
                    alert.setContentText("Veuillez sélectionner une offre pour postuler");
                    alert.showAndWait();
                }
            });
            
            pane.getChildren().addAll(new Label("Offres disponibles :"), tableView, btnPostuler);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            pane.getChildren().add(new Label("Erreur : " + ex.getMessage()));
        }
        
        return pane;
    }
    
    private Pane createMesCandidaturesTab() {
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(10));
        
        try {
            DemandeurEmploi demandeur = (DemandeurEmploi) utilisateurConnecte;
            
            TableView<Candidature> tableView = new TableView<>();
            
            TableColumn<Candidature, String> offreCol = new TableColumn<>("Offre");
            offreCol.setCellValueFactory(param -> 
                new javafx.beans.property.SimpleStringProperty(param.getValue().getOffre().getTitre()));
            
            TableColumn<Candidature, String> dateCol = new TableColumn<>("Date");
            dateCol.setCellValueFactory(param -> 
                new javafx.beans.property.SimpleStringProperty(param.getValue().getDateCandidature().toString()));
            
            TableColumn<Candidature, String> etatCol = new TableColumn<>("État");
            etatCol.setCellValueFactory(param -> 
                new javafx.beans.property.SimpleStringProperty(
                    param.getValue().getOffre().estActive() ? "Active" : "Désactivée"));
            
            tableView.getColumns().addAll(offreCol, dateCol, etatCol);
            
            tableView.getItems().addAll(candidatureService.getCandidaturesByDemandeur(demandeur.getIdUtilisateur()));
            
            pane.getChildren().addAll(new Label("Mes candidatures :"), tableView);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            pane.getChildren().add(new Label("Erreur : " + ex.getMessage()));
        }
        
        return pane;
    }
    
    private void showNouvelleOffreDialog() {
        try {
            Entreprise entreprise = (Entreprise) utilisateurConnecte;
            NouvelleOffreDialog dialog = new NouvelleOffreDialog(offreService, entreprise.getIdUtilisateur());
            
            java.util.Optional<Offre> result = dialog.showAndWait();
            if (result.isPresent()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText("Offre créée");
                alert.setContentText("L'offre a été créée avec succès");
                alert.showAndWait();
                // Rafraîchir la vue
                updateView();
            }
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors de la création");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }
    
    private void showNouvelAbonnementDialog() {
        try {
            Entreprise entreprise = (Entreprise) utilisateurConnecte;
            NouvelAbonnementDialog dialog = new NouvelAbonnementDialog(
                entrepriseService, journalService, entreprise.getIdUtilisateur());
            
            java.util.Optional<Abonnement> result = dialog.showAndWait();
            if (result.isPresent()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText("Abonnement souscrit");
                alert.setContentText("L'abonnement a été souscrit avec succès");
                alert.showAndWait();
                // Rafraîchir la vue
                updateView();
            }
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors de la souscription");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }
    
    private void handlePostuler(Offre offre) {
        try {
            DemandeurEmploi demandeur = (DemandeurEmploi) utilisateurConnecte;
            CandidatureDialog dialog = new CandidatureDialog(
                candidatureService, journalService, demandeur.getIdUtilisateur(), offre);
            
            java.util.Optional<Candidature> result = dialog.showAndWait();
            if (result.isPresent()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText("Candidature envoyée");
                alert.setContentText("Votre candidature a été envoyée avec succès");
                alert.showAndWait();
                updateView();
            }
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors de la candidature");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }
    
    private void handleRecrutement(Candidature candidature) {
        try {
            Entreprise entreprise = (Entreprise) utilisateurConnecte;
            DemandeurEmploi demandeur = candidature.getDemandeur();
            Offre offre = candidature.getOffre();
            
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmation");
            confirmAlert.setHeaderText("Confirmer le recrutement");
            confirmAlert.setContentText("Voulez-vous recruter " + demandeur.getNom() + " " + demandeur.getPrenom() + 
                                       " pour l'offre \"" + offre.getTitre() + "\" ?");
            
            java.util.Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                recrutementService.recruter(entreprise.getIdUtilisateur(), offre.getIdOffre(), 
                                           demandeur.getIdUtilisateur());
                
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Succès");
                successAlert.setHeaderText("Recrutement effectué");
                successAlert.setContentText("Le candidat a été recruté avec succès");
                successAlert.showAndWait();
                updateView();
            }
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors du recrutement");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }
    
    private void handleDeconnexion(Stage stage) {
        try {
            // Réinitialiser l'utilisateur connecté
            utilisateurConnecte = null;
            
            // Retourner à l'écran de connexion
            LoginController loginController = applicationContext.getBean(LoginController.class);
            loginController.setApplicationContext(applicationContext);
            loginController.reset(); // Réinitialiser les champs du formulaire de connexion
            
            // Si le stage n'a pas été fourni, essayer de le récupérer
            if (stage == null) {
                // Chercher dans toutes les fenêtres ouvertes
                for (javafx.stage.Window window : javafx.stage.Window.getWindows()) {
                    if (window instanceof Stage && window.isShowing()) {
                        stage = (Stage) window;
                        break;
                    }
                }
            }
            
            if (stage == null) {
                throw new IllegalStateException("Impossible de trouver la fenêtre principale");
            }
            
            // Créer et afficher la nouvelle scène de connexion
            VBox loginView = loginController.getView();
            Scene loginScene = new Scene(loginView, 600, 400);
            stage.setTitle("Agence de Recrutement - Connexion");
            stage.setScene(loginScene);
            stage.setMaximized(false);
            stage.centerOnScreen();
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la déconnexion: " + e.getMessage());
            e.printStackTrace();
            
            // Afficher une erreur à l'utilisateur
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur");
            errorAlert.setHeaderText("Erreur de déconnexion");
            errorAlert.setContentText("Une erreur est survenue lors de la déconnexion: " + e.getMessage());
            errorAlert.showAndWait();
        }
    }
    
    // Méthode de compatibilité pour les appels sans paramètre
    private void handleDeconnexion() {
        handleDeconnexion(null);
    }
    
    public BorderPane getView() {
        return root;
    }
    
    // Méthodes pour les actions d'administration
    private void showGestionUtilisateurs() {
        Stage stage = new Stage();
        stage.setTitle("Gestion des utilisateurs");
        
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        
        Label title = new Label("Gestion des utilisateurs");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // TableView pour afficher les utilisateurs
        TableView<Utilisateur> tableView = new TableView<>();
        
        TableColumn<Utilisateur, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("idUtilisateur"));
        idCol.setPrefWidth(80);
        
        TableColumn<Utilisateur, String> loginCol = new TableColumn<>("Login");
        loginCol.setCellValueFactory(new PropertyValueFactory<>("login"));
        loginCol.setPrefWidth(150);
        
        TableColumn<Utilisateur, String> roleCol = new TableColumn<>("Rôle");
        roleCol.setCellValueFactory(param -> 
            new javafx.beans.property.SimpleStringProperty(param.getValue().getRole().toString()));
        roleCol.setPrefWidth(150);
        
        TableColumn<Utilisateur, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(param -> {
            Utilisateur u = param.getValue();
            String type = "";
            if (u instanceof Entreprise) {
                type = "Entreprise";
            } else if (u instanceof DemandeurEmploi) {
                type = "Demandeur d'emploi";
            } else if (u instanceof Administrateur) {
                type = "Administrateur";
            }
            return new javafx.beans.property.SimpleStringProperty(type);
        });
        typeCol.setPrefWidth(150);
        
        TableColumn<Utilisateur, Boolean> actifCol = new TableColumn<>("Actif");
        actifCol.setCellValueFactory(new PropertyValueFactory<>("actif"));
        actifCol.setPrefWidth(100);
        
        tableView.getColumns().addAll(idCol, loginCol, roleCol, typeCol, actifCol);
        
        // Charger les utilisateurs
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        tableView.getItems().addAll(utilisateurs);
        
        // Boutons d'action
        HBox buttonBox = new HBox(10);
        Button btnActiver = new Button("Activer/Désactiver");
        btnActiver.setOnAction(e -> {
            Utilisateur selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setActif(!selected.getActif());
                utilisateurRepository.save(selected);
                tableView.refresh();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setContentText("Statut de l'utilisateur modifié avec succès");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Sélection");
                alert.setContentText("Veuillez sélectionner un utilisateur");
                alert.showAndWait();
            }
        });
        
        Button btnDetails = new Button("Détails");
        btnDetails.setOnAction(e -> {
            Utilisateur selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showDetailsUtilisateur(selected);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Sélection");
                alert.setContentText("Veuillez sélectionner un utilisateur");
                alert.showAndWait();
            }
        });
        
        Button btnRefresh = new Button("Actualiser");
        btnRefresh.setOnAction(e -> {
            tableView.getItems().clear();
            tableView.getItems().addAll(utilisateurRepository.findAll());
        });
        
        buttonBox.getChildren().addAll(btnActiver, btnDetails, btnRefresh);
        
        root.getChildren().addAll(title, tableView, buttonBox);
        
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
    
    private void showDetailsUtilisateur(Utilisateur utilisateur) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails de l'utilisateur");
        alert.setHeaderText("Informations de l'utilisateur");
        
        StringBuilder details = new StringBuilder();
        details.append("ID: ").append(utilisateur.getIdUtilisateur()).append("\n");
        details.append("Login: ").append(utilisateur.getLogin()).append("\n");
        details.append("Rôle: ").append(utilisateur.getRole()).append("\n");
        details.append("Actif: ").append(utilisateur.getActif() ? "Oui" : "Non").append("\n");
        
        if (utilisateur instanceof Entreprise) {
            Entreprise e = (Entreprise) utilisateur;
            details.append("\n--- Informations Entreprise ---\n");
            details.append("Raison sociale: ").append(e.getRaisonSociale()).append("\n");
            details.append("Adresse: ").append(e.getAdresse()).append("\n");
            details.append("Téléphone: ").append(e.getTelephone()).append("\n");
            details.append("Description: ").append(e.getDescriptionActivite()).append("\n");
        } else if (utilisateur instanceof DemandeurEmploi) {
            DemandeurEmploi d = (DemandeurEmploi) utilisateur;
            details.append("\n--- Informations Demandeur d'emploi ---\n");
            details.append("Nom: ").append(d.getNom()).append("\n");
            details.append("Prénom: ").append(d.getPrenom()).append("\n");
            details.append("Adresse: ").append(d.getAdresse()).append("\n");
            details.append("Téléphone: ").append(d.getTelephone()).append("\n");
            details.append("Diplôme: ").append(d.getDiplome()).append("\n");
            details.append("Expérience: ").append(d.getExperience()).append(" ans\n");
            details.append("Salaire souhaité: ").append(d.getSalaireSouhaite()).append("\n");
        }
        
        alert.setContentText(details.toString());
        alert.showAndWait();
    }
    
    private void showGestionJournaux() {
        Stage stage = new Stage();
        stage.setTitle("Gestion des journaux");
        
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        
        Label title = new Label("Gestion des journaux");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // TableView pour afficher les journaux
        TableView<Journal> tableView = new TableView<>();
        
        TableColumn<Journal, String> codeCol = new TableColumn<>("Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("codeJournal"));
        codeCol.setPrefWidth(100);
        
        TableColumn<Journal, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        nomCol.setPrefWidth(200);
        
        TableColumn<Journal, String> periodiciteCol = new TableColumn<>("Périodicité");
        periodiciteCol.setCellValueFactory(param -> 
            new javafx.beans.property.SimpleStringProperty(param.getValue().getPeriodicite().toString()));
        periodiciteCol.setPrefWidth(120);
        
        TableColumn<Journal, String> langueCol = new TableColumn<>("Langue");
        langueCol.setCellValueFactory(new PropertyValueFactory<>("langue"));
        langueCol.setPrefWidth(100);
        
        TableColumn<Journal, String> categorieCol = new TableColumn<>("Catégorie");
        categorieCol.setCellValueFactory(param -> 
            new javafx.beans.property.SimpleStringProperty(param.getValue().getCategorie().getLibelle()));
        categorieCol.setPrefWidth(150);
        
        tableView.getColumns().addAll(codeCol, nomCol, periodiciteCol, langueCol, categorieCol);
        
        // Charger les journaux
        List<Journal> journaux = journalRepository.findAll();
        tableView.getItems().addAll(journaux);
        
        // Boutons d'action
        HBox buttonBox = new HBox(10);
        Button btnNouveau = new Button("Nouveau journal");
        btnNouveau.setOnAction(e -> showNouveauJournalDialog(tableView));
        
        Button btnSupprimer = new Button("Supprimer");
        btnSupprimer.setOnAction(e -> {
            Journal selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirmation");
                confirm.setHeaderText("Supprimer le journal");
                confirm.setContentText("Êtes-vous sûr de vouloir supprimer le journal \"" + selected.getNom() + "\" ?");
                if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                    try {
                        journalRepository.delete(selected);
                        tableView.getItems().remove(selected);
                        Alert success = new Alert(Alert.AlertType.INFORMATION);
                        success.setTitle("Succès");
                        success.setContentText("Journal supprimé avec succès");
                        success.showAndWait();
                    } catch (Exception ex) {
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("Erreur");
                        error.setContentText("Erreur lors de la suppression: " + ex.getMessage());
                        error.showAndWait();
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Sélection");
                alert.setContentText("Veuillez sélectionner un journal");
                alert.showAndWait();
            }
        });
        
        Button btnRefresh = new Button("Actualiser");
        btnRefresh.setOnAction(e -> {
            tableView.getItems().clear();
            tableView.getItems().addAll(journalRepository.findAll());
        });
        
        buttonBox.getChildren().addAll(btnNouveau, btnSupprimer, btnRefresh);
        
        root.getChildren().addAll(title, tableView, buttonBox);
        
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
    
    private void showNouveauJournalDialog(TableView<Journal> tableView) {
        Dialog<Journal> dialog = new Dialog<>();
        dialog.setTitle("Nouveau journal");
        dialog.setHeaderText("Créer un nouveau journal");
        
        ButtonType createButtonType = new ButtonType("Créer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        TextField codeField = new TextField();
        codeField.setPromptText("Code journal");
        TextField nomField = new TextField();
        nomField.setPromptText("Nom");
        ComboBox<Journal.Periodicite> periodiciteCombo = new ComboBox<>();
        periodiciteCombo.getItems().addAll(Journal.Periodicite.values());
        periodiciteCombo.setPromptText("Périodicité");
        TextField langueField = new TextField();
        langueField.setPromptText("Langue");
        ComboBox<Categorie> categorieCombo = new ComboBox<>();
        categorieCombo.getItems().addAll(journalService.getAllCategories());
        categorieCombo.setPromptText("Catégorie");
        
        grid.add(new Label("Code:"), 0, 0);
        grid.add(codeField, 1, 0);
        grid.add(new Label("Nom:"), 0, 1);
        grid.add(nomField, 1, 1);
        grid.add(new Label("Périodicité:"), 0, 2);
        grid.add(periodiciteCombo, 1, 2);
        grid.add(new Label("Langue:"), 0, 3);
        grid.add(langueField, 1, 3);
        grid.add(new Label("Catégorie:"), 0, 4);
        grid.add(categorieCombo, 1, 4);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                try {
                    Journal journal = journalService.creerJournal(
                        codeField.getText(),
                        nomField.getText(),
                        periodiciteCombo.getValue(),
                        langueField.getText(),
                        categorieCombo.getValue().getIdCategorie()
                    );
                    return journal;
                } catch (Exception e) {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Erreur");
                    error.setContentText("Erreur lors de la création: " + e.getMessage());
                    error.showAndWait();
                    return null;
                }
            }
            return null;
        });
        
        java.util.Optional<Journal> result = dialog.showAndWait();
        if (result.isPresent()) {
            tableView.getItems().add(result.get());
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Succès");
            success.setContentText("Journal créé avec succès");
            success.showAndWait();
        }
    }
    
    private void showStatistiques() {
        Stage stage = new Stage();
        stage.setTitle("Statistiques");
        
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        
        Label title = new Label("Statistiques du système");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // Calcul des statistiques
        long nbUtilisateurs = utilisateurRepository.count();
        long nbEntreprises = entrepriseRepository.count();
        long nbDemandeurs = demandeurEmploiRepository.count();
        long nbJournaux = journalRepository.count();
        long nbOffres = offreRepository.count();
        long nbCandidatures = candidatureRepository.count();
        long nbRecrutements = recrutementRepository.count();
        long nbAbonnements = abonnementRepository.count();
        
        // Affichage des statistiques
        VBox statsBox = new VBox(10);
        statsBox.setPadding(new Insets(10));
        statsBox.setStyle("-fx-background-color: #f0f0f0; -fx-border-radius: 5px;");
        
        statsBox.getChildren().addAll(
            createStatLabel("Nombre total d'utilisateurs", nbUtilisateurs),
            createStatLabel("Nombre d'entreprises", nbEntreprises),
            createStatLabel("Nombre de demandeurs d'emploi", nbDemandeurs),
            createStatLabel("Nombre de journaux", nbJournaux),
            createStatLabel("Nombre d'offres", nbOffres),
            createStatLabel("Nombre de candidatures", nbCandidatures),
            createStatLabel("Nombre de recrutements", nbRecrutements),
            createStatLabel("Nombre d'abonnements", nbAbonnements)
        );
        
        // Statistiques des offres par état
        Label offresTitle = new Label("Offres par état:");
        offresTitle.setStyle("-fx-font-weight: bold;");
        VBox offresStats = new VBox(5);
        List<Offre> offresActives = offreRepository.findByEtat(Offre.EtatOffre.ACTIVE);
        List<Offre> offresDesactivees = offreRepository.findByEtat(Offre.EtatOffre.DESACTIVEE);
        offresStats.getChildren().addAll(
            createStatLabel("  - Offres actives", offresActives.size()),
            createStatLabel("  - Offres désactivées", offresDesactivees.size())
        );
        
        root.getChildren().addAll(title, statsBox, offresTitle, offresStats);
        
        Scene scene = new Scene(root, 500, 500);
        stage.setScene(scene);
        stage.show();
    }
    
    private Label createStatLabel(String text, long value) {
        Label label = new Label(text + ": " + value);
        label.setStyle("-fx-font-size: 14px;");
        return label;
    }
}

