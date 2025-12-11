package crawlers.reportgui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// ReportGUIApplication class to launch the JavaFX GUI for report display.
public class ReportGUIApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ReportGUIApplication.class.getResource("FMRGUI.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1270, 827);
        stage.setTitle("Report Display System");
        stage.setScene(scene);
        stage.show();
    }
}
