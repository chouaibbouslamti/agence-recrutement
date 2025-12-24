package com.example.agencerecrutement.javafx.dialogs;

import com.example.agencerecrutement.model.DemandeurEmploi;
import com.example.agencerecrutement.model.Entreprise;
import com.example.agencerecrutement.service.DemandeurEmploiService;
import com.example.agencerecrutement.service.EntrepriseService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.File;

public class InscriptionDialog extends Dialog<Object> {
    
    private final EntrepriseService entrepriseService;
    private final DemandeurEmploiService demandeurEmploiService;
    private final boolean isEntreprise;
    
    // Champs communs
    private TextField loginField;
    private PasswordField passwordField;
    
    // Champs entreprise
    private TextField raisonSocialeField;
    private TextField adresseField;
    private TextField telephoneField;
    private TextArea descriptionArea;
    
    // Champs demandeur
    private TextField nomField;
    private TextField prenomField;
    private TextField faxField;
    private TextField diplomeField;
    private Spinner<Integer> experienceSpinner;
    private Spinner<Double> salaireSpinner;
    private Label cvPathLabel;
    private File selectedCVFile;
    
    public InscriptionDialog(EntrepriseService entrepriseService,
                            DemandeurEmploiService demandeurEmploiService,
                            boolean isEntreprise) {
        this.entrepriseService = entrepriseService;
        this.demandeurEmploiService = demandeurEmploiService;
        this.isEntreprise = isEntreprise;
        
        setTitle(isEntreprise ? "Inscription Entreprise" : "Inscription Demandeur d'emploi");
        setHeaderText(isEntreprise ? "Créer un compte entreprise" : "Créer un compte demandeur d'emploi");
        
        // Créer les boutons
        ButtonType inscrireButtonType = new ButtonType("S'inscrire", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(inscrireButtonType, ButtonType.CANCEL);
        
        // Créer le formulaire
        GridPane grid = createForm();
        getDialogPane().setContent(grid);
        
        // Valider avant de fermer
        final Button inscrireButton = (Button) getDialogPane().lookupButton(inscrireButtonType);
        inscrireButton.setOnAction(e -> {
            if (!validerFormulaire()) {
                e.consume();
            }
        });
        
        // Convertir le résultat
        setResultConverter(dialogButton -> {
            if (dialogButton == inscrireButtonType) {
                try {
                    if (isEntreprise) {
                        return entrepriseService.creerEntreprise(
                            loginField.getText(),
                            passwordField.getText(),
                            raisonSocialeField.getText(),
                            adresseField.getText(),
                            telephoneField.getText(),
                            descriptionArea.getText()
                        );
                    } else {
                        return demandeurEmploiService.creerDemandeurEmploi(
                            loginField.getText(),
                            passwordField.getText(),
                            nomField.getText(),
                            prenomField.getText(),
                            adresseField.getText(),
                            telephoneField.getText(),
                            faxField.getText(),
                            diplomeField.getText(),
                            experienceSpinner.getValue(),
                            salaireSpinner.getValue()
                        );
                    }
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Erreur lors de l'inscription");
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
                    return null;
                }
            }
            return null;
        });
    }
    
    private GridPane createForm() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        int row = 0;
        
        // Champs communs
        loginField = new TextField();
        passwordField = new PasswordField();
        grid.add(new Label("Login :"), 0, row);
        grid.add(loginField, 1, row++);
        grid.add(new Label("Mot de passe :"), 0, row);
        grid.add(passwordField, 1, row++);
        
        if (isEntreprise) {
            raisonSocialeField = new TextField();
            adresseField = new TextField();
            telephoneField = new TextField();
            descriptionArea = new TextArea();
            descriptionArea.setPrefRowCount(3);
            
            grid.add(new Label("Raison sociale :"), 0, row);
            grid.add(raisonSocialeField, 1, row++);
            grid.add(new Label("Adresse :"), 0, row);
            grid.add(adresseField, 1, row++);
            grid.add(new Label("Téléphone :"), 0, row);
            grid.add(telephoneField, 1, row++);
            grid.add(new Label("Description :"), 0, row);
            grid.add(descriptionArea, 1, row++);
        } else {
            nomField = new TextField();
            prenomField = new TextField();
            adresseField = new TextField();
            telephoneField = new TextField();
            faxField = new TextField();
            diplomeField = new TextField();
            experienceSpinner = new Spinner<>(0, 50, 0, 1);
            experienceSpinner.setEditable(true);
            salaireSpinner = new Spinner<>(0.0, 100000.0, 0.0, 100.0);
            salaireSpinner.setEditable(true);
            
            grid.add(new Label("Nom :"), 0, row);
            grid.add(nomField, 1, row++);
            grid.add(new Label("Prénom :"), 0, row);
            grid.add(prenomField, 1, row++);
            grid.add(new Label("Adresse :"), 0, row);
            grid.add(adresseField, 1, row++);
            grid.add(new Label("Téléphone :"), 0, row);
            grid.add(telephoneField, 1, row++);
            grid.add(new Label("Fax :"), 0, row);
            grid.add(faxField, 1, row++);
            grid.add(new Label("Diplôme :"), 0, row);
            grid.add(diplomeField, 1, row++);
            grid.add(new Label("Expérience (années) :"), 0, row);
            grid.add(experienceSpinner, 1, row++);
            grid.add(new Label("Salaire souhaité :"), 0, row);
            grid.add(salaireSpinner, 1, row++);
            
            // Champ CV obligatoire pour les demandeurs
            cvPathLabel = new Label("Aucun CV sélectionné");
            cvPathLabel.setStyle("-fx-text-fill: gray; -fx-font-style: italic;");
            Button btnChoisirCV = new Button("Choisir un CV (PNG)");
            btnChoisirCV.setOnAction(e -> choisirCV());
            
            HBox cvBox = new HBox(10, btnChoisirCV, cvPathLabel);
            grid.add(new Label("CV (PNG obligatoire) :"), 0, row);
            grid.add(cvBox, 1, row++);
        }
        
        return grid;
    }
    
    private boolean validerFormulaire() {
        if (loginField.getText().trim().isEmpty() || passwordField.getText().isEmpty()) {
            showError("Le login et le mot de passe sont obligatoires");
            return false;
        }
        
        if (isEntreprise) {
            if (raisonSocialeField.getText().trim().isEmpty() ||
                adresseField.getText().trim().isEmpty() ||
                telephoneField.getText().trim().isEmpty()) {
                showError("Veuillez remplir tous les champs obligatoires");
                return false;
            }
        } else {
            if (nomField.getText().trim().isEmpty() ||
                prenomField.getText().trim().isEmpty() ||
                adresseField.getText().trim().isEmpty() ||
                telephoneField.getText().trim().isEmpty() ||
                diplomeField.getText().trim().isEmpty()) {
                showError("Veuillez remplir tous les champs obligatoires");
                return false;
            }
            
            // Validation du CV obligatoire
            if (selectedCVFile == null) {
                showError("Veuillez sélectionner un CV au format PNG");
                return false;
            }
            
            // Validation du format PNG uniquement
            String fileName = selectedCVFile.getName().toLowerCase();
            if (!fileName.endsWith(".png")) {
                showError("Le CV doit être au format PNG uniquement");
                return false;
            }
        }
        return true;
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation");
        alert.setHeaderText("Champs invalides");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void choisirCV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner votre CV");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Fichiers PNG", "*.png")
        );
        
        File file = fileChooser.showOpenDialog(getOwner());
        if (file != null) {
            String fileName = file.getName().toLowerCase();
            if (fileName.endsWith(".png")) {
                selectedCVFile = file;
                cvPathLabel.setText(file.getName());
                cvPathLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            } else {
                showError("Veuillez sélectionner un fichier au format PNG uniquement");
            }
        }
    }
    
    public File getSelectedCVFile() {
        return selectedCVFile;
    }
}

