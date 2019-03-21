package utils;

import javafx.scene.control.Alert;

public class ErrorAlert {

    public static void showError(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(error);
        alert.showAndWait();
    }
}
