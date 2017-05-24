package timeNow;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class timeNow {

    public String time() {
        String time = LocalTime.now().toString();
        return time.substring(0, time.length() - 4);
    }

    public String date() {
        String date = (LocalDate.now()).toString();
        return date;
    }

    public String year() {
        String year = String.valueOf((LocalDate.now().getYear()));
        return year;
    }

    public String month() {
        int month = Month.valueOf(LocalDate.now().getMonth().toString()).getValue();
        return String.format("%01d", month);
    }

    public Label timeCurr(Label label) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    String now = time();
                    label.setText(now + " HRS");

                });
            }
        }, 0, 1000);
        return label;
    }

    public Label blink(Label label) {
        Timeline tl = new Timeline();
        tl.getKeyFrames().add(new KeyFrame(Duration.millis(2000),
                (ActionEvent actionEvent) -> {
                    label.setText("");
                }));
        tl.setCycleCount(Animation.INDEFINITE);
        tl.setAutoReverse(true);
        tl.play();
        return label;
    }

}
