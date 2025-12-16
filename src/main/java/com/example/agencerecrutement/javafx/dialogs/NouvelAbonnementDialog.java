package com.example.agencerecrutement.javafx.dialogs;

import com.example.agencerecrutement.model.Abonnement;
import com.example.agencerecrutement.model.Journal;
import com.example.agencerecrutement.service.EntrepriseService;
import com.example.agencerecrutement.service.JournalService;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;

import java.time.LocalDate;

public class NouvelAbonnementDialog extends Dialog<Abonnement> {
    
    private final EntrepriseService entrepriseService;
    private final JournalService journalService;
    private final Long idEntreprise;
    
    private ComboBox<Journal> journalComboBox;
    private DatePicker dateExpirationPicker;
    
    public NouvelAbonnementDialog(EntrepriseService entrepriseService, 
                                 JournalService journalService,
                                 Long idEntreprise) {
        this.entrepriseService = entrepriseService;
        this.journalService = journalService;
        this.idEntreprise = idEntreprise;
        
        setTitle("Nouvel abonnement");
        setHeaderText("Souscrire un nouvel abonnement");
        
        // Créer les boutons
        ButtonType souscrireButtonType = new ButtonType("Souscrire", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(souscrireButtonType, ButtonType.CANCEL);
        
        // Créer le formulaire
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        journalComboBox = new ComboBox<>();
        try {
            journalComboBox.setItems(FXCollections.observableArrayList(journalService.getAllJournaux()));
            journalComboBox.setCellFactory(param -> new ListCell<Journal>() {
                @Override
                protected void updateItem(Journal journal, boolean empty) {
                    super.updateItem(journal, empty);
                    if (empty || journal == null) {
                        setText(null);
                    } else {
                        setText(journal.getNom() + " (" + journal.getCodeJournal() + ")");
                    }
                }
            });
            journalComboBox.setButtonCell(new ListCell<Journal>() {
                @Override
                protected void updateItem(Journal journal, boolean empty) {
                    super.updateItem(journal, empty);
                    if (empty || journal == null) {
                        setText(null);
                    } else {
                        setText(journal.getNom() + " (" + journal.getCodeJournal() + ")");
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        dateExpirationPicker = new DatePicker();
        dateExpirationPicker.setValue(LocalDate.now().plusMonths(1));
        
        grid.add(new Label("Journal :"), 0, 0);
        grid.add(journalComboBox, 1, 0);
        grid.add(new Label("Date d'expiration :"), 0, 1);
        grid.add(dateExpirationPicker, 1, 1);
        
        getDialogPane().setContent(grid);
        
        // Valider avant de fermer
        final Button souscrireButton = (Button) getDialogPane().lookupButton(souscrireButtonType);
        souscrireButton.setOnAction(e -> {
            if (!validerFormulaire()) {
                e.consume();
            }
        });
        
        // Convertir le résultat
        setResultConverter(dialogButton -> {
            if (dialogButton == souscrireButtonType) {
                try {
                    Journal journal = journalComboBox.getValue();
                    if (journal != null && dateExpirationPicker.getValue() != null) {
                        return entrepriseService.souscrireAbonnement(
                            idEntreprise,
                            journal.getCodeJournal(),
                            dateExpirationPicker.getValue()
                        );
                    }
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Erreur lors de la souscription");
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
                    return null;
                }
            }
            return null;
        });
    }
    
    private boolean validerFormulaire() {
        if (journalComboBox.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation");
            alert.setHeaderText("Champ requis");
            alert.setContentText("Veuillez sélectionner un journal");
            alert.showAndWait();
            return false;
        }
        if (dateExpirationPicker.getValue() == null || 
            dateExpirationPicker.getValue().isBefore(LocalDate.now())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation");
            alert.setHeaderText("Date invalide");
            alert.setContentText("La date d'expiration doit être dans le futur");
            alert.showAndWait();
            return false;
        }
        return true;
    }
}


