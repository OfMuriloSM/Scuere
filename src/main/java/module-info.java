module br.edu.ifce.ads.scuere {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens br.edu.ifce.ads.scuere to javafx.fxml;
    exports br.edu.ifce.ads.scuere;
}