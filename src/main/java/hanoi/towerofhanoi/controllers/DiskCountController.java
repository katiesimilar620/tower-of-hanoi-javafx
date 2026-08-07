package hanoi.towerofhanoi.controllers;

import hanoi.towerofhanoi.Moderator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

public class DiskCountController {

    @FXML
    private Label show;

    @FXML
    private Slider slider;

    public void handleHome(ActionEvent event) {
        Moderator.openPage(event, "home");
    }

    @FXML
    private void increase(ActionEvent event) {
        if (slider.getValue() < slider.getMax()) {
            slider.setValue(slider.getValue() + 1);
            show.setText("" + (int) slider.getValue());
        }
    }

    @FXML
    private void decrease(ActionEvent event) {
        if (slider.getValue() > slider.getMin()) {
            slider.setValue(slider.getValue() - 1);
            show.setText("" + (int) slider.getValue());
        }
    }

    @FXML
    private void onMouseReleased(MouseEvent event) {
        show.setText("" + (int) slider.getValue());
    }

    @FXML
    private void start(ActionEvent event) {
        Moderator.setDiskCount((int) slider.getValue());
        if (Moderator.getMoves() != null)
            Moderator.clearMoves();
        Moderator.setMoveNum(0);
        Moderator.setPausedTime(0);
        Moderator.setGameStarted(true);
        Moderator.openPage(event, "hanoiTower");
    }
}
