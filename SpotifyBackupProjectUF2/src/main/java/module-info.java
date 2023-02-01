module com.example.spotifybackupprojectuf2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jsoup;
    requires se.michaelthelin.spotify;
    requires org.apache.httpcomponents.core5.httpcore5;
    requires junit;


    opens com.example.spotifybackupprojectuf2 to javafx.fxml;
    exports com.example.spotifybackupprojectuf2;
}