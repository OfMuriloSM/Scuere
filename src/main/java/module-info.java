module br.edu.ifce.ads.scuere.scuere {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens br.edu.ifce.ads.scuere.scuere to javafx.fxml;
    exports br.edu.ifce.ads.scuere.scuere;
}