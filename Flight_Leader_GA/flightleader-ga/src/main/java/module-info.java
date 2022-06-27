module com.flightleader.flightleader_ga {
    requires javafx.controls;
	requires mixite.core.jvm;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
	requires com.google.gson;
	requires java.base;
	requires java.desktop;
    exports com.flightleader.main;
    exports com.flightleader.aircraft to com.google.gson;
    opens com.flightleader.controller to javafx.fxml;
    opens com.flightleader.aircraft;
}
