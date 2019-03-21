package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Employee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.DBUtils;

import repository.RideJDBCRepository;
import service.EmployeeService;
import service.RideService;
import utils.ErrorAlert;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Login extends AbstractController<Integer, Employee> {
    private static final Logger LOGGER = LogManager.getLogger();
    public TextField username;
    public PasswordField password;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleLogIn() {
        LOGGER.traceEntry("Entering login handle");
        if (((EmployeeService) this.service).login(username.getText(), password.getText())) {
            Stage stage = (Stage) username.getScene().getWindow();
            stage.hide();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/MainWindow.fxml"));
            try {
                Parent root = fxmlLoader.load();
                MainWindow controller = fxmlLoader.getController();
                DBUtils dbUtils = new DBUtils("db.properties");
                controller.setService(new RideService(new RideJDBCRepository(dbUtils)));
                Stage stage1 = new Stage();
                stage1.setTitle("Transport");
                stage1.setScene(new Scene(root));
                stage1.show();
                stage1.setOnCloseRequest(event -> stage.show());
                username.clear();
                password.clear();
            } catch (IOException e) {
                LOGGER.error(e);
                e.printStackTrace();
            }
        } else
            ErrorAlert.showError("Wrong username/password");
    }
}
