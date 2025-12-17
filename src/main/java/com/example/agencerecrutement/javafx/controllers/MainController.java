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

import java.time.LocalDate;
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
    private final PublicationOffreRepository publicationOffreRepository;
    private final AuthentificationService authentificationService;
    
    private ConfigurableApplicationContext applicationContext;
    private Utilisateur utilisateurConnecte;
    private BorderPane root;
    
    public MainController(EntrepriseService entrepriseService, OffreService offreService,
                         CandidatureService candidatureService, RecrutementService recrutementService,
                         JournalService journalService, DemandeurEmploiService demandeurEmploiService,
                         UtilisateurRepository utilisateurRepository, EntrepriseRepository entrepriseRepository,
                         DemandeurEmploiRepository demandeurEmploiRepository, JournalRepository journalRepository,
                         OffreRepository offreRepository, CandidatureRepository candidatureRepository,
                         RecrutementRepository recrutementRepository, AbonnementRepository abonnementRepository,
                         PublicationOffreRepository publicationOffreRepository, AuthentificationService authentificationService) {
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
        this.publicationOffreRepository = publicationOffreRepository;
        this.authentificationService = authentificationService;
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
    
    private void updateViewAndScene() {
        updateView();
        // Mettre à jour la scène avec le nouveau root
        Stage stage = null;
        for (javafx.stage.Window window : javafx.stage.Window.getWindows()) {
            if (window instanceof Stage && window.isShowing()) {
                stage = (Stage) window;
                break;
            }
        }
        if (stage != null && stage.getScene() != null) {
            Scene scene = stage.getScene();
            scene.setRoot(root);
        }
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

        // Barre de recherche globale (offres, journaux, éditions)
        HBox searchBox = new HBox(10);
        searchBox.setPadding(new Insets(10, 0, 0, 0));
        Label searchLabel = new Label("Recherche :");
        TextField searchField = new TextField();
        searchField.setPromptText("Tapez un mot-clé (titre d'offre, nom de journal, numéro d'édition...)");
        searchField.setPrefWidth(400);
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Offres", "Journaux", "Éditions");
        typeCombo.setValue("Offres");
        Button searchButton = new Button("Rechercher");
        searchButton.setOnAction(e -> showSearchDialog(searchField.getText().trim(), typeCombo.getValue()));
        searchBox.getChildren().addAll(searchLabel, searchField, typeCombo, searchButton);
        content.getChildren().add(searchBox);
        
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
        
        Button btnEditCoordonnees = new Button("Modifier mes coordonnées");
        btnEditCoordonnees.setOnAction(e -> showEditEntrepriseCoordonneesDialog());
        
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
        
        pane.getChildren().addAll(title, btnEditCoordonnees, tabPane);
        return pane;
    }
    
    private Pane createDemandeurPane() {
        VBox pane = new VBox(15);
        pane.setPadding(new Insets(20));
        
        Label title = new Label("Espace Demandeur d'Emploi");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        Button btnEditCoordonnees = new Button("Modifier mes coordonnées");
        btnEditCoordonnees.setOnAction(e -> showEditDemandeurCoordonneesDialog());
        
        TabPane tabPane = new TabPane();
        
        Tab tabOffres = new Tab("Offres disponibles");
        tabOffres.setContent(createOffresDisponiblesTab());
        tabOffres.setClosable(false);
        
        Tab tabMesCandidatures = new Tab("Mes candidatures");
        tabMesCandidatures.setContent(createMesCandidaturesTab());
        tabMesCandidatures.setClosable(false);
        Tab tabJournaux = new Tab("Journaux & éditions");
        tabJournaux.setContent(createJournauxPourDemandeurPane());
        tabJournaux.setClosable(false);
        
        tabPane.getTabs().addAll(tabOffres, tabMesCandidatures, tabJournaux);
        
        pane.getChildren().addAll(title, btnEditCoordonnees, tabPane);
        return pane;
    }
    
    private Pane createAdminPane() {
        VBox pane = new VBox(15);
        pane.setPadding(new Insets(20));
        
        Label title = new Label("Espace Administrateur");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // Onglets ou boutons pour les différentes fonctions admin
        Button btnGestionUsers = new Button("Gérer les utilisateurs");
        btnGestionUsers.setOnAction(e -> showGestionUtilisateurs());
        
        Button btnGestionJournaux = new Button("Gérer les journaux");
        btnGestionJournaux.setOnAction(e -> showGestionJournaux());
        
        Button btnGestionOffres = new Button("Gérer les offres");
        btnGestionOffres.setOnAction(e -> showGestionOffresAdmin());
        
        Button btnStats = new Button("Statistiques");
        btnStats.setOnAction(e -> showStatistiques());
        
        HBox buttons = new HBox(10, btnGestionUsers, btnGestionJournaux, btnGestionOffres, btnStats);
        buttons.setPadding(new Insets(10, 0, 0, 0));
        
        pane.getChildren().addAll(title, buttons);
        
        return pane;
    }
    
    private Pane createOffresTab() {
        VBox pane = new VBox(10);
        
        Button btnNouvelleOffre = new Button("Nouvelle offre");
        btnNouvelleOffre.setOnAction(e -> showNouvelleOffreDialog());
        Button btnPublierOffre = new Button("Publier l'offre sélectionnée");
        
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
        
        // Action de publication
        btnPublierOffre.setOnAction(e -> {
            Offre selected = tableView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showWarning("Sélection", "Veuillez sélectionner une offre à publier.");
                return;
            }
            if (!selected.estActive()) {
                showWarning("Offre désactivée", "Vous ne pouvez publier qu'une offre active.");
                return;
            }
            try {
                Entreprise entreprise = (Entreprise) utilisateurConnecte;
                PublierOffreDialog dialog = new PublierOffreDialog(
                    offreService,
                    entrepriseService,
                    journalService,
                    entreprise.getIdUtilisateur(),
                    selected
                );
                java.util.Optional<PublicationOffre> result = dialog.showAndWait();
                result.ifPresent(pub -> showInfo("Succès", "Offre publiée avec succès dans le journal."));
            } catch (Exception ex) {
                showError("Erreur lors de la publication", ex.getMessage());
            }
        });
        
        HBox buttons = new HBox(10, btnNouvelleOffre, btnPublierOffre);
        pane.getChildren().addAll(buttons, tableView);
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
                if (selected == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Sélection");
                    alert.setHeaderText("Aucune offre sélectionnée");
                    alert.setContentText("Veuillez sélectionner une offre pour postuler.");
                    alert.showAndWait();
                    return;
                }

                // Nouveau flux métier : la candidature se fait depuis l'onglet
                // \"Journaux & éditions\" afin que l'édition soit cohérente.
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle("Postuler à une offre");
                info.setHeaderText("Utilisez l'onglet \"Journaux & éditions\"");
                info.setContentText(
                    "Pour postuler à une offre, veuillez utiliser l'onglet \"Journaux & éditions\".\n\n" +
                    "Sélectionnez le journal puis l'édition où l'offre a été publiée,\n" +
                    "puis choisissez l'offre et cliquez sur \"Postuler à l'offre sélectionnée\"."
                );
                info.showAndWait();
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

    /**
     * Onglet \"Journaux & éditions\" pour le demandeur :
     * Journal -> Éditions -> Offres publiées dans l'édition sélectionnée.
     */
    private Pane createJournauxPourDemandeurPane() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Label journauxLabel = new Label("Journaux");
        TableView<Journal> journauxTable = new TableView<>();

        TableColumn<Journal, String> codeCol = new TableColumn<>("Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("codeJournal"));
        codeCol.setPrefWidth(100);

        TableColumn<Journal, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        nomCol.setPrefWidth(200);

        journauxTable.getColumns().addAll(codeCol, nomCol);
        journauxTable.getItems().addAll(journalRepository.findAll());

        Label editionsLabel = new Label("Éditions");
        TableView<Edition> editionsTable = new TableView<>();

        TableColumn<Edition, Long> idEditionCol = new TableColumn<>("ID");
        idEditionCol.setCellValueFactory(new PropertyValueFactory<>("idEdition"));
        idEditionCol.setPrefWidth(80);

        TableColumn<Edition, Integer> numEditionCol = new TableColumn<>("Numéro");
        numEditionCol.setCellValueFactory(new PropertyValueFactory<>("numeroEdition"));
        numEditionCol.setPrefWidth(100);

        TableColumn<Edition, String> dateParutionCol = new TableColumn<>("Date de parution");
        dateParutionCol.setCellValueFactory(param ->
            new javafx.beans.property.SimpleStringProperty(param.getValue().getDateParution().toString()));
        dateParutionCol.setPrefWidth(150);

        editionsTable.getColumns().addAll(idEditionCol, numEditionCol, dateParutionCol);

        Label offresLabel = new Label("Offres publiées dans l'édition sélectionnée");
        TableView<Offre> offresTable = new TableView<>();

        TableColumn<Offre, String> titreOffreCol = new TableColumn<>("Titre");
        titreOffreCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
        titreOffreCol.setPrefWidth(250);

        TableColumn<Offre, String> compOffreCol = new TableColumn<>("Compétences");
        compOffreCol.setCellValueFactory(new PropertyValueFactory<>("competences"));
        compOffreCol.setPrefWidth(250);

        offresTable.getColumns().addAll(titreOffreCol, compOffreCol);

        // Quand on sélectionne un journal, charger ses éditions
        journauxTable.getSelectionModel().selectedItemProperty().addListener((obs, oldJ, newJ) -> {
            editionsTable.getItems().clear();
            offresTable.getItems().clear();
            if (newJ != null) {
                editionsTable.getItems().addAll(journalService.getEditionsByJournal(newJ.getCodeJournal()));
            }
        });

        // Quand on sélectionne une édition, charger les offres publiées
        editionsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldE, newE) -> {
            offresTable.getItems().clear();
            if (newE != null) {
                var pubs = publicationOffreRepository.findByEditionIdEdition(newE.getIdEdition());
                pubs.stream()
                    .map(PublicationOffre::getOffre)
                    .forEach(offresTable.getItems()::add);
            }
        });

        Button btnPostuler = new Button("Postuler à l'offre sélectionnée");
        btnPostuler.setOnAction(e -> {
            Offre selectedOffre = offresTable.getSelectionModel().getSelectedItem();
            Edition selectedEdition = editionsTable.getSelectionModel().getSelectedItem();
            if (selectedOffre == null || selectedEdition == null) {
                showWarning("Sélection", "Veuillez sélectionner une édition et une offre.");
                return;
            }
            try {
                DemandeurEmploi demandeur = (DemandeurEmploi) utilisateurConnecte;
                candidatureService.postuler(
                    demandeur.getIdUtilisateur(),
                    selectedOffre.getIdOffre(),
                    selectedEdition.getIdEdition()
                );
                showInfo("Succès", "Votre candidature a été envoyée avec succès.");
                updateViewAndScene();
            } catch (Exception ex) {
                showError("Erreur lors de la candidature", ex.getMessage());
            }
        });

        root.getChildren().addAll(
            journauxLabel, journauxTable,
            editionsLabel, editionsTable,
            offresLabel, offresTable,
            btnPostuler
        );

        return root;
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
                
                // Rafraîchir complètement la vue pour que les nouvelles offres apparaissent
                updateViewAndScene();
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
                updateViewAndScene();
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
                updateViewAndScene();
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
                updateViewAndScene();
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
    /**
     * Gestion des offres côté administrateur.
     * L'admin peut :
     * - voir toutes les offres
     * - désactiver une offre
     * - supprimer une offre (seulement si aucune candidature / recrutement / publication)
     */
    private void showGestionOffresAdmin() {
        Stage stage = new Stage();
        stage.setTitle("Gestion des offres");
        
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        
        Label title = new Label("Gestion des offres");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        TableView<Offre> tableView = new TableView<>();
        
        TableColumn<Offre, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("idOffre"));
        idCol.setPrefWidth(70);
        
        TableColumn<Offre, String> titreCol = new TableColumn<>("Titre");
        titreCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
        titreCol.setPrefWidth(200);
        
        TableColumn<Offre, String> entrepriseCol = new TableColumn<>("Entreprise");
        entrepriseCol.setCellValueFactory(param ->
            new javafx.beans.property.SimpleStringProperty(
                param.getValue().getEntreprise() != null ? param.getValue().getEntreprise().getRaisonSociale() : ""));
        entrepriseCol.setPrefWidth(200);
        
        TableColumn<Offre, Integer> postesCol = new TableColumn<>("Postes");
        postesCol.setCellValueFactory(new PropertyValueFactory<>("nbPostes"));
        postesCol.setPrefWidth(80);
        
        TableColumn<Offre, Integer> postesDispCol = new TableColumn<>("Postes dispo.");
        postesDispCol.setCellValueFactory(param ->
            new javafx.beans.property.SimpleIntegerProperty(param.getValue().getNbPostesDisponibles()).asObject());
        postesDispCol.setPrefWidth(100);
        
        TableColumn<Offre, String> etatCol = new TableColumn<>("État");
        etatCol.setCellValueFactory(param ->
            new javafx.beans.property.SimpleStringProperty(
                param.getValue().estActive() ? "Active" : "Désactivée"));
        etatCol.setPrefWidth(100);
        
        tableView.getColumns().addAll(idCol, titreCol, entrepriseCol, postesCol, postesDispCol, etatCol);
        
        // Charger toutes les offres
        tableView.getItems().addAll(offreRepository.findAll());
        
        HBox buttonBox = new HBox(10);
        
        Button btnDesactiver = new Button("Désactiver l'offre");
        btnDesactiver.setOnAction(e -> {
            Offre selected = tableView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showWarning("Sélection", "Veuillez sélectionner une offre");
                return;
            }
            if (!selected.estActive()) {
                showWarning("État de l'offre", "L'offre est déjà désactivée");
                return;
            }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmation");
            confirm.setHeaderText("Désactiver l'offre");
            confirm.setContentText("Êtes-vous sûr de vouloir désactiver l'offre \"" + selected.getTitre() + "\" ?");
            if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                try {
                    offreService.desactiverOffre(selected.getIdOffre());
                    tableView.getItems().clear();
                    tableView.getItems().addAll(offreRepository.findAll());
                    showInfo("Succès", "Offre désactivée avec succès");
                    // rafraîchir éventuellement la vue principale
                    updateViewAndScene();
                } catch (Exception ex) {
                    showError("Erreur lors de la désactivation", ex.getMessage());
                }
            }
        });
        
        Button btnSupprimer = new Button("Supprimer l'offre");
        btnSupprimer.setOnAction(e -> {
            Offre selected = tableView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showWarning("Sélection", "Veuillez sélectionner une offre");
                return;
            }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmation");
            confirm.setHeaderText("Supprimer l'offre");
            confirm.setContentText("Êtes-vous sûr de vouloir supprimer l'offre \"" + selected.getTitre() + "\" ?\n\n" +
                                   "Attention : cette action est définitive.");
            if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                try {
                    offreService.supprimerOffre(selected.getIdOffre());
                    tableView.getItems().remove(selected);
                    showInfo("Succès", "Offre supprimée avec succès");
                    updateViewAndScene();
                } catch (Exception ex) {
                    showError("Suppression impossible", ex.getMessage());
                }
            }
        });
        
        Button btnRefresh = new Button("Actualiser");
        btnRefresh.setOnAction(e -> {
            tableView.getItems().clear();
            tableView.getItems().addAll(offreRepository.findAll());
        });
        
        buttonBox.getChildren().addAll(btnDesactiver, btnSupprimer, btnRefresh);
        
        root.getChildren().addAll(title, tableView, buttonBox);
        
        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
        stage.show();
    }
    
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showError(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Recherche globale (offres, journaux, éditions) accessible dans tous les espaces
     * via la barre de recherche en haut de l'écran.
     */
    private void showSearchDialog(String query, String type) {
        if (query == null || query.isEmpty()) {
            showWarning("Recherche", "Veuillez saisir un mot-clé pour la recherche.");
            return;
        }

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Résultats de la recherche");
        dialog.setHeaderText("Résultats pour \"" + query + "\" dans " + type.toLowerCase());

        ButtonType closeButtonType = new ButtonType("Fermer", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(closeButtonType);

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        String q = query.toLowerCase();

        if ("Offres".equals(type)) {
            TableView<Offre> table = new TableView<>();
            TableColumn<Offre, Long> idCol = new TableColumn<>("ID");
            idCol.setCellValueFactory(new PropertyValueFactory<>("idOffre"));
            idCol.setPrefWidth(70);

            TableColumn<Offre, String> titreCol = new TableColumn<>("Titre");
            titreCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
            titreCol.setPrefWidth(200);

            TableColumn<Offre, String> compCol = new TableColumn<>("Compétences");
            compCol.setCellValueFactory(new PropertyValueFactory<>("competences"));
            compCol.setPrefWidth(250);

            TableColumn<Offre, String> entrepriseCol = new TableColumn<>("Entreprise");
            entrepriseCol.setCellValueFactory(param ->
                new javafx.beans.property.SimpleStringProperty(
                    param.getValue().getEntreprise() != null ? param.getValue().getEntreprise().getRaisonSociale() : ""));
            entrepriseCol.setPrefWidth(200);

            table.getColumns().addAll(idCol, titreCol, compCol, entrepriseCol);

            var allOffres = offreRepository.findAll();
            allOffres.stream()
                .filter(o ->
                    (o.getTitre() != null && o.getTitre().toLowerCase().contains(q)) ||
                    (o.getCompetences() != null && o.getCompetences().toLowerCase().contains(q)))
                .forEach(table.getItems()::add);

            root.getChildren().addAll(new Label("Offres trouvées :"), table);

        } else if ("Journaux".equals(type)) {
            TableView<Journal> table = new TableView<>();

            TableColumn<Journal, String> codeCol = new TableColumn<>("Code");
            codeCol.setCellValueFactory(new PropertyValueFactory<>("codeJournal"));
            codeCol.setPrefWidth(100);

            TableColumn<Journal, String> nomCol = new TableColumn<>("Nom");
            nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
            nomCol.setPrefWidth(200);

            TableColumn<Journal, String> catCol = new TableColumn<>("Catégorie");
            catCol.setCellValueFactory(param ->
                new javafx.beans.property.SimpleStringProperty(
                    param.getValue().getCategorie() != null ? param.getValue().getCategorie().getLibelle() : ""));
            catCol.setPrefWidth(200);

            table.getColumns().addAll(codeCol, nomCol, catCol);

            var journaux = journalService.getAllJournaux();
            journaux.stream()
                .filter(j ->
                    (j.getNom() != null && j.getNom().toLowerCase().contains(q)) ||
                    (j.getCodeJournal() != null && j.getCodeJournal().toLowerCase().contains(q)))
                .forEach(table.getItems()::add);

            root.getChildren().addAll(new Label("Journaux trouvés :"), table);

        } else if ("Éditions".equals(type)) {
            TableView<Edition> table = new TableView<>();

            TableColumn<Edition, Long> idCol = new TableColumn<>("ID");
            idCol.setCellValueFactory(new PropertyValueFactory<>("idEdition"));
            idCol.setPrefWidth(70);

            TableColumn<Edition, Integer> numCol = new TableColumn<>("Numéro");
            numCol.setCellValueFactory(new PropertyValueFactory<>("numeroEdition"));
            numCol.setPrefWidth(100);

            TableColumn<Edition, String> dateCol = new TableColumn<>("Date");
            dateCol.setCellValueFactory(param ->
                new javafx.beans.property.SimpleStringProperty(param.getValue().getDateParution().toString()));
            dateCol.setPrefWidth(120);

            TableColumn<Edition, String> journalCol = new TableColumn<>("Journal");
            journalCol.setCellValueFactory(param ->
                new javafx.beans.property.SimpleStringProperty(
                    param.getValue().getJournal() != null ? param.getValue().getJournal().getNom() : ""));
            journalCol.setPrefWidth(220);

            table.getColumns().addAll(idCol, numCol, dateCol, journalCol);

            // Récupérer toutes les éditions via les journaux
            var journaux = journalService.getAllJournaux();
            journaux.stream()
                .flatMap(j -> journalService.getEditionsByJournal(j.getCodeJournal()).stream())
                .filter(ed ->
                    String.valueOf(ed.getNumeroEdition()).toLowerCase().contains(q) ||
                    ed.getDateParution().toString().toLowerCase().contains(q) ||
                    (ed.getJournal() != null &&
                     ((ed.getJournal().getNom() != null && ed.getJournal().getNom().toLowerCase().contains(q)) ||
                      (ed.getJournal().getCodeJournal() != null && ed.getJournal().getCodeJournal().toLowerCase().contains(q)))))
                .forEach(table.getItems()::add);

            root.getChildren().addAll(new Label("Éditions trouvées :"), table);
        }

        dialog.getDialogPane().setContent(root);
        dialog.showAndWait();
    }
    
    /**
     * Dialog pour permettre à une entreprise de modifier ses coordonnées.
     */
    private void showEditEntrepriseCoordonneesDialog() {
        if (!(utilisateurConnecte instanceof Entreprise)) {
            showError("Accès refusé", "Cette fonctionnalité est réservée aux entreprises.");
            return;
        }
        Entreprise entreprise = (Entreprise) utilisateurConnecte;
        
        Dialog<Entreprise> dialog = new Dialog<>();
        dialog.setTitle("Modifier mes coordonnées");
        dialog.setHeaderText("Modifier les coordonnées de l'entreprise");
        
        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        TextField raisonSocialeField = new TextField(entreprise.getRaisonSociale());
        TextField adresseField = new TextField(entreprise.getAdresse());
        TextField telephoneField = new TextField(entreprise.getTelephone());
        TextArea descriptionArea = new TextArea(entreprise.getDescriptionActivite());
        descriptionArea.setPrefRowCount(3);
        
        grid.add(new Label("Raison sociale :"), 0, 0);
        grid.add(raisonSocialeField, 1, 0);
        grid.add(new Label("Adresse :"), 0, 1);
        grid.add(adresseField, 1, 1);
        grid.add(new Label("Téléphone :"), 0, 2);
        grid.add(telephoneField, 1, 2);
        grid.add(new Label("Description :"), 0, 3);
        grid.add(descriptionArea, 1, 3);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                entreprise.setRaisonSociale(raisonSocialeField.getText().trim());
                entreprise.setAdresse(adresseField.getText().trim());
                entreprise.setTelephone(telephoneField.getText().trim());
                entreprise.setDescriptionActivite(descriptionArea.getText().trim());
                try {
                    entrepriseRepository.save(entreprise);
                    return entreprise;
                } catch (Exception e) {
                    showError("Erreur lors de la mise à jour", e.getMessage());
                    return null;
                }
            }
            return null;
        });
        
        java.util.Optional<Entreprise> result = dialog.showAndWait();
        if (result.isPresent()) {
            showInfo("Succès", "Vos coordonnées ont été mises à jour.");
            updateViewAndScene();
        }
    }
    
    /**
     * Dialog pour permettre à un demandeur d'emploi de modifier ses coordonnées.
     */
    private void showEditDemandeurCoordonneesDialog() {
        if (!(utilisateurConnecte instanceof DemandeurEmploi)) {
            showError("Accès refusé", "Cette fonctionnalité est réservée aux demandeurs d'emploi.");
            return;
        }
        DemandeurEmploi demandeur = (DemandeurEmploi) utilisateurConnecte;
        
        Dialog<DemandeurEmploi> dialog = new Dialog<>();
        dialog.setTitle("Modifier mes coordonnées");
        dialog.setHeaderText("Modifier vos coordonnées");
        
        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        TextField nomField = new TextField(demandeur.getNom());
        TextField prenomField = new TextField(demandeur.getPrenom());
        TextField adresseField = new TextField(demandeur.getAdresse());
        TextField telephoneField = new TextField(demandeur.getTelephone());
        TextField faxField = new TextField(demandeur.getFax());
        TextField diplomeField = new TextField(demandeur.getDiplome());
        Spinner<Integer> experienceSpinner = new Spinner<>(0, 50,
            demandeur.getExperience() != null ? demandeur.getExperience() : 0, 1);
        Spinner<Double> salaireSpinner = new Spinner<>(0.0, 1000000.0,
            demandeur.getSalaireSouhaite() != null ? demandeur.getSalaireSouhaite() : 0.0, 100.0);
        salaireSpinner.setEditable(true);
        
        grid.add(new Label("Nom :"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Prénom :"), 0, 1);
        grid.add(prenomField, 1, 1);
        grid.add(new Label("Adresse :"), 0, 2);
        grid.add(adresseField, 1, 2);
        grid.add(new Label("Téléphone :"), 0, 3);
        grid.add(telephoneField, 1, 3);
        grid.add(new Label("Fax :"), 0, 4);
        grid.add(faxField, 1, 4);
        grid.add(new Label("Diplôme :"), 0, 5);
        grid.add(diplomeField, 1, 5);
        grid.add(new Label("Expérience (années) :"), 0, 6);
        grid.add(experienceSpinner, 1, 6);
        grid.add(new Label("Salaire souhaité :"), 0, 7);
        grid.add(salaireSpinner, 1, 7);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                demandeur.setNom(nomField.getText().trim());
                demandeur.setPrenom(prenomField.getText().trim());
                demandeur.setAdresse(adresseField.getText().trim());
                demandeur.setTelephone(telephoneField.getText().trim());
                demandeur.setFax(faxField.getText().trim());
                demandeur.setDiplome(diplomeField.getText().trim());
                demandeur.setExperience(experienceSpinner.getValue());
                demandeur.setSalaireSouhaite(salaireSpinner.getValue());
                try {
                    demandeurEmploiRepository.save(demandeur);
                    return demandeur;
                } catch (Exception e) {
                    showError("Erreur lors de la mise à jour", e.getMessage());
                    return null;
                }
            }
            return null;
        });
        
        java.util.Optional<DemandeurEmploi> result = dialog.showAndWait();
        if (result.isPresent()) {
            showInfo("Succès", "Vos coordonnées ont été mises à jour.");
            updateViewAndScene();
        }
    }
    
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
        
        Button btnModifierMotDePasse = new Button("Modifier mot de passe");
        btnModifierMotDePasse.setOnAction(e -> {
            Utilisateur selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                ModifierMotDePasseDialog dialog = new ModifierMotDePasseDialog(authentificationService, selected);
                dialog.showAndWait();
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
        
        buttonBox.getChildren().addAll(btnActiver, btnModifierMotDePasse, btnDetails, btnRefresh);
        
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
        stage.setTitle("Gestion des journaux et éditions");
        
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        
        Label title = new Label("Gestion des journaux et de leurs éditions");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // TableView pour afficher les journaux
        TableView<Journal> journauxTable = new TableView<>();
        
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
        
        journauxTable.getColumns().addAll(codeCol, nomCol, periodiciteCol, langueCol, categorieCol);
        
        // Charger les journaux
        List<Journal> journaux = journalRepository.findAll();
        journauxTable.getItems().addAll(journaux);
        
        // TableView pour afficher les éditions du journal sélectionné
        Label editionsTitle = new Label("Éditions du journal sélectionné");
        editionsTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        TableView<Edition> editionsTable = new TableView<>();
        
        TableColumn<Edition, Long> idEditionCol = new TableColumn<>("ID");
        idEditionCol.setCellValueFactory(new PropertyValueFactory<>("idEdition"));
        idEditionCol.setPrefWidth(80);
        
        TableColumn<Edition, Integer> numEditionCol = new TableColumn<>("Numéro");
        numEditionCol.setCellValueFactory(new PropertyValueFactory<>("numeroEdition"));
        numEditionCol.setPrefWidth(100);
        
        TableColumn<Edition, String> dateParutionCol = new TableColumn<>("Date de parution");
        dateParutionCol.setCellValueFactory(param ->
            new javafx.beans.property.SimpleStringProperty(param.getValue().getDateParution().toString()));
        dateParutionCol.setPrefWidth(150);
        
        editionsTable.getColumns().addAll(idEditionCol, numEditionCol, dateParutionCol);
        
        // Quand on sélectionne un journal, charger ses éditions
        journauxTable.getSelectionModel().selectedItemProperty().addListener((obs, oldJournal, newJournal) -> {
            editionsTable.getItems().clear();
            if (newJournal != null) {
                editionsTable.getItems().addAll(journalService.getEditionsByJournal(newJournal.getCodeJournal()));
            }
        });
        
        // Boutons d'action
        HBox buttonBox = new HBox(10);
        Button btnNouveau = new Button("Nouveau journal");
        btnNouveau.setOnAction(e -> showNouveauJournalDialog(journauxTable));
        
        Button btnSupprimer = new Button("Supprimer journal");
        btnSupprimer.setOnAction(e -> {
            Journal selected = journauxTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirmation");
                confirm.setHeaderText("Supprimer le journal");
                confirm.setContentText("Êtes-vous sûr de vouloir supprimer le journal \"" + selected.getNom() + "\" ?");
                if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                    try {
                        journalRepository.delete(selected);
                        journauxTable.getItems().remove(selected);
                        editionsTable.getItems().clear();
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
        
        Button btnNouvelleEdition = new Button("Nouvelle édition");
        btnNouvelleEdition.setOnAction(e -> {
            Journal selected = journauxTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Sélection");
                alert.setContentText("Veuillez sélectionner un journal pour créer une édition");
                alert.showAndWait();
                return;
            }
            showNouvelleEditionDialog(selected, editionsTable);
        });
        
        Button btnRefresh = new Button("Actualiser");
        btnRefresh.setOnAction(e -> {
            journauxTable.getItems().clear();
            journauxTable.getItems().addAll(journalRepository.findAll());
            editionsTable.getItems().clear();
        });
        
        buttonBox.getChildren().addAll(btnNouveau, btnSupprimer, btnNouvelleEdition, btnRefresh);
        
        root.getChildren().addAll(title, journauxTable, buttonBox, editionsTitle, editionsTable);
        
        Scene scene = new Scene(root, 900, 650);
        stage.setScene(scene);
        stage.show();
    }

    private void showNouvelleEditionDialog(Journal journal, TableView<Edition> editionsTable) {
        Dialog<Edition> dialog = new Dialog<>();
        dialog.setTitle("Nouvelle édition");
        dialog.setHeaderText("Créer une nouvelle édition pour le journal \"" + journal.getNom() + "\"");
        
        ButtonType createButtonType = new ButtonType("Créer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        Spinner<Integer> numeroSpinner = new Spinner<>(1, 1000, 1, 1);
        DatePicker datePicker = new DatePicker(LocalDate.now());
        
        grid.add(new Label("Numéro d'édition:"), 0, 0);
        grid.add(numeroSpinner, 1, 0);
        grid.add(new Label("Date de parution:"), 0, 1);
        grid.add(datePicker, 1, 1);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                try {
                    Edition edition = journalService.creerEdition(
                        journal.getCodeJournal(),
                        numeroSpinner.getValue(),
                        datePicker.getValue()
                    );
                    return edition;
                } catch (Exception e) {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Erreur");
                    error.setContentText("Erreur lors de la création de l'édition: " + e.getMessage());
                    error.showAndWait();
                    return null;
                }
            }
            return null;
        });
        
        java.util.Optional<Edition> result = dialog.showAndWait();
        if (result.isPresent()) {
            editionsTable.getItems().add(result.get());
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Succès");
            success.setContentText("Édition créée avec succès");
            success.showAndWait();
        }
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
        List<Categorie> categories = journalService.getAllCategories();
        categorieCombo.getItems().addAll(categories);
        categorieCombo.setPromptText("Catégorie");
        // Configurer l'affichage du ComboBox pour montrer le libellé
        categorieCombo.setCellFactory(param -> new ListCell<Categorie>() {
            @Override
            protected void updateItem(Categorie item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getLibelle());
                }
            }
        });
        categorieCombo.setButtonCell(new ListCell<Categorie>() {
            @Override
            protected void updateItem(Categorie item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getLibelle());
                }
            }
        });
        
        // Bouton pour créer une nouvelle catégorie si aucune n'existe
        Button btnNouvelleCategorie = new Button("Nouvelle catégorie");
        btnNouvelleCategorie.setOnAction(e -> {
            TextInputDialog categorieDialog = new TextInputDialog();
            categorieDialog.setTitle("Nouvelle catégorie");
            categorieDialog.setHeaderText("Créer une nouvelle catégorie");
            categorieDialog.setContentText("Libellé de la catégorie:");
            java.util.Optional<String> result = categorieDialog.showAndWait();
            if (result.isPresent() && !result.get().trim().isEmpty()) {
                try {
                    Categorie nouvelleCategorie = journalService.creerCategorie(result.get().trim());
                    categorieCombo.getItems().add(nouvelleCategorie);
                    categorieCombo.setValue(nouvelleCategorie);
                    Alert success = new Alert(Alert.AlertType.INFORMATION);
                    success.setTitle("Succès");
                    success.setContentText("Catégorie créée avec succès");
                    success.showAndWait();
                } catch (Exception ex) {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Erreur");
                    error.setContentText("Erreur lors de la création de la catégorie: " + ex.getMessage());
                    error.showAndWait();
                }
            }
        });
        
        grid.add(new Label("Code:"), 0, 0);
        grid.add(codeField, 1, 0);
        grid.add(new Label("Nom:"), 0, 1);
        grid.add(nomField, 1, 1);
        grid.add(new Label("Périodicité:"), 0, 2);
        grid.add(periodiciteCombo, 1, 2);
        grid.add(new Label("Langue:"), 0, 3);
        grid.add(langueField, 1, 3);
        grid.add(new Label("Catégorie:"), 0, 4);
        HBox categorieBox = new HBox(10);
        categorieBox.getChildren().addAll(categorieCombo, btnNouvelleCategorie);
        grid.add(categorieBox, 1, 4);
        
        dialog.getDialogPane().setContent(grid);
        
        // Désactiver le bouton Créer si les champs ne sont pas remplis
        javafx.scene.control.Button createButton = (javafx.scene.control.Button) dialog.getDialogPane().lookupButton(createButtonType);
        createButton.setDisable(true);
        
        // Validation des champs
        Runnable validateFields = () -> {
            boolean isValid = !codeField.getText().trim().isEmpty() &&
                             !nomField.getText().trim().isEmpty() &&
                             periodiciteCombo.getValue() != null &&
                             !langueField.getText().trim().isEmpty() &&
                             categorieCombo.getValue() != null;
            createButton.setDisable(!isValid);
        };
        
        codeField.textProperty().addListener((observable, oldValue, newValue) -> validateFields.run());
        nomField.textProperty().addListener((observable, oldValue, newValue) -> validateFields.run());
        periodiciteCombo.valueProperty().addListener((observable, oldValue, newValue) -> validateFields.run());
        langueField.textProperty().addListener((observable, oldValue, newValue) -> validateFields.run());
        categorieCombo.valueProperty().addListener((observable, oldValue, newValue) -> validateFields.run());
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                // Validation finale
                if (codeField.getText().trim().isEmpty() || 
                    nomField.getText().trim().isEmpty() ||
                    periodiciteCombo.getValue() == null ||
                    langueField.getText().trim().isEmpty() ||
                    categorieCombo.getValue() == null) {
                    Alert error = new Alert(Alert.AlertType.WARNING);
                    error.setTitle("Validation");
                    error.setContentText("Veuillez remplir tous les champs");
                    error.showAndWait();
                    return null;
                }
                
                try {
                    Journal journal = journalService.creerJournal(
                        codeField.getText().trim(),
                        nomField.getText().trim(),
                        periodiciteCombo.getValue(),
                        langueField.getText().trim(),
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

