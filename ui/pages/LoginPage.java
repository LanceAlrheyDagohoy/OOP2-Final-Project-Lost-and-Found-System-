package ui.pages;

import dao.UserDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.User;
import session.Session;
import ui.components.UIComponents;
import ui.styles.Theme;

public class LoginPage {

    private final Stage    window;
    private final UserDAO  userDAO;
    private final Runnable onLoginSuccess;

    private Scene registerScene;

    private Button btnGoReg;

    private Scene scene;

    public LoginPage(Stage window, UserDAO userDAO, Scene registerScene, Runnable onLoginSuccess) {
        this.window         = window;
        this.userDAO        = userDAO;
        this.registerScene  = registerScene;
        this.onLoginSuccess = onLoginSuccess;
        build();
    }

    /**
     * Called after the register scene has been built.
     * Replaces the placeholder action on the ghost button.
     */
    public void setRegisterScene(Scene registerScene) {
        this.registerScene = registerScene;
        if (btnGoReg != null) {
            btnGoReg.setOnAction(e -> window.setScene(this.registerScene));
        }
    }

    private void build() {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, " + Theme.MAROON_DARK + " 0%, " + Theme.MAROON + " 50%, #3d0000 100%);");

        VBox card = new VBox(0);
        card.setMaxWidth(380);
        card.setMaxHeight(Region.USE_PREF_SIZE);
        card.setStyle(
                "-fx-background-color: " + Theme.WHITE + ";" +
                        "-fx-background-radius: 16px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 20, 0, 0, 10);"
        );

        // Brand strip
        VBox brandStrip = new VBox(4);
        brandStrip.setAlignment(Pos.CENTER);
        brandStrip.setPadding(new Insets(25, 40, 20, 40));
        brandStrip.setStyle("-fx-background-color: linear-gradient(to bottom, " + Theme.MAROON + ", " + Theme.MAROON_DARK + "); -fx-background-radius: 16px 16px 0 0;");

        Label logoLabel = new Label("CITUFind");
        logoLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: " + Theme.GOLD + "; -fx-font-family: 'Georgia';");
        Label tagline = new Label("University Lost & Found System");
        tagline.setStyle("-fx-font-size: 11px; -fx-text-fill: rgba(255,255,255,0.75);");
        brandStrip.getChildren().addAll(logoLabel, tagline);

        // Body
        VBox body = new VBox(12);
        body.setPadding(new Insets(20, 35, 25, 35));
        body.setAlignment(Pos.CENTER_LEFT);

        Label loginTitle = new Label("Welcome Back");
        loginTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + Theme.TEXT_PRIMARY + ";");
        Label loginSub = new Label("Sign in to your account");
        loginSub.setStyle("-fx-font-size: 13px; -fx-text-fill: " + Theme.TEXT_MUTED + ";");

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Email Address");
        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Password");
        UIComponents.styleFormField(txtEmail);
        UIComponents.styleFormField(txtPass);

        Button btnLogin = UIComponents.createPrimaryBtn("Sign In");
        btnGoReg = UIComponents.createGhostBtn("Create an Account");

        btnLogin.setOnAction(e -> {
            User user = userDAO.loginUser(txtEmail.getText(), txtPass.getText());
            if (user != null) {
                Session.login(user);
                onLoginSuccess.run();
            } else {
                UIComponents.showAlert(Alert.AlertType.ERROR, "Error", "Invalid email or password.");
            }
        });
        btnGoReg.setOnAction(e -> window.setScene(registerScene));

        body.getChildren().addAll(
                loginTitle, loginSub,
                UIComponents.createSpacer(10),
                txtEmail, txtPass,
                UIComponents.createSpacer(15),
                btnLogin, btnGoReg
        );

        card.getChildren().addAll(brandStrip, body);
        root.getChildren().add(card);
        StackPane.setAlignment(card, Pos.CENTER);

        scene = new Scene(root, 440, 500);
    }

    public Scene getScene() {
        return scene;
    }
}