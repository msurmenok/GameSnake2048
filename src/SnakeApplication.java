import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class SnakeApplication extends JFrame{
    Timer timer = new Timer(1000, new TimerListener());

    Image background = (new ImageIcon("images/bg.png")).getImage();
    Image snakeRightFace= (new ImageIcon("images/rightFace.png")).getImage();
    Image snakeLeftFace= (new ImageIcon("images/leftFace.png")).getImage();
    Image snakeUpFace= (new ImageIcon("images/upFace.png")).getImage();
    Image snakeDownFace= (new ImageIcon("images/downFace.png")).getImage();
    Image snakeHead = snakeRightFace;
    Image snakeBody = (new ImageIcon("images/snakeBody.png")).getImage();
    Image goodFood = (new ImageIcon("images/food.png")).getImage();
    Image badFood = (new ImageIcon("images/poison.png")).getImage();

    boolean isDie = false;
    char direction = 'r';
    ArrayList<Snake> snake;
    ArrayList<Food> food;
    int stepsCount = 0;

    public SnakeApplication() {
        snake = new ArrayList<Snake>();
        snake.add(new Snake());
        snake.add(new Snake(0, 500, 2));
        snake.add(new Snake(0, 600, 4));
        snake.add(new Snake(0, 600, 8));
        snake.add(new Snake(0, 600, 16));

        try {
            food.add(new Food(500, 700, 1, true));
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        DrawingSnake panel = new DrawingSnake();
        add(new DrawingSnake());
    }


    public static void main(String[] args) {
        JFrame game = new SnakeApplication();
        game.setSize(616, 839);
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.setLocationRelativeTo(null);
        game.setVisible(true);
    }

//    Move all parts of snake
    void moving() {
        int x_previous = snake.get(0).x;
        int y_previous = snake.get(0).y;
        if ((snake.get(0)).x >= 0 && (snake.get(0)).x <= 500 && (snake.get(0)).y >= 0 && (snake.get(0)).y <= 700) {
            switch (direction) {
                case 'l': turnLeft(); break;
                case 'r': turnRight(); break;
                case 'u': turnUp(); break;
                case 'd': turnDown(); break;
            }
        }
        else {
            timer.stop();
        }
        for (int i = 1; i < snake.size(); i++) {
            int x_temp = snake.get(i).x;
            int y_temp = snake.get(i).y;
            snake.get(i).x = x_previous;
            snake.get(i).y = y_previous;
            x_previous = x_temp;
            y_previous = y_temp;
        }
//          it works :)
//        stepsCount++;
    }

    void turnLeft() {
        (snake.get(0)).x -= 100;
    }

    void turnRight() {
        (snake.get(0)).x += 100;
    }

    void turnUp() {
        (snake.get(0)).y -= 100;
    }

    void turnDown() {
        (snake.get(0)).y += 100;
    }

//   check and change direction
    void setDirection(char ch) {
        if ((ch == 'l' && direction == 'r') ||
            (ch == 'r' && direction == 'l') ||
            (ch == 'u' && direction == 'd') ||
            (ch == 'd' && direction == 'u')) {
            return;
        }
        direction = ch;
        turnSnakeHead();
    }

// turn head-image
    void turnSnakeHead() {
        switch (direction) {
            case 'l': snakeHead = snakeLeftFace; break;
            case 'r': snakeHead = snakeRightFace; break;
            case 'u': snakeHead = snakeUpFace; break;
            case 'd': snakeHead = snakeDownFace; break;
        }
    }

//    generate random coordinates for food
    int[] generateRandomCoordinates() {
        int x = (int)(Math.random() * 6) * 100;
        int y = (int)(Math.random() * 8) * 100;
        return new int[]{x, y};
    }

//    add food in board
//    void addFood() {
//        int[] xy = generateRandomCoordinates();
//        int value = (int)(Math.pow(2, (int)(Math.random() * 10)));
//        if (!isSamePlace(xy[0], xy[1], 0)) {
//           food.add(new Food(xy[0], xy[1], value, snake.get(snake.size() - 1).value > value));
//        }
//        else {
//            addFood();
//        }
//    }
//    check similar coordinates with startingIndex 0 (for food) or 1 (for Die())
    boolean isSamePlace(int xParam, int yParam, int startingIndex) {
        for (int i = startingIndex; i < snake.size(); i++) {
            if (snake.get(i).x == xParam && snake.get(i).y == yParam) {
                return true;
            }
        }
        return false;
    }

//  constructor, handle from keyboard
     class DrawingSnake extends JPanel {

         DrawingSnake() {
             timer.start();

             addKeyListener(new KeyAdapter() {
                 @Override
                 public void keyPressed(KeyEvent e) {
                     switch (e.getKeyCode()) {
                         case KeyEvent.VK_LEFT: setDirection('l'); break;
                         case KeyEvent.VK_RIGHT: setDirection('r'); break;
                         case KeyEvent.VK_UP: setDirection('u'); break;
                         case KeyEvent.VK_DOWN: setDirection('d'); break;
                     }
                 }
             });

         }

//         Draw everything here
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(background, 0, 0, this);


            this.setFocusable(true);

            moving();
            g.drawImage(goodFood, 500, 700, this);
            if (!(snake.get(0).x == 600 || snake.get(0).x == -100 ||snake.get(0).y == 800 || snake.get(0).y == -100 )){
                g.setFont(new Font("Tahoma", Font.BOLD, 30));
                g.drawImage(snakeHead, (snake.get(0)).x, (snake.get(0)).y, this);
                for (int i = 1; i < snake.size(); i++) {
                    g.drawImage(snakeBody, (snake.get(i)).x, (snake.get(i)).y, this);
                    g.drawString(("" + (snake.get(i).value) + stepsCount), snake.get(i).x + 30, snake.get(i).y + 50);
                }
            }
            else {
                timer.stop();
            }


//            if (food.size() > 0) {
//                for (int i = 0; i < food.size(); i++) {
//                    Image image = badFood;
//                    if (food.get(i).isGood ) {
//                        image = goodFood;
//                    }
//                    g.drawImage(image, food.get(i).x, food.get(i).y, this);
////                    g.drawString(("" + (food.get(i).value)), food.get(i).x + 30, food.get(i).y + 50);
//                }
//            }


        }
    }

//    just timer
    class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            repaint();
        }
    }
}
