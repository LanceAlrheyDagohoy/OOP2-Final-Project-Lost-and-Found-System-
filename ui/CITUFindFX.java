package ui;

import dao.ActivityDAO;
import dao.ClaimDAO;
import dao.ItemDAO;
import dao.UserDAO;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Item;
import session.Session;
import ui.components.UIComponents;
import ui.pages.*;
import ui.styles.Theme;

import java.util.List;

public class CITUFindFX extends Application {

    // ── State ───────────────────────────────────────────────────────────────
    private boolean isDarkMode = false;

    // ── DAOs ────────────────────────────────────────────────────────────────
    private final ActivityDAO activityDAO = new ActivityDAO();
    private final UserDAO     userDAO     = new UserDAO();
    private final ItemDAO     itemDAO     = new ItemDAO();
    private final ClaimDAO    claimDAO    = new ClaimDAO();

    // ── Stage & Scenes ──────────────────────────────────────────────────────
    private Stage  window;
    private Scene  loginScene, registerScene, mainScene;

    // ── Content area ────────────────────────────────────────────────────────
    private StackPane contentArea;

    // ── Page instances (kept so refreshTableAndStats can reach their state) ─
    private DashboardPage    dashboardPage;
    private ManageItemsPage  manageItemsPage;

    // ════════════════════════════════════════════════════════════════════════
    //  APPLICATION ENTRY
    // ════════════════════════════════════════════════════════════════════════
    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("CITUFind — Lost & Found System");

        // Build auth scenes first (register needs a login scene reference,
        // login needs a register scene reference — solved with a forward ref).
        buildAuthScenes();

        window.setScene(loginScene);
        window.show();
    }

    /**
     * Builds loginScene and registerScene with mutual references.
     * The login scene needs to navigate to register, and vice versa.
     * We use a single-element array as a mutable forward reference for loginScene
     * so RegisterPage can be constructed before loginScene is formally assigned.
     */
    private void buildAuthScenes() {
        // ── Register page (built first so login can reference its scene) ────
        // Temporary placeholder: RegisterPage will reference loginScene by the
        // time its button is clicked, so we assign loginScene below and pass it.

        // Step 1 — build Register with a placeholder; will be patched after login builds.
        // Instead we use a simple two-pass: build login with a lambda that navigates
        // to the register scene via a holder array.
        Scene[] registerHolder = new Scene[1];

        // Login page — navigates to registerHolder[0] on button click.
        LoginPage loginPage = new LoginPage(
                window,
                userDAO,
                null,           // registerScene will be set after register builds
                () -> {
                    buildMainDashboard();
                    window.setScene(mainScene);
                    window.centerOnScreen();
                }
        );
        loginScene = loginPage.getScene();

        // Register page — navigates back to loginScene.
        RegisterPage registerPage = new RegisterPage(window, userDAO, loginScene);
        registerScene = registerPage.getScene();
        registerHolder[0] = registerScene;

        // Now patch the login page's "Create Account" button to use the real registerScene.
        // LoginPage exposes the registerScene via a setter to allow this late binding.
        loginPage.setRegisterScene(registerScene);
    }

   // ════════════════════════════════════════════════════════════════════════

    //  MAIN DASHBOARD SHELL

    // ════════════════════════════════════════════════════════════════════════

    private void buildMainDashboard() {

        // Archive old items (Admin only — unchanged logic)

        if (Session.getCurrentUser().getRole().equalsIgnoreCase("Admin")) {

            itemDAO.archiveOldItems();

        }



        BorderPane root = new BorderPane();

        root.setStyle("-fx-background-color: " + Theme.BG_COLOR + ";");



        // ── HEADER ──────────────────────────────────────────────────────────

        HBox header = buildHeader(root);

        root.setTop(header);



        // ── SIDEBAR ─────────────────────────────────────────────────────────

        VBox sidebar = buildSidebar();

        root.setLeft(sidebar);



        // ── CONTENT AREA ────────────────────────────────────────────────────

        contentArea = new StackPane();

        contentArea.setPadding(new Insets(28));

        root.setCenter(contentArea);



        // ── Build page instances ─────────────────────────────────────────────

        dashboardPage   = new DashboardPage();

        manageItemsPage = new ManageItemsPage(window, itemDAO, activityDAO, claimDAO, this::refreshTableAndStats);

        ClaimsPage  claimsPage  = new ClaimsPage(claimDAO, itemDAO, this::refreshTableAndStats);

        ProfilePage profilePage = new ProfilePage();



        // ── Sidebar nav wiring ───────────────────────────────────────────────

        boolean isAdmin = Session.getCurrentUser().getRole().equalsIgnoreCase("Admin");



        Button navDash      = UIComponents.createNavBtn("📊", "Dashboard");

        Button navManage    = UIComponents.createNavBtn("📦", isAdmin ? "Manage Items" : "Report / View Items");

        Button navClaimsBtn = UIComponents.createNavBtn("📋", isAdmin ? "Manage Claims" : "My Claim History");

        Button navProfile   = UIComponents.createNavBtn("👤", "My Profile");

        Button navLogout    = UIComponents.createNavBtn("🚪", "Logout");



        navDash.setOnAction(e      -> contentArea.getChildren().setAll(dashboardPage.getView()));

        navManage.setOnAction(e    -> contentArea.getChildren().setAll(manageItemsPage.getView()));

        navClaimsBtn.setOnAction(e -> contentArea.getChildren().setAll(claimsPage.getView()));

        navProfile.setOnAction(e   -> contentArea.getChildren().setAll(profilePage.getView()));

        navLogout.setOnAction(e    -> { Session.logout(); window.setScene(loginScene); });



        // Retrieve the sidebar's nav section (VBox child index 1 = navSection)

        VBox navSection = (VBox) sidebar.getChildren().get(1);

        navSection.getChildren().addAll(navDash, navManage, navClaimsBtn, navProfile);



        Region spacer = new Region();

        VBox.setVgrow(spacer, Priority.ALWAYS);

        sidebar.getChildren().addAll(spacer, navLogout, buildSidebarUserBadge());



        // ── Initial page ─────────────────────────────────────────────────────

        contentArea.getChildren().setAll(dashboardPage.getView());

        refreshTableAndStats();



        mainScene = new Scene(root, 1280, 800);

    }
