import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class SnakeApplication extends JFrame{
    Timer timer = new Timer(600, new TimerListener());
    String gameStyle = "normal";

    Image background = (new ImageIcon("images/bg.png")).getImage();
    Image snakeRightFace= (new ImageIcon("images/rightFace.png")).getImage();
    Image snakeLeftFace= (new ImageIcon("images/leftFace.png")).getImage();
    Image snakeUpFace= (new ImageIcon("images/upFace.png")).getImage();
    Image snakeDownFace= (new ImageIcon("images/downFace.png")).getImage();
    Image snakeRightFaceDeath= (new ImageIcon("images/rightFaceDeath.png")).getImage();
    Image snakeLeftFaceDeath= (new ImageIcon("images/leftFaceDeath.png")).getImage();
    Image snakeUpFaceDeath= (new ImageIcon("images/upFaceDeath.png")).getImage();
    Image snakeDownFaceDeath= (new ImageIcon("images/downFaceDeath.png")).getImage();
    Image snakeHead = snakeRightFace;
    Image snakeBody = (new ImageIcon("images/snakeBody.png")).getImage();
    Image goodFood = (new ImageIcon("images/food.png")).getImage();
    Image badFood = (new ImageIcon("images/poison.png")).getImage();

    Image gameOverBG = (new ImageIcon("images/gameOverBG.png")).getImage();

    boolean isDie = false;
    char direction = 'r';
    ArrayList<Snake> snake = new ArrayList<Snake>();
    ArrayList<Snake> oldSnake = new ArrayList<Snake>();
    ArrayList<Food> food = new ArrayList<Food>();
    int stepsCount = 0;
    long score = 0;
    int cellSize = 100;

    JButton jbtNewGame = new JButton("NEW GAME");
    JLabel jlbScore = new JLabel("score: ");
    JTextField jtfScore = new JTextField(10);


    public SnakeApplication() {
        snake.add(new Snake());

        DrawingSnake panel = new DrawingSnake();
        setLayout(new BorderLayout(10, 10));
        add(new DrawingSnake(), BorderLayout.CENTER);

        JPanel rightSidePanel = new JPanel(new GridLayout(10, 1));
        rightSidePanel.add(jbtNewGame);
        rightSidePanel.add(jlbScore);
        rightSidePanel.add(jtfScore);
        add(rightSidePanel, BorderLayout.EAST);

        Font myFont = new Font("Tahoma", Font.BOLD, 24);
        jbtNewGame.setFont(myFont);
        jlbScore.setFont(myFont);
        jtfScore.setFont(myFont);

        jbtNewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }


    public static void main(String[] args) {
        JFrame game = new SnakeApplication();
        game.setSize(870, 839);
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.setLocationRelativeTo(null);
        game.setVisible(true);
    }

//    Search and add similar value inside snake
    void compactSnake() {
        for (int i = 1; i < snake.size() - 1; i++) {
            if (snake.get(i).value == snake.get(i + 1).value) {
                snake.get(i).value += snake.get(i + 1).value;
                int x = snake.get(i + 1).x;
                int y = snake.get(i + 1).y;
                snake.remove(i + 1);
                for (int j = i + 1; j < snake.size(); j++) {
                    int x_temp = snake.get(j).x;
                    int y_temp = snake.get(j).y;
                    snake.get(j).x = x;
                    snake.get(j).y = y;
                    x = x_temp;
                    y = y_temp;
                }
            }
        }
    }

//    Move all parts of snake
    void moving() {
        int x_previous = snake.get(0).x;
        int y_previous = snake.get(0).y;

//        traceGame("" + stepsCount + "step before compact");
        compactSnake();
//        traceGame("" + stepsCount + "after  compact");

        oldSnake.clear();
//        clone snake to oldSnake
        for (Snake s : snake) {
            oldSnake.add(s);
        }
//        moving depends from keyboard
        switch (direction) {
            case 'l': turnLeft(); break;
            case 'r': turnRight(); break;
            case 'u': turnUp(); break;
            case 'd': turnDown(); break;
        }

        if (isDie) {
            return;
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
        else if (stepsCount % 2 == 0 && stepsCount != 0 && food.size() <= 15) {
            addFood();
        }
        else if (stepsCount % 4 == 0 && stepsCount != 0 && food.size() > 15) {
            addFood();
        }
        if (stepsCount % 30 == 0 && stepsCount != 0) {
            food.remove(0);
        }
//        traceGame("" + stepsCount + " end moving");
        stepsCount++;
    }

    void turnLeft() {
        int foodIndex = searchingFoodByCoordinates(snake.get(0).x - cellSize, snake.get(0).y);
        int snakeIndex = searchingSnakeByCoordinates(snake.get(0).x - cellSize, snake.get(0).y);
        if ((foodIndex != -1 && !food.get(foodIndex).isGood) || (snakeIndex != -1 && snakeIndex != snake.size() - 1)) {
                isDie = true;
                snakeHead = snakeLeftFaceDeath;
        }
        else if ((snake.get(0)).x == 0 ) {
            isDie = true;
            snakeHead = snakeLeftFaceDeath;
        }
        else {
            (snake.get(0)).x -= cellSize;
        }
    }

    void turnRight() {
        int foodIndex = searchingFoodByCoordinates(snake.get(0).x + cellSize, snake.get(0).y);
        int snakeIndex = searchingSnakeByCoordinates(snake.get(0).x + cellSize, snake.get(0).y);
        if ((foodIndex != -1 && !food.get(foodIndex).isGood) || (snakeIndex != -1 && snakeIndex != snake.size() - 1)) {
            isDie = true;
            snakeHead = snakeRightFaceDeath;
        }
        else if ((snake.get(0)).x == 5 * cellSize) {
            isDie = true;
            snakeHead = snakeRightFaceDeath;
        }
        else {
            (snake.get(0)).x += cellSize;
        }
    }

    void turnUp() {
        int foodIndex = searchingFoodByCoordinates(snake.get(0).x, snake.get(0).y - cellSize);
        int snakeIndex = searchingSnakeByCoordinates(snake.get(0).x, snake.get(0).y - cellSize);
        if ((foodIndex != -1 && !food.get(foodIndex).isGood) || (snakeIndex != -1 && snakeIndex != snake.size() - 1)) {
            isDie = true;
            snakeHead = snakeUpFaceDeath;
        }
        else if ((snake.get(0)).y == 0) {
            isDie = true;
            snakeHead = snakeUpFaceDeath;
        }
        else {
            (snake.get(0)).y -= cellSize;
        }
    }

    void turnDown() {
        int foodIndex = searchingFoodByCoordinates(snake.get(0).x, snake.get(0).y + cellSize);
        int snakeIndex = searchingSnakeByCoordinates(snake.get(0).x, snake.get(0).y + cellSize);
        if ((foodIndex != -1 && !food.get(foodIndex).isGood) || (snakeIndex != -1 && snakeIndex != snake.size() - 1)) {
            isDie = true;
            snakeHead = snakeDownFaceDeath;
        }
        else if ((snake.get(0)).y == 7 * cellSize) {
            isDie = true;
            snakeHead = snakeDownFaceDeath;
        }
        else {
            (snake.get(0)).y += cellSize;
        }
    }

    int searchingFoodByCoordinates(int element_x, int element_y) {
       int index = -1;
        for (int i = 0; i < food.size(); i++) {
            if (food.get(i).x == element_x && food.get(i).y == element_y) {
                index = i;
                break;
            }
        }
        return index;
    }

    int searchingSnakeByCoordinates(int element_x, int element_y) {
        int index = -1;
        if (gameStyle.equals("burrow")) {
            return index;
        }
        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(i).x == element_x && snake.get(i).y == element_y) {
                index = i;
                break;
            }
        }
        return index;
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
        int x = (int)(Math.random() * 6) * cellSize;
        int y = (int)(Math.random() * 8) * cellSize;
        return new int[]{x, y};
    }

//    add food in board
    void addFood() {
        boolean foodWasAdded = false;
        long lastElementValue = snake.get(snake.size() - 1).value == 0 ? 1 : snake.get(snake.size() - 1).value;
        long lastElementPower = (long)(Math.log(lastElementValue) / Math.log(2));
        double maxPower = lastElementPower + 4;
        double minPower = lastElementPower - 15;
        int power = lastElementPower > 15? (int) (minPower + (Math.random() * maxPower)) : (int)(Math.random() * maxPower);
        long value = (long)(Math.pow(2, power));
        int x_prohibited = snake.get(0).x;
        int y_prohibited = snake.get(0).y;

        while(!foodWasAdded) {
            int[] xy = generateRandomCoordinates();

            if (!isSamePlaceSnake(xy[0], xy[1], 0) && !isSamePlaceFood(xy[0], xy[1])) {
                if (snake.size() + food.size() < 40 &&
                        (xy[0] == x_prohibited || xy[0] == x_prohibited - cellSize || xy[0] == x_prohibited + cellSize) &&
                        (xy[1] == y_prohibited || xy[1] == y_prohibited - cellSize || xy[1] == y_prohibited + cellSize)){
                    continue;
                }

//                if game without burrow
//                else if (snake.size() + food.size() == 47) {
//                    timer.stop();
//                }
                else {
                    food.add(new Food(xy[0], xy[1], value, lastElementValue > value));
                    foodWasAdded = true;
                }
            }
        }
    }

    void addFood(long initialValue) {
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



    void eatFood() {
        for (int i = 0; i < food.size(); i++) {
            if (snake.get(0).x == food.get(i).x && snake.get(0).y == food.get(i).y && food.get(i).isGood) {
                score += 1;
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
//            moved to turnLeft, turnRight, turnUp, turnDown
//            else if (snake.get(0).x == food.get(i).x && snake.get(0).y == food.get(i).y && !food.get(i).isGood) {
//                System.out.println(score);
//                isDie = true;
//            }
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


//    formatting value to String
    String formatValue(long value) {
        long kb = 1024L;
        long mb = (long)Math.pow(1024, 2);
        long gb = (long)Math.pow(1024, 3);
        long tb = (long)Math.pow(1024, 4);
        if (value <= 8192) {
            return (long)value + "";
        }
        else if (value > 8192 && value <= (8192 * kb)) {
            return (long)(value / kb) + "K";
        }
        else if (value > (8192 * kb) && value <= (8192 * mb)) {
            return (long)(value / mb) + "M";
        }
        else if (value > (8192 * mb) && value <= (8192 * gb)) {
            return (long)(value / gb) + "G";
        }
        else {
            return (long)(value / tb) + "T";
        }
    }

    int setMarginForLetters(String s) {
        switch (s.length()) {
            case 1: return 40;
            case 2: return 30;
            case 3: return 20;
            case 4: return 12;
            case 5:return 5;
            default: return 0;
        }
    }


//      tracing
//    void traceGame(String message) {
//
//        PrintWriter out = null;
//        try {
//            out = new PrintWriter(new BufferedWriter(new FileWriter("e:\\COM\\FALL2014\\COMP135\\Final Project\\GameSnake2048\\logs\\log.txt", true)));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        String[][] matrix = new String[8][6];
//
//        for (int i = 0; i < snake.size(); i++) {
//            matrix[snake.get(i).y / cellSize][snake.get(i).x/ cellSize] = "[" + snake.get(i).value + "]";
//        }
//
//        for (int i = 0; i < food.size(); i++) {
//            matrix[food.get(i).y / cellSize][food.get(i).x / cellSize] = "" + food.get(i).value;
//        }
//
//        out.println(message);
//        for (int i = 0; i < matrix.length; i++) {
//            for (int j = 0; j < matrix[i].length; j++) {
//                out.printf("%5s", matrix[i][j]);
//            }
//            out.println();
//        }
//        out.println("\n\n\n");
//        out.flush();
//        out.close();
//    }

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
            checkFood();
//            g.setColor(Color.DARK_GRAY);
            g.drawImage(snakeHead, (snake.get(0)).x, (snake.get(0)).y, this);
            for (int i = 1; i < snake.size(); i++) {
                String formatString = formatValue(snake.get(i).value);
                if (formatString.length() > 4) {
                    g.setFont(new Font("Tahoma", Font.BOLD, 25));
                }
                else {
                    g.setFont(new Font("Tahoma", Font.BOLD, 30));
                }
                g.drawImage(snakeBody, (snake.get(i)).x, (snake.get(i)).y, this);
                g.drawString(formatString, snake.get(i).x + setMarginForLetters(formatString), snake.get(i).y + 60);
            }

            if (food.size() > 0) {
                for (int i = 0; i < food.size(); i++) {
                    Image image = badFood;
                    if (food.get(i).isGood ) {
                        image = goodFood;
                    }
                    String formatString = formatValue(food.get(i).value);
                    if (formatString.length() > 4) {
                        g.setFont(new Font("Tahoma", Font.BOLD, 27));
                    }
                    else {
                        g.setFont(new Font("Tahoma", Font.BOLD, 30));
                    }
                    g.drawImage(image, food.get(i).x, food.get(i).y, this);
                    g.drawString(formatString, food.get(i).x + setMarginForLetters(formatString), food.get(i).y + 60);
                }
            }

            if (isDie) {
                timer.stop();
                g.setColor(new Color(250, 250, 250));
                g.drawImage(gameOverBG, 80, 300, this);
//                g.fillRect(70, 300, 460, 210);
                g.setFont(new Font("Tahoma", Font.BOLD, 80));
                g.setColor(new Color(70, 70, 70));
                g.drawString("Game over", getWidth() / 2 - 210, getHeight() / 2);
                g.setFont(new Font("Tahoma", Font.BOLD, 50));
                g.drawString("score: " + score, getWidth() / 2 - 100, getHeight() / 2 + 60);
            }
            jtfScore.setText(score + "");

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
