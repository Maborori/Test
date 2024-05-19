package pacman;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Maze {

    private final int[][] mazeStructure;
    private final List<Rectangle> walls;
    private final List<Rectangle> pellets;

    public Maze() {
        // 0 = empty space, 1 = wall, 2 = pellet
        mazeStructure = new int[][]{
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
            {1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1},
            {1, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 1},
            {1, 2, 1, 2, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1, 2, 2, 1},
            {1, 2, 2, 2, 1, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 1},
            {1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1, 1},
            {1, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
            {1, 2, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1},
            {1, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
            {1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1},
            {1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };

        walls = new ArrayList<>();
        pellets = new ArrayList<>();
        loadMaze();
    }

    private void loadMaze() {
        for (int row = 0; row < mazeStructure.length; row++) {
            for (int col = 0; col < mazeStructure[row].length; col++) {
                if (mazeStructure[row][col] == 1) {
                    walls.add(new Rectangle(col * 40, row * 40, 40, 40));
                } else if (mazeStructure[row][col] == 2) {
                    pellets.add(new Rectangle(col * 40 + 15, row * 40 + 15, 10, 10));
                }
            }
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        for (Rectangle wall : walls) {
            g.fillRect(wall.x, wall.y, wall.width, wall.height);
        }

        g.setColor(Color.WHITE);
        for (Rectangle pellet : pellets) {
            g.fillOval(pellet.x, pellet.y, pellet.width, pellet.height);
        }
    }

    public void update() {
        // No need to update walls, but pellets might be consumed by Pacman
    }

    public boolean checkWallCollision(Rectangle rect) {
        for (int y = 0; y < mazeStructure.length; y++){
            for( int x = 0; x < mazeStructure[y].length; x++){
                if(mazeStructure[y][x] == 1){
                    for (Rectangle wall : walls) {
                    if (rect.intersects(wall)) {
                    return true;
                        }
                    } 
                }
            }

        }

        return false;
    }



    public Point findValidSpawnLocation(int mazeWidth, int mazeHeight, int cellSize) {
        // Create a list of all available spawn locations
        Random random = new Random();
        int maxAttempts = 100; // Maximum number of attempts to find a valid spawn location
        int attempt = 0;

        do {
            int x = random.nextInt(mazeWidth);
            int y = random.nextInt(mazeHeight);
            if (mazeStructure[y][x] == 2) { // 2 indicates an open space
                return new Point(x, y);
            }
            attempt++;
        } while (attempt < maxAttempts);

        // If no valid spawn location found, return null
        return null;
    }

    public boolean checkPelletCollision(Rectangle rect) {
        for (int i = 0; i < pellets.size(); i++) {
            if (rect.intersects(pellets.get(i))) {
                pellets.remove(i);
                return true;
            }
        }
        return false;
    }
}

