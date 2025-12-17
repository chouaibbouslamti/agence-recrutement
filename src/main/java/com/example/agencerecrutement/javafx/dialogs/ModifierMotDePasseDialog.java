package com.example.agencerecrutement.javafx.dialogs;

import com.example.agencerecrutement.model.Utilisateur;
import com.example.agencerecrutement.service.AuthentificationService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Optional;

public class ModifierMotDePasseDialog {
    
    private final AuthentificationService authentificationService;
    private final Utilisateur utilisateur;
    private Dialog<String> dialog;
    
    public ModifierMotDePasseDialog(AuthentificationService authentificationService, Utilisateur utilisateur) {
        this.authentificationService = authentificationService;
        this.utilisateur = utilisateur;
        createDialog();
    }
    
    private void createDialog() {
        dialog = new Dialog<>();
        dialog.setTitle("Modifier le mot de passe");
        dialog.setHeaderText("Modifier le mot de passe de : " + utilisateur.getLogin() + 
                          "\n(" + utilisateur.getRole() + ")");
        
        // Créer les champs
        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Nouveau mot de passe");
        newPasswordField.setPrefWidth(250);
        
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirmer le mot de passe");
        confirmPasswordField.setPrefWidth(250);
        
        // Créer le layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        grid.add(new Label("Nouveau mot de passe:"), 0, 0);
        grid.add(newPasswordField, 1, 0);
        grid.add(new Label("Confirmer le mot de passe:"), 0, 1);
        grid.add(confirmPasswordField, 1, 1);
        
        // Créer les boutons
        ButtonType buttonTypeOk = new ButtonType("Modifier", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, buttonTypeCancel);
        
        dialog.getDialogPane().setContent(grid);
        
        // Valider et désactiver/activer le bouton OK
        Button okButton = (Button) dialog.getDialogPane().lookupButton(buttonTypeOk);
        okButton.setDisable(true);
        
        // Validation en temps réel
        newPasswordField.textProperty().addListener((obs, oldVal, newVal) -> {
            validateFields(newPasswordField, confirmPasswordField, okButton);
        });
        
        confirmPasswordField.textProperty().addListener((obs, oldVal, newVal) -> {
            validateFields(newPasswordField, confirmPasswordField, okButton);
        });
        
        // Résultat quand on clique sur OK
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeOk) {
                return newPasswordField.getText();
            }
            return null;
        });
    }
    
    private void validateFields(PasswordField newPasswordField, PasswordField confirmPasswordField, Button okButton) {
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        boolean isValid = !newPassword.isEmpty() && 
                        !confirmPassword.isEmpty() && 
                        newPassword.equals(confirmPassword) &&
                        newPassword.length() >= 6;
        
        okButton.setDisable(!isValid);
        
        if (!newPassword.isEmpty() && newPassword.length() < 6) {
            newPasswordField.setStyle("-fx-border-color: red;");
        } else {
            newPasswordField.setStyle("");
        }
        
        if (!confirmPassword.isEmpty() && !newPassword.equals(confirmPassword)) {
            confirmPasswordField.setStyle("-fx-border-color: red;");
        } else {
            confirmPasswordField.setStyle("");
        }
    }
    
    public Optional<String> showAndWait() {
        Optional<String> result = dialog.showAndWait();
        
        if (result.isPresent()) {
            try {
                authentificationService.modifierMotDePasse(utilisateur.getIdUtilisateur(), result.get());
                
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Succès");
                successAlert.setHeaderText("Mot de passe modifié");
                successAlert.setContentText("Le mot de passe de " + utilisateur.getLogin() + " a été modifié avec succès.");
                successAlert.showAndWait();
                
                return result;
            } catch (Exception ex) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erreur");
                errorAlert.setHeaderText("Erreur lors de la modification");
                errorAlert.setContentText(ex.getMessage());
                errorAlert.showAndWait();
            }
        }
        
        return Optional.empty();
    }
}
