import java.awt.*;

import javax.swing.*;

public class Cell extends JButton{
	private Game g;
	public static Timer timer = new Timer(100, null);
	
	//island cell constructor
	public Cell(Game game, int island, int height, int i, int j) {
		g = game;
		setFocusable(false);
		//setting the corresponding color
        setBackground(getColor(height));
        //if it is water, no borders and not clickable
        if (height == 0) {
        	setEnabled(false);
            setBorder(BorderFactory.createEmptyBorder());
        }
       // else setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        addActionListener(e -> {
        	if(island == Game.maxIndex) { //correct answer
        		timer = new Timer(100, null);
        		Game.t.stop();
        		switchColors(Color.WHITE);
        		endMap(island, 0);
        		Game.score.setText("SCORE: "+Game.score());
        		MyTimer.label.setText("WELL DONE!");
        		MyTimer.label.setForeground(Color.BLUE);
        		//saving score
        		Start.saveScore(Game.score());
        		Game.clip.stop();
        		Game.playSound("win2.wav");
        		JOptionPane.showMessageDialog(null, "WELL DONE!", "Final score: " + Game.score(), JOptionPane.INFORMATION_MESSAGE);
        		//g.s.setVisible(true);
        		//g.setVisible(false);
        		
        	}
        	else {
        		Game.tries--;
        		Game.score.setText("attempts left: "+Game.tries);
        		endMap(island, 1);
        		Game.clip.stop();
        		
        		if(Game.tries == 0) {
        			timer = new Timer(100, null);
            		Game.t.stop();
        			endMap(island, -1);
        			switchColors(Color.BLACK);
        			Game.score.setText("GAME OVER!");
        			Game.score.setForeground(Color.RED);
        			MyTimer.label.setText("the island eludes you this time");
        			Game.clip.stop();
        			Game.playSound("over.wav");
        			//JOptionPane.showMessageDialog(null, "You lost.", "GAME OVER", JOptionPane.INFORMATION_MESSAGE);
        			int result = JOptionPane.showOptionDialog(null, "GAME OVER!", "Score: 0", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[] {"OK", "Try Again"}, null);
            		if(result == 1) {
        				Game.t.stop();  
        				timer.stop();
            			g.dispose();
            			new Game(Game.s);
            		}
        		}
        		else {
            		//if answer is wrong and not the end
            		Game.playSound("wrong.wav");
            		Game.playSound("level.wav"); //continue game sound
        		}
        	}
        });
	}
	
	//Color based on height
	private static Color getColor(int height){
		int red, green, blue;
		double normalizedValue = Math.max(0.0, height/1000.0);
		//water
		if(normalizedValue == 0) {
			red = 120;
			green = 200;
			blue = 255;
		}
		else if(normalizedValue <= 0.5) {
			red = (int) (normalizedValue * 2 * 255);
			green = 255;
			blue = 0;
		}
		else {
			red = 255;
			green = (int) ((1-normalizedValue) * 2 * 255);
			blue=0;
		}
		
		return new Color(red, green, blue);
	}
	

	
	public static void endMap(int island, int option) {		
		for (int i=0; i<Game.buttons[0].length; i++) {
			for(int j=0; j<Game.buttons[0].length; j++) {
				if(option == 0) { //in case guess is right
					Game.buttons[i][j].setEnabled(false);
					if(Game.visited[i][j] != island) Game.buttons[i][j].setBorder(BorderFactory.createEmptyBorder());
					else {
						//Game.buttons[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
						Game.buttons[i][j].setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
					}
				}
				else if(option == 1) { //still can guess
					if(Game.visited[i][j] == island) {
						Game.buttons[i][j].setBorder(BorderFactory.createEmptyBorder());
						Game.buttons[i][j].setEnabled(false);
					}
				}
				else { //game over, island not important
					if(Game.visited[i][j] == Game.maxIndex) {
						//Game.buttons[i][j].setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
					}		
    				else Game.buttons[i][j].setBorder(BorderFactory.createEmptyBorder());
    				Game.buttons[i][j].setEnabled(false);
				}
				
			}
		}
	}
	
	public static void switchColors(Color c) {
	    final int[] counter = {0}; // Tracks the number of switches

	    timer.addActionListener(e -> {
	        for (int i = 0; i < Game.buttons.length; i++) {
	            for (int j = 0; j < Game.buttons[i].length; j++) {
	            	if(Game.visited[i][j] == Game.maxIndex) { //the correct island
	            		if(counter[0]%2 == 0) Game.buttons[i][j].setBorder(BorderFactory.createLineBorder(c, 1)); //change border on switch
	            		else Game.buttons[i][j].setBorder(BorderFactory.createLineBorder(c, 4));
	            	}
	            }
	        }

	        counter[0]++;
	        // Stop the timer after 5 seconds (10 intervals of 500ms) timer.stop();
	        
	    });
	    timer.start();
}
	
	

}
