package com.example.pacman;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class GameView extends View {
    private Maze maze;
    private com.example.pacmana5.Player player;
    private final List<Ghost> ghosts = new ArrayList<>();
    private ScoreManager scoreManager;

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private long lastGhostMove = 0;
    private int gameState = 0;
    private float startX;
    private float startY;

    public GameView(Context context) {
        super(context);
        resetGame();
        setFocusable(true);
    }

    private void resetGame() {
        maze = new Maze();
        player = new com.example.pacmana5.Player(1, 1);

        ghosts.clear();
        ghosts.add(new Ghost(13, 1));
        ghosts.add(new Ghost(7, 7));
        ghosts.add(new Ghost(13, 13));

        scoreManager = new ScoreManager();
        lastGhostMove = 0;
        gameState = 0;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.rgb(8, 8, 28));

        float headerHeight = 85;
        float bottomPadding = 25;

        float cellSize = Math.min(
                (getWidth() - 32f) / maze.getCols(),
                (getHeight() - headerHeight - bottomPadding)
                        / maze.getRows()
        );

        float gridWidth = cellSize * maze.getCols();
        float gridHeight = cellSize * maze.getRows();

        float offsetX = (getWidth() - gridWidth) / 2f;

        float availableHeight = getHeight()
                - headerHeight - bottomPadding;

        float offsetY = headerHeight
                + (availableHeight - gridHeight) / 2f;

        drawHeader(canvas);

        for (int y = 0; y < maze.getRows(); y++) {
            for (int x = 0; x < maze.getCols(); x++) {
                float left = offsetX + x * cellSize;
                float top = offsetY + y * cellSize;

                if (maze.isWall(x, y)) {
                    paint.setColor(Color.rgb(25, 65, 210));

                    float wallGap = cellSize * 0.10f;

                    canvas.drawRoundRect(
                            new RectF(
                                    left + wallGap,
                                    top + wallGap,
                                    left + cellSize - wallGap,
                                    top + cellSize - wallGap
                            ),
                            5, 5, paint
                    );
                } else if (maze.hasDot(x, y)) {
                    paint.setColor(Color.WHITE);
                    canvas.drawCircle(
                            left + cellSize / 2,
                            top + cellSize / 2,
                            Math.max(2, cellSize * 0.08f),
                            paint
                    );
                }
            }
        }
        drawSafeHome(canvas, offsetX, offsetY, cellSize);
        drawPlayer(canvas, offsetX, offsetY, cellSize);

        int[] colors = {
                0xFFFF0000,
                0xFF00FFFF,
                0xFFFF00FF
        };

        for (int i = 0; i < ghosts.size(); i++) {
            drawGhost(canvas, offsetX, offsetY, cellSize,
                    ghosts.get(i), colors[i]);
        }

        if (gameState == 0) {
            updateGame();
            postInvalidateDelayed(300);
        } else {
            drawEndMessage(canvas);
        }
    }

    private void drawHeader(Canvas canvas) {
        paint.setColor(Color.YELLOW);
        paint.setTextSize(34);
        paint.setFakeBoldText(true);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("PAC-MAN", 25, 42, paint);

        paint.setTextSize(18);
        paint.setFakeBoldText(false);
        paint.setColor(Color.WHITE);
        canvas.drawText("Score: " + scoreManager.getScore(),
                25, 70, paint);

        // Draw larger hearts to represent remaining lives.
        paint.setTextSize(56);
        paint.setFakeBoldText(true);
        paint.setTextAlign(Paint.Align.RIGHT);

        for (int i = 0; i < 3; i++) {
            paint.setColor(
                    i < scoreManager.getLives()
                            ? Color.RED
                            : Color.DKGRAY
            );

            canvas.drawText("♥",
                    getWidth() - 35 - (2 - i) * 55,
                    55, paint);
        }

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setFakeBoldText(false);
    }


    private void drawSafeHome(Canvas canvas, float ox, float oy,
                              float size) {
        float left = ox + 6 * size;
        float top = oy + 2 * size;
        float right = ox + 9 * size;
        float bottom = oy + 4 * size;

        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);

        // Draw the house roof.
        canvas.drawLine(left, top + size,
                (left + right) / 2, top, paint);
        canvas.drawLine((left + right) / 2, top,
                right, top + size, paint);

        // Draw the house walls and base.
        canvas.drawRect(left, top + size,
                right, bottom, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(size * 0.35f);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("SAFE",
                (left + right) / 2,
                bottom - size * 0.3f, paint);

        paint.setTextAlign(Paint.Align.LEFT);
    }


    private void drawPlayer(Canvas canvas, float ox, float oy,
                            float size) {
        float cx = ox + (player.getX() + 0.5f) * size;
        float cy = oy + (player.getY() + 0.5f) * size;
        float radius = size * 0.46f;

        // Draw Pac-Man's yellow body.
        paint.setColor(Color.YELLOW);
        canvas.drawCircle(cx, cy, radius, paint);

        // Work out which direction the mouth should face.
        float angle = 0;

        if (player.getDirectionX() < 0) {
            angle = 180;
        } else if (player.getDirectionY() < 0) {
            angle = 270;
        } else if (player.getDirectionY() > 0) {
            angle = 90;
        }

        // Draw a dark wedge to create the open mouth.
        paint.setColor(Color.rgb(8, 8, 28));

        canvas.drawArc(
                new RectF(cx - radius, cy - radius,
                        cx + radius, cy + radius),
                angle - 28,
                56,
                true,
                paint
        );
    }


    private void drawGhost(Canvas canvas, float ox, float oy,
                           float size, Ghost ghost, int color) {
        float cx = ox + (ghost.getX() + 0.5f) * size;
        float cy = oy + (ghost.getY() + 0.5f) * size;

        paint.setColor(color);
        canvas.drawCircle(cx, cy, size * 0.36f, paint);

        paint.setColor(Color.WHITE);
        canvas.drawCircle(cx - size * 0.12f,
                cy - size * 0.05f, size * 0.09f, paint);
        canvas.drawCircle(cx + size * 0.12f,
                cy - size * 0.05f, size * 0.09f, paint);

        paint.setColor(Color.BLACK);
        canvas.drawCircle(cx - size * 0.12f,
                cy - size * 0.05f, size * 0.04f, paint);
        canvas.drawCircle(cx + size * 0.12f,
                cy - size * 0.05f, size * 0.04f, paint);
    }

    private void updateGame() {
        player.move(maze);
        player.collectDot(maze, scoreManager);

        long now = System.currentTimeMillis();

        if (now - lastGhostMove > 450) {
            for (Ghost ghost : ghosts) {
                ghost.move(maze);
            }
            lastGhostMove = now;
        }

        for (Ghost ghost : ghosts) {
            if (!maze.isSafeHome(player.getX(), player.getY())
                    && ghost.getX() == player.getX()
                    && ghost.getY() == player.getY()) {
                scoreManager.loseLife();

                if (scoreManager.getLives() <= 0) {
                    gameState = 2;
                } else {
                    player.setPosition(1, 1);
                    ghosts.get(0).setPosition(13, 1);
                    ghosts.get(1).setPosition(7, 7);
                    ghosts.get(2).setPosition(13, 13);
                }
                return;
            }
        }

        if (maze.countDots() == 0) {
            gameState = 1;
        }
    }


    private void drawEndMessage(Canvas canvas) {
        paint.setColor(Color.YELLOW);
        paint.setTextSize(32);
        paint.setFakeBoldText(true);
        paint.setTextAlign(Paint.Align.CENTER);

        String message = gameState == 1
                ? "YOU WIN!" : "GAME OVER";

        canvas.drawText(message,
                getWidth() / 2f,
                getHeight() / 2f - 35,
                paint);

        paint.setTextSize(24);
        paint.setColor(Color.WHITE);

        canvas.drawText("Final Score: " + scoreManager.getScore(),
                getWidth() / 2f,
                getHeight() / 2f + 5,
                paint);

        paint.setTextSize(18);
        paint.setFakeBoldText(false);

        canvas.drawText("Tap to play again",
                getWidth() / 2f,
                getHeight() / 2f + 45,
                paint);

        paint.setTextAlign(Paint.Align.LEFT);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startX = event.getX();
            startY = event.getY();
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            float dx = event.getX() - startX;
            float dy = event.getY() - startY;

            if (gameState != 0) {
                resetGame();
                return true;
            }

            // Ignore tiny movements.
            if (Math.abs(dx) < 15 && Math.abs(dy) < 15) {
                return true;
            }

            // Choose the strongest swipe direction.
            if (Math.abs(dx) > Math.abs(dy)) {
                player.setDirection(dx > 0 ? 1 : -1, 0);
            } else {
                player.setDirection(0, dy > 0 ? 1 : -1);
            }

            invalidate();
            return true;
        }

        return true;
    }
}
