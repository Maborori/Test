package pacman;

import javax.swing.*; // Importiert die Swing-Bibliothek für GUI-Komponenten
import java.awt.*; // Importiert die AWT-Bibliothek für Grafikkomponenten
import java.awt.event.ActionEvent; // Importiert ActionEvent für Ereignisbehandlung
import java.awt.event.ActionListener; // Importiert ActionListener für Ereignisbehandlung

public class MainMenu extends JPanel { // Deklariert die Klasse MainMenu, die JPanel erweitert

    // Deklaration der Schaltflächen und des Hintergrundbilds
    private JButton playButton, optionButton, exitButton; // Schaltflächen für Play, Option und Exit
    private Image backgroundImage; // Hintergrundbild für das Hauptmenü

    public MainMenu() { // Konstruktor für das Hauptmenü

        // Setzt das Layout des Panels auf ein GridBagLayout
        setLayout(new GridBagLayout()); // GridBagLayout ermöglicht flexible Anordnung der Komponenten
        GridBagConstraints constraints = new GridBagConstraints(); // Erstellt Constraints für das Layout
        constraints.insets = new Insets(10, 10, 10, 10); // Standard-Inset-Werte für Abstände zwischen Komponenten

        // Lädt das Hintergrundbild
        backgroundImage = new ImageIcon("src/images/backgroundMenu.png").getImage(); // Lädt das Bild aus dem angegebenen Pfad

        // Setzt die bevorzugte Größe des Panels
        setPreferredSize(new Dimension(1080, 720)); // Setzt die Größe des Panels auf 1080x720 Pixel

        // Lädt und skaliert das Titelbild und fügt es dem Panel hinzu
        ImageIcon titleIcon = scaleImageIcon(new ImageIcon("src/images/PacManTitle.png"), 1); // Skaliert das Titelbild
        JLabel titleLabel = new JLabel(titleIcon); // Erstellt ein JLabel mit dem Titelbild
        constraints.gridx = 0; // Setzt die x-Position der Komponente
        constraints.gridy = 0; // Setzt die y-Position der Komponente
        constraints.gridwidth = 1; // Setzt die Breite der Komponente im Layout
        constraints.insets = new Insets(10, 0, 20, 0); // Inset-Werte für das Titelbild
        add(titleLabel, constraints); // Fügt das Titelbild dem Panel hinzu

        // Lädt und skaliert die Play-Button-Bilder
        ImageIcon playButtonIcon0 = scaleImageIcon(new ImageIcon("src/images/play0.png"), 2); // Normales Icon
        ImageIcon playButtonIcon1 = scaleImageIcon(new ImageIcon("src/images/play1.png"), 2); // Rollover-Icon
        ImageIcon playButtonIcon2 = scaleImageIcon(new ImageIcon("src/images/play2.png"), 2); // Gedrücktes Icon
        playButton = createButton(playButtonIcon0, playButtonIcon1, playButtonIcon2); // Erstellt den Play-Button
        // Fügt einen ActionListener zum Play-Button hinzu
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handhabt die Aktion des Play-Buttons
                GamePanel gamePanel = new GamePanel(); // Erstellt ein neues Spielpanel
                gamePanel.startGame(); // Startet das Spiel
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(MainMenu.this); // Holt das übergeordnete JFrame
                frame.setContentPane(gamePanel); // Setzt das Content Pane auf das Spielpanel
                frame.revalidate(); // Validiert das Layout des Frames neu
                frame.repaint(); // Zeichnet das Frame neu
                gamePanel.requestFocusInWindow(); // Setzt den Fokus auf das Spielpanel
            }
        });
        constraints.gridy = 1; // Setzt die y-Position der Komponente auf die zweite Zeile
        constraints.gridwidth = 1; // Setzt die Breite der Komponente im Layout auf 1
        constraints.insets = new Insets(10, 10, 10, 10); // Rückkehr zu den Standard-Inset-Werten
        add(playButton, constraints); // Fügt den Play-Button dem Panel hinzu

        // Lädt und skaliert die Exit-Button-Bilder
        ImageIcon exitButtonIcon0 = scaleImageIcon(new ImageIcon("src/images/quit0.png"), 2); // Normales Icon
        ImageIcon exitButtonIcon1 = scaleImageIcon(new ImageIcon("src/images/quit1.png"), 2); // Rollover-Icon
        ImageIcon exitButtonIcon2 = scaleImageIcon(new ImageIcon("src/images/quit2.png"), 2); // Gedrücktes Icon
        exitButton = createButton(exitButtonIcon0, exitButtonIcon1, exitButtonIcon2); // Erstellt den Exit-Button
        // Fügt einen ActionListener zum Exit-Button hinzu
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Beendet das Programm
                System.exit(0); // System.exit(0) beendet das Programm mit dem Statuscode 0
            }
        });
        constraints.gridy = 3; // Setzt die y-Position der Komponente auf die vierte Zeile
        add(exitButton, constraints); // Fügt den Exit-Button dem Panel hinzu
    }

    @Override
    protected void paintComponent(Graphics g) { // Überschreibt die paintComponent-Methode von JPanel
        super.paintComponent(g); // Ruft die paintComponent-Methode der Superklasse auf
        // Zeichnet das Hintergrundbild
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // Zeichnet das Bild über das gesamte Panel
    }

    // Erstellt und konfiguriert eine Schaltfläche mit den gegebenen Icons
    private JButton createButton(ImageIcon icon0, ImageIcon icon1, ImageIcon icon2) {
        JButton button = new JButton(icon0); // Erstellt die Schaltfläche mit dem normalen Icon
        button.setRolloverIcon(icon1); // Setzt das Icon für den Rollover-Zustand
        button.setPressedIcon(icon2); // Setzt das Icon für den gedrückten Zustand
        button.setPreferredSize(new Dimension(icon0.getIconWidth(), icon0.getIconHeight())); // Setzt die bevorzugte Größe
        button.setBackground(Color.BLACK); // Setzt den Hintergrund der Schaltfläche auf die Farbe Schwarz
        button.setBorderPainted(false); // Entfernt die Rahmenzeichnung
        button.setFocusPainted(false); // Entfernt die Fokus-Markierung
        button.setContentAreaFilled(false); // Entfernt die Hintergrundfüllung
        return button; // Gibt die konfigurierte Schaltfläche zurück
    }

    // Skaliert ein ImageIcon um den gegebenen Faktor
    private ImageIcon scaleImageIcon(ImageIcon icon, double scale) {
        Image img = icon.getImage(); // Holt das Image-Objekt aus dem Icon
        int width = (int) (img.getWidth(null) * scale); // Berechnet die neue Breite basierend auf dem Skalierungsfaktor
        int height = (int) (img.getHeight(null) * scale); // Berechnet die neue Höhe basierend auf dem Skalierungsfaktor
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH); // Skaliert das Bild mit glatter Skalierung
        return new ImageIcon(scaledImg); // Gibt ein neues ImageIcon mit dem skalierten Bild zurück
    }

}