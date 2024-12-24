import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;


public class TableTennis {

    private int paddleWidth = 10, paddleHeight = 100;
    private double ballX, ballY, ballSpeedX = 3, ballSpeedY = 3;
    private double paddle1Y, paddle2Y;
    private double paddleSpeed = 5;
    private int player1Score = 0, player2Score = 0;
    private boolean wPressed, sPressed, upPressed, downPressed;

    private final int WIDTH, HEIGHT;
    private GraphicsContext gc;

    public TableTennis(int width, int height, GraphicsContext gc, Scene scene) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.gc = gc;

        // Initialize positions
        paddle1Y = (HEIGHT - paddleHeight) / 2;
        paddle2Y = (HEIGHT - paddleHeight) / 2;
        ballX = WIDTH / 2;
        ballY = HEIGHT / 2;

        // Add key listeners
        scene.setOnKeyPressed(e -> handleKeyPress(e.getCode(), true));
        scene.setOnKeyReleased(e -> handleKeyPress(e.getCode(), false));
    }

    public void start() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                draw();
            }
        };
        timer.start();
    }

    private void handleKeyPress(KeyCode code, boolean isPressed) {
        switch (code) {
            case W -> wPressed = isPressed;
            case S -> sPressed = isPressed;
            case UP -> upPressed = isPressed;
            case DOWN -> downPressed = isPressed;
        }
    }

    private void update() {
        // Move paddles
        if (wPressed && paddle1Y > 0) paddle1Y -= paddleSpeed;
        if (sPressed && paddle1Y < HEIGHT - paddleHeight) paddle1Y += paddleSpeed;
        if (upPressed && paddle2Y > 0) paddle2Y -= paddleSpeed;
        if (downPressed && paddle2Y < HEIGHT - paddleHeight) paddle2Y += paddleSpeed;

        // Move ball
        ballX += ballSpeedX;
        ballY += ballSpeedY;

        // Ball collision with walls
        if (ballY <= 0 || ballY >= HEIGHT) ballSpeedY *= -1;

        // Ball collision with paddles
        if (ballX <= paddleWidth && ballY >= paddle1Y && ballY <= paddle1Y + paddleHeight) {
            ballSpeedX *= -1;
        }
        if (ballX >= WIDTH - paddleWidth - 10 && ballY >= paddle2Y && ballY <= paddle2Y + paddleHeight) {
            ballSpeedX *= -1;
        }

        // Scoring
        if (ballX <= 0) {
            player2Score++;
            resetBall();
        }
        if (ballX >= WIDTH) {
            player1Score++;
            resetBall();
        }
    }

    private void resetBall() {
        ballX = WIDTH / 2;
        ballY = HEIGHT / 2;
        ballSpeedX *= -1;
    }

    private void draw() {
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        gc.setFill(Color.WHITE);

        // Draw paddles
        gc.fillRect(0, paddle1Y, paddleWidth, paddleHeight);
        gc.fillRect(WIDTH - paddleWidth - 10, paddle2Y, paddleWidth, paddleHeight);

        // Draw ball
        gc.fillOval(ballX, ballY, 10, 10);

        // Draw scores
        gc.fillText("Player 1: " + player1Score, 50, 50);
        gc.fillText("Player 2: " + player2Score, WIDTH - 150, 50);
    }
}
