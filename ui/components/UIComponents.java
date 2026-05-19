package ui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import ui.styles.Theme;

/**
 * UIComponents — Reusable JavaFX factory / helper methods.
 * All methods were originally private helpers in CITUFindFX.
 * They are now public static so every page class can call them.
 */
public final class UIComponents {

    private UIComponents() {}

    // ════════════════════════════════════════════════════════════════════════
    //  BUTTONS
    // ════════════════════════════════════════════════════════════════════════

    /** Nav button in the sidebar with icon + label. */
    public static Button createNavBtn(String icon, String text) {
        Button b = new Button(icon + "   " + text);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setPadding(new Insets(13, 20, 13, 18));
        b.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: rgba(255,255,255,0.82);" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-cursor: hand;"
        );
        b.setOnMouseEntered(e -> b.setStyle(
                "-fx-background-color: rgba(255,184,28,0.18);" +
                        "-fx-text-fill: " + Theme.GOLD + ";" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-cursor: hand;"
        ));
        b.setOnMouseExited(e -> b.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: rgba(255,255,255,0.82);" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-cursor: hand;"
        ));
        return b;
    }

    /** Primary (maroon) full-width button. */
    public static Button createPrimaryBtn(String text) {
        Button b = new Button(text);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setStyle(
                "-fx-background-color: " + Theme.MAROON + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-padding: 12px;" +
                        "-fx-cursor: hand;"
        );
        b.setOnMouseEntered(e -> b.setStyle(
                "-fx-background-color: " + Theme.MAROON_DARK + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-padding: 12px;" +
                        "-fx-cursor: hand;"
        ));
        b.setOnMouseExited(e -> b.setStyle(
                "-fx-background-color: " + Theme.MAROON + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-padding: 12px;" +
                        "-fx-cursor: hand;"
        ));
        return b;
    }

    /** Ghost (outline) full-width button. */
    public static Button createGhostBtn(String text) {
        Button b = new Button(text);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-color: " + Theme.MAROON + ";" +
                        "-fx-border-radius: 10px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-text-fill: " + Theme.MAROON + ";" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 10px;" +
                        "-fx-cursor: hand;"
        );
        b.setOnMouseEntered(e -> b.setStyle(
                "-fx-background-color: #fff0f0;" +
                        "-fx-border-color: " + Theme.MAROON + ";" +
                        "-fx-border-radius: 10px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-text-fill: " + Theme.MAROON + ";" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 10px;" +
                        "-fx-cursor: hand;"
        ));
        b.setOnMouseExited(e -> b.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-color: " + Theme.MAROON + ";" +
                        "-fx-border-radius: 10px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-text-fill: " + Theme.MAROON + ";" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 10px;" +
                        "-fx-cursor: hand;"
        ));
        return b;
    }

    /** Small colored action button for tables/forms. */
    public static Button createActionBtn(String text, String color) {
        Button b = new Button(text);
        b.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 13px;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-padding: 9px 18px;" +
                        "-fx-cursor: hand;"
        );
        b.setOnMouseEntered(e -> b.setOpacity(0.85));
        b.setOnMouseExited(e  -> b.setOpacity(1.0));
        return b;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  LABELS / LAYOUT
    // ════════════════════════════════════════════════════════════════════════

    /** Bold page title label. */
    public static Label createPageTitle(String text) {
        Label l = new Label(text);
        l.setStyle(
                "-fx-font-size: 26px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + Theme.TEXT_PRIMARY + ";"
        );
        return l;
    }

    /** Small uppercase field label used above form inputs. */
    public static Label createFieldLabel(String text) {
        Label l = new Label(text.toUpperCase());
        l.setStyle(
                "-fx-font-size: 11px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + Theme.TEXT_MUTED + ";" +
                        "-fx-padding: 0 0 2 2;"
        );
        return l;
    }

    /** Thin horizontal separator line. */
    public static Region createDivider() {
        Region div = new Region();
        div.setPrefHeight(1);
        div.setMaxWidth(Double.MAX_VALUE);
        div.setStyle("-fx-background-color: #F0F2F5;");
        return div;
    }

    /** Invisible vertical spacer of a given height. */
    public static Region createSpacer(double height) {
        Region r = new Region();
        r.setPrefHeight(height);
        return r;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  FORM FIELD STYLING
    // ════════════════════════════════════════════════════════════════════════

    /** Styles a TextField with rounded corners and focus ring. */
    public static void styleFormField(TextField tf) {
        String base =
                "-fx-background-color: " + Theme.WHITE + ";" +
                        "-fx-border-color: #D1D5DB;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-padding: 9px 12px;" +
                        "-fx-font-size: 14px;" +
                        "-fx-pref-width: 200px;";
        tf.setStyle(base);
        tf.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (isFocused) {
                tf.setStyle(base +
                        "-fx-border-color: " + Theme.MAROON + ";" +
                        "-fx-border-width: 1.5px;"
                );
            } else {
                tf.setStyle(base);
            }
        });
    }

    /** Styles a ComboBox to match the form fields. */
    public static <T> void styleComboBox(ComboBox<T> cb) {
        cb.setStyle(
                "-fx-background-color: " + Theme.WHITE + ";" +
                        "-fx-border-color: #D1D5DB;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-padding: 4px 8px;" +
                        "-fx-font-size: 14px;"
        );
    }

    // ════════════════════════════════════════════════════════════════════════
    //  TABLE STYLING
    // ════════════════════════════════════════════════════════════════════════

    /** Applies a clean modern style to any TableView. */
    public static <T> void styleTable(TableView<T> tv) {
        tv.setStyle(
                "-fx-background-color: " + Theme.WHITE + ";" +
                        "-fx-background-radius: 12px;" +
                        "-fx-table-cell-border-color: #F0F2F5;" +
                        "-fx-font-size: 13px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 10, 0, 0, 3);"
        );
        tv.setFixedCellSize(42);
        tv.setPrefHeight(280);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  STAT CARD
    // ════════════════════════════════════════════════════════════════════════

    public static VBox createStatCard(String title, String icon, Label val, String accentColor, String bgAccent) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(22, 24, 22, 24));
        card.setPrefWidth(240);
        card.setStyle(
                "-fx-background-color: " + bgAccent + ";" +
                        "-fx-background-radius: 14px;" +
                        "-fx-border-color: transparent transparent transparent " + accentColor + ";" +
                        "-fx-border-width: 0 0 0 5px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 3);"
        );

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 26px;");

        val.setStyle(
                "-fx-text-fill: " + accentColor + ";" +
                        "-fx-font-size: 36px;" +
                        "-fx-font-weight: bold;"
        );

        Label t = new Label(title);
        t.setStyle("-fx-text-fill: " + Theme.TEXT_SECONDARY + "; -fx-font-size: 13px; -fx-font-weight: bold;");

        card.getChildren().addAll(iconLabel, val, t);
        return card;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  ALERTS
    // ════════════════════════════════════════════════════════════════════════

    public static void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // ════════════════════════════════════════════════════════════════════════
    //  DARK MODE
    // ════════════════════════════════════════════════════════════════════════

    public static void applyTheme(Parent root, boolean isDarkMode) {
        if (isDarkMode) {
            root.setStyle(
                    "-fx-base: #1e1e2e;" +
                            "-fx-background: #1e1e2e;" +
                            "-fx-control-inner-background: #2a2a3e;" +
                            "-fx-text-base-color: #e0e0f0;" +
                            "-fx-text-fill: #e0e0f0;" +
                            "-fx-table-cell-border-color: #3a3a50;" +
                            "-fx-focus-color: " + Theme.GOLD + ";" +
                            "-fx-faint-focus-color: transparent;"
            );
        } else {
            root.setStyle("");
        }
    }
}
