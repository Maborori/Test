package pacman;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InputHandler extends KeyAdapter {
    private Pacman pacman;

    public InputHandler(Pacman pacman) {
        this.pacman = pacman;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_LEFT:
                pacman.move(-2, 0,Direction.LEFT); // Move left
                break;
            case KeyEvent.VK_RIGHT:
                pacman.move(2, 0, Direction.RIGHT); // Move right
                break;
            case KeyEvent.VK_UP:
                pacman.move(0, -2,Direction.UP); // Move up
                break;
            case KeyEvent.VK_DOWN:
                pacman.move(0, 2, Direction.DOWN); // Move down
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_RIGHT:
                pacman.move(0, 0, pacman.currentDirection); // Stop horizontal movement
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_DOWN:
                pacman.move(0, 0, pacman.currentDirection); // Stop vertical movement
                break;
        }
    }
}
