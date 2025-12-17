package mile1.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MainMenuView extends VBox {

    public MainMenuView() {
        setSpacing(12);
        setPadding(new Insets(20));

        Label title = new Label("=== Enterprise Agile Task Coordinator ===");

        Button register = new Button("1) Register User");
        register.setOnAction(e -> SceneRouter.goToRegister());

        Button login = new Button("2) Login");
        login.setOnAction(e -> SceneRouter.goToLogin());

        Button listUsers = new Button("3) List Users");
        listUsers.setOnAction(e -> MainMenuExtraViews.openListUsers());

        Button listTasks = new Button("4) List Tasks");
        listTasks.setOnAction(e -> MainMenuExtraViews.openListTasks());

        Button createSprint = new Button("5) Create Sprint / Sprint Management");
        createSprint.setOnAction(e -> SprintViews.openSprintManagement());

        Button viewSprints = new Button("6) View Sprints");
        viewSprints.setOnAction(e -> MainMenuExtraViews.openViewSprints());

        Button viewHierarchy = new Button("7) View Task Hierarchy");
        viewHierarchy.setOnAction(e -> MainMenuExtraViews.openViewTaskHierarchy());

        Button exit = new Button("0) Exit");
        exit.setOnAction(e -> System.exit(0));

        getChildren().addAll(
                title,
                register, login,
                listUsers, listTasks,
                createSprint, viewSprints,
                viewHierarchy,
                exit
        );
    }
}
