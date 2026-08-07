package hanoi.towerofhanoi.controllers;

import hanoi.towerofhanoi.Moderator;
import hanoi.towerofhanoi.User;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProfilePageController {

    @FXML
    private Label username;
    @FXML
    private Label email;
    @FXML
    private TextField password;
    @FXML
    private TextField confirmPassword;
    @FXML
    private Label error;

    private User user;

    private List<User> users = new ArrayList<>();

    @FXML
    private void initialize() {
        user = Moderator.getUser();
        username.setText(user.getUsername());
        email.setText(user.getEmail());
    }

    @FXML
    private void resetPasswordButton(ActionEvent event) {
        if (loadUserData()) {
            saveUserData();
            error.setStyle("-fx-text-fill: green;");
            error.setText("reset password successful!");
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                Moderator.openPage(event, "home");
            }));
            timeline.play();
        }
    }


    private boolean loadUserData() {
        boolean success = false;
        error.setText("");
        error.setStyle("-fx-text-fill: red;");
        if (!checkEmpty()) {
            File file = new File("files\\Users.dat");

            if (file.exists()) {
                try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                    boolean found = false;
                    boolean eof = false;
                    while (!eof && !found) {
                        try {
                            User user = (User) in.readObject();
                            if (user.getUsername().equals(username.getText())) {
                                if (user.getEmail().equals(email.getText())) {
                                    found = true;
                                    if (password.getText().equals(confirmPassword.getText())) {
                                        if (checkPasswordSafety()) {
                                            success = true;
                                            user.setPassword(password.getText());
                                            users.add(user);
                                            Moderator.setUser(user);
                                        }
                                    } else {
                                        error.setText("Password confirmation does not match!");
                                    }
                                }
                            } else {
                                users.add(user);
                            }
                        } catch (EOFException e) {
                            eof = true;
                        }
                    }
                    if (!found) {
                        error.setText("User not found");
                    }

                } catch (IOException | ClassNotFoundException e) {
                    error.setText("Error loading user data.");
                    e.printStackTrace();
                }
            }
        }
        return success;
    }

    private boolean checkPasswordSafety() {
        boolean safePassword = false;

        boolean lowerCase = false;
        boolean upperCase = false;
        boolean number = false;
        boolean special = false;
        boolean other = false;
        String pass = password.getText().trim();

        if (pass.length() >= 8) {
            for (int i = 0; i < pass.length(); i++) {
                char c = pass.charAt(i);
                if ("!@#._".contains(String.valueOf(c))) {
                    special = true;
                } else if (c >= 48 && c <= 57) {
                    number = true;
                } else if (c >= 65 && c <= 90) {
                    upperCase = true;
                } else if (c >= 97 && c <= 122) {
                    lowerCase = true;
                } else {
                    other = true;
                }
            }

            if (other){
                error.setText("Invalid password");
            } else if (special && number && upperCase && lowerCase) {
                safePassword = true;
            } else {
                error.setText("Password contains lower case letters, upper case letters,\n numbers and special characters (!@#._)");
            }

        } else {
            error.setText("Password must contain at least 8 characters");
        }



        return safePassword;
    }

    private boolean checkEmpty() {
        boolean emptyField = false;
        if (email == null || email.getText().trim().isEmpty()
                || username == null || username.getText().trim().isEmpty()
                || password == null || password.getText().trim().isEmpty()
                || confirmPassword == null || confirmPassword.getText().trim().isEmpty()) {

            emptyField = true;
            error.setText("Please fill all the fields");

        }

        return emptyField;
    }

    private void saveUserData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("files\\Users.dat"))) {
            for (User user : users) {
                out.writeObject(user);
            }
        } catch (IOException e) {
            error.setText("Error saving user data.");
            e.printStackTrace();
        }
    }

    @FXML
    private void logoutButton(ActionEvent event) {
        Moderator.setUser(null);
        Moderator.openPage(event, "home");
    }
}
