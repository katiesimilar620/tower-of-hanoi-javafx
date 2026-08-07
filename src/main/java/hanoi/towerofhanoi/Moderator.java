package hanoi.towerofhanoi;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

public class Moderator {
    private static User user;

    private static boolean saving;

    private static ArrayList<int[]> moves;

    private static int moveNum;

    private static int diskCount;

    private static String previousPage;

    private static boolean GameStarted;

    private static long pausedTime;

    public static long getPausedTime() {
        return pausedTime;
    }

    public static void setPausedTime(long pausedTime) {
        Moderator.pausedTime = pausedTime;
    }

    public static boolean isGameStarted() {
        return GameStarted;
    }

    public static void setGameStarted(boolean isGameStarted) {
        Moderator.GameStarted = isGameStarted;
    }

    public static String getPreviousPage() {
        return previousPage;
    }

    public static void setPreviousPage(String previousPage) {
        Moderator.previousPage = previousPage;
    }

    public static int getDiskCount() {
        return diskCount;
    }

    public static void setDiskCount(int diskCount) {
        Moderator.diskCount = diskCount;
    }

    public static ArrayList<int[]> getMoves() {
        return moves;
    }

    public static void setMoves(ArrayList<int[]> moves) {
        Moderator.moves = moves;
    }

    public static void clearMoves() {
        moves.clear();
    }


    public static void openPage(Event event, String page) {
        try {
            FXMLLoader loader = new FXMLLoader(Moderator.class.getResource(page + ".fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load " + page + " page!");
        }
    }


    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        Moderator.user = user;
    }

    public static void saveToFile() {
        ArrayList<User> arrayList = new ArrayList<>();
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("files\\Users.dat"));
            boolean eof = false;
            while (!eof ) {
                try {
                    User user1 = (User) in.readObject();
                    if (!user1.getEmail().equals(user.getEmail())) {
                        arrayList.add(user1);
                    }
                } catch (EOFException e) {
                    eof = true;
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            arrayList.add(user);
            in.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("files\\Users.dat"));
            for (User user : arrayList) {
                out.writeObject(user);
            }
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isSaving() {
        return saving;
    }

    public static void setSaving(boolean saving) {
        Moderator.saving = saving;
    }

    public static int getMoveNum() {
        return moveNum;
    }

    public static void setMoveNum(int moveNum) {
        Moderator.moveNum = moveNum;
    }
}
