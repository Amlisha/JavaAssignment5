package com.example.pacman;

public class ScoreManager {
    private int score;
    private int lives;

    public ScoreManager() {
        reset();
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public void addPoints(int points) {
        score += points;
    }

    public void loseLife() {
        if (lives > 0) {
            lives--;
        }
    }

    public void reset() {
        score = 0;
        lives = 3;
    }
}
