module ro.ubbcluj.map {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;

    opens ro.ubbcluj.map to javafx.fxml;
    exports ro.ubbcluj.map;
    exports ro.ubbcluj.map.domain;
    exports ro.ubbcluj.map.service;
}