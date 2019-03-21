

import controller.Login;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Ride;
import repository.DBUtils;
import repository.EmployeeJDBCRepository;


import service.EmployeeService;

public class Main extends Application {
    public static void main(String args[])
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/LogIn.fxml"));
        Parent root = loader.load();
        Login controller = loader.getController();
        DBUtils dbUtils = new DBUtils("db.properties");
        controller.setService(new EmployeeService(new EmployeeJDBCRepository(dbUtils)));
        primaryStage.setTitle("Transport");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(true);
        primaryStage.show();
    }
}
