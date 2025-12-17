package mile1.gui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import mile1.entity.User;
import mile1.service.UserService;

public class LoginView extends VBox {

    private final UserService userService = new UserService();

    public LoginView() {
        setSpacing(10);
        setPadding(new Insets(20));

        Label title = new Label("Login");

        TextField username = new TextField();
        username.setPromptText("Enter username");

        PasswordField password = new PasswordField();
        password.setPromptText("Enter password");

        TextArea output = new TextArea();
        output.setEditable(false);
        output.setPrefHeight(180);

        Button loginBtn = new Button("Login");
        loginBtn.setOnAction(e -> {
            String u = username.getText().trim();
            String p = password.getText().trim();

            User user = userService.getUser(u);
            if (user == null) {
                output.setText("❌ User not found!");
                return;
            }
            if (!user.checkPassword(p)) {
                output.setText("❌ Incorrect password!");
                return;
            }

            output.setText("✔ Login successful! Welcome " + u + " (" + user.getRole() + ")");
            SceneRouter.setLoggedInUser(user);
            SceneRouter.goToDashboard();
        });

        Button back = new Button("Back");
        back.setOnAction(e -> SceneRouter.goToMainMenu());

        getChildren().addAll(title, username, password, loginBtn, output, back);
    }
}
