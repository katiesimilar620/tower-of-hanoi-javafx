package hanoi.towerofhanoi.controllers;


import hanoi.towerofhanoi.Moderator;
import hanoi.towerofhanoi.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.*;

public class LoginPageController {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label error;

    private User user;

    public void handleLogin(ActionEvent event) {
        boolean emptyField = checkEmpty();
        if (!emptyField) {
            boolean checkUserExistence = checkUserExistence(username.getText().trim());
            if (checkUserExistence) {

                Moderator.setUser(user);
                Moderator.openPage(event, "home");
            }
        }
    }

    private boolean checkEmpty() {
        if (username == null || username.getText().trim().isEmpty() || password == null || password.getText().trim().isEmpty()) {
            error.setText("Please fill all the fields");
            return true;
        }
        return false;
    }

    private boolean checkUserExistence(String usernameInput) {
        boolean exists = false;
        boolean correctPassword = true;
        File file = new File("files\\Users.dat");
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            boolean eof = false;
            while (!eof && !exists) {
                try {
                    User user = (User) in.readObject();
                    if (user.getUsername().equals(usernameInput)) {
                        if (user.checkPassword(password.getText().trim())) {
                            error.setText("Login successful!");
                            this.user = user;
                            exists = true;
                        } else {
                            error.setText("Username or password is incorrect!");
                            correctPassword = false;
                        }
                    }
                } catch (EOFException e) {
                    eof = true;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!exists && correctPassword) {
            error.setText("User doesn't exist!");
        }
        return exists;
    }

    public void handleHome(ActionEvent event) {
        Moderator.openPage(event, "home");
    }

    public void handleSignup(ActionEvent event) {
        Moderator.openPage(event, "signup");
    }

    public void handleForgotPassword(ActionEvent event) { Moderator.openPage(event, "forgetPassword"); }

}