package com.example.agencerecrutement.javafx.controllers;

import com.example.agencerecrutement.javafx.dialogs.InscriptionDialog;
import com.example.agencerecrutement.model.Utilisateur;
import com.example.agencerecrutement.service.AuthentificationService;
import com.example.agencerecrutement.service.DemandeurEmploiService;
import com.example.agencerecrutement.service.EntrepriseService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class LoginController {
    
    private final AuthentificationService authentificationService;
    private final EntrepriseService entrepriseService;
    private final DemandeurEmploiService demandeurEmploiService;
    private ConfigurableApplicationContext applicationContext;
    private Utilisateur utilisateurConnecte;
    
    private VBox root;
    private TextField loginField;
    private PasswordField passwordField;
    private Label errorLabel;
    private Button loginButton;
    
    public LoginController(AuthentificationService authentificationService,
                          EntrepriseService entrepriseService,
                          DemandeurEmploiService demandeurEmploiService) {
        this.authentificationService = authentificationService;
        this.entrepriseService = entrepriseService;
        this.demandeurEmploiService = demandeurEmploiService;
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
        
        loginField = new TextField();
        loginField.setPromptText("Login");
        loginField.setPrefWidth(250);
        
        passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");
        passwordField.setPrefWidth(250);
        
        loginButton = new Button("Se connecter");
        loginButton.setPrefWidth(250);
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        loginButton.setOnAction(e -> handleLogin());
        
        Button inscriptionButton = new Button("S'inscrire");
        inscriptionButton.setPrefWidth(250);
        inscriptionButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px;");
        inscriptionButton.setOnAction(e -> handleInscription());
        
        errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(false);
        
        root.getChildren().addAll(titleLabel, subtitleLabel, loginField, passwordField, loginButton, inscriptionButton, errorLabel);
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
        
        TextField newLoginField = new TextField();
        newLoginField.setPromptText("Login");
        newLoginField.setPrefWidth(250);
        
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
            String login = newLoginField.getText();
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
        
        newRoot.getChildren().addAll(titleLabel, subtitleLabel, newLoginField, newPasswordField, newLoginButton, newInscriptionButton, newErrorLabel);
        
        return newRoot;
    }
    
    private void showErrorInView(Label errorLabel, String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
    
    private void handleLogin() {
        String login = loginField.getText();
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
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText("Inscription réussie");
                alert.setContentText("Votre compte a été créé avec succès. Vous pouvez maintenant vous connecter.");
                alert.showAndWait();
            }
        }
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
        if (loginField != null) {
            loginField.clear();
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

