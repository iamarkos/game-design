import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.Queue;

public class Game extends JFrame {
	public static int tries;
	public static Start s;
	public static int[][] map;
	public static int[][] visited = new int[30][30];
	public static JButton[][] buttons = new Cell[30][30];
	private static double maxAvg;
	public static int maxIndex;
	public static MyTimer t;
	public static JLabel score;
	public static Clip clip;
	public static Clip over;
	
	//Game constructor
	public Game(Start start) {
		map =  GetMap();
		s = start;
		tries = 3;
		maxAvg = 0;
		maxIndex = -1;
		
		for(int i=0; i<visited.length; i++) {
			for(int j=0; j<visited[0].length; j++) {
				visited[i][j] = -1;
			}
		}
		findIslands(map);
		
		setTitle("TopIsland");
		JPanel title = title();
		JPanel grid = map();
		
		setLayout(new BorderLayout());
		add(title, BorderLayout.NORTH);
		add(grid);
		
		setSize(800,800);
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		playSound("level.wav");
	}
	
	//First panel with info
	private JPanel title () {
		JPanel p = new JPanel();
		
		JLabel time = new JLabel ("00:00", SwingConstants.CENTER);
		t = new MyTimer(time);
		time.setFont(new java.awt.Font("Ariel", Font.PLAIN, 20));
		
		score = new JLabel("attempts left: 3", SwingConstants.CENTER);
		score.setFont(new java.awt.Font("Ariel", Font.PLAIN, 16));
		JButton menu = new OptionButton("Menu");
		menu.addActionListener(mainMenu);
		
		p.setLayout(new BorderLayout(1, 3));
		p.add(menu, BorderLayout.WEST);
		p.add(time, BorderLayout.CENTER);
		p.add(score, BorderLayout.EAST);
		p.setBorder(BorderFactory.createEmptyBorder(15,25,0,20)); 
		t.start();
		
		return p;
	}
	
	//Panel with the map
	private JPanel map() {
		JPanel p = new JPanel();
		p.setBorder(BorderFactory.createEmptyBorder(0,10,10,10)); 
		p.setLayout(new GridLayout(30, 30, 0, 0));
		
		for(int i=0; i<buttons.length; i++) {
			for(int j=0; j<buttons[0].length; j++) {
				buttons[i][j] = new Cell(Game.this, visited[i][j], map[i][j], i, j);
				p.add(buttons[i][j]);
			}
		}
		
		
		return p;
	}
	
	//ActionListener for Main menu button
	private ActionListener mainMenu = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JFrame Frame = new JFrame();
			if (JOptionPane.showConfirmDialog(Frame, "Go to Main Menu?","TopIsland",
					JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
				dispose();
				s.setVisible(true);
				t.stop();
				Cell.timer.stop();
				clip.stop();
				//show title
				if(!Start.isRules) {
					Start.subtitle.setText("<html>Try beating the best score of <b>"+Start.bestScore+" points</b>.</html>");
				}
			}		
		}
		
	};
	
	//Map from URL
	private int[][] GetMap() {
		int[][] map = new int[30][30];
        try {
            URL url = new URL("https://jobfair.nordeus.com/jf24-fullstack-challenge/test");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            for(int i=0; i<map.length; i++) {  
            	String[] line = reader.readLine().split(" ");
                for (int j = 0; j < map[0].length; j++) {
                    map[i][j] = Integer.parseInt(line[j]);
                }
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error reading from URL: " + e.getMessage());
        }

        return map;
	}
	
	//calculating score
	public static int score() {
		int result = tries * 100 + (MyTimer.score) * 2;
		return result;
	}
	
	
	//
	private static boolean isValid(int x, int y, int rows, int cols, int[][] visited, int[][] map) {
        return x >= 0 && x < rows && y >= 0 && y < cols && visited[x][y] == -1 && map[x][y] != 0;
    }
	
	private static double findIsland(int x, int y, int island, int[][] visited, int[][] map) {
		int[][] directions = {
				{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}  // 8 directions
	    };
		visited[x][y] = island;
		Queue<int[]> queue = new LinkedList<>();
		queue.add(new int[]{x, y});
		
		int sum = map[x][y];
		int n = 0;
		
		 while (!queue.isEmpty()) {
			//checking all four directions
			int[] cell = queue.poll();
	        int i = cell[0], j = cell[1];
        	n++;
			for(int[] dir : directions) {
				int nx = i + dir[0];
	            int ny = j + dir[1];
	            if (isValid(nx, ny, visited.length, visited[0].length, visited, map)) {
	            	visited[nx][ny] = island;
	            	queue.add(new int[]{nx, ny});
	            	sum+=map[nx][ny];
				}
			}
		 }
		 
		 double avg = (double) sum / (double) n;
		 return avg;
	}
	
	private static void findIslands(int[][] map) {
		 int islandIndex = 0;
		 for(int i=0; i<map.length; i++) {
			 for(int j=0; j<map[0].length; j++) {
				 if (map[i][j] != 0 && visited[i][j] == -1) {
	                    double avg = findIsland(i, j, islandIndex, visited, map);
	                    if (avg > maxAvg) {
	                    	maxAvg = avg;
	                    	maxIndex = islandIndex;
	                    }
	                    islandIndex++;
	             }
			 }
		 }
	 }
	
	
	public static void playSound(String filePath) {
        try {
            File soundFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

}
