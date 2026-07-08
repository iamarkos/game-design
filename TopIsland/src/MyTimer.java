import java.time.Duration;
import java.time.Instant;
import java.awt.*;
import javax.swing.*;

public class MyTimer {
    private Instant startTime;
    private Duration elapsedTime;
    private boolean running;
    public static JLabel label;
    private Thread thread;
    public static int score;
    private int gameTime = 121;

    public MyTimer(JLabel label) {
    	score = 100;
    	this.label = label;
        this.elapsedTime = Duration.ofSeconds(gameTime);
        this.running = false;
    }
    
    public void start() {
        if (!running) {
            startTime = Instant.now();
            running = true;

            // every second update label
            thread = new Thread(() -> {
                while (running) {
                    label.setText(getElapsedTimeFormatted());
                    if (getTime().compareTo(Duration.ofSeconds(6)) < 0) label.setForeground(Color.RED);
                    if (score>0) score--;
                    try {
                        Thread.sleep(500); 
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("Timer thread interrupted.");
                    }
                }
            });
            thread.start();
        } 
    }
    
    public void stop() {
        if (running) {
            elapsedTime = elapsedTime.minus(Duration.between(startTime, Instant.now()));
            running = false;
            if (thread != null) {
                thread.interrupt(); 
            }
        } else {
            System.out.println("Timer is not running.");
        }
    }
    
    // get current time
    private Duration getTime() {
        if (running) {
            Duration time = elapsedTime.minus(Duration.between(startTime, Instant.now()));
            if (time.compareTo(Duration.ZERO) >= 0) return time;
            else {
            	Cell.endMap(0, -1); //game over if timer 0
            	//set labels for end game
            	Cell.timer = new Timer(100, null);
            	Cell.switchColors(Color.BLACK);
            	Game.clip.stop();
            	Game.playSound("over.wav");
    			JOptionPane.showMessageDialog(null, "GAME OVER!", "Time has run out.", JOptionPane.INFORMATION_MESSAGE);
            	Game.score.setText("GAME OVER!");
    			Game.score.setForeground(Color.RED);
    			stop();
            	return Duration.ZERO;
            }
        }
        else {
            return elapsedTime;
        }
    }

    // time in mm:ss
    private String getElapsedTimeFormatted() {
        Duration currentElapsedTime = getTime();
        long minutes = currentElapsedTime.toMinutesPart();
        long seconds = currentElapsedTime.toSecondsPart();
        return String.format("%02d:%02d", minutes, seconds);
    }

}