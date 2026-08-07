module hanoi.towerofhanoi {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires jlayer;

    opens hanoi.towerofhanoi to javafx.fxml;
    exports hanoi.towerofhanoi;
    exports hanoi.towerofhanoi.controllers;
    opens hanoi.towerofhanoi.controllers to javafx.fxml;
}