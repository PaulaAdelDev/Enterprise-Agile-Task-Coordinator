package mile1.gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import mile1.database.Database;
import mile1.entity.*;
import mile1.service.TaskService;
import mile1.service.UserService;

public class TaskViews {

    private static final TaskService taskService = new TaskService();
    private static final UserService userService = new UserService();

    // ---------------------------------------
    // Helper: standard window shell
    // ---------------------------------------
    private static Stage createWindow(String title, Pane content) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(content, 900, 600));
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
    // Helper: Task type selector (for safe lookup)
    // ---------------------------------------
    private static ComboBox<String> makeTaskTypeBox() {
        ComboBox<String> box = new ComboBox<>();
        box.getItems().addAll("Epic", "Story", "SubTask", "Bug", "StandardTask");
        box.setPromptText("Select task type");
        return box;
    }

    private static Task getTaskByType(String title, String type) {
        if (type == null || type.trim().isEmpty()) return null;
        if (title == null || title.trim().isEmpty()) return null;

        String tt = title.trim();

        return switch (type) {
            case "Epic" -> taskService.getTask(tt, Epic.class);
            case "Story" -> taskService.getTask(tt, Story.class);
            case "SubTask" -> taskService.getTask(tt, SubTask.class);
            case "Bug" -> taskService.getTask(tt, Bug.class);
            case "StandardTask" -> taskService.getTask(tt, StandardTask.class);
            default -> null;
        };
    }

    // ---------------------------------------
    // 1) View Tasks (calls user.viewTasks())
    // ---------------------------------------
    public static void openViewTasks(User user) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label title = new Label("View Tasks (" + user.getRole() + ")");
        TextArea output = makeOutputBox();

        Button run = new Button("Show Tasks");
        run.setOnAction(e -> {
            String msg = OutputCapture.runAndCapture(user::viewTasks);
            output.setText(msg);
        });

        root.getChildren().addAll(title, run, output);
        createWindow("View Tasks", root).show();
    }

    // ---------------------------------------
    // 2) Create Task
    // Stakeholder: Epic, Story ONLY (high-level)
    // Developer: Bug, StandardTask ONLY
    // ---------------------------------------
    public static void openCreateTask(User user) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label title = new Label("Create Task (" + user.getRole() + ")");
        TextField taskTitle = new TextField();
        taskTitle.setPromptText("Enter task title");

        ComboBox<String> typeBox = new ComboBox<>();
        if (user instanceof Stakeholder) {
            // ✅ Only high-level requests
            typeBox.getItems().addAll("Epic", "Story");
        } else if (user instanceof Developer) {
            typeBox.getItems().addAll("Bug", "StandardTask");
        } else {
            typeBox.getItems().add("No Permission");
            typeBox.setDisable(true);
        }
        typeBox.setPromptText("Select task type");

        TextArea output = makeOutputBox();

        Button create = new Button("Create");
        create.setDisable(!(user instanceof Stakeholder || user instanceof Developer));
        create.setOnAction(e -> {
            String t = taskTitle.getText().trim();
            String type = typeBox.getValue();

            String msg = OutputCapture.runAndCapture(() -> {
                if (!(user instanceof Stakeholder || user instanceof Developer)) {
                    System.out.println("❌ You don't have permission to create tasks.");
                    return;
                }

                if (type == null || type.trim().isEmpty()) {
                    System.out.println("❌ Invalid choice!");
                    return;
                }

                if (t.isEmpty()) {
                    System.out.println("❌ Task title cannot be empty!");
                    return;
                }

                if (user instanceof Stakeholder s) {
                    // ✅ Only Epic/Story
                    switch (type) {
                        case "Epic" -> s.createEpic(new Epic(t));
                        case "Story" -> s.createStory(new Story(t));
                        default -> System.out.println("❌ Stakeholder can only create Epic or Story!");
                    }
                } else if (user instanceof Developer) {
                    // Developer can create Bug/StandardTask
                    switch (type) {
                        case "Bug" -> {
                            Bug bug = new Bug(t);
                            bug.setCreator(user);
                            taskService.addTask(bug);
                        }
                        case "StandardTask" -> {
                            StandardTask task = new StandardTask(t);
                            task.setCreator(user);
                            taskService.addTask(task);
                        }

                        default -> System.out.println("❌ Developer can only create Bug or StandardTask!");
                    }
                }
            });

            output.setText(msg);
        });

        root.getChildren().addAll(title, typeBox, taskTitle, create, output);
        createWindow("Create Task", root).show();
    }

    // ---------------------------------------
    // 3) Assign / Manage Task (ScrumMaster)
    // ✅ Uses type-safe task lookup
    // ---------------------------------------
    public static void openAssignTask(ScrumMaster sm) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label title = new Label("Assign / Manage Task (ScrumMaster)");

        ComboBox<String> taskType = makeTaskTypeBox();

        TextField taskTitle = new TextField();
        taskTitle.setPromptText("Enter task title");

        TextField assigneeUsername = new TextField();
        assigneeUsername.setPromptText("Enter assignee username");

        TextArea output = makeOutputBox();

        Button assign = new Button("Assign");
        assign.setOnAction(e -> {
            String tt = taskTitle.getText().trim();
            String type = taskType.getValue();
            String uu = assigneeUsername.getText().trim();

            String msg = OutputCapture.runAndCapture(() -> {
                if (type == null) {
                    System.out.println("❌ Please select a task type.");
                    return;
                }

                Task task = getTaskByType(tt, type);
                if (task == null) {
                    System.out.println("❌ Task not found!");
                    return;
                }

                User assignee = userService.getUser(uu);
                if (assignee == null) {
                    System.out.println("❌ User not found!");
                    return;
                }

                sm.assignTask(task, assignee);
            });

            output.setText(msg);
        });

        root.getChildren().addAll(title, taskType, taskTitle, assigneeUsername, assign, output);
        createWindow("Assign Task", root).show();
    }

    // ---------------------------------------
    // 4) Developer Actions (start/complete)
    // ✅ Uses type-safe lookup (implementation tasks only)
    // ---------------------------------------
    public static void openDevActions(Developer dev) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label title = new Label("Developer Actions");

        ComboBox<String> taskType = new ComboBox<>();
        taskType.getItems().addAll("Bug", "StandardTask", "SubTask");
        taskType.setPromptText("Select task type");

        TextField taskTitle = new TextField();
        taskTitle.setPromptText("Enter task title");

        ComboBox<String> action = new ComboBox<>();
        action.getItems().addAll("Start Task", "Complete Task");
        action.setPromptText("Select action");

        TextArea output = makeOutputBox();

        Button run = new Button("Run");
        run.setOnAction(e -> {
            String tt = taskTitle.getText().trim();
            String type = taskType.getValue();
            String act = action.getValue();

            String msg = OutputCapture.runAndCapture(() -> {
                if (type == null) {
                    System.out.println("❌ Please select a task type.");
                    return;
                }

                Task task = getTaskByType(tt, type);
                if (task == null) {
                    System.out.println("❌ Task not found!");
                    return;
                }

                if (act == null) {
                    System.out.println("❌ Invalid choice!");
                    return;
                }

                if (act.equals("Start Task")) dev.startTask(task);
                else if (act.equals("Complete Task")) dev.completeTask(task);
                else System.out.println("❌ Invalid choice!");
            });

            output.setText(msg);
        });

        root.getChildren().addAll(title, taskType, taskTitle, action, run, output);
        createWindow("Developer Actions", root).show();
    }

    // ---------------------------------------
    // 5) QA Actions (test task)
    // ✅ Uses type-safe lookup
    // ---------------------------------------
    public static void openQaActions(QAEngineer qa) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label title = new Label("QA Actions");

        ComboBox<String> taskType = new ComboBox<>();
        taskType.getItems().addAll("Bug", "StandardTask", "SubTask");
        taskType.setPromptText("Select task type");

        TextField taskTitle = new TextField();
        taskTitle.setPromptText("Enter task title");

        ComboBox<String> resultBox = new ComboBox<>();
        resultBox.getItems().addAll("Approved", "Rejected", "Tested");
        resultBox.setPromptText("Select QA result");

        TextArea output = makeOutputBox();

        Button run = new Button("Set QA Result");
        run.setOnAction(e -> {
            String tt = taskTitle.getText().trim();
            String type = taskType.getValue();
            String rr = resultBox.getValue();

            String msg = OutputCapture.runAndCapture(() -> {
                if (type == null) {
                    System.out.println("❌ Please select a task type.");
                    return;
                }

                Task task = getTaskByType(tt, type);
                if (task == null) {
                    System.out.println("❌ Task not found!");
                    return;
                }

                if (rr == null || rr.trim().isEmpty()) {
                    System.out.println("❌ QA result cannot be empty!");
                    return;
                }

                qa.testTask(task, rr);
            });

            output.setText(msg);
        });

        root.getChildren().addAll(title, taskType, taskTitle, resultBox, run, output);
        createWindow("QA Actions", root).show();
    }

    // ---------------------------------------
    // 6) Break Story into SubTask (ScrumMaster)
    // ✅ Uses type-safe Story lookup
    // ---------------------------------------
    public static void openBreakTask(ScrumMaster sm) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label title = new Label("Break Story into SubTask");

        TextField storyTitle = new TextField();
        storyTitle.setPromptText("Enter Story title");

        TextField subTitle = new TextField();
        subTitle.setPromptText("Enter SubTask title");

        TextArea output = makeOutputBox();

        Button run = new Button("Create + Link SubTask");
        run.setOnAction(e -> {
            String st = storyTitle.getText().trim();
            String sub = subTitle.getText().trim();

            String msg = OutputCapture.runAndCapture(() -> {
                Story story = taskService.getTask(st, Story.class);
                if (story == null) {
                    System.out.println("❌ ERROR: Story not found!");
                    return;
                }

                if (sub.isEmpty()) {
                    System.out.println("❌ SubTask title cannot be empty!");
                    return;
                }

                sm.breakTask(story, new SubTask(sub));
            });

            output.setText(msg);
        });

        root.getChildren().addAll(title, storyTitle, subTitle, run, output);
        createWindow("Break Task", root).show();
    }

    // ---------------------------------------
    // 7) Finalize Task (ScrumMaster)
    // ✅ Uses type-safe lookup
    // ---------------------------------------
    public static void openFinalizeTask(ScrumMaster sm) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label title = new Label("Finalize Task (Done)");

        ComboBox<String> taskType = makeTaskTypeBox();

        TextField taskTitle = new TextField();
        taskTitle.setPromptText("Enter task title to finalize");

        TextArea output = makeOutputBox();

        Button run = new Button("Finalize");
        run.setOnAction(e -> {
            String tt = taskTitle.getText().trim();
            String type = taskType.getValue();

            String msg = OutputCapture.runAndCapture(() -> {
                if (type == null) {
                    System.out.println("❌ Please select a task type.");
                    return;
                }

                Task task = getTaskByType(tt, type);
                if (task == null) {
                    System.out.println("❌ Task not found!");
                    return;
                }

                sm.finalizeTask(task);
            });

            output.setText(msg);
        });

        root.getChildren().addAll(title, taskType, taskTitle, run, output);
        createWindow("Finalize Task", root).show();
    }

    // ---------------------------------------
    // 8) Hierarchy Management
    // calls TaskService.addStoryToEpic / addSubTaskToStory / viewHierarchy
    // ---------------------------------------
    public static void openHierarchyManagement() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label title = new Label("Hierarchy Management");

        TextField epicTitle = new TextField();
        epicTitle.setPromptText("Epic title (for linking Story->Epic)");

        TextField storyTitle = new TextField();
        storyTitle.setPromptText("Story title");

        TextField subTitle = new TextField();
        subTitle.setPromptText("SubTask title (for linking SubTask->Story)");

        TextArea output = makeOutputBox();

        HBox buttons = new HBox(10);

        Button linkStoryToEpic = new Button("Link Story -> Epic");
        linkStoryToEpic.setOnAction(e -> {
            String msg = OutputCapture.runAndCapture(() ->
                    taskService.addStoryToEpic(epicTitle.getText().trim(), storyTitle.getText().trim())
            );
            output.setText(msg);
        });

        Button linkSubToStory = new Button("Link SubTask -> Story");
        linkSubToStory.setOnAction(e -> {
            String msg = OutputCapture.runAndCapture(() ->
                    taskService.addSubTaskToStory(storyTitle.getText().trim(), subTitle.getText().trim())
            );
            output.setText(msg);
        });

        Button viewHierarchy = new Button("View Task Hierarchy");
        viewHierarchy.setOnAction(e -> {
            String msg = OutputCapture.runAndCapture(taskService::viewHierarchy);
            output.setText(msg);
        });

        buttons.getChildren().addAll(linkStoryToEpic, linkSubToStory, viewHierarchy);

        // Optional quick view of current tasks in-memory (no modification, just display)
        Button showAllRaw = new Button("Show All Tasks (Raw List)");
        showAllRaw.setOnAction(e -> {
            String msg = OutputCapture.runAndCapture(() -> {
                if (Database.tasks.isEmpty()) {
                    System.out.println("No tasks found.");
                } else {
                    System.out.println("All tasks:");
                    Database.tasks.forEach(System.out::println);
                }
            });
            output.setText(msg);
        });

        root.getChildren().addAll(title, epicTitle, storyTitle, subTitle, buttons, showAllRaw, output);
        createWindow("Hierarchy Management", root).show();
    }
}
