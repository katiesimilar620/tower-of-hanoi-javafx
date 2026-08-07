module hanoi.towerofhanoi {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jlayer;

    opens hanoi.towerofhanoi to javafx.fxml;
    exports hanoi.towerofhanoi;
    exports hanoi.towerofhanoi.controllers;
    opens hanoi.towerofhanoi.controllers to javafx.fxml;
}