package com.example.agencerecrutement.javafx.dialogs;

import com.example.agencerecrutement.model.Candidature;
import com.example.agencerecrutement.model.Edition;
import com.example.agencerecrutement.model.Offre;
import com.example.agencerecrutement.service.CandidatureService;
import com.example.agencerecrutement.service.JournalService;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class CandidatureDialog extends Dialog<Candidature> {
    
    private final CandidatureService candidatureService;
    private final JournalService journalService;
    private final Long idDemandeur;
    private final Offre offre;
    
    private ComboBox<Edition> editionComboBox;
    
    public CandidatureDialog(CandidatureService candidatureService,
                            JournalService journalService,
                            Long idDemandeur,
                            Offre offre) {
        this.candidatureService = candidatureService;
        this.journalService = journalService;
        this.idDemandeur = idDemandeur;
        this.offre = offre;
        
        setTitle("Postuler à une offre");
        setHeaderText("Postuler à : " + offre.getTitre());
        
        // Créer les boutons
        ButtonType postulerButtonType = new ButtonType("Postuler", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(postulerButtonType, ButtonType.CANCEL);
        
        // Créer le formulaire
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        // Afficher les informations de l'offre
        Label titreLabel = new Label("Titre : " + offre.getTitre());
        Label expLabel = new Label("Expérience requise : " + offre.getExperienceRequise() + " ans");
        Label postesLabel = new Label("Nombre de postes : " + offre.getNbPostes());
        
        editionComboBox = new ComboBox<>();
        try {
            // Charger les éditions (en pratique, vous devriez charger les éditions liées aux publications de l'offre)
            editionComboBox.setItems(FXCollections.observableArrayList(journalService.getAllJournaux().stream()
                .flatMap(j -> journalService.getEditionsByJournal(j.getCodeJournal()).stream())
                .toList()));
            editionComboBox.setCellFactory(param -> new ListCell<Edition>() {
                @Override
                protected void updateItem(Edition edition, boolean empty) {
                    super.updateItem(edition, empty);
                    if (empty || edition == null) {
                        setText(null);
                    } else {
                        setText(edition.toString());
                    }
                }
            });
            editionComboBox.setButtonCell(new ListCell<Edition>() {
                @Override
                protected void updateItem(Edition edition, boolean empty) {
                    super.updateItem(edition, empty);
                    if (empty || edition == null) {
                        setText(null);
                    } else {
                        setText(edition.toString());
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        grid.add(titreLabel, 0, 0, 2, 1);
        grid.add(expLabel, 0, 1, 2, 1);
        grid.add(postesLabel, 0, 2, 2, 1);
        grid.add(new Label("Édition du journal où vous avez vu l'offre :"), 0, 3);
        grid.add(editionComboBox, 1, 3);
        
        getDialogPane().setContent(grid);
        
        // Valider avant de fermer
        final Button postulerButton = (Button) getDialogPane().lookupButton(postulerButtonType);
        postulerButton.setOnAction(e -> {
            if (!validerFormulaire()) {
                e.consume();
            }
        });
        
        // Convertir le résultat
        setResultConverter(dialogButton -> {
            if (dialogButton == postulerButtonType) {
                try {
                    Edition edition = editionComboBox.getValue();
                    if (edition != null) {
                        return candidatureService.postuler(idDemandeur, offre.getIdOffre(), edition.getIdEdition());
                    }
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Erreur lors de la candidature");
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
                    return null;
                }
            }
            return null;
        });
    }
    
    private boolean validerFormulaire() {
        if (editionComboBox.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation");
            alert.setHeaderText("Champ requis");
            alert.setContentText("Veuillez sélectionner l'édition du journal");
            alert.showAndWait();
            return false;
        }
        return true;
    }
}


