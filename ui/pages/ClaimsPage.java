package ui.pages;

import dao.ClaimDAO;
import dao.ItemDAO;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import model.Claim;
import session.Session;
import ui.components.UIComponents;
import ui.styles.Theme;

/**
 * ClaimsPage — Builds the "Manage Claims" / "My Claim History" content panel.
 * Extracted verbatim from CITUFindFX.buildClaimsPage().
 */
public class ClaimsPage {

    private final ClaimDAO  claimDAO;
    private final ItemDAO   itemDAO;
    private final Runnable  onRefresh;

    private final VBox view;

    public ClaimsPage(ClaimDAO claimDAO, ItemDAO itemDAO, Runnable onRefresh) {
        this.claimDAO  = claimDAO;
        this.itemDAO   = itemDAO;
        this.onRefresh = onRefresh;
        view = build();
    }

    private VBox build() {
        VBox page = new VBox(20);
        boolean isAdmin = Session.getCurrentUser().getRole().equalsIgnoreCase("Admin");

        Label title = UIComponents.createPageTitle(isAdmin ? "Manage Claims" : "My Claim History");

        TableView<Claim> claimTable = new TableView<>();
        UIComponents.styleTable(claimTable);

        // Columns (unchanged property names)
        TableColumn<Claim, Integer> cId      = new TableColumn<>("Claim ID");
        cId.setCellValueFactory(new PropertyValueFactory<>("id"));
        cId.setPrefWidth(90);

        TableColumn<Claim, String> cItemName = new TableColumn<>("Item Name");
        cItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));

        TableColumn<Claim, String> cClaimant = new TableColumn<>("Claimant");
        cClaimant.setCellValueFactory(new PropertyValueFactory<>("claimantName"));

        TableColumn<Claim, String> cProof    = new TableColumn<>("Proof of Ownership");
        cProof.setCellValueFactory(new PropertyValueFactory<>("proof"));

        TableColumn<Claim, String> cStatus   = new TableColumn<>("Status");
        cStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        cStatus.setPrefWidth(110);

        claimTable.getColumns().addAll(cId, cItemName, cClaimant, cProof, cStatus);
        claimTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(claimTable, Priority.ALWAYS);

        // Load data (unchanged logic)
        if (isAdmin) {
            claimTable.setItems(FXCollections.observableArrayList(claimDAO.getAllClaims()));
        } else {
            claimTable.setItems(FXCollections.observableArrayList(
                    claimDAO.getClaimsByUser(Session.getCurrentUser().getName())
            ));
        }

        HBox btns = new HBox(12);
        btns.setAlignment(Pos.CENTER_LEFT);

        if (isAdmin) {
            Button btnApprove = UIComponents.createActionBtn("✔  Approve Claim", Theme.SUCCESS);
            Button btnReject  = UIComponents.createActionBtn("✕  Reject Claim",  Theme.DANGER);

            btnApprove.setOnAction(e -> {
                Claim selected = claimTable.getSelectionModel().getSelectedItem();
                if (selected != null && selected.getStatus().equals("Pending")) {
                    claimDAO.updateClaimStatus(selected.getId(), "Approved");
                    itemDAO.updateItemStatus(selected.getItemId(), "Claimed/Returned");
                    claimTable.setItems(FXCollections.observableArrayList(claimDAO.getAllClaims()));
                    onRefresh.run();
                    UIComponents.showAlert(Alert.AlertType.INFORMATION, "Approved", "Claim approved and item marked as returned.");
                }
            });
            btnReject.setOnAction(e -> {
                Claim selected = claimTable.getSelectionModel().getSelectedItem();
                if (selected != null && selected.getStatus().equals("Pending")) {
                    claimDAO.updateClaimStatus(selected.getId(), "Rejected");
                    claimTable.setItems(FXCollections.observableArrayList(claimDAO.getAllClaims()));
                    UIComponents.showAlert(Alert.AlertType.INFORMATION, "Rejected", "Claim rejected.");
                }
            });
            btns.getChildren().addAll(btnApprove, btnReject);
        } else {
            Button btnRefresh = UIComponents.createActionBtn("↺  Refresh", Theme.INFO);
            btnRefresh.setOnAction(e -> claimTable.setItems(FXCollections.observableArrayList(
                    claimDAO.getClaimsByUser(Session.getCurrentUser().getName())
            )));
            btns.getChildren().add(btnRefresh);
        }

        page.getChildren().addAll(title, claimTable, btns);
        return page;
    }

    public VBox getView() { return view; }
}