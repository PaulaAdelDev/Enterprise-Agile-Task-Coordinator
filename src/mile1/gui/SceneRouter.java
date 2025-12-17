package mile1.gui;

import javafx.scene.Scene;
import javafx.stage.Stage;
import mile1.entity.User;

public class SceneRouter {

    private static Stage stage;
    private static User loggedInUser;

    public static void init(Stage primaryStage) {
        stage = primaryStage;
    }

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void goToMainMenu() {
        stage.setScene(new Scene(new MainMenuView(), 900, 600));
    }

    public static void goToRegister() {
        stage.setScene(new Scene(new RegisterView(), 900, 600));
    }

    public static void goToLogin() {
        stage.setScene(new Scene(new LoginView(), 900, 600));
    }

    public static void goToDashboard() {
        stage.setScene(new Scene(new DashboardView(loggedInUser), 1000, 650));
    }
}
