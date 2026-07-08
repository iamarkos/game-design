import javax.swing.*;
import java.util.*;
import java.awt.*;
public class OptionButton extends JButton {
	//Constructor for an option button
	public OptionButton(String s) {
		setText(s);
		setFocusable(false);
		setFont(new Font("Arial", Font.BOLD, 15));
		setMargin(new Insets(10,10,10,10));
		setBorder(BorderFactory.createLineBorder(Color.black));
		setPreferredSize(new Dimension(100,25));
		setBackground(Color.GRAY);
        setForeground(Color.WHITE);
	}
}
