package ui.pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import session.Session;
import ui.components.UIComponents;
import ui.styles.Theme;

/**
 * DashboardPage — Builds the Dashboard content panel.
 * Extracted verbatim from CITUFindFX.buildDashboardPage().
 * The three stat Labels (lblTotalItems, lblFoundItems, lblLostItems) are
 * exposed via getters so CITUFindFX can update them via refreshTableAndStats().
 */
public class DashboardPage {

    private final Label lblTotalItems;
    private final Label lblFoundItems;
    private final Label lblLostItems;

    private final VBox view;

    public DashboardPage() {
        lblTotalItems = new Label("0");
        lblFoundItems = new Label("0");
        lblLostItems  = new Label("0");
        view = build();
    }

    private VBox build() {
        VBox page = new VBox(24);

        // Page heading
        Label title = UIComponents.createPageTitle("System Dashboard");
        Label sub   = new Label("Overview of all lost and found items in the system.");
        sub.setStyle("-fx-font-size: 14px; -fx-text-fill: " + Theme.TEXT_MUTED + ";");

        // Stat cards row
        HBox stats = new HBox(20);
        stats.getChildren().addAll(
                UIComponents.createStatCard("Total Items",    "📦", lblTotalItems, Theme.MAROON,  "#fff0f0"),
                UIComponents.createStatCard("Items Found",    "✅", lblFoundItems, Theme.SUCCESS, "#f0fdf4"),
                UIComponents.createStatCard("Pending / Lost", "⚠",  lblLostItems,  Theme.DANGER,  "#fef2f2")
        );

        // Welcome banner
        HBox banner = new HBox();
        banner.setPadding(new Insets(20, 24, 20, 24));
        banner.setAlignment(Pos.CENTER_LEFT);
        banner.setStyle(
                "-fx-background-color: linear-gradient(to right, " + Theme.MAROON + ", " + Theme.MAROON_LIGHT + ");" +
                        "-fx-background-radius: 12px;"
        );
        Label bannerText = new Label(
                "👋  Welcome, " + Session.getCurrentUser().getName() +
                        "! Use the sidebar to navigate the system and manage lost/found items."
        );
        bannerText.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-wrap-text: true;");
        bannerText.setMaxWidth(700);
        banner.getChildren().add(bannerText);

        page.getChildren().addAll(title, sub, stats, banner);
        return page;
    }

    // ── Getters for stat labels (used by CITUFindFX.refreshTableAndStats()) ──

    public Label getLblTotalItems() { return lblTotalItems; }
    public Label getLblFoundItems() { return lblFoundItems; }
    public Label getLblLostItems()  { return lblLostItems;  }

    public VBox getView() { return view; }
}