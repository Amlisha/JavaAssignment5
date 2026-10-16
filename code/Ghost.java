package com.example.pacman;

import java.util.Random;

public class Ghost extends GameCharacter implements Movable{
    private final Random random = new Random();

    public Ghost(int x, int y) {
        super(x, y);
    }

    @Override
    public void move(Maze maze) {

        int[][] directions = {
                {1, 0},
                {-1, 0},
                {0, 1},
                {0, -1}
        };

        for (int i = 0; i < directions.length; i++) {

            int index = random.nextInt(directions.length);

            int dx = directions[index][0];
            int dy = directions[index][1];

            if (!maze.isWall(x + dx, y + dy)) {
                x += dx;
                y += dy;
                return;
            }
        }
    }
}
