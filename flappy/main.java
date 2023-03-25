package flappy;

import javax.swing.JFrame;

/**
 *
 * @author aryalpatrick
 */
public class main {

    public static void main(String[] args) {
        GamePanel gamepanel = new GamePanel();
        JFrame window = new JFrame();

        window.add(gamepanel);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Flappy Bird");
        window.pack();
        window.setVisible(true);
        gamepanel.startGameThread();

    }

}
