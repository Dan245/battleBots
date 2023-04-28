package bots;
import arena.BattleBotArena;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.util.Random;

import arena.BotInfo;
import arena.Bullet;

public class JackBot extends Bot{

    private String nextMessage = null;
    private String[] killMessages = {"ez", "reyna diff", "aimlabs is free"};
    Image current, up, down, right, left;
    private String name = null;
    private int moveCount = 0;
    private int move;
    private int msgCounter = 0;
    private int targetNum = (int)(Math.random()*BattleBotArena.NUM_BOTS);
    private int sleep = (int)(Math.random()*5+1);
    private boolean overheat = false;
    private double prevX;
    private double currX;
    private double prevY;
    private double currY;

    BotHelper bh = new BotHelper();

    @Override
    public void newRound() {
        if (botNumber >= targetNum-3 && botNumber <= targetNum+3)
			overheat = true;
    }

    @Override
    public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) {
        if (bullets.length > 0) {
            arena.Bullet closestBullet = bh.findClosest(me, bullets);
            currY = me.getY();
            currX = me.getX();
            if (closestBullet.getXSpeed() != 0) {
                if (closestBullet.getX() >= (me.getX() + 36) || closestBullet.getX() <= (me.getX() - 10)) {
                    if (closestBullet.getY() >= me.getY() && closestBullet.getY() <= (me.getY() + 12)) {
                        move = BattleBotArena.DOWN;
                        if (prevY == currY) {
                            move = BattleBotArena.UP;
                        }
                        prevY = currY;
                        return move;
                    } else if (closestBullet.getY() >= (me.getY() + 13) && closestBullet.getY() <= (me.getY() + 25)) {
                        move = BattleBotArena.UP;
                        if (prevY == currY) {
                            move = BattleBotArena.DOWN;
                        }
                        prevY = currY;
                        return move;
                    }
                }
            } else if (closestBullet.getYSpeed() != 0) {
                if (closestBullet.getY() >= (me.getY() + 36) || closestBullet.getY() <= (me.getY() - 10)) {
                    if (closestBullet.getX() >= me.getX() && closestBullet.getX() <= (me.getX() + 12)) {
                        move = BattleBotArena.RIGHT;
                        if (prevX == currX) {
                            move = BattleBotArena.LEFT;
                        }
                        prevX = currX;
                        return move;
                    } else if (closestBullet.getX() >= (me.getX() + 13) && closestBullet.getX() <= (me.getX() + 25)) {
                        move = BattleBotArena.LEFT;
                        if (prevX == currX) {
                            move = BattleBotArena.RIGHT;
                        }
                        prevX = currX;
                        return move;
                    }
                }
            }
        }
        if (liveBots.length > 0) {
            arena.BotInfo closestBot = bh.findClosest(me, liveBots);
            if (closestBot.getX() < me.getX()) {
                if (me.getTeamName() != closestBot.getTeamName()) {
                    move = BattleBotArena.FIRELEFT;
                    return move;
                }
            } else if (closestBot.getX() > me.getX()) {
                if (me.getTeamName() != closestBot.getTeamName()) {
                    move = BattleBotArena.FIRERIGHT;
                    return move;
                }
            }
            if (closestBot.getY() < me.getY()) {
                if (me.getTeamName() != closestBot.getTeamName()) {
                    move = BattleBotArena.FIREDOWN;
                    return move;
                }
            } else if (closestBot.getY() > me.getY()) {
                if (me.getTeamName() != closestBot.getTeamName()) {
                    move = BattleBotArena.FIREUP;
                    return move;
                }
            }
        }
        return move;
    }

    @Override
    public void draw(Graphics g, int x, int y) {
        if (current != null)
			g.drawImage(current, x, y, Bot.RADIUS*2, Bot.RADIUS*2, null);
		else
		{
			g.setColor(Color.lightGray);
			g.fillOval(x, y, Bot.RADIUS*2, Bot.RADIUS*2);
		}
    }

    @Override
    public String getName() {
        if (name == null) {
            name = "JackBot";
        }
        return name;
    }

    @Override
    public String getTeamName() {
        return "TheBigSteppers";
    }

    @Override
    public String outgoingMessage() {
        String msg = nextMessage;
		nextMessage = null;
		return msg;
    }

    @Override
    public void incomingMessage(int botNum, String msg) {
        if (botNum == BattleBotArena.SYSTEM_MSG && msg.matches(".*destroyed by "+getName()+".*"))
		{
			int msgNum = (int)(Math.random()*killMessages.length);
			nextMessage = killMessages[msgNum];
			msgCounter = (int)(Math.random()*30 + 30);
		}
    }

    @Override
    public String[] imageNames() {
        String[] paths = {"drone_up.png", "drone_down.png", "drone_right.png", "drone_left.png"};
		return paths;
    }

    @Override
    public void loadedImages(Image[] images) {
        if (images != null)
		{
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
