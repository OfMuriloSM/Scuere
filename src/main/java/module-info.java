module br.edu.ifce.ads.scuere {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens br.edu.ifce.ads.scuere to javafx.fxml;
    exports br.edu.ifce.ads.scuere;
}
