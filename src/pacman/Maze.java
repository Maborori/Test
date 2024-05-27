package pacman;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Maze {

    // 2D-Array, das die Struktur des Labyrinths speichert: 0 = leer, 1 = Wand, 2 = Pellet
    private final int[][] mazeStructure;
    // Liste, die die Positionen der Wände als Rechtecke speichert
    private final List<Rectangle> walls;
    // Liste, die die Positionen der Pellets als Rechtecke speichert
    private final List<Rectangle> pellets;

    // Konstruktor, der die Struktur des Labyrinths, die Wände und die Pellets initialisiert
    public Maze() {
        // Initialisierung der Labyrinthstruktur mit vordefinierten Werten
        mazeStructure = new int[][]{
                // 1 = Wand, 2 = Pellet, 0 = leerer Raum
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 1, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 1},
                {1, 2, 1, 1, 1, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 1, 1, 1, 2, 1},
                {1, 2, 1, 2, 2, 2, 1, 2, 2, 2, 1, 0, 2, 2, 2, 0, 1, 2, 2, 2, 1, 2, 2, 2, 1, 2, 1},
                {1, 2, 1, 2, 1, 2, 1, 2, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 2, 1, 2, 1, 2, 1, 2, 1},
                {1, 2, 2, 2, 1, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 1, 2, 2, 2, 1},
                {1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1},
                {1, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 1},
                {1, 2, 1, 2, 1, 2, 1, 2, 1, 1, 0, 1, 0, 0, 0, 1, 0, 1, 1, 2, 1, 2, 1, 2, 1, 2, 1},
                {1, 2, 1, 2, 1, 2, 1, 2, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 2, 1, 2, 1, 2, 1, 2, 1},
                {1, 2, 1, 2, 1, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 1, 2, 1, 2, 1},
                {1, 2, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 2, 1},
                {1, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2, 1, 2, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 1},
                {1, 2, 1, 2, 1, 2, 1, 2, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 2, 1, 2, 1, 2, 1, 2, 1},
                {1, 2, 1, 2, 2, 2, 1, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 1, 2, 2, 2, 1, 2, 1},
                {1, 2, 1, 1, 1, 2, 1, 2, 1, 2, 1, 2, 1, 1, 1, 2, 1, 2, 1, 2, 1, 2, 1, 1, 1, 2, 1},
                {1, 2, 2, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2, 2, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };

        // Initialisierung der Listen für Wände und Pellets
        walls = new ArrayList<>();
        pellets = new ArrayList<>();
        // Aufrufen der Methode zum Laden des Labyrinths
        loadMaze();
    }

    // Private Methode, um die Struktur des Labyrinths in Wände und Pellets zu laden
    private void loadMaze() {
        // Iteration über jede Zeile des Labyrinths
        for (int row = 0; row < mazeStructure.length; row++) {
            // Iteration über jede Spalte der aktuellen Zeile
            for (int col = 0; col < mazeStructure[row].length; col++) {
                // Überprüfen, ob die aktuelle Zelle eine Wand ist
                if (mazeStructure[row][col] == 1) {
                    // Hinzufügen eines Rechtecks für die Wand zur Liste
                    walls.add(new Rectangle(col * 40, row * 40, 40, 40));
                    // Überprüfen, ob die aktuelle Zelle ein Pellet ist
                } else if (mazeStructure[row][col] == 2) {
                    // Hinzufügen eines Rechtecks für das Pellet zur Liste
                    pellets.add(new Rectangle(col * 40 + 15, row * 40 + 15, 10, 10));
                }
            }
        }
    }

    // Methode zum Zeichnen des Labyrinths
    public void draw(Graphics g) {
        // Setzen der Farbe für die Wände
        g.setColor(Color.BLUE);
        // Zeichnen jeder Wand als gefülltes Rechteck
        for (Rectangle wall : walls) {
            g.fillRect(wall.x, wall.y, wall.width, wall.height);
        }

        // Setzen der Farbe für die Pellets
        g.setColor(Color.WHITE);
        // Zeichnen jedes Pellets als gefüllten Kreis
        for (Rectangle pellet : pellets) {
            g.fillOval(pellet.x, pellet.y, pellet.width, pellet.height);
        }
    }

    // Methode zur Überprüfung auf Kollision mit Wänden
    public boolean checkWallCollision(Rectangle rect) {
        // Iteration über alle Wände im Labyrinth
        for (Rectangle wall : walls) {
            // Überprüfen, ob das Rechteck mit einer Wand kollidiert
            if (rect.intersects(wall)) {
                return true; // Kollision gefunden, daher true zurückgeben
            }
        }
        return false; // Keine Kollision gefunden, daher false zurückgeben
    }
    
    // Methode zur Überprüfung auf Kollision mit Pellets und deren Entfernung bei Kollision
    public boolean checkPelletCollision(Rectangle rect) {
        // Iteration über alle Pellets im Labyrinth
        for (int i = 0; i < pellets.size(); i++) {
            // Überprüfen, ob das Rechteck mit einem Pellet kollidiert
            if (rect.intersects(pellets.get(i))) {
                pellets.remove(i); // Pellet aus der Liste entfernen
                return true; // Kollision gefunden, daher true zurückgeben
            }
        }
        return false; // Keine Kollision gefunden, daher false zurückgeben
    }

    // Methode zur Überprüfung, ob alle Pellets im Labyrinth verbraucht wurden
    public boolean allPelletsConsumed() {
        return pellets.isEmpty(); // Rückgabe, ob die Liste der Pellets leer ist (alle verbraucht)
    }
}