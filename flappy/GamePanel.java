package flappy;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author aryalpatrick
 */
public class GamePanel extends JPanel implements Runnable, ActionListener, MouseListener, KeyListener {

    final int blockHeight = 16; //size of a texture  
    final int blockWidht = 16; //size of a texture  

    final int game_Height = 800; //height of the window of game    
    final int game_Width = 800; //width of the window of game

    int rectSpeed = 5;
    int FPS = 120;
    int score = 00;

    public ArrayList<Rectangle> rect;

    public Random random;
    public Rectangle bird;
    public Rectangle grass;

    public int ticks, yMotion;
    public boolean started, gameOver;

    Thread gameThread;

    public GamePanel() {

        this.setPreferredSize(new Dimension(game_Width, game_Height));
        this.setDoubleBuffered(true);//supposed to increase rendering performance
        this.addKeyListener(this);
        this.addMouseListener(this);

        this.setFocusable(true);// makes Game Panel focused to listen for key input

        bird = new Rectangle(200, 200, blockWidht, blockHeight);
        grass = new Rectangle(0, game_Height - 120, game_Width, 120);

        rect = new ArrayList<>();
        random = new Random();

        Timer timer = new Timer(20, this);

        addrect(true);
        addrect(true);
        addrect(true);
        addrect(true);

        timer.start();

    }

    public void addrect(boolean start) {

        int gap = 300;
        int width = 100;
        int height = 50 + random.nextInt(300);

        if (start) {

            rect.add(new Rectangle(game_Width + width + rect.size() * 300, game_Height - height - grass.height, width, height));
            rect.add(new Rectangle(game_Width + width + (rect.size() - 1) * 300, 0, width, game_Height - height - gap));

        } else {

            rect.add(new Rectangle(rect.get(rect.size() - 1).x + 600, game_Height - height - grass.height, width, height));
            rect.add(new Rectangle(rect.get(rect.size() - 1).x, 0, width, game_Height - height - gap));

        }
    }

    public void paintRect(Graphics g, Rectangle rect) {
        g.setColor(new Color(235, 69, 95));
        g.fillRect(rect.x, rect.y, rect.width, rect.height);

    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();//to start irl timer or sth like that
        //it calls run(); method
        // thread is sequence of program running
    }

    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {

        double drawInterval = 1000000000 / FPS;
        double nexDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {

            repaint();//calling paintComponent function

            try {

                double remainingTime = nexDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);

                nexDrawTime += drawInterval;

            } catch (InterruptedException ex) {

            }

        }
    }

    public void jump() {
        if (gameOver) {

            bird = new Rectangle(bird.x, bird.y, bird.width, bird.height);
            yMotion = 0;
            score = 00;
            rect.clear();

            addrect(true);
            addrect(true);
            addrect(true);
            addrect(true);

            gameOver = false;
        }
        if (!started) {

            started = true;

        } else if (!gameOver) {

            if (yMotion > 0) {
                yMotion = 0;
            }
            yMotion = yMotion - 10;

        }
    }

    @Override
    public void paintComponent(Graphics g) {//this is a inbuilt fiunction of JPanel, this method is calling class Graphics as g
        super.paintComponent(g);

        //background color
        g.setColor(new Color(43, 52, 103));
        g.fillRect(0, 0, game_Width, game_Height);

        //character
        g.setColor(new Color(252, 255, 231));
        g.fillRect(bird.x, bird.y, bird.width, bird.height);

        //ground texture
        g.setColor(new Color(31, 138, 112));
        g.fillRect(grass.x, grass.y, grass.width, grass.height);

        //rectangle texture
        for (Rectangle column : rect) {
            paintRect(g, column);
        }

        //gameover
        g.setColor(Color.white);
        g.setFont(new Font("Times New Roman", 6, 30));

        if (!started) {
            g.drawString(" click or press (up/space/w) to start", (game_Width / 3)-60, (game_Height / 2)-25);
        }

        if (gameOver == true) {
            rect.clear();
            g.setColor(Color.white);
            g.setFont(new Font("Times New Roman", 6, 40));
            g.drawString("game over", game_Width / 2 - 250, game_Height / 2);
        }
        
        g.setFont(new Font("Times New Roman", 6, 30));
        g.drawString(String.valueOf(score / 28), 407, 65);
        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ticks++;

        if (started) {

//        moving the column:(decreasing x cord of column by column speed)
            for (int i = 0; i < rect.size(); i++) {

                Rectangle column = rect.get(i);
                column.x -= rectSpeed;

            }

            if (ticks % 2 == 0 && yMotion < 15) {
                yMotion += 2;
            }
            bird.y += yMotion;

            for (Rectangle column : rect) {

                if (!gameOver && column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 && bird.x + bird.width / 2 > column.x + column.width / 2) {
                    score++;
                } else if (gameOver) {
                    score = score;
                }

                if (column.intersects(bird)) {
                    gameOver = true;
                }

                if (bird.y < 0 || bird.y > game_Height - grass.height - 1) {
                    gameOver = true;

                }
                if (bird.y + yMotion >= game_Height - grass.height) {
                    bird.y = game_Height - grass.height - bird.width;
                }
            }

            //if xcord of rectangle < 0 :remove
            for (int i = 0; i < rect.size(); i++) {
                Rectangle column = rect.get(i);
                if (column.x + column.width < 0) {

                    rect.remove(column);
                    if (column.y == 0) {
                        addrect(false);
                    }
                }
            }

            for (int i = 0; i < rect.size(); i++) {
                Rectangle column = rect.get(i);
                
                if (column.x + column.width < 0) {
                    
                }
                column.x -= rectSpeed;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        jump();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
            jump();
        }

    }
}
