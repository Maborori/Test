package pacman;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Pacman extends GameObject {
    private int dx, dy; // Movement deltas
    private Map<Direction, Image[]> animations; // Map to store animations for each direction
    public Direction currentDirection; // Current movement direction
    private int currentImageIndex; // Index of the current image in the animation
    private int animationDelay = 10; // Delay between animation frames
    private int animationCounter = 0;

    public Pacman() {
        super(50, 50, 16, 16); // Initialize the position and size using the constructor of GameObject
        animations = new HashMap<>();
        loadAnimations();
        currentDirection = Direction.RIGHT; // Default direction
        currentImageIndex = 0;
    }

    private void loadAnimations() {
        try {
            // Load animations for each direction
            animations.put(Direction.RIGHT, loadAnimationImages("/images/pacman_right_", 3));
            animations.put(Direction.LEFT, loadAnimationImages("/images/pacman_left_", 3));
            animations.put(Direction.UP, loadAnimationImages("/images/pacman_up_", 3));
            animations.put(Direction.DOWN, loadAnimationImages("/images/pacman_down_", 3));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Image[] loadAnimationImages(String baseName, int count) throws IOException {
        Image[] images = new Image[count];
        for (int i = 0; i < count; i++) {
            String imagePath = baseName + i + ".png";
            URL imageUrl = getClass().getResource(imagePath);
            System.out.println("Loading image: " + imagePath + " from URL: " + imageUrl);
            if (imageUrl.equals(null)) {
                throw new IOException("Image not found: " + imagePath);
            }
            images[i] = ImageIO.read(imageUrl);
        }
        return images;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(animations.get(currentDirection)[currentImageIndex], x, y, null);
    }

    @Override
    public void update() {
        x += dx;
        y += dy;

        animationCounter++;
        if (animationCounter >= animationDelay) {
            // Move to the next image in the animation
            currentImageIndex = (currentImageIndex + 1) % animations.get(currentDirection).length;
            animationCounter = 0; // Reset animation counter
        }
    }

    public void move(int dx, int dy, Direction direction) {
        this.dx = dx * 3;
        this.dy = dy * 3;
        this.currentDirection = direction;
    }

    // Getters and setters for position
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
