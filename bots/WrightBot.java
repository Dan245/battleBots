package bots;
//test
import arena.BattleBotArena;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.lang.model.util.ElementScanner14;

import java.awt.Color;

import arena.BotInfo;
import arena.Bullet;

public class WrightBot extends Bot {
    Image current, up, down, right, left;
    BotHelper bh;
    boolean debug = true;
    private int rightEdge;
    private int leftEdge;
    private int top;
    private int bottom;
    private ArrayList<Integer> moves;
    int moveNum = 0;
    int lastMove = 0;
    private double prevX = 0;
    private double prevY = 0;
    private boolean movedLastTime = false;

    public WrightBot() {
        bh = new BotHelper();
        moves = new ArrayList<>();

    }

    @Override
    public void newRound() {

        System.out.println("New round");
    }

    public BotInfo closestVertical(BotInfo me, BotInfo[] liveBots) {
        BotInfo closest = liveBots[0];
        for (int i = 1; i < liveBots.length; i++) {
            // if the distance of the nextBot is less than the closest, make the current bot
            // the closest
            if (bh.calcManhattanDist(liveBots[i].getY(), me.getY()) < closest.getY()) {
                closest = liveBots[i];

            }

        }
        return closest;

    }

    @Override
    public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) {
        int move = 0;
        moveNum++;

        if (movedLastTime) {
            if (prevX == me.getX() && prevY == me.getY()) {
                // i'm stuck and trying to move, do something
                if (lastMove == BattleBotArena.UP) {
                    move = BattleBotArena.DOWN;
                } else if (lastMove == BattleBotArena.DOWN) {
                    move = BattleBotArena.UP;
                } else if (lastMove == BattleBotArena.LEFT) {
                    move = BattleBotArena.RIGHT;
                } else {
                    move = BattleBotArena.LEFT;
                }

            }
        }
        prevX = me.getX();
        prevY = me.getY();
        if (move == 0) {
            if (liveBots.length > 0) {
                BotInfo closestBot = bh.findClosest(me, liveBots);
                // determine if the closestBot is a threat
                if (bullets.length > 0) {
                    arena.Bullet closestBullet = bh.findClosest(me, bullets);
                    // if bullet is moving in the x direction
                    move = moveBasedOnBullet(me, closestBullet);

                } // bullet list > 0

                // get closest bot
                // check if bot is within 6 pixels of any of my edges
                // check if there is a bot super close by
                move = overlapping(me, closestBot);

            } // live bots list > 0
        } // move = 0

        // UP:1, D, L, R:4, FU:5, FR:8
        // shot randomly if no move
        if (move == 0 && shotOK) {
            int random_int = (int) Math.floor(Math.random() * (8 - 4 + 1) + 4);
            move = random_int;

            // for(int i =0; i<moves.size(); i++){
            // if(moves[i] == 5)

            // }

        }
        if (move >= 1 && move <= 4) {
            movedLastTime = true;
        } else {
            movedLastTime = false;
        }
        lastMove = move;

        return move;

    }

    // check for overlapping pixels
    private int overlapping(BotInfo me, BotInfo c) {
        // check if bot is above or below me
        if (c.getX() + RADIUS * 2 > me.getX() && c.getX() + RADIUS * 2 <= me.getX() + RADIUS * 2 ||
                c.getX() > me.getX() && c.getX() <= me.getX() + RADIUS * 2) {
            // check if bot above
            if (c.getY() + RADIUS * 2 - me.getY() < 0 && c.getY() + RADIUS * 2 - me.getY() > -100) {
                // bot is above me and within 100 pixels
                // is bot above and within firing range?
                if (c.getX() + RADIUS * 2 + 5 < me.getX() + RADIUS) {
                    return BattleBotArena.LEFT;
                } else if (c.getX() - 5 > me.getX() + RADIUS) {
                    return BattleBotArena.RIGHT;
                }
                System.out.println("bot above FIRE");
                return BattleBotArena.FIREUP;
            } else if (c.getY() + RADIUS * 2 - me.getY() > 0 && c.getY() + RADIUS * 2 - me.getY() < 100) {
                // bot is above me and within 100 pixels
                if (c.getX() + RADIUS * 2 + 5 < me.getX() + RADIUS) {
                    return BattleBotArena.LEFT;
                } else if (c.getX() - 5 > me.getX() + RADIUS) {
                    return BattleBotArena.RIGHT;
                }
                System.out.println("bot below FIRE");
                return BattleBotArena.FIREDOWN;
            }

        } // bot overlapping above or below
          // test if a bot is overlapping beside
        else if (c.getY() + RADIUS * 2 > me.getY() && c.getX() + RADIUS * 2 <= me.getY() + RADIUS * 2 ||
                c.getY() > me.getY() && c.getY() <= me.getY() + RADIUS * 2) {
            // check if bot is left of me
            if (c.getX() + RADIUS * 2 - me.getX() < 0 && c.getX() + RADIUS * 2 - me.getX() > -100) {
                // bot is above me and within 100 pixels
                if (c.getY() + RADIUS * 2 + 5 < me.getY() + RADIUS) {
                    return BattleBotArena.UP;
                } else if (c.getY() - 5 > me.getY() + RADIUS) {
                    return BattleBotArena.DOWN;
                }
                System.out.println("bot left FIRE");
                return BattleBotArena.FIRELEFT;
            } else if (c.getX() + RADIUS * 2 - me.getX() > 0 && c.getX() + RADIUS * 2 - me.getX() < 100) {
                // bot is right of me and within 100 pixels
                System.out.println("bot right FIRE");
                if (c.getY() + RADIUS * 2 + 5 < me.getY() + RADIUS) {
                    return BattleBotArena.UP;
                } else if (c.getY() - 5 > me.getY() + RADIUS) {
                    return BattleBotArena.DOWN;
                }
                return BattleBotArena.FIRERIGHT;
            }
        } // bot overlapping beside
        return 0;
    }

    // create a method to determine if bullet is within my range
    // direction - 0- up, 1-right, 2-down, 3-left
    public boolean bulletInRange(Bullet bullet, BotInfo me, int direction) {
        switch (direction) {
            // X
            case 0:
                if (bullet.getX() > me.getX() && bullet.getX() < me.getX() + Bot.RADIUS * 2) {
                    return true;
                }

                break;
            // Y- bullet is going to hit right side of my bot
            case 1:
                if (bullet.getY() > me.getY() && bullet.getY() < me.getY() + Bot.RADIUS * 2) {
                    return true;
                }

                break;

        }

        return false;
    }

    private int moveBasedOnBullet(BotInfo me, Bullet closestBullet) {
        int move = 0;
        if (closestBullet.getXSpeed() != 0) {

            if (bulletInRange(closestBullet, me, 1)) {
                // is the bullet in line with me and travelling towards me?
                if (closestBullet.getX() < me.getX() && closestBullet.getXSpeed() > 0) {

                    if (bh.manhattanDist(me.getX(), me.getY(), closestBullet.getX(),
                            closestBullet.getY()) < 200.0) {

                        // move up or down depending on closest vertical bot
                        if (closestBullet.getY() > me.getY() + Bot.RADIUS && me.getY() > 50) {
                            move = BattleBotArena.UP;
                        } else {
                            move = BattleBotArena.DOWN;
                        }

                    } // bullet is within 7 pixels of me
                } // bullet is left of me and moving towards me
                else if (closestBullet.getX() > me.getX() && closestBullet.getXSpeed() < 0) {

                    if (bh.manhattanDist(me.getX(), me.getY(), closestBullet.getX(),
                            closestBullet.getY()) < 200.0) {

                        // move up or down depending on closest vertical bot
                        if (closestBullet.getY() > me.getY() + Bot.RADIUS && me.getY() > 50) {
                            move = BattleBotArena.UP;
                        } else {
                            move = BattleBotArena.DOWN;
                        }

                    } // bullet is within 7 pixels of me
                } // bullet is right of me and moving towards me

            } // bullet is in my x
        } // bullet is moving in x direction
          // if bullet is moving in the y direction
        else {

            if (bulletInRange(closestBullet, me, 0)) {
                // is the bullet in line with me and travelling towards me?
                if (closestBullet.getY() < me.getY() && closestBullet.getYSpeed() > 0) {

                    if (bh.manhattanDist(me.getX(), me.getY(), closestBullet.getX(),
                            closestBullet.getY()) < 200.0) {

                        // move up or down depending on closest vertical bot
                        if (closestBullet.getX() > me.getX() + Bot.RADIUS && me.getX() > 50) {
                            move = BattleBotArena.LEFT;
                        } else {
                            move = BattleBotArena.RIGHT;
                        }

                    } // bullet is within 7 pixels of me
                } // bullet is above me and moving down
                else if (closestBullet.getY() > me.getY() + Bot.RADIUS * 2
                        && closestBullet.getYSpeed() < 0) {

                    if (bh.manhattanDist(me.getX(), me.getY(), closestBullet.getX(),
                            closestBullet.getY()) < 200.0) {

                        // move up or down depending on closest vertical bot
                        if (closestBullet.getX() > me.getX() + Bot.RADIUS && me.getY() > 50) {
                            move = BattleBotArena.LEFT;
                        } else {
                            move = BattleBotArena.RIGHT;
                        }

                    } // bullet is within 7 pixels of me
                } // bullet is below me and moving up

            } // bullet is in my y

        } // bullet is moving in the y direction
        return move;
    }

    @Override
    public void draw(Graphics g, int x, int y) {
        if (current != null)
            g.drawImage(current, x, y, Bot.RADIUS * 2, Bot.RADIUS * 2, null);
        else {
            g.setColor(Color.blue);
            g.fillOval(x, y, Bot.RADIUS * 2, Bot.RADIUS * 2);
        }
    }

    @Override
    public String getName() {
        return "WrightBot";
    }

    @Override
    public String getTeamName() {
        return " ";
    }

    @Override
    public String outgoingMessage() {
        return "";
    }

    @Override
    public void incomingMessage(int botNum, String msg) {
    }

    @Override
    public String[] imageNames() {
        // String[] paths = { "roomba_up.png", "roomba_down.png", "roomba_right.png",
        // "roomba_left.png" };
        String[] paths = { "" };
        return null;
        // return paths;
    }

    @Override
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

}
