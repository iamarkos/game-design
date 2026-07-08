import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class Start extends JFrame{
	//constructor for start window
	public static int bestScore;
	private JPanel p = new JPanel();
	JLabel title = new JLabel("TopIsland!", SwingConstants.CENTER);
	public static JLabel subtitle = new JLabel("score", SwingConstants.CENTER);
	JLabel desc = new JLabel("", SwingConstants.CENTER);
	JButton rules = new OptionButton("Rules");
	ImageIcon scale = new ImageIcon(new ImageIcon("scale2.png").getImage().getScaledInstance(350, 18, Image.SCALE_SMOOTH));
	JLabel img = new JLabel(scale);
	public static boolean isRules = false; //if rules are shown
	
	public Start() {
		setTitle("TopIsland");
		setLayout(new BorderLayout());
		
		//READ SCORE
		bestScore = readScore();
		
		JPanel p1 = title();
		JPanel p2 = buts();
		add(p1, BorderLayout.CENTER);
		add(p2, BorderLayout.SOUTH);
		
		setSize(400,230);
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
	}
	
	//panel with title
	public JPanel title () {
		title.setFont(new java.awt.Font("Ariel", Font.BOLD, 40));
		subtitle.setText("<html>Try beating the best score of <b>"+bestScore+" points</b>.</html>");
		subtitle.setFont(new java.awt.Font("Ariel", Font.PLAIN, 15));
		
		p.remove(desc);
		p.remove(img);
		p.setLayout(new BorderLayout());
		p.add(title, BorderLayout.CENTER);
		p.add(subtitle, BorderLayout.SOUTH);
		p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10)); 	
		
		return p;
	}
	
	//panel with option buttons
	private JPanel buts() {
		JPanel p = new JPanel();
		JButton start = new OptionButton("Start"); 
		JButton exit = new OptionButton("Exit");
		
		p.add(start);
		start.addActionListener(startGame);
		p.add(rules);
		rules.addActionListener(showRules);
		//p.add(exit);
		exit.addActionListener(exitGame);
		
		p.setLayout(new GridLayout(1, 2, 10, 10));
		p.setSize(300,200);
		p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10)); 
		
		return p;
	}
	
	private void rules() { 
		title.setFont(new java.awt.Font("Ariel", Font.BOLD, 25));
		//rules descirption
		desc.setText("<html><br/> Islands are colored by height and surrounded by water. <br/> "
				+ "Guess the island with the greatest average height. <br/>"
				+ "You have three guesses and limited time. Ready? <br/>"
				+ " </html>");

		p.remove(title);
		subtitle.setText("0           200           400          600          800           1000");
		subtitle.setFont(new java.awt.Font("Ariel", Font.PLAIN, 14));
		p.add(img, BorderLayout.SOUTH);
		p.add(title, BorderLayout.NORTH);
		p.add(desc, BorderLayout.CENTER);
	}
	
	public static void saveScore(int score) {
		if(readScore() < score) {
			try {
				bestScore = score;
				//subtitle.setText("<html>Try beating the best score of <b>"+bestScore+" points</b>.</html>");
	            FileWriter writer = new FileWriter("score.txt", false);
	            writer.write(String.valueOf(score));
	            writer.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
	}
	
	public static int readScore() {
		int score = 0;
		File file = new File("score.txt");
		Scanner scanner;
		try {
			scanner = new Scanner(file);
			score = scanner.nextInt();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return score;
	}
	
	//action listener for start button
	private ActionListener startGame = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			setVisible(false);
			new Game(Start.this);
		}
		
	};
	
	//action listener for exit
	private ActionListener exitGame = new ActionListener() { //exits the game
		public void actionPerformed(ActionEvent e) {
				System.exit(0);
		}
	};
	
	public ActionListener showRules = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			isRules = true;
			rules();
			rules.removeActionListener(showRules);
			rules.addActionListener(hideRules);
		}
	};
	
	public ActionListener hideRules = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			isRules = false;
			title();
			rules.removeActionListener(hideRules);
			rules.addActionListener(showRules);
		}
		
	};
	
	public static void main(String[] args) {
		JFrame s = new Start();
	}

}
