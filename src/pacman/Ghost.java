package pacman;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class Ghost extends GameObject {
    private Image image; 
    private Maze maze;
    private int dx, dy; 
    private int speed = 8; 
    private Direction currentDirection;
    private Direction nextDirection; 
    private int moveCounter;
    private static final int MAX_MOVE_COUNT = 60; 
    private static final int[] VALID_DIRECTIONS = {0, 90, 180, 270}; 
    private Random random; 

    // Konstruktor für die Erzeugung eines einzelnen Geists
    public Ghost(int x, int y, String imagePath, Maze maze) {
        super(x, y, 20, 20);
        this.maze = maze;
        loadImage(imagePath);
        random = new Random();
        chooseRandomDirection();
    }

    // Methode zum Laden jeder einzelnen Bilddatei der Geister 
    private void loadImage(String imagePath) {
        try {
            URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl == null) {
                throw new IOException("Image not found");
            }
            image = ImageIO.read(imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Zeichnet, bzw. generiert das Bild jedes Geists an seiner aktuellen Position 
    public void draw(Graphics g) {
        g.drawImage(image, x, y, null);
    }

    // Aktualisiert die Bewegung des Geists 
    public void update() {
        move();
    }

    // Bestimmt die Bewegung des Geists
    private void move() {

         // Speichert die alte X- und Y-Position der Geister
        int oldX = x;
        int oldY = y;

        // Aktualisiert die X- und Y-Position der Geister basierend auf ihre Geschwindigkeit
        x += dx;
        y += dy;

        Rectangle ghostRect = new Rectangle(x, y, 20, 20);

        // Setzt die Geister auf ihre letzte Position zurück, wenn sie mit einer Wand kollidieren, und ändert gleichzeitig ihre Bewegungsrichtung
        if (maze.checkWallCollision(ghostRect)) {
            x = oldX;
            y = oldY;
            chooseRandomDirection();
        }


        // Erhöht den Bewegungszähler bei jeder Aktualisierung des Spielzustands und wählt eine neue zufällige Richtung, wenn die maximale Anzahl an Bewegungsschritten erreicht wird
        moveCounter++;
        if (moveCounter >= MAX_MOVE_COUNT) {
            chooseRandomDirection();
            moveCounter = 0; // Reset counter
        }

        // Bestimmt die aktuelle Bewegungsrichtigung jedes einzelnen Geists
        switch (currentDirection) {
            case RIGHT:
                dx = speed;
                dy = 0;
                break;
            case LEFT:
                dx = -speed;
                dy = 0;
                break;
            case UP:
                dx = 0;
                dy = -speed;
                break;
            case DOWN:
                dx = 0;
                dy = speed;
                break;
        }


    }

    // Zwei Methoden (Getter) zur Rückgabe der aktuellen X- und Y-Position
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Methode zur Auswahl einer zufälligen Richtung
    private void chooseRandomDirection() {
        
        int angle = VALID_DIRECTIONS[random.nextInt(VALID_DIRECTIONS.length)];
        switch (angle) {
            case 0:
                nextDirection = Direction.RIGHT;
                break;
            case 90:
                nextDirection = Direction.UP;
                break;
            case 180:
                nextDirection = Direction.LEFT;
                break;
            case 270:
                nextDirection = Direction.DOWN;
                break;
        }

        
        if (!isOppositeDirection(nextDirection)) {
            currentDirection = nextDirection;
        }
    }

    // Methode zur Überprüfung, ob eine Richtung entgegengesetzt zur aktuellen Richtung ist
    private boolean isOppositeDirection(Direction direction) {
        return (currentDirection == Direction.RIGHT && direction == Direction.LEFT) ||
                (currentDirection == Direction.LEFT && direction == Direction.RIGHT) ||
                (currentDirection == Direction.UP && direction == Direction.DOWN) ||
                (currentDirection == Direction.DOWN && direction == Direction.UP);
    }
}