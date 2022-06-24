module com.flightleader.flightleader_ga {
    requires javafx.controls;
	requires mixite.core.jvm;
	requires javafx.fxml;
    exports com.flightleader.main;
    opens com.flightleader.controller to javafx.fxml;
}
