package pacman;

import java.awt.*;

// Position und Dimensionen eines Spielobjekts
public abstract class GameObject {
    protected int x, y, width, height;

    // Konstruktor für die Initialisierung eines Spielobjekts
    public GameObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // Abstrakte Methode zur Darstellung des Objekts
    public abstract void draw(Graphics g);

    // Abstrakte Methode zur Aktualisierung des Objekts
    public abstract void update();

    // Methode zur Rückgabe des Begrenzungrechtecks jedes einzelnen Objekts
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}