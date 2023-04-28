package bots;

import java.awt.Graphics;
import java.awt.Image;

import arena.BotInfo;
import arena.Bullet;
import arena.BattleBotArena;

public class ModhvadiaBot extends Bot{

    // Images for the bot
    Image current, left, right, up, down;

    // Getting bot helper
    BotHelper bh = new BotHelper();

    // Dodge/shoot/run variables
    int action = BattleBotArena.UP;
    int dodgeDistance = 70;
    int shotDistance = 15;
    int runDistance = 100;

    // Gravestone Variables
    boolean blockingRight = false;
    boolean blockingLeft = false;
    boolean blockingUp = false;
    boolean blockingDown = false;

    @Override
    public void newRound() {
        
    }

    @Override
    public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) {

    // Variables

    // To know if I will get hit
    boolean willHitX = false;
    boolean willHitY = false;

        // -- DODGING -- //

        // Checking if there are bullets and bots in the arena
        if (liveBots.length > 0){
            if (bullets.length > 0) {

        // Checking if the bullet is in line with my bot
        for (int i = 0; i < bullets.length; i++) {
                    if (bullets[i].getY() >= me.getY() && bullets[i].getY() <= me.getY() + 2 * RADIUS && bullets[i].getXSpeed() != 0) {
                            willHitX = true;
                    } else if (bullets[i].getX() >= me.getX() && bullets[i].getX() <= me.getX() + 2 * RADIUS && bullets[i].getYSpeed() != 0) {
                            willHitY = true;
                    }
                
        // Checking if the bullet is close enough to me to pose a threat
        if (willHitX == true && bh.calcDistance(me.getX() + RADIUS, me.getY() + RADIUS, bullets[i].getX(), bullets[i].getY()) <= dodgeDistance) {
        // moving out of the way
            if (bullets[i].getY() <= me.getY() + RADIUS && bullets[i].getY() >= me.getY()) {
                current = down;
                action = BattleBotArena.DOWN;
            } else if (bullets[i].getY() > me.getY() + RADIUS && bullets[i].getY() <= me.getY() + 2 * RADIUS) {
                current = up;
                action = BattleBotArena.UP;
            }
            return action;
        } else if(willHitY == true && bh.calcDistance(me.getX() + RADIUS, me.getY() + RADIUS, bullets[i].getX(), bullets[i].getY()) <= dodgeDistance) {
            if (bullets[i].getX() <= me.getX() + RADIUS && bullets[i].getX() >= me.getX()) {
                current = right;
                action = BattleBotArena.RIGHT;
            } else if (bullets[i].getX() > me.getX() + RADIUS && bullets[i].getX() <= me.getX() + 2 * RADIUS) {
                current = left;
                action = BattleBotArena.LEFT;
            }
            return action;
        }
        
    }
    }
}

        // -- GRAVESTONE -- //

// Checking if a gravestone is in the way
if ((bh.findClosest(me, deadBots).getY() >= bh.findClosest(me, liveBots).getY() && bh.findClosest(me, deadBots).getY() <= bh.findClosest(me, liveBots).getY() + 2 * RADIUS) || (bh.findClosest(me, deadBots).getY() + 2 * RADIUS >= bh.findClosest(me, liveBots).getY() && bh.findClosest(me, deadBots).getY() + 2 * RADIUS <= bh.findClosest(me, liveBots).getY() + 2 * RADIUS)) {
    if (bh.findClosest(me, deadBots).getX() < bh.findClosest(me, liveBots).getX() && bh.findClosest(me, deadBots).getX() > me.getX()) {
    blockingRight = true;
    }
} else if ((bh.findClosest(me, deadBots).getY() >= bh.findClosest(me, liveBots).getY() && bh.findClosest(me, deadBots).getY() <= bh.findClosest(me, liveBots).getY() + 2 * RADIUS) || (bh.findClosest(me, deadBots).getY() + 2 * RADIUS >= bh.findClosest(me, liveBots).getY() && bh.findClosest(me, deadBots).getY() + 2 * RADIUS <= bh.findClosest(me, liveBots).getY() + 2 * RADIUS)) {
    if (bh.findClosest(me, deadBots).getX() > bh.findClosest(me, liveBots).getX() && bh.findClosest(me, deadBots).getX() < me.getX()) {
    blockingLeft = true;
    }
} else if ((bh.findClosest(me, deadBots).getX() >= bh.findClosest(me, liveBots).getX() && bh.findClosest(me, deadBots).getX() <= bh.findClosest(me, liveBots).getX() + 2 * RADIUS) || (bh.findClosest(me, deadBots).getX() + 2 * RADIUS >= bh.findClosest(me, liveBots).getX() && bh.findClosest(me, deadBots).getX() + 2 * RADIUS <= bh.findClosest(me, liveBots).getX() + 2 * RADIUS)) {
    if (bh.findClosest(me, deadBots).getY() > bh.findClosest(me, liveBots).getY() && bh.findClosest(me, deadBots).getY() < me.getY()) {
    blockingUp = true;
    }
} else if ((bh.findClosest(me, deadBots).getX() >= bh.findClosest(me, liveBots).getX() && bh.findClosest(me, deadBots).getX() <= bh.findClosest(me, liveBots).getX() + 2 * RADIUS) || (bh.findClosest(me, deadBots).getX() + 2 * RADIUS >= bh.findClosest(me, liveBots).getX() && bh.findClosest(me, deadBots).getX() + 2 * RADIUS <= bh.findClosest(me, liveBots).getX() + 2 * RADIUS)) {
    if (bh.findClosest(me, deadBots).getY() < bh.findClosest(me, liveBots).getY() && bh.findClosest(me, deadBots).getY() > me.getY()) {
    blockingDown = true;
    }
} else {
    blockingRight = false;
    blockingLeft = false;
    blockingUp = false;
    blockingDown = false;
}

        // -- SHOOTING -- //

        // Firing shot at given directions if conditions are right (right/left)
        if (me.getTeamName() != bh.findClosest(me, liveBots).getTeamName() && Math.abs(bh.calcDisplacement(bh.findClosest(me, liveBots).getY(), me.getY())) <= shotDistance) {
            if (bh.calcDisplacement(bh.findClosest(me, liveBots).getX(), me.getX()) < 0 && blockingRight != true) {
                current = right;
                action = BattleBotArena.FIRERIGHT;
            } else if (bh.calcDisplacement(bh.findClosest(me, liveBots).getX(), me.getX()) >= 0 && blockingLeft != true) {
                current = left;
                action = BattleBotArena.FIRELEFT;
            }
            return action;

        // Firing shot at given directions if conditions are right (down/up)
        } else if (me.getTeamName() != bh.findClosest(me, liveBots).getTeamName() && Math.abs(bh.calcDisplacement(bh.findClosest(me, liveBots).getX(), me.getX())) <= shotDistance) {
            if (bh.calcDisplacement(bh.findClosest(me, liveBots).getY(), me.getY()) < 0 && blockingDown != true) {
                current = down;
                action = BattleBotArena.FIREDOWN;
            } else if (bh.calcDisplacement(bh.findClosest(me, liveBots).getY(), me.getY()) >= 0 && blockingUp != true) {
                current = up;
                action = BattleBotArena.FIREUP;
            }
            return action;

        // -- RUN AWAY -- //

        // Run away if enemies are too close
        } else if (Math.abs(bh.calcDisplacement(bh.findClosest(me, liveBots).getX(), me.getX())) <= runDistance && Math.abs(bh.calcDisplacement(bh.findClosest(me, liveBots).getY(), me.getY())) <= runDistance && willHitX == false && willHitY == false) {
                // Find location of enemies so I can run the opposite direction
                if (bh.calcDisplacement(bh.findClosest(me, liveBots).getX(), me.getX()) < 0 && bh.calcDisplacement(bh.findClosest(me, liveBots).getY(), me.getY()) > 0) {
                    current = down;
                    action = BattleBotArena.DOWN;
                } else if (bh.calcDisplacement(bh.findClosest(me, liveBots).getX(), me.getX()) < 0 && bh.calcDisplacement(bh.findClosest(me, liveBots).getY(), me.getY()) < 0) {
                    current = left;
                    action = BattleBotArena.LEFT;
                } else if (bh.calcDisplacement(bh.findClosest(me, liveBots).getX(), me.getX()) > 0 && bh.calcDisplacement(bh.findClosest(me, liveBots).getY(), me.getY()) < 0) {
                    current = up;
                    action = BattleBotArena.UP;
            } else if (bh.calcDisplacement(bh.findClosest(me, liveBots).getX(), me.getX()) > 0 && bh.calcDisplacement(bh.findClosest(me, liveBots).getY(), me.getY()) > 0) {
                current = right;
                action = BattleBotArena.RIGHT;
        }
            return action;

        // Stay in place if theres no threat near
        } else {
            action = BattleBotArena.STAY;
            return action;
        }
    

}
    @Override
    public void draw(Graphics g, int x, int y) {
        g.drawImage(current, x, y, 2 * RADIUS, 2 * RADIUS, null);
    }

    @Override
    public String getName() {
        return "ModhvadiaBot";
    }

    @Override
    public String getTeamName() {
        return "Team Jay";
    }

    @Override
    public String outgoingMessage() {
        return " ";
    }

    @Override
    public void incomingMessage(int botNum, String msg) {

    }

    @Override
    public String[] imageNames() {
        String[] images = {"drone_left.png", "drone_right.png", "drone_up.png", "drone_down.png"};
        return images;
    }

    @Override
    public void loadedImages(Image[] images) {
        if (images != null) {
            current = up;
            left = images[0];
            right = images[1];
            up = images[2];
            down = images[3];
        }
    }
    
}
