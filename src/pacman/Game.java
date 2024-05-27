package pacman;

import javax.swing.*;

public class Game extends JFrame {

    public void startGame() {
        // Erstelle ein neues JFrame-Fenster mit dem Titel "PacMan Game"
        JFrame frame = new JFrame("PacMan Game");

        // Setze die Standard-Schließoperation, damit das Spiel beendet wird, wenn das Fenster geschlossen wird
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Erstelle eine Instanz des MainMenu, das die Spiel-Logik und Grafik enthalten wird
        MainMenu mainMenu = new MainMenu();

        // Füge das MainMenu-Panel zum Frame hinzu
        frame.add(mainMenu);

        // Passe das Frame an die bevorzugte Größe und das Layout seiner Komponenten an
        frame.pack();

        // Mache das Fenster nicht veränderbar
        frame.setResizable(false);

        // Zentriere das Fenster auf dem Bildschirm
        frame.setLocationRelativeTo(null);

        // Mache das Fenster sichtbar
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Erstelle eine Instanz des Spiels
        Game game = new Game();

        // Starte das Spiel
        game.startGame();
    }
}