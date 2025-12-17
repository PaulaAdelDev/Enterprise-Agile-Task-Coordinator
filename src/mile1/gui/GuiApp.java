package mile1.gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class GuiApp extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Enterprise Agile Task Coordinator");
        SceneRouter.init(stage);
        SceneRouter.goToMainMenu();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
