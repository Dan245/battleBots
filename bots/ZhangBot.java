package bots;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import arena.BotInfo;
import arena.Bullet;

import arena.BattleBotArena;
// import arena.BotInfo;
// import arena.Bullet;

public class ZhangBot extends Bot {
	// Instance Variables:
	Image current, up, down, right, left;
	private String name = null;
	private int moveCount = 99;
	private int move = BattleBotArena.UP;
	private boolean overheat = false;
	private int msgCounter = 0;
	private String nextMessage = null;
	private String[] killMessages = { "hi nathan", "hi mr nathan", "hi mr nathan q", "hi nathan the q" };
	private int sleep = (int) (Math.random() * 5 + 1);
	private int targetNum = (int) (Math.random() * BattleBotArena.NUM_BOTS);

	BotHelper bh = new BotHelper(); // Access Bothelper methods

	@Override
	public void draw(Graphics g, int x, int y) { // USE THiS METHOD AND IT'S PARAMETERS TO CHECK IF BULLET (X,Y) IS
													// NEAR, THEN MOVE
		if (current != null)
			g.drawImage(current, x, y, Bot.RADIUS * 2, Bot.RADIUS * 2, null);
		else {
			g.setColor(Color.lightGray);
			g.fillOval(x, y, Bot.RADIUS * 2, Bot.RADIUS * 2);
		}

	}

	@Override
	public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) {
		double centerofPlayerX = me.getX() + RADIUS;
		double centerofPlayerY = me.getY() + RADIUS;

		double GDIST = 100;
		// System.out.println(xTraj + " X Traj");
		// System.out.println(yTraj + " Y Traj");
		// EVADING BULLET AVOIDANCE SYSTEM
		if (bullets.length > 0) {
			Bullet closestBullet = bh.findClosest(me, bullets);
			// Take evasive action
			if (bh.calcDistance(centerofPlayerX, centerofPlayerY, closestBullet.getX(), closestBullet.getY()) < 100) { // Calculate
																														// distance
																														// from
																														// player
																														// and
																														// bullet
				if (closestBullet.getXSpeed() != 0) { // If bullet x speed is not 0
					if (closestBullet.getY() > centerofPlayerY) { // if y position of bullet is above the center of
																	// player's x axis
						// if (closestLiveBot.getY() > centerofPlayerX || closestDeadBot.getY() >
						// centerofPlayerX) { //if livebot/deadbot y position is greater than player's x
						// axis
						current = up;
						return BattleBotArena.UP; // move up
						// } else {
						// current = down;
						// return BattleBotArena.DOWN; //move down
						// }
					} else {
						return BattleBotArena.DOWN; // move down
					}
				} else if (closestBullet.getYSpeed() != 0) { // if bullet y speed is not 0
					if (closestBullet.getX() > centerofPlayerX) { // if x position of bullet is above the center of
																	// player's y axis
						// if (closestLiveBot.getX() > centerofPlayerY || closestDeadBot.getX() >
						// centerofPlayerY) {//if livebot/deadbot x position is greater than player's y
						// axis
						current = left;
						return BattleBotArena.LEFT; // move left
						// } else {
						// current = right;
						// return BattleBotArena.RIGHT; //move right
						// }
					} else {
						return BattleBotArena.RIGHT; // move left
					}
				}
			}
			// BULLET SHOT SYSTEM
			BotInfo closestLiveBot = bh.findClosest(me, liveBots);

			double xTraj = me.getX() + RADIUS - closestLiveBot.getX();
			double yTraj = me.getY() + RADIUS - closestLiveBot.getY();
			System.out.println(Math.abs(xTraj) + " xTraj RIGHT");
			System.out.println(Math.abs(yTraj) + " YTraj RIGHT");
			if (closestLiveBot.getTeamName() != "Team Jay") {
				if (Math.abs(xTraj) <= 30) { // if the absolute x distance from player and bot is less than or equal to
												// the player shot range (15)
					if (yTraj <= 0) {
						// System.out.println(Math.abs(xTraj) + " xTraj RIGHT");
						current = down;
						return BattleBotArena.FIREDOWN; // Fire down
					} else {
						// System.out.println(Math.abs(xTraj) + " xTraj LEFT");
						current = up;
						return BattleBotArena.FIREUP;// Fire up
					}
				} else if (Math.abs(yTraj) <= 30) { // if the y absolute distance from player and bot is less than or
													// equal to the player shot range (15)
					if (xTraj <= 0) {
						current = right;
						return BattleBotArena.FIRERIGHT;// Fire right
					} else {
						current = left;
						return BattleBotArena.FIRELEFT;// Fire left
					}
				}
			}
			// Slight offset in player to player
			// Check bottom left
			if ((closestLiveBot.getX() < centerofPlayerX && closestLiveBot.getX() > centerofPlayerX - 10)
					&& (closestLiveBot.getY() < centerofPlayerY && closestLiveBot.getY() > centerofPlayerY - 10)) {
				current = down;
				return BattleBotArena.DOWN;
				// Bottom Right Check
			} else if ((closestLiveBot.getX() > centerofPlayerX && closestLiveBot.getX() < centerofPlayerX + 10)
					&& (closestLiveBot.getY() < centerofPlayerY && closestLiveBot.getY() > centerofPlayerY - 10)) {
				current = down;
				return BattleBotArena.DOWN;
				//Top Right
			} else if ((closestLiveBot.getX() > centerofPlayerX && closestLiveBot.getX() < centerofPlayerX - 10)
					&& (closestLiveBot.getY() > centerofPlayerY && closestLiveBot.getY() < centerofPlayerY + 10)) {
				current = up;
				return BattleBotArena.UP;
				//Top Left
			} else if ((closestLiveBot.getX() < centerofPlayerX && closestLiveBot.getX() < centerofPlayerX + 10)
					&& (closestLiveBot.getY() < centerofPlayerY && closestLiveBot.getY() > centerofPlayerY + 10)) {
				current = down;
				return BattleBotArena.DOWN;
			}
			// IF PLAYER DISTANCE IS GREATER THAN 50 BUT LESS THAN 100, MOVE TOWARDS BOT:
			if (xTraj > me.getX() + 50 && xTraj < me.getX() + 100) {
				current = right;
				return BattleBotArena.RIGHT; // Fire down
			} else if (xTraj < me.getX() - 50 && xTraj > me.getX() - 100) {
				current = left;
				return BattleBotArena.LEFT; // Fire down
			}
			if (yTraj > me.getY() + 50 && yTraj < me.getY() + 100) {
				current = up;
				return BattleBotArena.UP; // Fire down
			} else if (yTraj < me.getY() - 50 && yTraj > me.getY() - 100) {
				current = down;
				return BattleBotArena.DOWN; // Fire down
			}

		}
		// DEAD/LIVE BOT AVOIDANCE SYSTEM
		if (deadBots.length > 0 && liveBots.length > 0) { // if the array of these systems are greater than 0
															// (preventing method crash)
			BotInfo closestLiveBot = bh.findClosest(me, liveBots);
			BotInfo closestDeadBot = bh.findClosest(me, deadBots);
			double botDistance = distance(closestLiveBot.getX(), closestLiveBot.getY(), me.getX(), me.getY());
			GDIST = distance(closestDeadBot.getX(), closestDeadBot.getY(), me.getX(), me.getY()); // set GDIST to be the
																									// distance between
																									// closestdead bot
																									// and player
			if (GDIST < 5 || botDistance < 20) { // if GDIST and botdistance's distance is less than or equal to the
													// player range of detection
				if (closestLiveBot.getX() > me.getX() || closestDeadBot.getX() > me.getX()) { // if closest live bot's
																								// or closest dead bot's
																								// x value is greater
																								// than player x
																								// (meaning these bots
																								// are inhibiting player
																								// movement from right
																								// side)
					current = left;
					return BattleBotArena.LEFT; // move left
				} else if (closestLiveBot.getX() < me.getX() || closestDeadBot.getX() < me.getX()) {// if closest live
																									// bot's or closest
																									// dead bot's x
																									// value is greater
																									// than player x
																									// (meaning these
																									// bots are
																									// inhibiting player
																									// movement from
																									// left side)
					current = right;
					return BattleBotArena.RIGHT; // move right
				}
			} else {
				if (closestLiveBot.getY() > me.getY() || closestDeadBot.getY() > me.getY()) {// if closest live bot's or
																								// closest dead bot's y
																								// value is greater than
																								// player y (meaning
																								// these bots are
																								// inhibiting player
																								// movement from top
																								// side)
					current = down;
					return BattleBotArena.DOWN; // move down
				} else if (closestLiveBot.getY() < me.getY() || closestDeadBot.getY() < me.getY()) { // if closest live
																										// bot's or
																										// closest dead
																										// bot's y value
																										// is greater
																										// than player y
																										// (meaning
																										// these bots
																										// are
																										// inhibiting
																										// player
																										// movement from
																										// right side)
					current = up;
					return BattleBotArena.UP; // move up

				}
			}
		}

		// for overheating
		if (overheat) {
			try {
				Thread.sleep(sleep);
			} catch (Exception e) {
			}
		}

		// increase the move counter
		moveCount++;

		// Is it time to send a message?
		if (--msgCounter == 0) {
			move = BattleBotArena.SEND_MESSAGE;
			moveCount = 99;
		}
		// Time to choose a new move?
		else if (moveCount >= 30 + (int) Math.random() * 60) // This if method cycles the 30 times per second
			// System.out.println(move);
		{

		}
		return move;

	}

	private double distance(double x, double y, double x2, double y2) {
		return 0;
	}

	@Override
	public String getName() {
		if (name == null)
			name = "Chris15";
		return name;
	}

	@Override
	public String getTeamName() {
		return "Team Jay";
	}

	@Override
	public void incomingMessage(int botNum, String msg) {
		if (botNum == BattleBotArena.SYSTEM_MSG && msg.matches(".*destroyed by " + getName() + ".*")) {
			int msgNum = (int) (Math.random() * killMessages.length);
			nextMessage = killMessages[msgNum];
			msgCounter = (int) (Math.random() * 30 + 30);
		}
	}

	public String[] imageNames() {
		String[] images = { "zhangbot.png", "zhangbot.png", "zhangbot.png", "zhangbot.png" };
		return images;
	}

	/**
	 * Store the loaded images
	 */
	public void loadedImages(Image[] images) {
		if (images != null) {
			current = up = images[0];
			down = images[1];
			left = images[2];
			right = images[3];
		}
	}

	@Override
	public void newRound() {
		if (botNumber >= targetNum - 3 && botNumber <= targetNum + 3)
			overheat = true;

	}

	@Override
	public String outgoingMessage() {
		String msg = nextMessage;
		nextMessage = null;
		return msg;
	}
}
