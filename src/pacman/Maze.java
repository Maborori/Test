package pacman;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Maze {

    private final int[][] mazeStructure;
    private final List<Rectangle> walls;
    private final List<Rectangle> pellets;
    private final int CELL_SIZE = 36;

    public Maze() {
        // 0 = empty space, 1 = wall, 2 = pellet
        mazeStructure = new int[][]{
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
        for (Rectangle wall : walls) {
            if (rect.intersects(wall)) {
                return true;
            }
        }
        return false;
    }


    public Point findValidSpawnLocation() {
        Random random = new Random();
        int maxAttempts = 100;

        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            int x = random.nextInt(mazeStructure[0].length);
            int y = random.nextInt(mazeStructure.length);

            if (mazeStructure[y][x] == 2) { // Check if the spot is an open space
                // Calculate the center of the open space
                int centerX = x * CELL_SIZE + CELL_SIZE / 2;
                int centerY = y * CELL_SIZE + CELL_SIZE / 2;
                return new Point(centerX, centerY);
            }
        }
        // If no valid spawn location found, return a default safe location or null
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

    public boolean allPelletsConsumed() {
        return pellets.isEmpty();

    }

}