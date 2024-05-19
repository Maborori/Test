package pacman;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class Ghost extends GameObject {
    private Image image; // Image of the ghost
    private Maze maze;
    private int dx, dy; // Movement deltas
    private int speed = 2; // Ghost speed
    private Direction currentDirection; // Current movement direction
    private Direction nextDirection; // Next movement direction
    private int moveCounter; // Counter to control movement changes
    private static final int MAX_MOVE_COUNT = 20; // Max counter before changing direction
    private static final int[] VALID_DIRECTIONS = {0, 90, 180, 270}; // Valid angles for directions
    private Random random; // Random number generator
    
    public Ghost(int x, int y, String imagePath, Maze maze) {
        super(x, y, 20, 20); // Adjust the size and position as needed
        this.maze = maze;
        loadImage(imagePath);
        random = new Random();
        chooseRandomDirection();
    }
    
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

    @Override
    public void draw(Graphics g) {
        // Draw the ghost image
        g.drawImage(image, x, y, null);
    }

    @Override
    public void update() {
        move();


        // Move the ghost
    }

    private void move() {
        // Check if it's time to change direction
        moveCounter++;
        if (moveCounter >= MAX_MOVE_COUNT) {
            chooseRandomDirection();
            moveCounter = 0; // Reset counter
        }

        // Move in the current direction
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

        // Update position
        x += dx;
        y += dy;
    }

    private void chooseRandomDirection() {
        // Choose a random valid direction
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

        // Check if the chosen direction is valid (not opposite to current direction)
        if (!isOppositeDirection(nextDirection)) {
            currentDirection = nextDirection;
        }
    }

    private boolean isOppositeDirection(Direction direction) {
        // Check if the given direction is opposite to the current direction
        return (currentDirection == Direction.RIGHT && direction == Direction.LEFT) ||
                (currentDirection == Direction.LEFT && direction == Direction.RIGHT) ||
                (currentDirection == Direction.UP && direction == Direction.DOWN) ||
                (currentDirection == Direction.DOWN && direction == Direction.UP);
    }
}