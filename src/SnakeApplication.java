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
    ArrayList<Snake> snake = new ArrayList<Snake>();
    ArrayList<Snake> oldSnake = new ArrayList<Snake>();
    ArrayList<Food> food = new ArrayList<Food>();
    int stepsCount = 0;

    public SnakeApplication() {
        snake.add(new Snake());

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


        oldSnake.clear();
//        clone snake to oldSnake
        for (Snake s : snake) {
            oldSnake.add(s);
        }
//        moving depends from keyboard
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

        eatFood();

        for (int i = 1; i < snake.size(); i++) {
            int x_temp = snake.get(i).x;
            int y_temp = snake.get(i).y;
            snake.get(i).x = x_previous;
            snake.get(i).y = y_previous;
            x_previous = x_temp;
            y_previous = y_temp;
        }
        if (stepsCount == 0) {
            addFood(1);
        }
        else if (stepsCount % 4 == 0 && stepsCount != 0) {
            addFood();
        }
        if (stepsCount % 30 == 0 && stepsCount != 0) {
            deleteFood();
        }

        stepsCount++;
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
    void addFood() {
        int[] xy = generateRandomCoordinates();
        int lastElementValue = snake.get(snake.size() - 1).value == 0 ? 1 : snake.get(snake.size() - 1).value;
        double maxPower = Math.log(lastElementValue) / Math.log(2) + 5;
        int power = (int) (Math.random() * maxPower);

        System.out.println("Last element: " + lastElementValue);
        System.out.println("max power: " + maxPower);
        System.out.println("power: " + power);

        int value = (int)(Math.pow(2, power));
        if (!isSamePlaceSnake(xy[0], xy[1], 0) && !isSamePlaceFood(xy[0], xy[1])) {
           food.add(new Food(xy[0], xy[1], value, lastElementValue > value));
        }
        else {
            addFood();
        }
    }

    void addFood(int initialValue) {
        int[] xy = generateRandomCoordinates();
        if (!isSamePlaceSnake(xy[0], xy[1], 0) && !isSamePlaceFood(xy[0], xy[1])) {
            food.add(new Food(xy[0], xy[1], initialValue, true));
        }
        else {
            addFood();
        }
    }

//    good food or not for every step
    void checkFood() {
        for (Food f : food) {
            if (f.value == 1 || f.value == 2) {
                f.isGood = true;
            }
            else if (f.value <= snake.get(snake.size() - 1).value) {
                f.isGood = true;
            }
            else {
                f.isGood = false;
            }
        }
    }

    void deleteFood() {
        food.remove(0);
    }

    void eatFood() {
        for (int i = 0; i < food.size(); i++) {
            if (snake.get(0).x == food.get(i).x && snake.get(0).y == food.get(i).y && food.get(i).isGood) {
                for (int j = (snake.size() - 1); j >= 0 ; j--) {
                    if (snake.get(j).value == food.get(i).value) {
                        snake.get(j).value += food.get(i).value;
                        food.remove(i);
                        return;
                    }
                }
                if (snake.size() == 1) {
                    snake.add(1, new Snake(oldSnake.get(0).x, oldSnake.get(0).y, food.get(i).value));
                }

                else if (food.get(i).value > snake.get(snake.size() - 1).value) {
                    snake.add(new Snake(oldSnake.get(oldSnake.size()-1).x, oldSnake.get(oldSnake.size()-1).y, food.get(i).value));
                }
                else {
                    for (int j = 0; j < snake.size(); j++) {
                        Snake high = snake.get(j + 1);
                        Snake low = snake.get(j);
                        if (food.get(i).value > low.value && food.get(i).value < high.value) {
                            snake.add(snake.indexOf(high), new Snake(high.x, high.y, food.get(i).value));
                            for (int k = snake.indexOf(high); k < snake.size(); k++) {
                                snake.get(k).x = oldSnake.get(k-1).x;
                                snake.get(k).y = oldSnake.get(k-1).y;
                            }
                            break;
                        }
                    }
                }
                food.remove(i);
            }
        }
    }
//    check similar coordinates with startingIndex 0 (for add food) or 1 (for Die())
    boolean isSamePlaceSnake(int xParam, int yParam, int startingIndex) {
        for (int i = startingIndex; i < snake.size(); i++) {
            if (snake.get(i).x == xParam && snake.get(i).y == yParam) {
                return true;
            }
        }
        return false;
    }

//    check similar coordinates for food
    boolean isSamePlaceFood(int xParam, int yParam) {
        for (int i = 0; i < food.size(); i++) {
            if (food.get(i).x == xParam && food.get(i).y == yParam) {
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
            if (!(snake.get(0).x == 600 || snake.get(0).x == -100 ||snake.get(0).y == 800 || snake.get(0).y == -100 )){
                g.setFont(new Font("Tahoma", Font.BOLD, 30));
                g.drawImage(snakeHead, (snake.get(0)).x, (snake.get(0)).y, this);
                for (int i = 1; i < snake.size(); i++) {
                    g.drawImage(snakeBody, (snake.get(i)).x, (snake.get(i)).y, this);
                    g.drawString(("" + (snake.get(i).value)), snake.get(i).x + 30, snake.get(i).y + 50);
                }
            }
            else {
                timer.stop();
            }

            checkFood();
            if (food.size() > 0) {
                for (int i = 0; i < food.size(); i++) {
                    Image image = badFood;
                    if (food.get(i).isGood ) {
                        image = goodFood;
                    }
                    g.drawImage(image, food.get(i).x, food.get(i).y, this);
                    g.drawString(("" + (food.get(i).value)), food.get(i).x + 30, food.get(i).y + 50);
                }
            }

//            print oldSnake
//            System.out.println(stepsCount + " iteration");
//            for (Object s : oldSnake ) {
//                System.out.print(s.toString() + " ");
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
