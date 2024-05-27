package pacman;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// InputHandler-Klasse, die KeyAdapter erweitert, um Tastatureingaben zu verarbeiten
public class InputHandler extends KeyAdapter {
    private int keyspressed = 0;
    private final Pacman pacman; // Referenz auf das Pacman-Objekt

    // Konstruktor, der ein Pacman-Objekt übergibt und speichert
    public InputHandler(Pacman pacman) {
        this.pacman = pacman;
    }

    // Methode, die aufgerufen wird, wenn eine Taste gedrückt wird
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode(); // Holt den Code der gedrückten Taste
        keyspressed++;

        // Überprüft, welche Taste gedrückt wurde, und bewegt Pacman entsprechend
        switch (key) {
            case KeyEvent.VK_LEFT:
                pacman.move(-2, 0, Direction.LEFT); // Bewegt Pacman nach links
                
                break;
            case KeyEvent.VK_RIGHT:
                pacman.move(2, 0, Direction.RIGHT); // Bewegt Pacman nach rechts
                
                break;
            case KeyEvent.VK_UP:
                pacman.move(0, -2, Direction.UP); // Bewegt Pacman nach oben
                
                break;
            case KeyEvent.VK_DOWN:
                pacman.move(0, 2, Direction.DOWN); // Bewegt Pacman nach unten
                break;
        }
    }

    // Methode, die aufgerufen wird, wenn eine Taste losgelassen wird
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode(); // Holt den Code der losgelassenen Taste
        keyspressed--;

        // Überprüft, welche Taste losgelassen wurde, und stoppt Pacman entsprechend
        switch (key) {
            case KeyEvent.VK_LEFT:
            // verhindert, dass PAC-Man stoppt, wenn ein andere KEY zur gleichen Zeit gedrückt wird 
                if(keyspressed == 0 || pacman.currentDirection == Direction.LEFT){            
                    pacman.move(0, 0, pacman.currentDirection); // Stoppt die Bewegung nach links
                }
                break;
            case KeyEvent.VK_RIGHT:
                if(keyspressed == 0 || pacman.currentDirection == Direction.RIGHT){
                    pacman.move(0, 0, pacman.currentDirection); // Stoppt die Bewegung nach rechts
                }   
                break;
            case KeyEvent.VK_UP:
                if(keyspressed == 0 || pacman.currentDirection == Direction.UP){
                    pacman.move(0, 0, pacman.currentDirection); // Stoppt die Bewegung nach open
                }
                else{}
                break;
            case KeyEvent.VK_DOWN: 
                if(keyspressed == 0 || pacman.currentDirection == Direction.DOWN ){
                    pacman.move(0, 0, pacman.currentDirection); // Stoppt die nach Bewegung unten
                }
                else{}
                break;
        }
    }
}
