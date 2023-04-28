package bots;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import arena.BattleBotArena;
import arena.BotInfo;
import arena.Bullet;

/**
 * <b>Introduction</b> <br>
 * <br>
 *
 * This is an abstract class for a BattleBots player. All Bots to be used in the
 * arena
 * <i>must</i> extend this class, and <i>must</i> be part of the <i>bots</i>
 * package.<br>
 * <Br>
 *
 * The Bots decide at each frame whether to move, shoot, and message one
 * another,
 * but they do <i>not</i> get to decide where they are, and they do <i>not</i>
 * get to do
 * any processing except when their methods are called by the BattleBotArena
 * (i.e. no
 * Timers or extra Threads allowed). Bots are also limited in the amount of
 * processing
 * they can do in the course of a match (see the rules on this in the
 * BattleBotArena
 * class). <br>
 * <br>
 *
 * The arena is the referee and decides where the Bots are based on the moves
 * they request.
 * It also decides when they are allowed to make those moves, when they
 * overheat, and when
 * they die. The Bots do draw themselves, but are honour-bound to draw
 * themselves at the
 * location the Arena gives them when it calls their <i>draw()</i> method. <br>
 * <br>
 *
 * <b>Rules</b>
 * <ol>
 *
 * <li>All code for a single Bot must be contained within a single class within
 * a single file.
 * No inner classes or anonymous inner classes are allowed either.</li>
 *
 * <li>Bots may not spawn threads or use timers or any other method to get
 * around
 * the CPU limits imposed by the arena.</li>
 *
 * <li>Bots should draw themselves (mostly) within a circle inscribed in
 * a square of side length equal to RADIUS * 2.</li>
 *
 * <li>You may draw your Bots using images (see <i>imageNames()</i> and
 * <i>loadedImages()</i>
 * below) but these images should be resized to be squares of side length RADIUS
 * * 2 so as
 * not to take up too much memory (most of the available memory is needed for
 * the instant
 * replay system).</li>
 *
 * <li>Messages to other Bots will be truncated to 200 characters. For display
 * purposes,
 * they will be truncated to whatever fits on the screen.</li>
 * </ol>
 * <br>
 * 
 * <b>Rubric</b><br>
 * <br>
 *
 * To get an A+, your robot should meet all of the following criteria:
 * <ol>
 * <li>Your code must meet all the standards and rules set out in this
 * documentation.</li>
 * <li>Your code must be fully documented to javadoc standards.</li>
 * <li>Your strategy must be completely described in the class header. It should
 * be well thought out
 * and ambitious.</li>
 * <li>Your code must be commented in a way that makes it clear where and how
 * the different
 * parts of your strategy are implemented.</li>
 * <li>Your strategy must make use of at least two of the three arrays of
 * information provided
 * by the Arena in each call to <i>getMove()</i>.</li>
 * <li>Your code must be as efficient as possible. There should be no
 * superfluous or unnecessary
 * comparison or assignment operations.</li>
 * </ol>
 * <br>
 * 
 * <b>More Information</b><br>
 * <br>
 *
 * For a more complete set of rules, see the BattleBotArena class. For more info
 * about this class,
 * see the comments on each of its methods.
 *
 * @author Sam Scott
 * @version 1.0 (March 3, 2011)
 */
public class NorthcottBot extends Bot {

    private String nextMessage = null;

    private String[] killMessages = { "GYAT DAMN U BAD", "LOL", "MF U SUCK", "idiot." };

    Image current, up, down, right, left;

    private String name = null;

    private int moveCount = 99;

    private int move = BattleBotArena.UP;

    private int msgCounter = 0;

    private int targetNum = (int) (Math.random() * BattleBotArena.NUM_BOTS);

    private int sleep = (int) (Math.random() * 5 + 1);

    private boolean overheat = false;

    public String[] imageNames() {
        String[] paths = { "pikachu_up.png", "pikachu_down.png", "pikachu_right.png", "pikachu_left.png" };
        return paths;
    }

    public void loadedImages(Image[] images) {
        if (images != null) {
            if (images.length > 0)
                up = images[0];
            if (images.length > 1)
                down = images[1];
            if (images.length > 2)
                right = images[2];
            if (images.length > 3)
                left = images[3];
            current = up;
        }
    }

    public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) {

        if (overheat) {
            try {
                Thread.sleep(sleep);
            } catch (Exception e) {
            }
        }

        moveCount++;

        if (--msgCounter == 0) {
            move = BattleBotArena.SEND_MESSAGE;
            moveCount = 99;
        }

        else if (moveCount >= 30 + (int) Math.random() * 60) {
            moveCount = 0;
            int choice = (int) (Math.random() * 8);
            if (choice == 0) {
                move = BattleBotArena.UP;
                current = up;
            } else if (choice == 1) {
                move = BattleBotArena.DOWN;
                current = down;
            } else if (choice == 2) {
                move = BattleBotArena.LEFT;
                current = left;
            } else if (choice == 3) {
                move = BattleBotArena.RIGHT;
                current = right;
            } else if (choice == 4) {
                move = BattleBotArena.FIREUP;
                moveCount = 99;
                current = up;
            } else if (choice == 5) {
                move = BattleBotArena.FIREDOWN;
                moveCount = 99;
                current = down;
            } else if (choice == 6) {
                move = BattleBotArena.FIRELEFT;
                moveCount = 99;
                current = left;
            } else if (choice == 7) {
                move = BattleBotArena.FIRERIGHT;
                moveCount = 99;
                current = right;
            }
        }
        return move;
    }

    public void newRound() {
        if (botNumber >= targetNum - 3 && botNumber <= targetNum + 3)
            overheat = true;
    }

    public String outgoingMessage() {
        String msg = nextMessage;
        nextMessage = null;
        return msg;
    }

    public String getName() {
        if (name == null)
            name = "Northcott";
        return name;
    }

    public String getTeamName() {
        return "Arena";
    }

    /**
     * Draws the bot at x, y
     * 
     * @param g The Graphics object to draw on
     * @param x Left coord
     * @param y Top coord
     */
    public void draw(Graphics g, int x, int y) {
        if (current != null)
            g.drawImage(current, x, y, Bot.RADIUS * 2, Bot.RADIUS * 2, null);
        else {
            g.setColor(Color.lightGray);
            g.fillOval(x, y, Bot.RADIUS * 2, Bot.RADIUS * 2);
        }
    }

    /**
     * If the message is announcing a kill for me, schedule a trash talk message.
     * 
     * @param botNum ID of sender
     * @param msg    Text of incoming message
     */
    public void incomingMessage(int botNum, String msg) {
        if (botNum == BattleBotArena.SYSTEM_MSG && msg.matches(".*destroyed by " + getName() + ".*")) {
            int msgNum = (int) (Math.random() * killMessages.length);
            nextMessage = killMessages[msgNum];
            msgCounter = (int) (Math.random() * 30 + 30);
        }
    }

}