package hanoi.towerofhanoi.controllers;

import hanoi.towerofhanoi.Moderator;
import hanoi.towerofhanoi.User;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javazoom.jl.player.Player;


import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HanoiController {

    @FXML private Pane pane;
    @FXML private VBox vbox1, vbox2, vbox3;
    @FXML private Button solveButton;
    @FXML private Button login;
    @FXML private Button undoButton;
    @FXML private Button redoButton;
    @FXML private Label timeLabel;
    @FXML private Label moveNumLabel;

    private List<VBox> boxes;
    private ArrayList<Button> discs = new ArrayList<>();

    private ArrayList<int[]> moves = new ArrayList<>();
    private ArrayList<int[]> solveMoves = new ArrayList<>();

    private double xValue, yValue;
    private VBox previousVBox;
    private int diskCount;
    private int moveNum = 0;
    private long startTime = 0;
    private long pausedTime = 0;
    private boolean running = false;
    private AnimationTimer timer;
    private boolean paused = true;


    @FXML
    public void initialize() {
        diskCount = Moderator.getDiskCount();
        populateDisks();

        boxes = new ArrayList<>();
        boxes.add(vbox1);
        boxes.add(vbox2);
        boxes.add(vbox3);

        if (Moderator.getMoves() != null) {
            moves = Moderator.getMoves();
            moveNum = Moderator.getMoveNum();
            for (int i = 0; i < moveNum; i++) {
                int[] move = moves.get(i);
                Button disc = (Button) boxes.get(move[0]).getChildren().getFirst();
                boxes.get(move[0]).getChildren().remove(disc);
                boxes.get(move[1]).getChildren().addFirst(disc);
            }
        }

        User user = Moderator.getUser();
        if (user != null) {
            login.setText(user.getUsername());
        }

        makeMovable();

        moveNumLabel.setText(moveNum + "");
        pausedTime = Moderator.getPausedTime();
        startChronometer();

    }

    private void populateDisks() {
        for (int i = 0; i < diskCount; i++) {
            Button b = new Button();
            b.setPrefSize(50 + 10 * i, 20);
            if (i % 2 == 0) {
                b.setStyle("-fx-background-color: red");
            } else {
                b.setStyle("-fx-background-color: black");
            }
            b.setId("disc" + (i + 1));
            b.setText("" + (i + 1));

            vbox1.getChildren().add(b);
            discs.add(b);
        }
    }

    public void handleHome(ActionEvent event) {
        paused = true;
        stopChronometer();
        saveChanges();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), e -> {
            Moderator.openPage(event, "home");
        }));
        timeline.play();
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        paused = true;
        stopChronometer();
        saveChanges();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), e -> {
            User user = Moderator.getUser();
            if (user == null) {
                Moderator.openPage(event, "login");
            } else {
                Moderator.openPage(event, "profile");
            }
            Moderator.setPreviousPage("hanoiTower");
        }));
        timeline.play();
    }

    @FXML
    private void handleReset(ActionEvent event) {
        paused = true;

        vbox1.getChildren().clear();
        vbox2.getChildren().clear();
        vbox3.getChildren().clear();

        discs.clear();
        populateDisks();

        moveNum = 0;
        moveNumLabel.setText(moveNum + "");
        moves.clear();
        solveMoves.clear();

        makeMovable();

        resetChronometer();
        startChronometer();
    }

    private void makeMovable() {
        for (VBox box : List.of(vbox1, vbox2, vbox3)) {
            var discs = box.getChildren();
            int i = 0;
            for (var disc : discs) {
                if (disc != null && i == 0) {
                    disc.setOnMousePressed(this::onMousePressed);
                    disc.setOnMouseDragged(this::onMouseDragged);
                    disc.setOnMouseReleased(this::onMouseReleased);
                } else {
                    disc.setOnMousePressed(null);
                    disc.setOnMouseDragged(null);
                    disc.setOnMouseReleased(null);
                }
                i++;
            }
        }
    }


    private void onMousePressed(MouseEvent event) {
        Button disc = (Button) event.getSource();
        VBox vbox = (VBox) disc.getParent();
        previousVBox = vbox;

        xValue = event.getSceneX() - disc.getLayoutX() - vbox.getLayoutX();
        yValue = event.getSceneY() - disc.getLayoutY() - vbox.getLayoutY();

        vbox.getChildren().remove(disc);
        pane.getChildren().add(disc);

        disc.setLayoutX(event.getSceneX() - xValue);
        disc.setLayoutY(event.getSceneY() - yValue);
    }

    private void onMouseDragged(MouseEvent event) {
        Button disc = (Button) event.getSource();
        disc.setLayoutX(event.getSceneX() - xValue);
        disc.setLayoutY(event.getSceneY() - yValue);
    }

    private void onMouseReleased(MouseEvent event) {
        Button disc = (Button) event.getSource();

        VBox closestVBox = findClosestVBox(disc);

        if (closestVBox != null) {
            pane.getChildren().remove(disc);
            if (!closestVBox.getChildren().isEmpty()) {
                Button btn = (Button) closestVBox.getChildren().getFirst();
                if (btn.getId().compareTo(disc.getId()) > 0) {
                    closestVBox.getChildren().addFirst(disc);
                    if (closestVBox != previousVBox) {
                        int[] movement = {boxes.indexOf(previousVBox), boxes.indexOf(closestVBox)};
                        clearAfter(moves, moveNum);
                        moves.add(movement);
                        moveNum++;
                        moveNumLabel.setText(moveNum + "");
                    }

                } else {
                    previousVBox.getChildren().addFirst(disc);
                }
            } else {
                closestVBox.getChildren().addFirst(disc);
                if (closestVBox != previousVBox) {
                    int[] movement = {boxes.indexOf(previousVBox), boxes.indexOf(closestVBox)};
                    clearAfter(moves, moveNum);
                    moves.add(movement);
                    moveNum++;
                    moveNumLabel.setText(moveNum + "");
                }
            }
        }
        previousVBox = null;
        playMP3("move");
        makeMovable();

        if (vbox3.getChildren().size() == diskCount) {
            stopChronometer();
            saveChanges();
            playMP3("win");
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                Moderator.openPage(event, "endGame");
            }));
            timeline.play();
        }
    }

    private void clearAfter(ArrayList<int[]> moves, int moveNum) {
        if (moveNum < moves.size()) {
            for (int i = moves.size() - 1; i >= moveNum; i--) {
                moves.remove(i);
            }
        }
    }

    private VBox findClosestVBox(Button disc) {
        double discCenterX = disc.getLayoutX() + disc.getWidth() / 2;
        double closestDistance = Double.MAX_VALUE;
        VBox closestVbox = null;

        for (VBox vbox : boxes) {
            double vboxCenterX = vbox.getLayoutX() + vbox.getWidth() / 2;
            double distance = Math.abs(vboxCenterX - discCenterX);
            if (distance < closestDistance) {
                closestDistance = distance;
                closestVbox = vbox;
            }
        }

        return closestVbox;
    }

    @FXML
    private void undo() {
        if (moveNum > 0 && paused) {
            undoButton.setDisable(true);

            moveNum--;
            moveNumLabel.setText(moveNum + "");
            int[] movement = moves.get(moveNum);
            Button disc = (Button) boxes.get(movement[1]).getChildren().getFirst();
            VBox destinationVbox = boxes.get(movement[0]);
            TranslateTransition transition = moveButtonToVBox(disc, destinationVbox);

            transition.setOnFinished(event -> {
                if (disc.getParent() != null) {
                    ((Pane) disc.getParent()).getChildren().remove(disc);
                    playMP3("move");
                }

                disc.setTranslateX(0);
                disc.setTranslateY(0);

                destinationVbox.getChildren().addFirst(disc);

                destinationVbox.layout();

                System.out.println(disc.getParent());

                undoButton.setDisable(false);
            });

            transition.play();
        }
        paused = true;
    }

    @FXML
    private void redo() {
        if (moveNum < moves.size()) {
            redoButton.setDisable(true);

            int[] move = moves.get(moveNum);
            Button disc = (Button) boxes.get(move[0]).getChildren().getFirst();
            VBox destinationVbox = boxes.get(move[1]);
            TranslateTransition transition = moveButtonToVBox(disc, destinationVbox);

            transition.setOnFinished(event -> {
                if (disc.getParent() != null) {
                    ((Pane) disc.getParent()).getChildren().remove(disc);
                    playMP3("move");
                }

                disc.setTranslateX(0);
                disc.setTranslateY(0);

                destinationVbox.getChildren().addFirst(disc);

                destinationVbox.layout();

                System.out.println(disc.getParent());

                redoButton.setDisable(false);
            });

            transition.play();
            moveNum++;
            moveNumLabel.setText(moveNum + "");
        }
    }

    @FXML
    private void autoSolve() {
        if (Objects.equals(solveButton.getText(), "Stop")) {
            paused = true;
            return;
        }

        paused = false;

        if (vbox1.getChildren().size() != diskCount || !moves.isEmpty() || !solveMoves.isEmpty()) {
            vbox1.getChildren().clear();
            vbox2.getChildren().clear();
            vbox3.getChildren().clear();

            discs.clear();
            populateDisks();

            moveNum = 0;
            moveNumLabel.setText(moveNum + "");
            moves.clear();
            solveMoves.clear();
        }

        resetChronometer();
        startChronometer();

        solve(discs.size(), 0, 1, 2);

        if (!solveMoves.isEmpty()) {
            playMove(0);
        }
    }

    private void playMove(int moveIndex) {
        if (paused) {
            solveButton.setText("Solve");
            solveButton.setStyle("-fx-background-color: #3498db");
            makeMovable();
            return;
        }

        solveButton.setText("Stop");
        solveButton.setStyle("-fx-background-color: red");

        if (moveIndex >= solveMoves.size()) {
            System.out.println("All moves completed!");
            solveButton.setText("Solve");
            solveButton.setStyle("-fx-background-color: #3498db");
            stopChronometer();
            paused = true;
            return;
        }

        int[] move = solveMoves.get(moveIndex);
        Button disc = discs.get(move[0]);
        VBox destinationVbox = boxes.get(move[1]);

        int[] movement = {boxes.indexOf((VBox) disc.getParent()), boxes.indexOf(destinationVbox)};
        clearAfter(moves, moveNum);
        moves.add(movement);
        moveNum++;
        moveNumLabel.setText(moveNum + "");

        TranslateTransition transition = moveButtonToVBox(disc, destinationVbox);

        transition.setOnFinished(event -> {
            if (disc.getParent() != null) {
                ((Pane) disc.getParent()).getChildren().remove(disc);
                playMP3("move");
            }

            disc.setTranslateX(0);
            disc.setTranslateY(0);

            destinationVbox.getChildren().addFirst(disc);

            destinationVbox.layout();

            System.out.println(move[0] + " -> " + move[1]);

            playMove(moveIndex + 1);
            System.out.println(disc.getParent());
        });

        transition.play();
    }

    private void solve(int n, int a, int b, int c) {
        if (n <= 0) {
            return;
        }


        if (n == 1) {
            int buttonId = 0;
            int vboxId = c;
            solveMoves.add(new int[]{buttonId, vboxId});
            return;
        }

        solve(n - 1, a, c, b);

        int buttonId = n - 1;
        int vboxId = c;
        solveMoves.add(new int[]{buttonId, vboxId});

        solve(n - 1, b, a, c);
    }


    private TranslateTransition moveButtonToVBox(Button button, VBox destinationVBox) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(700), button);

        double startX = button.getLayoutX();
        double startY = button.getLayoutY();

        double vboxX = destinationVBox.getLayoutX();
        double vboxY = destinationVBox.getLayoutY();

        double targetX = (vboxX - startX + (destinationVBox.getWidth() - button.getWidth()) / 2) - (button.getParent().getLayoutX() - vbox1.getLayoutX()) - 10;
        double targetY = vboxY - startY - (destinationVBox.getChildren().size() * button.getHeight()) - 15;

        System.out.println("Button (" + startX + ", " + startY + ")");
        System.out.println("VBox (" + vboxX + ", " + vboxY + ")");
        System.out.println("Target (" + targetX + ", " + targetY + ")");

        transition.setToX(targetX);
        transition.setToY(targetY);

        return transition;
    }

    private void startChronometer() {
        if (!running) {
            if (pausedTime == 0) {
                startTime = System.currentTimeMillis();
            } else {
                startTime = System.currentTimeMillis() - pausedTime;
                pausedTime = 0;
            }
            running = true;
            timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    updateTimeLabel(elapsedTime);
                }
            };
            timer.start();
        }
    }

    private void stopChronometer() {
        if (running) {
            timer.stop();
            pausedTime = System.currentTimeMillis() - startTime;
            running = false;
        }
    }

    private void resetChronometer() {
        stopChronometer();
        startTime = 0;
        pausedTime = 0;
        timeLabel.setText("00:00:00.000");
    }

    private void updateTimeLabel(long elapsedTime) {
        long hours = elapsedTime / 3600000;
        long minutes = (elapsedTime % 3600000) / 60000;
        long seconds = (elapsedTime % 60000) / 1000;
        long milliseconds = elapsedTime % 1000;

        timeLabel.setText(String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds));
    }

    private void saveChanges() {
        Moderator.setDiskCount(diskCount);
        Moderator.setMoves(moves);
        Moderator.setMoveNum(moveNum);
        Moderator.setPausedTime(pausedTime);
    }

    public static void playMP3(String soundName) {
        new Thread(() -> {
            try {
                FileInputStream fileInputStream = new FileInputStream("files\\" + soundName + "Sound.mp3");

                Player player = new Player(fileInputStream);

                player.play();

                player.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}