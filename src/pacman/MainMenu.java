package pacman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel {
    private JButton playButton, optionButton, exitButton;

    public MainMenu() {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        setBackground(Color.BLACK);

        // Load and scale play button images
        ImageIcon playButtonIcon0 = scaleImageIcon(new ImageIcon("src/images/start0.png"), 2);
        ImageIcon playButtonIcon1 = scaleImageIcon(new ImageIcon("src/images/start1.png"), 2);
        ImageIcon playButtonIcon2 = scaleImageIcon(new ImageIcon("src/images/start2.png"), 2);
        playButton = createButton(playButtonIcon0, playButtonIcon1, playButtonIcon2);
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle Play button action
                GamePanel gamePanel = new GamePanel();
                gamePanel.startGame();
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(MainMenu.this);
                frame.setContentPane(gamePanel);
                frame.revalidate();
                frame.repaint();
            }
        });
        add(playButton, constraints);

        // Load and scale option button images
        ImageIcon optionButtonIcon0 = scaleImageIcon(new ImageIcon("src/images/mute0.png"), 2);
        ImageIcon optionButtonIcon1 = scaleImageIcon(new ImageIcon("src/images/mute1.png"), 2);
        ImageIcon optionButtonIcon2 = scaleImageIcon(new ImageIcon("src/images/mute2.png"), 2);
        optionButton = createButton(optionButtonIcon0, optionButtonIcon1, optionButtonIcon2);
        optionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle Option button action
            }
        });
        constraints.gridy = 1;
        add(optionButton, constraints);

        // Load and scale exit button images
        ImageIcon exitButtonIcon0 = scaleImageIcon(new ImageIcon("src/images/close0.png"), 2);
        ImageIcon exitButtonIcon1 = scaleImageIcon(new ImageIcon("src/images/close1.png"), 2);
        ImageIcon exitButtonIcon2 = scaleImageIcon(new ImageIcon("src/images/close2.png"), 2);
        exitButton = createButton(exitButtonIcon0, exitButtonIcon1, exitButtonIcon2);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle Exit button action
                System.exit(0);
            }
        });
        constraints.gridy = 2;
        add(exitButton, constraints);
    }

    private JButton createButton(ImageIcon icon0, ImageIcon icon1, ImageIcon icon2) {
        JButton button = new JButton(icon0);
        button.setRolloverIcon(icon1);
        button.setPressedIcon(icon2);
        button.setPreferredSize(new Dimension(icon0.getIconWidth(), icon0.getIconHeight()));
        button.setBackground(Color.BLACK);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        return button;
    }

    private ImageIcon scaleImageIcon(ImageIcon icon, double scale) {
        Image img = icon.getImage();
        int width = (int) (img.getWidth(null) * scale);
        int height = (int) (img.getHeight(null) * scale);
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }

}
