package hanoi.towerofhanoi.controllers;

import hanoi.towerofhanoi.Moderator;
import hanoi.towerofhanoi.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class HomeController {

    @FXML private Button login;
    @FXML private Button resume;
    @FXML private Button saveGame;
    @FXML private Button loadGame;

    @FXML
    public void initialize() {
        User user = Moderator.getUser();
        if (user != null) {
            login.setText(user.getUsername());
            loadGame.setOnAction(this::loadGame);
            loadGame.getStyleClass().remove("inactive-button");
            loadGame.getStyleClass().add("active-button");
        }

        if (Moderator.isGameStarted()) {
            resume.setOnAction(this::resumeGame);
            resume.getStyleClass().remove("inactive-button");
            resume.getStyleClass().add("active-button");
        }

        if (user != null && Moderator.isGameStarted()) {
            saveGame.setOnAction(this::saveGame);
            saveGame.getStyleClass().remove("inactive-button");
            saveGame.getStyleClass().add("active-button");
        }
    }

    @FXML
    private void resumeGame(ActionEvent event) {
        Moderator.openPage(event, "hanoiTower");
    }

    @FXML
    private void newGame(ActionEvent event) {
        Moderator.openPage(event, "diskCount");
    }

    @FXML
    private void loadGame(ActionEvent event) {
        Moderator.setSaving(false);
        Moderator.setPreviousPage("home");
        Moderator.openPage(event, "save");
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        User user = Moderator.getUser();
        if (user == null) {
            Moderator.openPage(event, "login");
        } else {
            Moderator.openPage(event, "profile");
        }
        Moderator.setPreviousPage("home");
    }

    @FXML
    private void saveGame(ActionEvent event) {
        Moderator.setSaving(true);
        User user = Moderator.getUser();
        if (user == null) {
            Moderator.openPage(event, "login");
            Moderator.setPreviousPage("save");
        } else {
            Moderator.openPage(event, "save");
            Moderator.setPreviousPage("home");
        }
    }
}
