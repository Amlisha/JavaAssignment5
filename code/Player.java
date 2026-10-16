
package com.example.pacmana5;

import com.example.pacman.GameCharacter;
import com.example.pacman.Maze;
import com.example.pacman.Movable;
import com.example.pacman.ScoreManager;

public class Player extends GameCharacter implements Movable {

    private int directionX = 0;
    private int directionY = 0;

    private int nextDirectionX = 0;
    private int nextDirectionY = 0;

    public Player(int x, int y) {
        super(x, y);
    }

    public void setDirection(int dx, int dy) {
        nextDirectionX = dx;
        nextDirectionY = dy;
    }

    public int getDirectionX() {
        return directionX;
    }

    public int getDirectionY() {
        return directionY;
    }

    @Override
    public void move(Maze maze) {

        // Try the direction the player requested first.
        int turnX = x + nextDirectionX;
        int turnY = y + nextDirectionY;

        if (!maze.isWall(turnX, turnY)) {
            directionX = nextDirectionX;
            directionY = nextDirectionY;
        }

        // Continue moving in the current direction.
        int nextX = x + directionX;
        int nextY = y + directionY;

        if (!maze.isWall(nextX, nextY)) {
            x = nextX;
            y = nextY;
        }
    }

    public void collectDot(Maze maze, ScoreManager score) {
        if (maze.hasDot(x, y)) {
            maze.collectDot(x, y);
            score.addPoints(10);
        }
    }
}
