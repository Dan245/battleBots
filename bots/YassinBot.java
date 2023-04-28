package bots;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import arena.BattleBotArena;
import arena.BotInfo;
import arena.Bullet;

/**
 * The YassinBot is a basic Bot that moves and shoots. Sometimes it overheats.
 * It trash talks when it kills someone.
 *
 * @author Ahmed Yassin
 * @version 1.0 (March 23, 2023)
 */
public class YassinBot extends Bot {

    /**
	 * Next message to send, or null if nothing to send.
	 */
	private String nextMessage = null;
    /**
	 * An array of trash talk messages.
	 */
	private String[] killMessages = {"Goteeem", "L", "Light work", "Siuuuu"};


    BotHelper botHelper = new BotHelper();

    int move;
    double radius = Bot.RADIUS * 2;
    double speed = BattleBotArena.BOT_SPEED;

	public double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(Math.abs(x1 - x2), 2) + Math.pow(Math.abs(y1 - y2), 2));
    }

    @Override
    public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) {
        
        BotInfo closestBot = botHelper.findClosest(me, liveBots);
        double botDist = distance(closestBot.getX() + Bot.RADIUS, closestBot.getY() + Bot.RADIUS, me.getX() + Bot.RADIUS, me.getY() + Bot.RADIUS);
        Bullet closestBullet = botHelper.findClosest(me, bullets);
        double bulletY = closestBullet.getY();
        double bulletX = closestBullet.getX();
        double bulletXSpeed = closestBullet.getXSpeed();
        double meTopY = me.getY();
        double meBottomY = me.getY() + radius;
        double bulletYSpeed = closestBullet.getYSpeed();
        double meLeftX = me.getX();
        double meRightX = me.getX() + radius;
        double closeBotY = closestBot.getY();
        double closeBotX = closestBot.getX();

        //horizontal distance between YassinBot and closest bot
        double botXDist = meLeftX - closeBotX;
        //vertical distance between YassinBot and closest bot
        double botYDist = meTopY - closeBotY;
        //distance for specific vicinity
        double distThreshold2 = 30.0;

		if (liveBots.length > 0) {

            if (bullets.length > 0) {

                double distThreshold = 200.0;

                //X
                if ((bulletY > meTopY) && (bulletY < meBottomY)) {
                    //L
                    if ((bulletX < me.getX())) {
                        if (bulletXSpeed > 0) {
                            if ((Math.abs(me.getX()) - Math.abs(bulletX)) < distThreshold) {
                                if (bulletY < (meTopY + Bot.RADIUS)) {
                                    move = BattleBotArena.DOWN;
                                    return move;
                                } else {
                                    move = BattleBotArena.UP;
                                    return move;
                                }
                            }
                        }
                    //R
                    } else {
                        if (bulletXSpeed < 0) {
                            if ((Math.abs(bulletX) - Math.abs(me.getX())) < distThreshold) {
                                if (bulletY < (meTopY + Bot.RADIUS)) {
                                    move = BattleBotArena.DOWN;
                                    return move;
                                } else {
                                    move = BattleBotArena.UP;
                                    return move;
                                }
                            }
                        }
                    }
                }

                //Y
                if ((bulletX > meLeftX) && (bulletX < meRightX)) {
                    //U
                    if (bulletY < meTopY) {
                        if (bulletYSpeed > 0) {
                            if ((Math.abs(meTopY) - Math.abs(bulletY)) < distThreshold) {
                                if (bulletX < (meLeftX + Bot.RADIUS)) {
                                    move = BattleBotArena.RIGHT;
                                    return move;
                                } else {
                                    move = BattleBotArena.LEFT;
                                    return move;
                                }
                            }
                        }
                    //D
                    } else {
                        if (bulletYSpeed < 0) {
                            if ((Math.abs(bulletY) - Math.abs(meTopY)) < distThreshold) {
                                if (bulletX < (meLeftX + Bot.RADIUS)) {
                                    move = BattleBotArena.RIGHT;
                                    return move;
                                } else {
                                    move = BattleBotArena.LEFT;
                                    return move;
                                }
                            }
                        }
                    }
                }


                boolean withinYRange = (closeBotY <= (meTopY + Bot.RADIUS) && (closeBotY + radius) >= (meTopY + Bot.RADIUS));
                boolean withinXRange = (meLeftX + Bot.RADIUS >= closeBotX && meLeftX + Bot.RADIUS <= closeBotX + radius);
                
                String botName = closestBot.getTeamName();
                String meName = me.getTeamName();
                
                if (!closestBot.getName().equals(this.getName())) {
                    if (withinYRange && (botName != meName)) {
                        if (shotOK) {
                            return fireBullets(me, deadBots, closestBot, false);
                        }
                    } else if (withinXRange && (botName != meName)) {
                        if (shotOK) {
                            return fireBullets(me, deadBots, closestBot, true);
                        }
                    }
                }
                
                //avoid other bots
                if (botDist < distThreshold2) {
                
                    if (Math.abs(botXDist) > Math.abs(botYDist)) {
                        if (botXDist > 0) {
                            move = BattleBotArena.RIGHT;
                            return move;
                        } else {
                            move = BattleBotArena.LEFT;
                            return move;
                        }
                    } else {
                        if (botYDist > 0) {
                            move = BattleBotArena.DOWN;
                            return move;
                        } else {
                            move = BattleBotArena.UP;
                            return move;
                        }
                    }
                }
            }

            //variables
            double graveDist = 1000;
            BotInfo closestGrave = botHelper.findClosest(me, deadBots);
            double graveX = closestGrave.getX();
            double graveY = closestGrave.getY();
            double distThreshold3 = 70.0;

            if (deadBots.length > 0) {
                graveDist = distance(graveX + Bot.RADIUS, graveY + Bot.RADIUS, meLeftX + Bot.RADIUS, meTopY + Bot.RADIUS);
            }

            if (botDist <= 100 || graveDist <= 100) {
                //X
                if (closeBotX > meLeftX) {
                    if (bulletX > meLeftX && bulletX < meLeftX + speed + radius && bulletYSpeed != 0 && (botDist <= distThreshold3 || graveDist <= distThreshold3)) {
                        if (closeBotY >= meTopY || graveY >= meTopY) {
                            move = BattleBotArena.UP;
                            return move;
                        }
                        move = BattleBotArena.DOWN;
                        return move;
                    }
                    move = BattleBotArena.RIGHT;
                    return move;
                    
                } else if (closeBotX < meLeftX) {
                    if (bulletX > meLeftX - speed && bulletX < meLeftX && bulletYSpeed != 0 && (botDist <= distThreshold3 || graveDist <= distThreshold3)) {
                        if (closestBot.getY() <= me.getY() || closestGrave.getY() <= me.getY()) {
                            move = BattleBotArena.DOWN;
                            return move;
                        }
                        move = BattleBotArena.UP;
                        return move;
                    }
                    move = BattleBotArena.LEFT;
                    return move;
                }

            } else {
                //Y
                if (closeBotY > meTopY) {
                    if (bulletY > meTopY && bulletY < meTopY + speed + radius && bulletXSpeed != 0 && (botDist <= distThreshold3 || graveDist <= distThreshold3)) {
                        if (closeBotX <= meLeftX || graveX <= meLeftX) {
                            move = BattleBotArena.RIGHT;
                            return move;
                        }
                        move = BattleBotArena.LEFT;
                        return move;
                    }
                    move = BattleBotArena.DOWN;
                    return move;

                } else if (closeBotY < meTopY) {
                    if (bulletY > meTopY - speed && bulletY < meTopY && bulletXSpeed != 0 && (botDist <= distThreshold3 || graveDist <= distThreshold3)) {
                        if (closeBotX >= meLeftX || graveX >= meLeftX) {
                            move = BattleBotArena.LEFT;
                            return move;
                        }
                        move = BattleBotArena.RIGHT;
                        return move;
                    }
                    move = BattleBotArena.UP;
                    return move;
                }
            }


        }
        return 0;
    }

    public int fireBullets(BotInfo me, BotInfo[] deadBots, BotInfo closestBot, boolean align) {

        //variables
        BotInfo closestDeadBot = botHelper.findClosest(me, deadBots);
        double deadBotX  = closestDeadBot.getX();
        double deadBotY = closestDeadBot.getY();
        double meTopY = me.getY();
        double meLeftX = me.getX();
        double closeBotY = closestBot.getY();
        double closeBotX = closestBot.getX();    

        boolean withinDeadXRange = (meLeftX + Bot.RADIUS) >= deadBotX && (meLeftX + Bot.RADIUS) <= (deadBotX + radius);
        boolean withinDeadYRange = (meTopY + Bot.RADIUS) >= deadBotY && (meTopY + Bot.RADIUS) <= (deadBotY + radius);

        if (deadBots.length > 0) {
            
            if (align) {
                if (withinDeadXRange) {
                    if (closeBotY < meTopY && deadBotY < closeBotY || meTopY < deadBotY) {
                        return BattleBotArena.FIREUP;
                    } else if (closeBotY > meTopY && deadBotY > closeBotY || meTopY > deadBotY) {
                        return BattleBotArena.FIREDOWN;
                    } else {
                        move = BattleBotArena.STAY;
                        return move;
                    }
                }
            } else {
                if (withinDeadYRange) {
                    if (closeBotX < meLeftX && deadBotX < closeBotX || meLeftX < deadBotX) {
                        return BattleBotArena.FIRELEFT;
                    } else if (closeBotX > meLeftX && deadBotX > closeBotX || meLeftX > deadBotX) {
                        return BattleBotArena.FIRERIGHT;
                    } else {
                        move = BattleBotArena.STAY;
                        return move;
                    }
                }
            }
        }

        if (align) {
            if (closeBotY < meTopY) {
                return BattleBotArena.FIREUP;
            } else {
                return BattleBotArena.FIREDOWN;
            }
        } else {
            if (closeBotX < meLeftX) {
                return BattleBotArena.FIRELEFT;
            } else {
                return BattleBotArena.FIRERIGHT;
            }
        }
    }

    public boolean teammate(BotInfo[] liveBots) {
        
        for (BotInfo bot : liveBots) {  
            // if the bot has the same team name as me, return true
            if (bot.getTeamName() == "The Big Steppers") {
                return true;
            }

        }
        return false;
    }

    @Override
    public void draw(Graphics g, int x, int y) {
        g.setColor(Color.YELLOW);
        g.fillRect(x + 2, y + 2, RADIUS * 2 - 4, RADIUS * 2 - 4);
    }

    @Override
    public String getName() {
        return "Yassin";
    }

    @Override
    public String getTeamName() {
        return "The Big Steppers";
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
		return null;
    }

    @Override
    public void loadedImages(Image[] images) {
		//
	}

    @Override
    public void newRound() {
        //
    }
    
}
