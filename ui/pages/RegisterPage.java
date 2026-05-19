package ui.pages;

import dao.UserDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import ui.components.UIComponents;
import ui.styles.Theme;

/**
 * RegisterPage — Builds the registration scene.
 * All UI code is extracted verbatim from CITUFindFX.buildRegisterScene().
 */
public class RegisterPage {

    private final Stage window;
    private final UserDAO userDAO;
    private final Scene loginScene;

    private Scene scene;

    public RegisterPage(Stage window, UserDAO userDAO, Scene loginScene) {
        this.window     = window;
        this.userDAO    = userDAO;
        this.loginScene = loginScene;
        build();
    }

    private void build() {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, " + Theme.MAROON_DARK + " 0%, " + Theme.MAROON + " 50%, #3d0000 100%);");

        VBox card = new VBox(0);
        card.setMaxWidth(380);
        card.setMaxHeight(Region.USE_PREF_SIZE);
        card.setStyle("-fx-background-color: " + Theme.WHITE + "; -fx-background-radius: 16px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 20, 0, 0, 10);");

        // Brand strip
        VBox brandStrip = new VBox(2);
        brandStrip.setAlignment(Pos.CENTER);
        brandStrip.setPadding(new Insets(20, 40, 15, 40));
        brandStrip.setStyle("-fx-background-color: linear-gradient(to bottom, " + Theme.MAROON + ", " + Theme.MAROON_DARK + "); -fx-background-radius: 16px 16px 0 0;");

        Label logoLabel = new Label("CITUFind");
        logoLabel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: " + Theme.GOLD + "; -fx-font-family: 'Georgia';");
        Label regTitleText = new Label("Create Your Account");
        regTitleText.setStyle("-fx-font-size: 11px; -fx-text-fill: rgba(255,255,255,0.75);");
        brandStrip.getChildren().addAll(logoLabel, regTitleText);

        // Body
        VBox body = new VBox(10);
        body.setPadding(new Insets(20, 35, 25, 35));
        body.setAlignment(Pos.CENTER_LEFT);

        TextField txtNameReg   = new TextField(); txtNameReg.setPromptText("Full Name");
        TextField txtStudentID = new TextField(); txtStudentID.setPromptText("Student ID (e.g., 21-0000-123)");
        TextField txtEmailReg  = new TextField(); txtEmailReg.setPromptText("Email Address");
        PasswordField txtPassReg = new PasswordField(); txtPassReg.setPromptText("Password");

        UIComponents.styleFormField(txtNameReg);
        UIComponents.styleFormField(txtStudentID);
        UIComponents.styleFormField(txtEmailReg);
        UIComponents.styleFormField(txtPassReg);

        Button btnReg  = UIComponents.createPrimaryBtn("Register");
        Button btnBack = UIComponents.createGhostBtn("Back to Login");

        btnReg.setOnAction(e -> {
            if (!txtStudentID.getText().matches("\\d{2}-\\d{4}-\\d{3}")) {
                UIComponents.showAlert(Alert.AlertType.ERROR, "Verification", "Invalid Student ID format. Use XX-XXXX-XXX");
                return;
            }
            String role = "User";
            if (userDAO.registerUser(txtNameReg.getText(), txtStudentID.getText(), txtEmailReg.getText(), txtPassReg.getText(), role)) {
                UIComponents.showAlert(Alert.AlertType.INFORMATION, "Success", "Registration complete! You can now login.");
                window.setScene(loginScene);
            } else {
                UIComponents.showAlert(Alert.AlertType.ERROR, "Error", "Registration failed. Email might already be in use.");
            }
        });
        btnBack.setOnAction(e -> window.setScene(loginScene));

        body.getChildren().addAll(
                txtNameReg, txtStudentID, txtEmailReg, txtPassReg,
                UIComponents.createSpacer(10),
                btnReg, btnBack
        );

        card.getChildren().addAll(brandStrip, body);
        root.getChildren().add(card);
        StackPane.setAlignment(card, Pos.CENTER);

        scene = new Scene(root, 440, 580);
    }

    public Scene getScene() {
        return scene;
    }
}