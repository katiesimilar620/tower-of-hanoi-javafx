package hanoi.towerofhanoi.controllers;

import hanoi.towerofhanoi.Moderator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class EndGameController {

    @FXML private Label star1;
    @FXML private Label star2;
    @FXML private Label star3;

    @FXML private Label timeLabel;
    @FXML private Label movesLabel;

    private int diskCount;
    private int moveNum;
    private long pausedTime;

    @FXML
    private void initialize() {
        diskCount = Moderator.getDiskCount();
        moveNum = Moderator.getMoveNum();
        pausedTime = Moderator.getPausedTime();

        updateTimeLabel(pausedTime);
        movesLabel.setText(Moderator.getMoveNum() + "");

        calculateScore();
    }

    @FXML
    private void newGame(ActionEvent event) {
        if (Moderator.getMoves() != null)
            Moderator.clearMoves();
        Moderator.setMoveNum(0);
        Moderator.setPausedTime(0);
        Moderator.setGameStarted(false);
        Moderator.openPage(event, "diskCount");
    }

    @FXML
    private void handleHome(ActionEvent event) {
        if (Moderator.getMoves() != null)
            Moderator.clearMoves();
        Moderator.setMoveNum(0);
        Moderator.setPausedTime(0);
        Moderator.setGameStarted(false);
        Moderator.openPage(event, "home");
    }

    private void updateTimeLabel(long elapsedTime) {
        long hours = elapsedTime / 3600000;
        long minutes = (elapsedTime % 3600000) / 60000;
        long seconds = (elapsedTime % 60000) / 1000;
        long milliseconds = elapsedTime % 1000;

        timeLabel.setText(String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds));
    }

    private void calculateScore() {
        long SECONDS = 1000;

        int bestMoveNum = (int) (Math.pow(2, diskCount + 1) - 1);
        long bestTime = (long) bestMoveNum * 2 * SECONDS;

        boolean goodMoveNum = moveNum <= bestMoveNum;
        boolean goodTime = pausedTime <= bestTime;

        if ((goodMoveNum && !goodTime) || (!goodMoveNum && goodTime)) {
            star2.setText("★");
        }
        if (goodMoveNum && goodTime) {
            star2.setText("★");
            star3.setText("★");
        }

        if (goodMoveNum) {
            movesLabel.setStyle("-fx-text-fill: green;");
        } else {
            movesLabel.setStyle("-fx-text-fill: red;");
        }

        if (goodTime) {
            timeLabel.setStyle("-fx-text-fill: green;");
        } else {
            timeLabel.setStyle("-fx-text-fill: red;");
        }

    }
}
