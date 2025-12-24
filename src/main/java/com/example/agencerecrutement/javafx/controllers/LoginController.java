package com.example.agencerecrutement.javafx.controllers;

import com.example.agencerecrutement.javafx.dialogs.InscriptionDialog;
import com.example.agencerecrutement.javafx.dialogs.MotDePasseOublieDialog;
import com.example.agencerecrutement.model.DemandeurEmploi;
import com.example.agencerecrutement.model.Document;
import com.example.agencerecrutement.model.Utilisateur;
import com.example.agencerecrutement.repository.UtilisateurRepository;
import com.example.agencerecrutement.service.AuthentificationService;
import com.example.agencerecrutement.service.DemandeurEmploiService;
import com.example.agencerecrutement.service.DocumentService;
import com.example.agencerecrutement.service.EntrepriseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LoginController {
    
    private final AuthentificationService authentificationService;
    private final EntrepriseService entrepriseService;
    private final DemandeurEmploiService demandeurEmploiService;
    private final DocumentService documentService;
    private final UtilisateurRepository utilisateurRepository;
    private ConfigurableApplicationContext applicationContext;
    private Utilisateur utilisateurConnecte;
    
    private VBox root;
    private ComboBox<String> loginCombo;
    private PasswordField passwordField;
    private Label errorLabel;
    private Button loginButton;
    private ObservableList<String> loginSuggestions;
    
    public LoginController(AuthentificationService authentificationService,
                          EntrepriseService entrepriseService,
                          DemandeurEmploiService demandeurEmploiService,
                          DocumentService documentService,
                          UtilisateurRepository utilisateurRepository) {
        this.authentificationService = authentificationService;
        this.entrepriseService = entrepriseService;
        this.demandeurEmploiService = demandeurEmploiService;
        this.documentService = documentService;
        this.utilisateurRepository = utilisateurRepository;
        initializeView();
    }
    
    public void setApplicationContext(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    
    private void initializeView() {
        root = new VBox(15);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #f5f5f5;");
        
        Label titleLabel = new Label("Agence de Recrutement");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        Label subtitleLabel = new Label("Connexion");
        subtitleLabel.setStyle("-fx-font-size: 16px;");
        
        // Charger les logins existants pour l'autofill
        loadLoginSuggestions();
        
        // Utiliser un ComboBox avec autocomplétion pour le login
        loginCombo = new ComboBox<>(loginSuggestions);
        loginCombo.setEditable(true);
        loginCombo.setPromptText("Login");
        loginCombo.setPrefWidth(250);
        loginCombo.setVisibleRowCount(5);
        
        // Configurer l'autocomplétion
        loginCombo.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                loginCombo.setItems(loginSuggestions);
            } else {
                // Filtrer les suggestions basées sur la saisie
                ObservableList<String> filtered = FXCollections.observableArrayList(
                    loginSuggestions.stream()
                        .filter(login -> login.toLowerCase().startsWith(newValue.toLowerCase()))
                        .collect(Collectors.toList())
                );
                loginCombo.setItems(filtered);
            }
        });
        
        // Permettre la navigation avec les flèches et Enter
        loginCombo.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER && loginCombo.getSelectionModel().getSelectedItem() != null) {
                passwordField.requestFocus();
            }
        });
        
        passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");
        passwordField.setPrefWidth(250);
        
        // Permettre la connexion avec Enter dans le champ mot de passe
        passwordField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                handleLogin();
            }
        });
        
        loginButton = new Button("Se connecter");
        loginButton.setPrefWidth(250);
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        loginButton.setOnAction(e -> handleLogin());
        
        Button inscriptionButton = new Button("S'inscrire");
        inscriptionButton.setPrefWidth(250);
        inscriptionButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px;");
        inscriptionButton.setOnAction(e -> handleInscription());
        
        Button motDePasseOublieButton = new Button("Mot de passe oublié");
        motDePasseOublieButton.setPrefWidth(250);
        motDePasseOublieButton.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; -fx-font-size: 12px; -fx-border-color: #5a6268; -fx-border-radius: 3px;");
        motDePasseOublieButton.setOnAction(e -> handleMotDePasseOublie());
        
        errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(false);
        
        root.getChildren().addAll(titleLabel, subtitleLabel, loginCombo, passwordField, loginButton, inscriptionButton, motDePasseOublieButton, errorLabel);
    }
    
    private void loadLoginSuggestions() {
        try {
            List<String> logins = utilisateurRepository.findAll().stream()
                .map(Utilisateur::getLogin)
                .collect(Collectors.toList());
            loginSuggestions = FXCollections.observableArrayList(logins);
        } catch (Exception e) {
            // Si erreur, créer une liste vide
            loginSuggestions = FXCollections.observableArrayList();
        }
    }
    
    private VBox createNewView() {
        VBox newRoot = new VBox(15);
        newRoot.setPadding(new Insets(40));
        newRoot.setAlignment(Pos.CENTER);
        newRoot.setStyle("-fx-background-color: #f5f5f5;");
        
        Label titleLabel = new Label("Agence de Recrutement");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        Label subtitleLabel = new Label("Connexion");
        subtitleLabel.setStyle("-fx-font-size: 16px;");
        
        // Charger les suggestions pour la nouvelle vue
        loadLoginSuggestions();
        
        ComboBox<String> newLoginCombo = new ComboBox<>(loginSuggestions);
        newLoginCombo.setEditable(true);
        newLoginCombo.setPromptText("Login");
        newLoginCombo.setPrefWidth(250);
        newLoginCombo.setVisibleRowCount(5);
        
        // Configurer l'autocomplétion
        newLoginCombo.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                newLoginCombo.setItems(loginSuggestions);
            } else {
                ObservableList<String> filtered = FXCollections.observableArrayList(
                    loginSuggestions.stream()
                        .filter(login -> login.toLowerCase().startsWith(newValue.toLowerCase()))
                        .collect(Collectors.toList())
                );
                newLoginCombo.setItems(filtered);
            }
        });
        
        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Mot de passe");
        newPasswordField.setPrefWidth(250);
        
        Label newErrorLabel = new Label();
        newErrorLabel.setTextFill(Color.RED);
        newErrorLabel.setVisible(false);
        
        Button newLoginButton = new Button("Se connecter");
        newLoginButton.setPrefWidth(250);
        newLoginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        newLoginButton.setOnAction(e -> {
            String login = newLoginCombo.getEditor().getText();
            String password = newPasswordField.getText();
            
            if (login.isEmpty() || password.isEmpty()) {
                showErrorInView(newErrorLabel, "Veuillez remplir tous les champs");
                return;
            }
            
            try {
                utilisateurConnecte = authentificationService.authentifier(login, password);
                newErrorLabel.setVisible(false);
                
                // Rediriger vers le tableau de bord approprié selon le rôle
                MainController mainController = applicationContext.getBean(MainController.class);
                mainController.setUtilisateurConnecte(utilisateurConnecte);
                mainController.setApplicationContext(applicationContext);
                
                Stage stage = (Stage) newLoginButton.getScene().getWindow();
                Scene scene = new Scene(mainController.getView(), 1200, 800);
                stage.setTitle("Agence de Recrutement - Tableau de bord");
                stage.setScene(scene);
                stage.setMaximized(true);
                
            } catch (Exception ex) {
                showErrorInView(newErrorLabel, ex.getMessage());
            }
        });
        
        Button newInscriptionButton = new Button("S'inscrire");
        newInscriptionButton.setPrefWidth(250);
        newInscriptionButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px;");
        newInscriptionButton.setOnAction(e -> handleInscription());
        
        Button newMotDePasseOublieButton = new Button("Mot de passe oublié");
        newMotDePasseOublieButton.setPrefWidth(250);
        newMotDePasseOublieButton.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; -fx-font-size: 12px; -fx-border-color: #5a6268; -fx-border-radius: 3px;");
        newMotDePasseOublieButton.setOnAction(e -> handleMotDePasseOublie());
        
        // Permettre la connexion avec Enter
        newPasswordField.setOnKeyPressed(ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                String login = newLoginCombo.getEditor().getText();
                String password = newPasswordField.getText();
                
                if (login.isEmpty() || password.isEmpty()) {
                    showErrorInView(newErrorLabel, "Veuillez remplir tous les champs");
                    return;
                }
                
                try {
                    utilisateurConnecte = authentificationService.authentifier(login, password);
                    newErrorLabel.setVisible(false);
                    
                    MainController mainController = applicationContext.getBean(MainController.class);
                    mainController.setUtilisateurConnecte(utilisateurConnecte);
                    mainController.setApplicationContext(applicationContext);
                    
                    Stage stage = (Stage) newPasswordField.getScene().getWindow();
                    Scene scene = new Scene(mainController.getView(), 1200, 800);
                    stage.setTitle("Agence de Recrutement - Tableau de bord");
                    stage.setScene(scene);
                    stage.setMaximized(true);
                    
                } catch (Exception ex) {
                    showErrorInView(newErrorLabel, ex.getMessage());
                }
            }
        });
        
        newRoot.getChildren().addAll(titleLabel, subtitleLabel, newLoginCombo, newPasswordField, newLoginButton, newInscriptionButton, newMotDePasseOublieButton, newErrorLabel);
        
        return newRoot;
    }
    
    private void showErrorInView(Label errorLabel, String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
    
    private void handleLogin() {
        String login = loginCombo.getEditor().getText();
        String password = passwordField.getText();
        
        if (login.isEmpty() || password.isEmpty()) {
            showError("Veuillez remplir tous les champs");
            return;
        }
        
        try {
            utilisateurConnecte = authentificationService.authentifier(login, password);
            errorLabel.setVisible(false);
            
            // Rediriger vers le tableau de bord approprié selon le rôle
            MainController mainController = applicationContext.getBean(MainController.class);
            mainController.setUtilisateurConnecte(utilisateurConnecte);
            mainController.setApplicationContext(applicationContext);
            
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(mainController.getView(), 1200, 800);
            stage.setTitle("Agence de Recrutement - Tableau de bord");
            stage.setScene(scene);
            stage.setMaximized(true);
            
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }
    
    private void handleInscription() {
        // Dialog pour choisir le type d'inscription
        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("Entreprise", "Entreprise", "Demandeur d'emploi");
        choiceDialog.setTitle("Inscription");
        choiceDialog.setHeaderText("Type de compte");
        choiceDialog.setContentText("Choisissez le type de compte :");
        
        java.util.Optional<String> result = choiceDialog.showAndWait();
        if (result.isPresent()) {
            boolean isEntreprise = result.get().equals("Entreprise");
            InscriptionDialog dialog = new InscriptionDialog(entrepriseService, demandeurEmploiService, isEntreprise);
            java.util.Optional<Object> inscriptionResult = dialog.showAndWait();
            
            if (inscriptionResult.isPresent()) {
                Object resultObject = inscriptionResult.get();
                
                try {
                    if (!isEntreprise && resultObject instanceof DemandeurEmploi) {
                        // C'est un demandeur d'emploi, il faut gérer le CV
                        DemandeurEmploi demandeur = (DemandeurEmploi) resultObject;
                        File cvFile = dialog.getSelectedCVFile();
                        
                        if (cvFile != null) {
                            // Convertir le fichier en MultipartFile
                            MultipartFile multipartFile = convertFileToMultipartFile(cvFile);
                            
                            // Créer le document CV
                            Document cvDocument = documentService.uploadDocument(
                                multipartFile, 
                                demandeur, 
                                "CV"
                            );
                            
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Succès");
                            alert.setHeaderText("Inscription réussie");
                            alert.setContentText("Votre compte et votre CV ont été créés avec succès. Votre CV est automatiquement validé et visible par les entreprises. Vous pouvez maintenant vous connecter.");
                            alert.showAndWait();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Erreur");
                            alert.setHeaderText("CV manquant");
                            alert.setContentText("Une erreur est survenue lors du traitement de votre CV.");
                            alert.showAndWait();
                        }
                    } else {
                        // C'est une entreprise, inscription normale
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Succès");
                        alert.setHeaderText("Inscription réussie");
                        alert.setContentText("Votre compte a été créé avec succès. Vous pouvez maintenant vous connecter.");
                        alert.showAndWait();
                    }
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Erreur lors de l'inscription");
                    alert.setContentText("Une erreur est survenue: " + e.getMessage());
                    alert.showAndWait();
                }
            }
        }
    }
    
    private void handleMotDePasseOublie() {
        MotDePasseOublieDialog dialog = new MotDePasseOublieDialog();
        dialog.showAndWait();
    }
    
    private MultipartFile convertFileToMultipartFile(File file) throws IOException {
        byte[] fileContent = Files.readAllBytes(file.toPath());
        String contentType = Files.probeContentType(file.toPath());
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        
        final String finalContentType = contentType;
        
        return new MultipartFile() {
            @Override
            public String getName() {
                return file.getName();
            }
            
            @Override
            public String getOriginalFilename() {
                return file.getName();
            }
            
            @Override
            public String getContentType() {
                return finalContentType;
            }
            
            @Override
            public boolean isEmpty() {
                return fileContent.length == 0;
            }
            
            @Override
            public long getSize() {
                return fileContent.length;
            }
            
            @Override
            public byte[] getBytes() throws IOException {
                return fileContent;
            }
            
            @Override
            public java.io.InputStream getInputStream() throws IOException {
                return new java.io.ByteArrayInputStream(fileContent);
            }
            
            @Override
            public void transferTo(java.io.File dest) throws IOException {
                Files.write(dest.toPath(), fileContent);
            }
        };
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
    
    public VBox getView() {
        // Toujours créer une nouvelle vue si le root existe déjà (pour éviter les conflits)
        // Sauf si c'est la première fois (root n'a pas encore de scène)
        if (root != null) {
            // Si le root est déjà dans une scène, créer une nouvelle vue
            if (root.getScene() != null) {
                System.out.println("LoginController: root déjà dans une scène, création d'une nouvelle vue");
                return createNewView();
            }
            // Si c'est la première fois, retourner le root existant
            return root;
        }
        // Si root est null, initialiser la vue
        initializeView();
        return root;
    }
    
    public Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }
    
    public void reset() {
        // Réinitialiser les champs et l'utilisateur connecté
        utilisateurConnecte = null;
        // Recharger les suggestions au cas où de nouveaux utilisateurs ont été créés
        loadLoginSuggestions();
        if (loginCombo != null) {
            loginCombo.getEditor().clear();
            loginCombo.setItems(loginSuggestions);
        }
        if (passwordField != null) {
            passwordField.clear();
        }
        if (errorLabel != null) {
            errorLabel.setVisible(false);
            errorLabel.setText("");
        }
    }
}

