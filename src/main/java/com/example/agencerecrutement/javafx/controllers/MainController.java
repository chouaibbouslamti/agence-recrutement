package com.example.agencerecrutement.javafx.controllers;

import com.example.agencerecrutement.javafx.dialogs.*;
import com.example.agencerecrutement.model.*;
import com.example.agencerecrutement.service.*;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MainController {
    
    private final EntrepriseService entrepriseService;
    private final OffreService offreService;
    private final CandidatureService candidatureService;
    private final RecrutementService recrutementService;
    private final JournalService journalService;
    private final DemandeurEmploiService demandeurEmploiService;
    
    private ConfigurableApplicationContext applicationContext;
    private Utilisateur utilisateurConnecte;
    private BorderPane root;
    
    public MainController(EntrepriseService entrepriseService, OffreService offreService,
                         CandidatureService candidatureService, RecrutementService recrutementService,
                         JournalService journalService, DemandeurEmploiService demandeurEmploiService) {
        this.entrepriseService = entrepriseService;
        this.offreService = offreService;
        this.candidatureService = candidatureService;
        this.recrutementService = recrutementService;
        this.journalService = journalService;
        this.demandeurEmploiService = demandeurEmploiService;
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
        itemDeconnexion.setOnAction(e -> handleDeconnexion());
        menuFichier.getItems().add(itemDeconnexion);
        menuBar.getMenus().add(menuFichier);
        
        // Ajouter des menus selon le rôle
        if (utilisateurConnecte.getRole() == Utilisateur.Role.ADMINISTRATEUR) {
            Menu menuAdmin = new Menu("Administration");
            MenuItem itemGestionUsers = new MenuItem("Gérer les utilisateurs");
            MenuItem itemGestionJournaux = new MenuItem("Gérer les journaux");
            MenuItem itemStats = new MenuItem("Statistiques");
            menuAdmin.getItems().addAll(itemGestionUsers, itemGestionJournaux, itemStats);
            menuBar.getMenus().add(menuAdmin);
        }
        
        return menuBar;
    }
    
    private Pane createContentPane() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        
        Label welcomeLabel = new Label("Bienvenue, " + utilisateurConnecte.getLogin());
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        content.getChildren().add(welcomeLabel);
        
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
    
    private void handleDeconnexion() {
        utilisateurConnecte = null;
        // Retourner à l'écran de connexion
        LoginController loginController = applicationContext.getBean(LoginController.class);
        loginController.setApplicationContext(applicationContext);
        
        Stage stage = (Stage) root.getScene().getWindow();
        Scene scene = new Scene(loginController.getView(), 600, 400);
        stage.setTitle("Agence de Recrutement - Connexion");
        stage.setScene(scene);
        stage.setMaximized(false);
    }
    
    public BorderPane getView() {
        return root;
    }
}

