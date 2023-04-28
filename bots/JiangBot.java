package bots;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import arena.BattleBotArena;
import arena.BotInfo;
import arena.Bullet;

public class JiangBot extends Bot {

	// Constants
	private final int SHOOTING_DIST = RADIUS - 1;
	private final int DE = 20;
	private final int DODGE_DIST = 100;
	private final boolean X = true, Y = false;
	private final String TEAM_NAME = "cool";

	// Bot images
	Image current, up, down, left, right;

	// Bot helper
	private final BotHelper bh = new BotHelper();

	// Trash talk messages
	private String[] killMessages = {"Dog water", "Dollar tree headset", "Get rolled", "L bozo", "Clapped"};
	private String nextMessage;

	@Override
	public void newRound() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) {
		
		if (bullets.length > 0) {
			// dodge the bullet
			// centre x and centre y
			double centreX = me.getX() + RADIUS;
			double centreY = me.getY() + RADIUS;
			// Get the closest bullet
			Bullet closestBullet = bh.findClosest(me, bullets);
			// check if bullet is on track to hitting us and dodge accordingly
			if (bh.calcDistance(centreX, centreY, closestBullet.getX(), closestBullet.getY()) < DODGE_DIST) {
				// if the bullet comes from our left
				if (closestBullet.getX() < me.getX() && closestBullet.getXSpeed() > 0 && 
						closestBullet.getY() > me.getY() - DE && closestBullet.getY() < me.getY() + 2 * RADIUS + DE) {
					if (closestBullet.getY() > centreY) {
						current = up;
						return BattleBotArena.UP;
					}
					else {
						current = down;
						return BattleBotArena.DOWN;
					}

				}
				// if the bullet comes from our right
				else if (closestBullet.getX() > me.getX() && closestBullet.getXSpeed() < 0 &&
						closestBullet.getY() > me.getY() - DE && closestBullet.getY() < me.getY() + 2 * RADIUS + DE) {
					if (closestBullet.getY() > centreY) {
						current = up;
						return BattleBotArena.UP;
					}
					else {
						current = down;
						return BattleBotArena.DOWN;
					}

				}
				// if the bullet comes from above
				else if (closestBullet.getY() < me.getY() && closestBullet.getYSpeed() > 0 &&
						closestBullet.getX() > me.getX() - DE && closestBullet.getX() < me.getX() + 2 * RADIUS + DE) {
					if (closestBullet.getX() > centreX) {
						current = left;
						return BattleBotArena.LEFT;
					}
					else {
						current = right;
						return BattleBotArena.RIGHT;
					}
				}
				// if the bullet comes from below
				else if (closestBullet.getY() > me.getY() && closestBullet.getYSpeed() < 0 &&
						closestBullet.getX() > me.getX() - DE && closestBullet.getX() < me.getX() + 2 * RADIUS + DE) {
					if (closestBullet.getX() > centreX) {
						current = left;
						return BattleBotArena.LEFT;
					}
					else {
						current = right;
						return BattleBotArena.RIGHT;
					}
				}
			}
		}

		// move out of the way of teammates
		for (BotInfo bot : liveBots) {
			if (bot.getTeamName().equals(TEAM_NAME)) {
				double xDist = Math.abs(bot.getX() - me.getX());
				double yDist = Math.abs(bot.getY() - me.getY());
				if (xDist < SHOOTING_DIST && !checkGravestone(deadBots, me.getX(), me.getY(), bot.getX(), bot.getY(), Y)) {
					if (me.getX() < bot.getX()) {
						current = left;
						return BattleBotArena.LEFT;
					}
					else {
						current = right;
						return BattleBotArena.RIGHT;
					}
				}
				if (yDist < SHOOTING_DIST && !checkGravestone(deadBots, me.getX(), me.getY(), bot.getX(), bot.getY(), X)) {
					if (me.getY() < bot.getY()) {
						current = up;
						return BattleBotArena.UP;
					}
					else {
						current = down;
						return BattleBotArena.DOWN;
					}
				}
			}
		}

		// try to shoot another bot
		if (liveBots.length > 0) {
			BotInfo closestBot = bh.findClosest(me, liveBots);
			// make sure this bot is not my teammate
			if (closestBot.getTeamName().equals(TEAM_NAME)) {
				double closestDist = 0;
				BotInfo closest = me;
				closestDist = Double.MAX_VALUE;
				for (int i = 0; i < liveBots.length; i++) {
					double distance = Math.abs(me.getX() - liveBots[i].getX()) + Math.abs(me.getY() - liveBots[i].getY());
					if (!liveBots[i].getTeamName().equals(TEAM_NAME) && distance < closestDist) {
						closest = liveBots[i];
						closestDist = distance;
					}
				}
				closestBot = closest;
			}
			// line up with them if not lined up
			double xDist = me.getX() - closestBot.getX();
			double yDist = me.getY() - closestBot.getY();

			if (Math.abs(xDist) <= SHOOTING_DIST) {
				// if there is a gravestone in between, line up the other way
				if (deadBots.length > 0 && checkGravestone(deadBots, me.getX(), me.getY(), closestBot.getX(), closestBot.getY(), Y)) {
					if (xDist < 0) {
						current = right;
						return BattleBotArena.RIGHT;
					}
					else {
						current = left;
						return BattleBotArena.LEFT;
					}
				}
				if (yDist < 0) {
					current = down;
					return BattleBotArena.FIREDOWN;
				}
				else {
					current = up;
					return BattleBotArena.FIREUP;
				}
			}
			else if (Math.abs(yDist) <= SHOOTING_DIST) {
				if (deadBots.length > 0 && checkGravestone(deadBots, me.getX(), me.getY(), closestBot.getX(), closestBot.getY(), X)) {
					System.out.println(me.getX() + " " + me.getY() + " " + closestBot.getX() + " " + closestBot.getY());
					if (yDist > 0) {
						current = up;
						return BattleBotArena.UP;
					}
					else {
						current = down;
						return BattleBotArena.DOWN;
					}
				}
				if (xDist < 0) {
					current = right;
					return BattleBotArena.FIRERIGHT;
				}
				else {
					current = left;
					return BattleBotArena.FIRELEFT;
				}
			}
			else {
				if (Math.abs(xDist) < Math.abs(yDist)) {
					if (xDist < 0) {
						current = right;
						return BattleBotArena.RIGHT;
					}
					else {
						current = left;
						return BattleBotArena.LEFT;
					}
				}
				else {
					if (yDist > 0) {
						current = up;
						return BattleBotArena.UP;
					}
					else {
						current = down;
						return BattleBotArena.DOWN;
					}
				}
			}
		}


		return BattleBotArena.STAY;
	}
	
	// method to check if a gravestone is in between two points
	private boolean checkGravestone(BotInfo[] deadBots, double x1, double y1, double x2, double y2, boolean xx) {
		for (BotInfo bot : deadBots) {
			double x = bot.getX();
			double y = bot.getY();
			// if we are checking in the x-axis
			if (xx) {
				if (Math.abs(y1 - y2) < SHOOTING_DIST && x >= Math.min(x1, x2) && x <= Math.max(x1, x2) && Math.abs(y - y1) < SHOOTING_DIST && Math.abs(y - y2) < SHOOTING_DIST) {
					return true;
				}
			}
			
			// if we are checking in the y-axis
			else {
				if (Math.abs(x1 - x2) < SHOOTING_DIST && y >= Math.min(y1, y2) && y <= Math.max(y1, y2) && Math.abs(x - x1) < SHOOTING_DIST && Math.abs(x - x2) < SHOOTING_DIST) {
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public void draw (Graphics g, int x, int y) {
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
		return "JiangBot";
	}

	@Override
	public String getTeamName() {
		// TODO Auto-generated method stub
		return TEAM_NAME;
	}

	@Override
	public String outgoingMessage() {
		String msg = nextMessage;
		nextMessage = null;
		return msg;
	}

	@Override
	public void incomingMessage(int botNum, String msg) {
		if (botNum == BattleBotArena.SYSTEM_MSG && msg.matches(".*destroyed by "+getName()+".*")) {
			int msgNum = (int)(Math.random()*killMessages.length);
			nextMessage = killMessages[msgNum];
		}
	}

	@Override
	public String[] imageNames() {
		String[] imageNames = {"jiang_up.png", "jiang_down.png", "jiang_right.png", "jiang_left.png"};
		return imageNames;
	}

	@Override
	public void loadedImages(Image[] images)
	{
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
