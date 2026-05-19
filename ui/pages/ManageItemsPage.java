package ui.pages;

import dao.ActivityDAO;
import dao.ClaimDAO;
import dao.ItemDAO;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Item;
import session.Session;
import ui.components.UIComponents;
import ui.styles.Theme;

import java.io.File;
import java.util.List;

/**
 * ManageItemsPage — Builds the "Manage Items" / "Report & View Items" content panel.
 * All CRUD logic, table setup, form fields, image upload, and claim dialog are
 * extracted verbatim from CITUFindFX.
 *
 * selectedImagePath is managed here via getSelectedImagePath() / setSelectedImagePath().
 * The table and form fields are exposed via getters so CITUFindFX can call
 * refreshTableAndStats() without change.
 */
public class ManageItemsPage {

    // ── State ────────────────────────────────────────────────────────────────
    private String selectedImagePath = "";

    // ── DAOs ─────────────────────────────────────────────────────────────────
    private final ItemDAO     itemDAO;
    private final ActivityDAO activityDAO;
    private final ClaimDAO    claimDAO;
    private final Stage       window;

    // ── Table & form fields (same names as original) ─────────────────────────
    private TableView<Item> table;
    private TextField txtName, txtDesc, txtCategory, txtLocation, txtStatus, txtReportedBy;
    private ComboBox<String> comboLocation;

    // ── Refresh callback ──────────────────────────────────────────────────────
    private final Runnable onRefresh;

    private final VBox view;

    public ManageItemsPage(Stage window, ItemDAO itemDAO, ActivityDAO activityDAO,
                           ClaimDAO claimDAO, Runnable onRefresh) {
        this.window      = window;
        this.itemDAO     = itemDAO;
        this.activityDAO = activityDAO;
        this.claimDAO    = claimDAO;
        this.onRefresh   = onRefresh;
        view = build();
    }

    private VBox build() {
        VBox page = new VBox(20);
        boolean isAdmin = Session.getCurrentUser().getRole().equalsIgnoreCase("Admin");

        Label title = UIComponents.createPageTitle(isAdmin ? "Manage Items" : "Report & View Items");

        // ── Table ────────────────────────────────────────────────────────────
        table = new TableView<>();
        setupTable();
        UIComponents.styleTable(table);
        VBox.setVgrow(table, Priority.ALWAYS);

        // ── Form card ────────────────────────────────────────────────────────
        VBox formCard = new VBox(16);
        formCard.setPadding(new Insets(24));
        formCard.setStyle(
                "-fx-background-color: " + Theme.WHITE + ";" +
                        "-fx-background-radius: 12px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 10, 0, 0, 3);"
        );

        Label formTitle = new Label(isAdmin ? "Item Details" : "Report an Item");
        formTitle.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + Theme.MAROON + ";"
        );

        // Initialize fields (unchanged field names)
        txtName       = new TextField(); txtName.setPromptText("e.g., Black Wallet");
        txtDesc       = new TextField(); txtDesc.setPromptText("Brief description");
        txtCategory   = new TextField(); txtCategory.setPromptText("e.g., Accessories");
        txtStatus     = new TextField(); txtStatus.setPromptText("Lost  or  Found");
        txtReportedBy = new TextField(Session.getCurrentUser().getName());
        txtReportedBy.setEditable(false);

        UIComponents.styleFormField(txtName);
        UIComponents.styleFormField(txtDesc);
        UIComponents.styleFormField(txtCategory);
        UIComponents.styleFormField(txtStatus);
        UIComponents.styleFormField(txtReportedBy);
        txtReportedBy.setStyle(txtReportedBy.getStyle() + "-fx-background-color: " + Theme.SURFACE + ";");

        comboLocation = new ComboBox<>(FXCollections.observableArrayList(
                "Main Library", "GLE Building", "Canteen",
                "Engineering Building", "Rizal Building", "Open Court"
        ));
        comboLocation.setPromptText("Select Location");
        comboLocation.setMaxWidth(Double.MAX_VALUE);
        UIComponents.styleComboBox(comboLocation);

        // Two-column grid
        GridPane grid = new GridPane();
        grid.setHgap(16); grid.setVgap(12);

        grid.add(UIComponents.createFieldLabel("Item Name"),   0, 0); grid.add(txtName,       1, 0);
        grid.add(UIComponents.createFieldLabel("Description"), 2, 0); grid.add(txtDesc,       3, 0);
        grid.add(UIComponents.createFieldLabel("Category"),    0, 1); grid.add(txtCategory,   1, 1);
        grid.add(UIComponents.createFieldLabel("Location"),    2, 1); grid.add(comboLocation, 3, 1);
        grid.add(UIComponents.createFieldLabel("Status"),      0, 2); grid.add(txtStatus,     1, 2);
        grid.add(UIComponents.createFieldLabel("Reported By"), 2, 2); grid.add(txtReportedBy, 3, 2);

        ColumnConstraints labelCol = new ColumnConstraints(90);
        ColumnConstraints fieldCol = new ColumnConstraints(200);
        fieldCol.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().addAll(labelCol, fieldCol, labelCol, fieldCol);

        // Upload button
        Button btnUpload = new Button("📸  Upload Image");
        btnUpload.setStyle(
                "-fx-background-color: " + Theme.SURFACE + ";" +
                        "-fx-border-color: #D1D5DB;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-padding: 8px 16px;" +
                        "-fx-font-size: 13px;" +
                        "-fx-cursor: hand;"
        );
        btnUpload.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(window);
            if (file != null) {
                selectedImagePath = file.getAbsolutePath();
                btnUpload.setText("✅  Image Selected");
                btnUpload.setStyle(btnUpload.getStyle() +
                        "-fx-border-color: " + Theme.SUCCESS + "; -fx-text-fill: " + Theme.SUCCESS + ";");
            }
        });

        // Action buttons
        HBox btns = new HBox(10);
        btns.setAlignment(Pos.CENTER_LEFT);
        Button btnAdd   = UIComponents.createActionBtn(isAdmin ? "＋  Add Item" : "＋  Report Item", Theme.SUCCESS);
        Button btnUpdate = UIComponents.createActionBtn("✎  Update", Theme.INFO);
        Button btnDelete = UIComponents.createActionBtn("✕  Delete", Theme.DANGER);
        Button btnClear = UIComponents.createActionBtn("↺  Clear", Theme.NEUTRAL);


        btnAdd.setOnAction(e   -> addItem());
        btnUpdate.setOnAction(e -> updateItem());
        btnDelete.setOnAction(e -> deleteItem());
        btnClear.setOnAction(e -> clearFields());

        if (isAdmin) {
            btns.getChildren().addAll(btnAdd, btnUpdate, btnDelete, btnClear);

        } else {
            Button btnClaim = UIComponents.createActionBtn("🏷  Claim Item", Theme.GOLD);
            btnClaim.setStyle(btnClaim.getStyle() + "-fx-text-fill: " + Theme.TEXT_PRIMARY + ";");
            btnClaim.setOnAction(e -> handleClaimProcess());
            btns.getChildren().addAll(btnAdd, btnUpdate, btnClaim, btnDelete, btnClear);
        }

        HBox bottomRow = new HBox(16, btnUpload, btns);
        bottomRow.setAlignment(Pos.CENTER_LEFT);

        formCard.getChildren().addAll(formTitle, UIComponents.createDivider(), grid, bottomRow);
        page.getChildren().addAll(title, table, formCard);
        return page;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  TABLE SETUP
    // ════════════════════════════════════════════════════════════════════════
    private void setupTable() {
        TableColumn<Item, Integer> cId        = new TableColumn<>("ID");
        cId.setCellValueFactory(new PropertyValueFactory<>("id"));
        cId.setPrefWidth(55);

        TableColumn<Item, String> cReportedBy = new TableColumn<>("Reported By");
        cReportedBy.setCellValueFactory(new PropertyValueFactory<>("reportedBy"));

        TableColumn<Item, String> cName       = new TableColumn<>("Item Name");
        cName.setCellValueFactory(new PropertyValueFactory<>("itemName"));

        TableColumn<Item, String> cDesc       = new TableColumn<>("Description");
        cDesc.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Item, String> cCat        = new TableColumn<>("Category");
        cCat.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Item, String> cLoc        = new TableColumn<>("Location");
        cLoc.setCellValueFactory(new PropertyValueFactory<>("locationFound"));

        TableColumn<Item, String> cStat       = new TableColumn<>("Status");
        cStat.setCellValueFactory(new PropertyValueFactory<>("status"));
        cStat.setPrefWidth(130);

        table.getColumns().addAll(cId, cReportedBy, cName, cDesc, cCat, cLoc, cStat);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                if (txtReportedBy != null) txtReportedBy.setText(newSel.getReportedBy());
                if (txtName       != null) txtName.setText(newSel.getItemName());
                if (txtDesc       != null) txtDesc.setText(newSel.getDescription());
                if (txtCategory   != null) txtCategory.setText(newSel.getCategory());
                if (txtLocation   != null) txtLocation.setText(newSel.getLocationFound());
                if (txtStatus     != null) txtStatus.setText(newSel.getStatus());
            }
        });
    }

    // ════════════════════════════════════════════════════════════════════════
    //  CRUD LOGIC (unchanged)
    // ════════════════════════════════════════════════════════════════════════
    private void addItem() {
        String name     = txtName.getText().trim();
        String location = comboLocation.getValue();
        String status   = txtStatus.getText().trim();

        if (name.isEmpty() || location == null || status.isEmpty()) {
            UIComponents.showAlert(Alert.AlertType.WARNING, "Input Error", "Please fill in Item Name, Status, and Location.");
            return;
        }

        Item item = new Item(
                name, txtDesc.getText(), txtCategory.getText(),
                location, status,
                Session.getCurrentUser().getName(),
                selectedImagePath, false
        );

        if (itemDAO.isDuplicate(name, location)) {
            UIComponents.showAlert(Alert.AlertType.ERROR, "Duplicate Found", "This item was already reported at this location.");
            return;
        }

        if (itemDAO.addItem(item)) {
            UIComponents.showAlert(Alert.AlertType.INFORMATION, "Success", "Item reported successfully.");
            onRefresh.run();
            clearFields();
        } else {
            UIComponents.showAlert(Alert.AlertType.ERROR, "Database Error", "Check console for SQL errors.");
        }
    }

    private void updateItem() {
        // 1. Get the item selected in the table
        Item selectedItem = table.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            UIComponents.showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an item from the table to update.");
            return;
        }

        // 2. Security Check: Is the user an Admin OR the original reporter?
        boolean isAdmin = Session.getCurrentUser().getRole().equalsIgnoreCase("Admin");
        String currentUserName = Session.getCurrentUser().getName();
        String itemOwner = selectedItem.getReportedBy();

        if (!isAdmin && !currentUserName.equals(itemOwner)) {
            UIComponents.showAlert(Alert.AlertType.ERROR, "Access Denied", "You can only edit items that you reported.");
            return;
        }

        // 3. Prevent editing if the item is already claimed (optional but recommended)
        if (selectedItem.getStatus().equalsIgnoreCase("Claimed") || selectedItem.getStatus().equalsIgnoreCase("Returned")) {
            UIComponents.showAlert(Alert.AlertType.WARNING, "Locked", "This item is already claimed and cannot be edited.");
            return;
        }

        // 4. Update the object with new values from the text fields
        selectedItem.setItemName(txtName.getText());
        selectedItem.setDescription(txtDesc.getText());
        selectedItem.setCategory(txtCategory.getText());
        selectedItem.setLocationFound(comboLocation.getValue());
        selectedItem.setStatus(txtStatus.getText());

        // 5. Save to Database via DAO
        if (itemDAO.updateItem(selectedItem)) {
            onRefresh.run();
            UIComponents.showAlert(Alert.AlertType.INFORMATION, "Success", "Your report has been updated.");
        } else {
            UIComponents.showAlert(Alert.AlertType.ERROR, "Database Error", "Could not update the item.");
        }
    }

    private void deleteItem() {
        Item selectedItem = table.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            UIComponents.showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an item from the table to update.");
            return;
        }

        // 2. Security Check: Is the user an Admin OR the original reporter?
        boolean isAdmin = Session.getCurrentUser().getRole().equalsIgnoreCase("Admin");
        String currentUserName = Session.getCurrentUser().getName();
        String itemOwner = selectedItem.getReportedBy();

        if (!isAdmin && !currentUserName.equals(itemOwner)) {
            UIComponents.showAlert(Alert.AlertType.ERROR, "Access Denied", "You can only delete items that you reported.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete item '" + selectedItem.getItemName() + "'?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait();

        if (confirm.getResult() == ButtonType.YES) {
            if (itemDAO.deleteItem(selectedItem.getId())) {
                onRefresh.run();
                clearFields();
                UIComponents.showAlert(Alert.AlertType.INFORMATION, "Deleted", "Item deleted successfully.");
            } else {
                UIComponents.showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to delete item.");
            }
        }
    }

    private void clearFields() {
        txtName.clear();
        txtDesc.clear();
        txtCategory.clear();
        txtStatus.clear();
        comboLocation.setValue(null);
        selectedImagePath = "";
        table.getSelectionModel().clearSelection();
    }

    // ════════════════════════════════════════════════════════════════════════
    //  CLAIM DIALOG
    // ════════════════════════════════════════════════════════════════════════
    private void handleClaimProcess() {
        Item selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            UIComponents.showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an item from the table to claim.");
            return;
        }
        if (selected.getStatus().equalsIgnoreCase("Claimed/Returned")) {
            UIComponents.showAlert(Alert.AlertType.ERROR, "Invalid Action", "This item has already been claimed.");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Claim Item & Schedule Pickup");
        dialog.setHeaderText("Claiming: " + selected.getItemName());

        VBox content = new VBox(12);
        content.setPadding(new Insets(20));
        content.setPrefWidth(400);

        DatePicker picker = new DatePicker();
        picker.setPromptText("Select Pickup Date");
        picker.setMaxWidth(Double.MAX_VALUE);

        ComboBox<String> timeBox = new ComboBox<>(FXCollections.observableArrayList(
                "09:00 AM", "10:30 AM", "01:30 PM", "03:00 PM", "04:30 PM"
        ));
        timeBox.setPromptText("Select Time Slot");
        timeBox.setMaxWidth(Double.MAX_VALUE);

        TextArea proofArea = new TextArea();
        proofArea.setPromptText("Describe proof of ownership (color, serial number, unique marks)...");
        proofArea.setPrefRowCount(4);
        proofArea.setStyle("-fx-font-size: 13px; -fx-border-radius: 8px; -fx-background-radius: 8px;");

        content.getChildren().addAll(
                UIComponents.createFieldLabel("Pickup Date"), picker,
                UIComponents.createFieldLabel("Time Slot"),   timeBox,
                UIComponents.createFieldLabel("Proof of Ownership"), proofArea
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Button okBtn = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okBtn.setText("Submit Claim");
        okBtn.setStyle(
                "-fx-background-color: " + Theme.MAROON + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-padding: 8px 20px;"
        );

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (picker.getValue() == null || timeBox.getValue() == null || proofArea.getText().trim().isEmpty()) {
                    UIComponents.showAlert(Alert.AlertType.ERROR, "Input Error", "All fields are required to submit a claim.");
                    return;
                }
                String schedule = picker.getValue().toString() + " at " + timeBox.getValue();
                boolean success = claimDAO.addClaim(
                        selected.getId(), selected.getItemName(),
                        Session.getCurrentUser().getName(), proofArea.getText()
                );
                if (success) {
                    activityDAO.log(
                            Session.getCurrentUser().getName(),
                            "Submitted claim for: " + selected.getItemName() + " (Scheduled: " + schedule + ")"
                    );
                    UIComponents.showAlert(Alert.AlertType.INFORMATION, "Claim Submitted",
                            "Your claim has been sent to Admin for review.\nPlease visit the office on " + schedule);
                } else {
                    UIComponents.showAlert(Alert.AlertType.ERROR, "Database Error", "Could not submit claim at this time.");
                }
            }
        });
    }

    // ════════════════════════════════════════════════════════════════════════
    //  REFRESH (called from CITUFindFX)
    // ════════════════════════════════════════════════════════════════════════
    public void refreshTable(List<Item> items) {
        table.setItems(FXCollections.observableArrayList(items));
    }

    // ── Getters ──────────────────────────────────────────────────────────────
    public VBox getView()                    { return view; }
    public TableView<Item> getTable()        { return table; }
    public String getSelectedImagePath()     { return selectedImagePath; }
    public void setSelectedImagePath(String p) { this.selectedImagePath = p; }
}