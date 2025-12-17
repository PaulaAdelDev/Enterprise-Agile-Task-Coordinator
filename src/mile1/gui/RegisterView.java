package mile1.gui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import mile1.entity.*;
import mile1.service.UserService;

public class RegisterView extends VBox {

    private final UserService userService = new UserService();

    public RegisterView() {
        setSpacing(10);
        setPadding(new Insets(20));

        Label title = new Label("Register User");

        TextField username = new TextField();
        username.setPromptText("Enter username");

        PasswordField password = new PasswordField();
        password.setPromptText("Enter password");

        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("Stakeholder", "ScrumMaster", "Developer", "QAEngineer");
        roleBox.setPromptText("Select role");

        TextArea output = new TextArea();
        output.setEditable(false);
        output.setPrefHeight(220);

        Button submit = new Button("Register");
        submit.setOnAction(e -> {
            String u = username.getText().trim();
            String p = password.getText().trim();
            String r = roleBox.getValue();

            String msg = OutputCapture.runAndCapture(() -> {
                if (u.isEmpty() || p.isEmpty() || r == null) {
                    System.out.println("❌ Error: Username/Password/Role cannot be empty!");
                    return;
                }

                User user;
                switch (r) {
                    case "Stakeholder" -> user = new Stakeholder(u, p);
                    case "ScrumMaster" -> user = new ScrumMaster(u, p);
                    case "Developer" -> user = new Developer(u, p);
                    case "QAEngineer" -> user = new QAEngineer(u, p);
                    default -> {
                        System.out.println("❌ Invalid role selection.");
                        return;
                    }
                }

                // IMPORTANT: only call the service. Do NOT print success here.
                userService.addUser(user);
            });

            output.setText(msg);
        });


        Button back = new Button("Back");
        back.setOnAction(e -> SceneRouter.goToMainMenu());

        getChildren().addAll(title, username, password, roleBox, submit, output, back);
    }
}
