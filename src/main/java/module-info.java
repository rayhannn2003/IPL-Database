module com.example.ipldatabase {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;

    requires fontawesomefx;
    requires javafx.media;
    opens com.example.ipldatabase.Author to javafx.graphics, javafx.fxml, javafx.base;
    opens com.example.ipldatabase.Controllers to javafx.base, javafx.fxml, javafx.graphics;
    opens com.example.ipldatabase.DataModel to javafx.base, javafx.fxml, javafx.graphics;

    // Ensure JavaFX is included
// Add this line


    opens com.example.ipldatabase to javafx.fxml;
}
