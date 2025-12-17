package com.example.agencerecrutement.javafx.dialogs;

import com.example.agencerecrutement.model.Abonnement;
import com.example.agencerecrutement.model.Edition;
import com.example.agencerecrutement.model.Journal;
import com.example.agencerecrutement.model.Offre;
import com.example.agencerecrutement.model.PublicationOffre;
import com.example.agencerecrutement.service.EntrepriseService;
import com.example.agencerecrutement.service.JournalService;
import com.example.agencerecrutement.service.OffreService;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class PublierOffreDialog extends Dialog<PublicationOffre> {

    private final OffreService offreService;
    private final EntrepriseService entrepriseService;
    private final JournalService journalService;
    private final Long idEntreprise;
    private final Offre offre;

    private ComboBox<Abonnement> abonnementCombo;
    private ComboBox<Edition> editionCombo;

    public PublierOffreDialog(OffreService offreService,
                              EntrepriseService entrepriseService,
                              JournalService journalService,
                              Long idEntreprise,
                              Offre offre) {
        this.offreService = offreService;
        this.entrepriseService = entrepriseService;
        this.journalService = journalService;
        this.idEntreprise = idEntreprise;
        this.offre = offre;

        setTitle("Publier l'offre");
        setHeaderText("Publier l'offre : " + offre.getTitre());

        ButtonType publierButtonType = new ButtonType("Publier", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(publierButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        Label offreLabel = new Label("Offre : " + offre.getTitre());

        abonnementCombo = new ComboBox<>();
        editionCombo = new ComboBox<>();

        try {
            var abonnements = entrepriseService.getAbonnements(idEntreprise);
            abonnements.removeIf(a -> !a.estActif());
            abonnementCombo.setItems(FXCollections.observableArrayList(abonnements));
            abonnementCombo.setPromptText("Choisissez un abonnement (journal)");
        } catch (Exception e) {
            e.printStackTrace();
        }

        abonnementCombo.setCellFactory(param -> new ListCell<Abonnement>() {
            @Override
            protected void updateItem(Abonnement item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    Journal j = item.getJournal();
                    setText(j.getNom() + " (" + j.getCodeJournal() + ")");
                }
            }
        });
        abonnementCombo.setButtonCell(new ListCell<Abonnement>() {
            @Override
            protected void updateItem(Abonnement item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    Journal j = item.getJournal();
                    setText(j.getNom() + " (" + j.getCodeJournal() + ")");
                }
            }
        });

        abonnementCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            editionCombo.getItems().clear();
            if (newVal != null) {
                Journal j = newVal.getJournal();
                var editions = journalService.getEditionsByJournal(j.getCodeJournal());
                editionCombo.setItems(FXCollections.observableArrayList(editions));
            }
        });

        editionCombo.setPromptText("Choisissez une édition");

        grid.add(offreLabel, 0, 0, 2, 1);
        grid.add(new Label("Abonnement / Journal :"), 0, 1);
        grid.add(abonnementCombo, 1, 1);
        grid.add(new Label("Édition :"), 0, 2);
        grid.add(editionCombo, 1, 2);

        getDialogPane().setContent(grid);

        final Button publierButton = (Button) getDialogPane().lookupButton(publierButtonType);
        publierButton.setOnAction(e -> {
            if (!validerFormulaire()) {
                e.consume();
            }
        });

        setResultConverter(dialogButton -> {
            if (dialogButton == publierButtonType) {
                Abonnement ab = abonnementCombo.getValue();
                Edition ed = editionCombo.getValue();
                if (ab == null || ed == null) {
                    return null;
                }
                try {
                    return offreService.publierOffre(offre.getIdOffre(), ab.getIdAbonnement(), ed.getIdEdition());
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Erreur lors de la publication");
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
                    return null;
                }
            }
            return null;
        });
    }

    private boolean validerFormulaire() {
        if (abonnementCombo.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation");
            alert.setHeaderText("Champ requis");
            alert.setContentText("Veuillez choisir un abonnement (journal).");
            alert.showAndWait();
            return false;
        }
        if (editionCombo.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation");
            alert.setHeaderText("Champ requis");
            alert.setContentText("Veuillez choisir une édition.");
            alert.showAndWait();
            return false;
        }
        return true;
    }
}


