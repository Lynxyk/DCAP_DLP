module com.example.dsap1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires spring.context;
    requires spring.beans;
    requires spring.core;
    requires spring.web;
    requires com.fasterxml.jackson.databind;
    requires org.apache.tika.core;
    requires org.apache.tika.parser.ocr;
    requires org.apache.tika.parser.pdf;

    opens com.example.dsap1 to javafx.fxml, spring.core, spring.beans;
    opens com.example.dsap1.Controllers to javafx.fxml, spring.core, spring.beans;

    exports com.example.dsap1;
    exports com.example.dsap1.Controllers;
    exports com.example.dsap1.Controllers.Utils;
    exports com.example.dsap1.Controllers.model;
    opens com.example.dsap1.Controllers.Utils to javafx.fxml, spring.beans, spring.core;
}
