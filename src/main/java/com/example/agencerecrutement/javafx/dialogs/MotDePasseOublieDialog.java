package com.example.agencerecrutement.javafx.dialogs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class MotDePasseOublieDialog {
    
    private Dialog<Void> dialog;
    
    public MotDePasseOublieDialog() {
        createDialog();
    }
    
    private void createDialog() {
        dialog = new Dialog<>();
        dialog.setTitle("Mot de passe oublié");
        dialog.setHeaderText("Récupération de mot de passe");
        
        // Créer le contenu
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.CENTER);
        
        // Message principal
        Text mainMessage = new Text("Pour récupérer votre mot de passe, veuillez contacter le bureau sur un de ces emails :");
        mainMessage.setTextAlignment(TextAlignment.CENTER);
        mainMessage.setWrappingWidth(400);
        
        // Liste des emails
        VBox emailsBox = new VBox(10);
        emailsBox.setAlignment(Pos.CENTER);
        emailsBox.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 15px; -fx-border-radius: 5px; -fx-border-color: #dee2e6;");
        
        Label email1 = new Label("1. HibaZouitina@gmail.com");
        email1.setStyle("-fx-font-weight: bold; -fx-text-fill: #007bff;");
        
        Label email2 = new Label("2. Saida27stifi@gmail.com");
        email2.setStyle("-fx-font-weight: bold; -fx-text-fill: #007bff;");
        
        Label email3 = new Label("3. Imanetaleb@gmail.com");
        email3.setStyle("-fx-font-weight: bold; -fx-text-fill: #007bff;");
        
        Label email4 = new Label("4. chouaibbouslamti7@gmail.com");
        email4.setStyle("-fx-font-weight: bold; -fx-text-fill: #007bff;");
        
        emailsBox.getChildren().addAll(email1, email2, email3, email4);
        
        // Message informatif
        Text infoMessage = new Text("Notre équipe vous répondra dans les plus brefs délais pour vous aider à récupérer l'accès à votre compte.");
        infoMessage.setTextAlignment(TextAlignment.CENTER);
        infoMessage.setWrappingWidth(400);
        infoMessage.setFont(Font.font("System", FontWeight.NORMAL, 12));
        
        content.getChildren().addAll(mainMessage, emailsBox, infoMessage);
        
        // Créer les boutons
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk);
        
        dialog.getDialogPane().setContent(content);
        
        // Centrer le bouton OK
        Button okButton = (Button) dialog.getDialogPane().lookupButton(buttonTypeOk);
        okButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold;");
    }
    
    public void showAndWait() {
        dialog.showAndWait();
    }
}
