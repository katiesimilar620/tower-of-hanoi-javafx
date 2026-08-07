package hanoi.towerofhanoi.controllers;

import java.io.Serializable;
import java.util.ArrayList;

public class SaveSlot implements Serializable {

    private int diskCount;
    private long pausedTime;
    private ArrayList<int[]> moves;
    private int moveNum;
    private String dateAndTime;

    public long getPausedTime() {
        return pausedTime;
    }

    public void setPausedTime(long pausedTime) {
        this.pausedTime = pausedTime;
    }

    public ArrayList<int[]> getMoves() {
        return moves;
    }

    public void setMoves(ArrayList<int[]> moves) {
        this.moves = moves;
    }

    public int getMoveNum() {
        return moveNum;
    }

    public void setMoveNum(int moveNum) {
        this.moveNum = moveNum;
    }

    public int getDiskCount() {
        return diskCount;
    }

    public void setDiskCount(int diskCount) {
        this.diskCount = diskCount;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }
}