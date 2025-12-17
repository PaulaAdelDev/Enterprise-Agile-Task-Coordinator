package mile1.gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import mile1.entity.Sprint;
import mile1.entity.Task;
import mile1.service.SprintService;
import mile1.service.TaskService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SprintViews {

    private static final SprintService sprintService = new SprintService();
    private static final TaskService taskService = new TaskService();

    private static Stage createWindow(String title, Pane content) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(content, 950, 650));
        return stage;
    }

    private static TextArea makeOutputBox() {
        TextArea output = new TextArea();
        output.setEditable(false);
        output.setWrapText(true);
        output.setPrefHeight(260);
        return output;
    }

    // ---------------------------------------
    // Main Sprint Management window:
    // - Create Sprint (same dd-MM-yyyy parsing)
    // - View all sprints
    // - Manage existing sprint: add task / add epic / add story / view sprint hierarchy
    // ---------------------------------------
    public static void openSprintManagement() {
        VBox root = new VBox(12);
        root.setPadding(new Insets(20));

        Label title = new Label("Sprint Management");

        // ---- Create Sprint area (matches console createSprint)
        Label createLabel = new Label("Create Sprint");
        TextField objective = new TextField();
        objective.setPromptText("Enter sprint objective");

        TextField startDate = new TextField();
        startDate.setPromptText("Start date (dd-MM-yyyy)");

        TextField endDate = new TextField();
        endDate.setPromptText("End date (dd-MM-yyyy)");

        TextArea output = makeOutputBox();

        Button createSprintBtn = new Button("Create Sprint");
        createSprintBtn.setOnAction(e -> {
            String msg = OutputCapture.runAndCapture(() -> {
                try {
                    String obj = objective.getText().trim();
                    Date start = new SimpleDateFormat("dd-MM-yyyy").parse(startDate.getText().trim());
                    Date end = new SimpleDateFormat("dd-MM-yyyy").parse(endDate.getText().trim());

                    sprintService.createSprint(obj, start, end);
                    System.out.println("✔ Sprint created successfully!");
                } catch (Exception ex) {
                    System.out.println("❌ Invalid date format!");
                }
            });
            output.setText(msg);
        });

        Button viewAllSprintsBtn = new Button("View All Sprints");
        viewAllSprintsBtn.setOnAction(e -> {
            String msg = OutputCapture.runAndCapture(sprintService::viewAllSprints);
            output.setText(msg);
        });

        Separator sep = new Separator();

        // ---- Manage existing sprint (matches console sprintManagement)
        Label manageLabel = new Label("Manage Existing Sprint");
        TextField sprintObjective = new TextField();
        sprintObjective.setPromptText("Enter Sprint objective (must exist)");

        TextField taskTitle = new TextField();
        taskTitle.setPromptText("Task title (for Add Task / Add Epic / Add Story)");

        HBox manageButtons = new HBox(10);

        Button addTaskBtn = new Button("1) Add Task");
        addTaskBtn.setOnAction(e -> {
            String msg = OutputCapture.runAndCapture(() -> {
                Sprint sprint = sprintService.getSprint(sprintObjective.getText().trim());
                if (sprint == null) {
                    System.out.println("❌ Sprint not found!");
                    return;
                }

                Task task = taskService.getTask(taskTitle.getText().trim());
                if (task != null) sprintService.addTaskToSprint(sprint, task);
                else System.out.println("❌ Task not found!");
            });
            output.setText(msg);
        });

        Button addEpicBtn = new Button("2) Add Epic with hierarchy");
        addEpicBtn.setOnAction(e -> {
            String msg = OutputCapture.runAndCapture(() -> {
                Sprint sprint = sprintService.getSprint(sprintObjective.getText().trim());
                if (sprint == null) {
                    System.out.println("❌ Sprint not found!");
                    return;
                }
                sprintService.addEpicToSprint(sprint, taskTitle.getText().trim());
            });
            output.setText(msg);
        });

        Button addStoryBtn = new Button("3) Add Story with hierarchy");
        addStoryBtn.setOnAction(e -> {
            String msg = OutputCapture.runAndCapture(() -> {
                Sprint sprint = sprintService.getSprint(sprintObjective.getText().trim());
                if (sprint == null) {
                    System.out.println("❌ Sprint not found!");
                    return;
                }
                sprintService.addStoryToSprint(sprint, taskTitle.getText().trim());
            });
            output.setText(msg);
        });

        Button viewSprintHierarchyBtn = new Button("4) View Sprint hierarchy");
        viewSprintHierarchyBtn.setOnAction(e -> {
            String msg = OutputCapture.runAndCapture(() -> {
                Sprint sprint = sprintService.getSprint(sprintObjective.getText().trim());
                if (sprint == null) {
                    System.out.println("❌ Sprint not found!");
                    return;
                }
                sprintService.viewSprintWithHierarchy(sprint);
            });
            output.setText(msg);
        });

        manageButtons.getChildren().addAll(addTaskBtn, addEpicBtn, addStoryBtn, viewSprintHierarchyBtn);

        root.getChildren().addAll(
                title,
                createLabel, objective, startDate, endDate, createSprintBtn,
                viewAllSprintsBtn,
                sep,
                manageLabel, sprintObjective, taskTitle, manageButtons,
                output
        );

        createWindow("Sprint Management", root).show();
    }
}
