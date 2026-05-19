package ui.pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import session.Session;
import ui.components.UIComponents;
import ui.styles.Theme;

/**
 * ProfilePage — Builds the "My Profile" content panel.
 * Extracted verbatim from CITUFindFX.buildProfilePage().
 */
public class ProfilePage {

    private final VBox view;

    public ProfilePage() {
        view = build();
    }

    private VBox build() {
        VBox page = new VBox(24);
        Label title = UIComponents.createPageTitle("My Profile");

        // Avatar + name header
        VBox avatarSection = new VBox(8);
        avatarSection.setAlignment(Pos.CENTER_LEFT);
        avatarSection.setPadding(new Insets(24, 28, 24, 28));
        avatarSection.setStyle(
                "-fx-background-color: linear-gradient(to right, " + Theme.MAROON + ", " + Theme.MAROON_LIGHT + ");" +
                        "-fx-background-radius: 12px;"
        );

        Label avatarIcon = new Label("👤");
        avatarIcon.setStyle("-fx-font-size: 40px;");
        Label uName = new Label(Session.getCurrentUser().getName());
        uName.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");
        Label uRole = new Label(Session.getCurrentUser().getRole());
        uRole.setStyle(
                "-fx-background-color: " + Theme.GOLD + ";" +
                        "-fx-text-fill: " + Theme.MAROON_DARK + ";" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 12px;" +
                        "-fx-padding: 3px 12px;"
        );

        avatarSection.getChildren().addAll(avatarIcon, uName, uRole);

        // Detail card — includes Student ID (Requirement 2)
        VBox detailCard = new VBox(0);
        detailCard.setStyle(
                "-fx-background-color: " + Theme.WHITE + ";" +
                        "-fx-background-radius: 12px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 10, 0, 0, 3);"
        );

        detailCard.getChildren().addAll(
                profileRow("Full Name",      Session.getCurrentUser().getName()),
                UIComponents.createDivider(),
                profileRow("Student ID",     Session.getCurrentUser().getStudentID()),
                UIComponents.createDivider(),
                profileRow("Email Address",  Session.getCurrentUser().getEmail()),
                UIComponents.createDivider(),
                profileRow("Account Role",   Session.getCurrentUser().getRole())
        );

        page.getChildren().addAll(title, avatarSection, detailCard);
        return page;
    }

    /** One row in the profile detail card. */
    private HBox profileRow(String label, String value) {
        HBox row = new HBox(16);
        row.setPadding(new Insets(16, 24, 16, 24));
        row.setAlignment(Pos.CENTER_LEFT);

        Label lbl = new Label(label);
        lbl.setMinWidth(120);
        lbl.setStyle("-fx-font-size: 13px; -fx-text-fill: " + Theme.TEXT_MUTED + "; -fx-font-weight: bold;");

        Label val = new Label(value);
        val.setStyle("-fx-font-size: 15px; -fx-text-fill: " + Theme.TEXT_PRIMARY + ";");

        row.getChildren().addAll(lbl, val);
        return row;
    }

    public VBox getView() { return view; }
}
