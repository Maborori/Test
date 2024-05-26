package pacman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel {
    private JButton playButton, optionButton, exitButton;
    private Image backgroundImage;

    public MainMenu() {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        // Hintergrundbild laden
        backgroundImage = new ImageIcon("src/images/backgroundMenu.png").getImage();

        setPreferredSize(new Dimension(1080, 720));

        // Lade und füge das Titelbild hinzu
        ImageIcon titleIcon = scaleImageIcon(new ImageIcon("src/images/PacManTitle.png"), 1);
        JLabel titleLabel = new JLabel(titleIcon);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.insets = new Insets(10, 0, 20, 0); // Abstände um das Bild
        add(titleLabel, constraints);

        // Lade und skaliere die Play-Button-Bilder
        ImageIcon playButtonIcon0 = scaleImageIcon(new ImageIcon("src/images/play0.png"), 2);
        ImageIcon playButtonIcon1 = scaleImageIcon(new ImageIcon("src/images/play1.png"), 2);
        ImageIcon playButtonIcon2 = scaleImageIcon(new ImageIcon("src/images/play2.png"), 2);
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
                gamePanel.requestFocusInWindow();
            }
        });
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.insets = new Insets(10, 10, 10, 10); // Rückkehr zu den normalen Abständen
        add(playButton, constraints);

        // Lade und skaliere die Option-Button-Bilder
        ImageIcon optionButtonIcon0 = scaleImageIcon(new ImageIcon("src/images/option0.png"), 2);
        ImageIcon optionButtonIcon1 = scaleImageIcon(new ImageIcon("src/images/option1.png"), 2);
        ImageIcon optionButtonIcon2 = scaleImageIcon(new ImageIcon("src/images/option2.png"), 2);
        optionButton = createButton(optionButtonIcon0, optionButtonIcon1, optionButtonIcon2);
        optionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle Option button action
            }
        });
        constraints.gridy = 2;
        add(optionButton, constraints);

        // Lade und skaliere die Exit-Button-Bilder
        ImageIcon exitButtonIcon0 = scaleImageIcon(new ImageIcon("src/images/quit0.png"), 2);
        ImageIcon exitButtonIcon1 = scaleImageIcon(new ImageIcon("src/images/quit1.png"), 2);
        ImageIcon exitButtonIcon2 = scaleImageIcon(new ImageIcon("src/images/quit2.png"), 2);
        exitButton = createButton(exitButtonIcon0, exitButtonIcon1, exitButtonIcon2);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle Exit button action
                System.exit(0);
            }
        });
        constraints.gridy = 3;
        add(exitButton, constraints);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
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
