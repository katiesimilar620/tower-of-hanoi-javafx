package hanoi.towerofhanoi.controllers;


import hanoi.towerofhanoi.Moderator;
import hanoi.towerofhanoi.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;


public class SignUpPageController {

    @FXML
    private Label error;


    @FXML
    private TextField email;
    @FXML
    private TextField username;

    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confirmPassword;


    public void handleHome(ActionEvent event) {
        Moderator.openPage(event, "home");
    }

    public void handleSignUp(ActionEvent event) {
        error.setText("");

        boolean emptyField = checkEmpty();
        if (!emptyField) {
            boolean userExists = checkUserExistence();
            if (!userExists) {
                boolean usernameExists = checkUsernameExistence();
                if (!usernameExists) {
                    if (password.getText().trim().equals(confirmPassword.getText().trim())) {
                        boolean safePassword = checkPasswordSafety();
                        if (safePassword) {
                            User newUser = getInfo();
                            try {
                                addUser(newUser);
                                error.setText("User added successfully");
                                Moderator.setUser(newUser);
                                Moderator.saveToFile();
                                Moderator.openPage(event, Moderator.getPreviousPage());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } else {
                        error.setText("Password confirmation does not match!");
                    }
                }
            }
        }
    }

    public void handleLogin(ActionEvent event) {
        Moderator.openPage(event, "login");
    }

    private boolean checkEmpty() {
        boolean emptyField = false;
        if (email == null || email.getText().trim().isEmpty()
                || username == null || username.getText().trim().isEmpty()
                || password == null || password.getText().trim().isEmpty()) {

            emptyField = true;
            error.setText("Please fill all the fields");

        }

        return emptyField;
    }

    private boolean checkUserExistence() {
        boolean exists = false;

        try {
            File file = new File("files\\Users.dat");
            if (!file.exists() || file.length() == 0) {
                return false;
            }

            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));

            boolean eof = false;
            while (!eof && !exists) {
                try {
                    User user = (User) in.readObject();

                    if (user.getEmail().equals(email.getText().trim())) {
                        exists = true;
                        error.setText("User already exists!");
                    }

                } catch (EOFException e) {
                    eof = true;
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return exists;
    }

    private boolean checkUsernameExistence() {
        boolean exists = false;


        try {
            File file = new File("files\\Users.dat");
            if (!file.exists() || file.length() == 0) {
                return false;
            }

            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));

            boolean eof = false;
            while (!eof && !exists) {
                try {
                    User user = (User) in.readObject();

                    if (user.getUsername().equals(username.getText().trim())) {
                        exists = true;
                        error.setText("Username already has been taken");
                    }

                } catch (EOFException e) {
                    eof = true;
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return exists;
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

    private User getInfo() {
        User newUser = new User();
        newUser.setUsername(username.getText().trim());
        newUser.setPassword(password.getText().trim());
        newUser.setEmail(email.getText().trim());

        return newUser;
    }

    private static void addUser(User newUser) throws IOException {
        File file = new File("files\\Users.dat");
        boolean append = file.exists() && file.length() > 0;

        ObjectOutputStream out;
        if (append) {
            out = new ObjectOutputStream(new FileOutputStream(file, true)) {
                @Override
                protected void writeStreamHeader() throws IOException {
                    reset();
                }
            };
        } else {
            out = new ObjectOutputStream(new FileOutputStream(file));
        }

        out.writeObject(newUser);
        out.close();
    }
}
