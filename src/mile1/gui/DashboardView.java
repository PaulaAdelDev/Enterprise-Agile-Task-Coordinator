package mile1.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import mile1.entity.*;

public class DashboardView extends VBox {

    public DashboardView(User user) {
        setSpacing(10);
        setPadding(new Insets(20));

        Label title = new Label("--- " + user.getRole() + " Dashboard ---");

        Button viewTasks = new Button("1) View Tasks");
        viewTasks.setOnAction(e -> TaskViews.openViewTasks(user));

        Button createTask = new Button("2) Create Task");
        createTask.setDisable(!(user instanceof Stakeholder || user instanceof Developer));
        createTask.setOnAction(e -> TaskViews.openCreateTask(user));

        Button assignTask = new Button("3) Assign/Manage Task");
        assignTask.setDisable(!(user instanceof ScrumMaster));
        assignTask.setOnAction(e -> TaskViews.openAssignTask((ScrumMaster) user));

        Button devActions = new Button("4) Start/Complete Task");
        devActions.setDisable(!(user instanceof Developer));
        devActions.setOnAction(e -> TaskViews.openDevActions((Developer) user));

        Button qaActions = new Button("5) QA Test Task");
        qaActions.setDisable(!(user instanceof QAEngineer));
        qaActions.setOnAction(e -> TaskViews.openQaActions((QAEngineer) user));

        Button breakTask = new Button("6) Break Task into Subtask");
        breakTask.setDisable(!(user instanceof ScrumMaster));
        breakTask.setOnAction(e -> TaskViews.openBreakTask((ScrumMaster) user));

        Button finalizeTask = new Button("7) Finalize Task");
        finalizeTask.setDisable(!(user instanceof ScrumMaster));
        finalizeTask.setOnAction(e -> TaskViews.openFinalizeTask((ScrumMaster) user));

        Button hierarchy = new Button("8) Hierarchy Management");
        hierarchy.setOnAction(e -> TaskViews.openHierarchyManagement());

        Button sprintMgmt = new Button("9) Sprint Management");
        sprintMgmt.setOnAction(e -> SprintViews.openSprintManagement());

        Button logout = new Button("0) Logout");
        logout.setOnAction(e -> {
            SceneRouter.setLoggedInUser(null);
            SceneRouter.goToMainMenu();
        });

        getChildren().addAll(
                title,
                viewTasks, createTask, assignTask, devActions, qaActions,
                breakTask, finalizeTask, hierarchy, sprintMgmt,
                logout
        );
    }
}
