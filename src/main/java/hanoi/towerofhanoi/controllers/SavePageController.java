package hanoi.towerofhanoi.controllers;

import hanoi.towerofhanoi.Moderator;
import hanoi.towerofhanoi.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class SavePageController {

    private User user;
    private boolean saving;

    @FXML private Button slot1Button;
    @FXML private Button slot2Button;
    @FXML private Button slot3Button;

    @FXML private Label date1;
    @FXML private Label date2;
    @FXML private Label date3;

    @FXML private Label message;

    private SaveSlot slot1 = new SaveSlot();
    private SaveSlot slot2 = new SaveSlot();
    private SaveSlot slot3 = new SaveSlot();

    @FXML
    private void initialize() {
        user = Moderator.getUser();
        saving = Moderator.isSaving();

        if (user.getSlot1() != null) {
            slot1 = user.getSlot1();
            date1.setText(slot1.getDateAndTime());
        }
        if (user.getSlot2() != null) {
            slot2 = user.getSlot2();
            date2.setText(slot2.getDateAndTime());
        }
        if (user.getSlot3() != null) {
            slot3 = user.getSlot3();
            date3.setText(slot3.getDateAndTime());
        }


        if (saving) {
            slot1Button.setOnAction(this::saveSlot1);
            slot2Button.setOnAction(this::saveSlot2);
            slot3Button.setOnAction(this::saveSlot3);
        } else {
            if (user.getSlot1() != null)
                slot1Button.setOnAction(this::loadSlot1);
            else
                slot1Button.getStyleClass().add("inactive-button");

            if (user.getSlot2() != null)
                slot2Button.setOnAction(this::loadSlot2);
            else
                slot2Button.getStyleClass().add("inactive-button");

            if (user.getSlot3() != null)
                slot3Button.setOnAction(this::loadSlot3);
            else
                slot3Button.getStyleClass().add("inactive-button");
        }
    }

    @FXML
    private void back(ActionEvent event) {
        Moderator.openPage(event, Moderator.getPreviousPage());
    }

    private void saveSlot1(ActionEvent event) {

        slot1.setDiskCount(Moderator.getDiskCount());
        slot1.setMoves(Moderator.getMoves());
        slot1.setMoveNum(Moderator.getMoveNum());
        slot1.setPausedTime(Moderator.getPausedTime());
        slot1.setDateAndTime(getDateAndTime());

        user.setSlot1(slot1);
        Moderator.setUser(user);

        date1.setText(slot1.getDateAndTime());

        message.setText("Your progress successfully saved in slot 1!");

    }

    private void saveSlot2(ActionEvent event) {

        slot2.setDiskCount(Moderator.getDiskCount());
        slot2.setMoves(Moderator.getMoves());
        slot2.setMoveNum(Moderator.getMoveNum());
        slot2.setPausedTime(Moderator.getPausedTime());
        slot2.setDateAndTime(getDateAndTime());

        user.setSlot2(slot2);
        Moderator.setUser(user);

        date2.setText(slot2.getDateAndTime());

        message.setText("Your progress successfully saved in slot 2!");

    }

    private void saveSlot3(ActionEvent event) {

        slot3.setDiskCount(Moderator.getDiskCount());
        slot3.setMoves(Moderator.getMoves());
        slot3.setMoveNum(Moderator.getMoveNum());
        slot3.setPausedTime(Moderator.getPausedTime());
        slot3.setDateAndTime(getDateAndTime());

        user.setSlot3(slot3);
        Moderator.setUser(user);

        date3.setText(slot3.getDateAndTime());

        message.setText("Your progress successfully saved in slot 3!");

    }

    private void loadSlot1(ActionEvent event) {

        Moderator.setDiskCount(slot1.getDiskCount());
        Moderator.setMoves(slot1.getMoves());
        Moderator.setMoveNum(slot1.getMoveNum());
        Moderator.setPausedTime(slot1.getPausedTime());

        Moderator.openPage(event, "hanoiTower");
    }

    private void loadSlot2(ActionEvent event) {

        Moderator.setDiskCount(slot2.getDiskCount());
        Moderator.setMoves(slot2.getMoves());
        Moderator.setMoveNum(slot2.getMoveNum());
        Moderator.setPausedTime(slot2.getPausedTime());

        Moderator.openPage(event, "hanoiTower");
    }

    private void loadSlot3(ActionEvent event) {

        Moderator.setDiskCount(slot3.getDiskCount());
        Moderator.setMoves(slot3.getMoves());
        Moderator.setMoveNum(slot3.getMoveNum());
        Moderator.setPausedTime(slot3.getPausedTime());

        Moderator.openPage(event, "hanoiTower");
    }

    private String getDateAndTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);

        return formattedDateTime;
    }
}
