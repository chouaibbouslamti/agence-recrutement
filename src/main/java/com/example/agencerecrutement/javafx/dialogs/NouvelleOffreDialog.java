package com.example.agencerecrutement.javafx.dialogs;

import com.example.agencerecrutement.model.Offre;
import com.example.agencerecrutement.service.OffreService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NouvelleOffreDialog extends Dialog<Offre> {
    
    private final OffreService offreService;
    private final Long idEntreprise;
    
    private TextField titreField;
    private TextArea competencesArea;
    private Spinner<Integer> experienceSpinner;
    private Spinner<Integer> nbPostesSpinner;
    
    public NouvelleOffreDialog(OffreService offreService, Long idEntreprise) {
        this.offreService = offreService;
        this.idEntreprise = idEntreprise;
        
        setTitle("Nouvelle offre d'emploi");
        setHeaderText("Créer une nouvelle offre d'emploi");
        
        // Créer les boutons
        ButtonType creerButtonType = new ButtonType("Créer", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(creerButtonType, ButtonType.CANCEL);
        
        // Créer le formulaire
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        titreField = new TextField();
        titreField.setPromptText("Titre de l'offre");
        
        competencesArea = new TextArea();
        competencesArea.setPromptText("Compétences requises");
        competencesArea.setPrefRowCount(5);
        
        experienceSpinner = new Spinner<>(0, 50, 0, 1);
        experienceSpinner.setEditable(true);
        
        nbPostesSpinner = new Spinner<>(1, 100, 1, 1);
        nbPostesSpinner.setEditable(true);
        
        grid.add(new Label("Titre :"), 0, 0);
        grid.add(titreField, 1, 0);
        grid.add(new Label("Compétences :"), 0, 1);
        grid.add(competencesArea, 1, 1);
        grid.add(new Label("Expérience requise (années) :"), 0, 2);
        grid.add(experienceSpinner, 1, 2);
        grid.add(new Label("Nombre de postes :"), 0, 3);
        grid.add(nbPostesSpinner, 1, 3);
        
        getDialogPane().setContent(grid);
        
        // Valider avant de fermer
        final Button creerButton = (Button) getDialogPane().lookupButton(creerButtonType);
        creerButton.setOnAction(e -> {
            if (!validerFormulaire()) {
                e.consume();
            }
        });
        
        // Convertir le résultat
        setResultConverter(dialogButton -> {
            if (dialogButton == creerButtonType) {
                try {
                    return offreService.creerOffre(
                        idEntreprise,
                        titreField.getText(),
                        competencesArea.getText(),
                        experienceSpinner.getValue(),
                        nbPostesSpinner.getValue()
                    );
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Erreur lors de la création de l'offre");
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
                    return null;
                }
            }
            return null;
        });
    }
    
    private boolean validerFormulaire() {
        if (titreField.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation");
            alert.setHeaderText("Champ requis");
            alert.setContentText("Le titre est obligatoire");
            alert.showAndWait();
            return false;
        }
        return true;
    }
}


