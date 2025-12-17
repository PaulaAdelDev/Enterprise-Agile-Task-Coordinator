package mile1.gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mile1.service.SprintService;
import mile1.service.TaskService;
import mile1.service.UserService;

public class MainMenuExtraViews {

    private static final UserService userService = new UserService();
    private static final TaskService taskService = new TaskService();
    private static final SprintService sprintService = new SprintService();

    private static TextArea makeOutputBox() {
        TextArea output = new TextArea();
        output.setEditable(false);
        output.setWrapText(true);
        output.setPrefHeight(350);
        return output;
    }

    private static void showWindow(String title, VBox root) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 900, 600));
        stage.show();
    }

    public static void openListUsers() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label title = new Label("List Users");
        TextArea output = makeOutputBox();

        Button run = new Button("Show Users");
        run.setOnAction(e -> {
            // same as Main: userService.listUsers() :contentReference[oaicite:3]{index=3}
            String msg = OutputCapture.runAndCapture(userService::listUsers);
            output.setText(msg);
        });

        root.getChildren().addAll(title, run, output);
        showWindow("List Users", root);
    }

    public static void openListTasks() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label title = new Label("List Tasks");
        TextArea output = makeOutputBox();

        Button run = new Button("Show Tasks");
        run.setOnAction(e -> {
            // same as Main: taskService.listAllTasks() :contentReference[oaicite:4]{index=4}
            String msg = OutputCapture.runAndCapture(taskService::listAllTasks);
            output.setText(msg);
        });

        root.getChildren().addAll(title, run, output);
        showWindow("List Tasks", root);
    }

    public static void openViewSprints() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label title = new Label("View Sprints");
        TextArea output = makeOutputBox();

        Button run = new Button("Show All Sprints");
        run.setOnAction(e -> {
            // same as Main: sprintService.viewAllSprints() :contentReference[oaicite:5]{index=5}
            String msg = OutputCapture.runAndCapture(sprintService::viewAllSprints);
            output.setText(msg);
        });

        root.getChildren().addAll(title, run, output);
        showWindow("View Sprints", root);
    }

    public static void openViewTaskHierarchy() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label title = new Label("View Task Hierarchy");
        TextArea output = makeOutputBox();

        Button run = new Button("Show Hierarchy");
        run.setOnAction(e -> {
            // same as Main: taskService.viewHierarchy() :contentReference[oaicite:6]{index=6}
            String msg = OutputCapture.runAndCapture(taskService::viewHierarchy);
            output.setText(msg);
        });

        root.getChildren().addAll(title, run, output);
        showWindow("Task Hierarchy", root);
    }
}
