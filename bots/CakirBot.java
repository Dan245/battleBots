package bots;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;

import arena.BattleBotArena;
import arena.BotInfo;
import arena.Bullet;

public class CakirBot extends Bot {

    BotHelper botHelper;
    int adjustment = 20;
    double radius = Bot.RADIUS * 2;
    double speed = BattleBotArena.BOT_SPEED * 2;
    int counter = 0;

    public CakirBot() {
        botHelper = new BotHelper();
    }

    @Override
    public void newRound() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'newRound'");
    }

    @Override
    public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) {
        // TODO Auto-generated method stub
        ArrayList<BotInfo> tempLiveBots = new ArrayList<>(Arrays.asList(liveBots));
        for (BotInfo bot : liveBots) {
            if (bot.getTeamName().equals(me.getTeamName())) {
                tempLiveBots.remove(bot);
            }
        }
        liveBots = tempLiveBots.toArray(new BotInfo[tempLiveBots.size()]);
        counter++;
        shotOK = false;
        double graveDistance = 1000;
        double tooClose = radius *1.5;
        if (counter == 5) {
            shotOK = true;
            counter = 0;
        }
        BotInfo closestBot = botHelper.findClosest(me, liveBots);
        if (liveBots.length > 0 && bullets.length > 0) {
            Bullet closestBullet = botHelper.findClosest(me, bullets);
            double close = 100.0;
            if ((closestBullet.getY() > (me.getY() - adjustment)) && (closestBullet.getY() < (me.getY() + radius + adjustment))) {
                // Bullet coming from the Left of the screen
                if ((closestBullet.getX() < me.getX())) {
                    if (closestBullet.getXSpeed() > 0) {
                        if ((Math.abs(me.getX()) - Math.abs(closestBullet.getX())) < close) {
                            if (closestBullet.getY() < (me.getY() + Bot.RADIUS)) {
                                if (deadBots.length > 0) {
                                    BotInfo closestGrave = botHelper.findClosest(me, deadBots);
                                    graveDistance = distance(closestGrave.getX() + Bot.RADIUS, closestGrave.getY() + Bot.RADIUS, me.getX() + Bot.RADIUS, me.getY() + Bot.RADIUS);
                                    if (graveDistance <= tooClose) {
                                        if (closestGrave.getY() >= me.getY()) {
                                            return BattleBotArena.UP;
                                        }
                                    }
                                }
                                return BattleBotArena.DOWN;
                            } else {
                                if (deadBots.length > 0) {
                                    BotInfo closestGrave = botHelper.findClosest(me, deadBots);
                                    graveDistance = distance(closestGrave.getX() + Bot.RADIUS, closestGrave.getY() + Bot.RADIUS, me.getX() + Bot.RADIUS, me.getY() + Bot.RADIUS);
                                    if (graveDistance <= tooClose) {
                                        if (closestGrave.getY() <= me.getY()) {
                                            return BattleBotArena.DOWN;
                                        }
                                    }
                                }
                                return BattleBotArena.UP;
                            }
                        }
                    }
                    // Bullet coming from the right of the screen
                } else {
                    if (closestBullet.getXSpeed() < 0) {
                        if ((Math.abs(closestBullet.getX()) - Math.abs(me.getX())) < close) {
                            if (closestBullet.getY() < (me.getY() + Bot.RADIUS)) {
                                if (deadBots.length > 0) {
                                    BotInfo closestGrave = botHelper.findClosest(me, deadBots);
                                    graveDistance = distance(closestGrave.getX() + Bot.RADIUS, closestGrave.getY() + Bot.RADIUS, me.getX() + Bot.RADIUS, me.getY() + Bot.RADIUS);
                                    if (graveDistance <= tooClose) {
                                        if (closestGrave.getY() >= me.getY()) {
                                            return BattleBotArena.UP;
                                        }
                                    }
                                }
                                return BattleBotArena.DOWN;
                            } else {
                                if (deadBots.length > 0) {
                                    BotInfo closestGrave = botHelper.findClosest(me, deadBots);
                                    graveDistance = distance(closestGrave.getX() + Bot.RADIUS, closestGrave.getY() + Bot.RADIUS, me.getX() + Bot.RADIUS, me.getY() + Bot.RADIUS);
                                    if (graveDistance <= tooClose) {
                                        if (closestGrave.getY() <= me.getY()) {
                                            return BattleBotArena.DOWN;
                                        }
                                    }
                                }
                                return BattleBotArena.UP;
                            }
                        }
                    }
                }
            }

            if ((closestBullet.getX() > (me.getX() - adjustment)) && (closestBullet.getX() < (me.getX() + radius + adjustment))) {
                // Bullet coming from the Top of the Screen
                if ((closestBullet.getY() < me.getY())) {
                    if (closestBullet.getYSpeed() > 0) {
                        if ((Math.abs(me.getY()) - Math.abs(closestBullet.getY())) < close) {
                            if (closestBullet.getX() < (me.getX() + Bot.RADIUS)) {
                                if (deadBots.length > 0) {
                                    BotInfo closestGrave = botHelper.findClosest(me, deadBots);
                                    graveDistance = distance(closestGrave.getX() + Bot.RADIUS, closestGrave.getY() + Bot.RADIUS, me.getX() + Bot.RADIUS, me.getY() + Bot.RADIUS);
                                    if (graveDistance <= tooClose) {
                                        if (closestGrave.getX() >= me.getX()) {
                                            return BattleBotArena.LEFT;
                                        }
                                    }
                                }
                                return BattleBotArena.RIGHT;
                            } else {
                                if (deadBots.length > 0) {
                                    BotInfo closestGrave = botHelper.findClosest(me, deadBots);
                                    graveDistance = distance(closestGrave.getX() + Bot.RADIUS, closestGrave.getY() + Bot.RADIUS, me.getX() + Bot.RADIUS, me.getY() + Bot.RADIUS);
                                    if (graveDistance <= tooClose) {
                                        if (closestGrave.getX() <= me.getX()) {
                                            return BattleBotArena.RIGHT;
                                        }
                                    }
                                }
                                return BattleBotArena.LEFT;
                            }
                        }
                    }
                    // Bullet coming from the Bottom of the Screen
                } else {
                    if (closestBullet.getYSpeed() < 0) {
                        if ((Math.abs(closestBullet.getY()) - Math.abs(me.getY())) < close) {
                            if (closestBullet.getX() < (me.getX() + Bot.RADIUS)) {
                                if (deadBots.length > 0) {
                                    BotInfo closestGrave = botHelper.findClosest(me, deadBots);
                                    graveDistance = distance(closestGrave.getX() + Bot.RADIUS, closestGrave.getY() + Bot.RADIUS, me.getX() + Bot.RADIUS, me.getY() + Bot.RADIUS);
                                    if (graveDistance <= tooClose) {
                                        if (closestGrave.getX() >= me.getX()) {
                                            return BattleBotArena.LEFT;
                                        }
                                    }
                                }
                                return BattleBotArena.RIGHT;
                            } else {
                                if (deadBots.length > 0) {
                                    BotInfo closestGrave = botHelper.findClosest(me, deadBots);
                                    graveDistance = distance(closestGrave.getX() + Bot.RADIUS, closestGrave.getY() + Bot.RADIUS, me.getX() + Bot.RADIUS, me.getY() + Bot.RADIUS);
                                    if (graveDistance <= tooClose) {
                                        if (closestGrave.getX() <= me.getX()) {
                                            return BattleBotArena.RIGHT;
                                        }
                                    }
                                }
                                return BattleBotArena.LEFT;
                            }
                        }
                    }
                }
            }
            
            if (!closestBot.getName().equals(this.getName())) {
                if (closestBot.getY() <= (me.getY() + Bot.RADIUS) && (closestBot.getY() + radius) >= (me.getY() + Bot.RADIUS)) {
                    if (shotOK) {
                        return shootBullets(me, deadBots, closestBot, false, bullets);
                    }
                } else if ((me.getX() + Bot.RADIUS) >= closestBot.getX() && (me.getX() + Bot.RADIUS) <= (closestBot.getX() + radius)) {
                    if (shotOK) {
                        return shootBullets(me, deadBots, closestBot, true, bullets);
                    }
                }
            }

            double botDistance = distance(closestBot.getX() + Bot.RADIUS, closestBot.getY() + Bot.RADIUS, me.getX() + Bot.RADIUS, me.getY() + Bot.RADIUS);
            if (deadBots.length > 0) {
                BotInfo closestGrave = botHelper.findClosest(me, deadBots);
                graveDistance = distance(closestGrave.getX() + Bot.RADIUS, closestGrave.getY() + Bot.RADIUS, me.getX() + Bot.RADIUS, me.getY() + Bot.RADIUS);
            }
            if (botDistance <= 100 || graveDistance <= 100) {
                if (closestBot.getX() > me.getX()) {
                    if (closestBullet.getX() > me.getX() && closestBullet.getX() < me.getX() + speed + radius && closestBullet.getYSpeed() != 0) {
                        return BattleBotArena.STAY;
                    }
                    if (botDistance <= tooClose) {
                        if (closestBot.getY() < me.getY()) {
                            if (me.getY() < closestBot.getY() + radius) {
                                if (Math.abs(closestBot.getX() - me.getX()) >= radius) {
                                    return BattleBotArena.UP;
                                } 
                            }
                        } else {
                            if (me.getY() + radius > closestBot.getY()) {
                                if (Math.abs(closestBot.getX() - me.getX()) >= radius) {
                                    return BattleBotArena.DOWN;
                                }
                            }
                        }
                    }
                    if (deadBots.length > 0) {
                        BotInfo closestGrave = botHelper.findClosest(me, deadBots);
                        graveDistance = distance(closestGrave.getX() + Bot.RADIUS, closestGrave.getY() + Bot.RADIUS, me.getX() + Bot.RADIUS, me.getY() + Bot.RADIUS);
                        if (graveDistance <= tooClose) {
                            if (closestGrave.getX() >= me.getX()) {
                                if (closestBot.getY() < me.getY()) {
                                    return BattleBotArena.UP;
                                }
                                return BattleBotArena.DOWN;
                            }
                        }
                    }
                    return BattleBotArena.RIGHT;
                //checks if the closest bot is to my left
                } else if (closestBot.getX() < me.getX()) {
                    //checks if there is a bullet travelling in the Y axis and if the bot will run into it if it chases
                    if (closestBullet.getX() > me.getX() - speed && closestBullet.getX() < me.getX() && closestBullet.getYSpeed() != 0) {
                        return BattleBotArena.STAY;
                    }
                    if (botDistance <= tooClose) {
                        if (closestBot.getY() < me.getY()) {
                            if (me.getY() < closestBot.getY() + radius) {
                                if (Math.abs(closestBot.getX() - me.getX()) >= radius) {
                                    return BattleBotArena.UP;
                                } 
                            }
                        } else {
                            if (me.getY() + radius > closestBot.getY()) {
                                if (Math.abs(closestBot.getX() - me.getX()) >= radius) {
                                    return BattleBotArena.DOWN;
                                }
                            }
                        }
                    }
                    if (deadBots.length > 0) {
                        BotInfo closestGrave = botHelper.findClosest(me, deadBots);
                        graveDistance = distance(closestGrave.getX() + Bot.RADIUS, closestGrave.getY() + Bot.RADIUS, me.getX() + Bot.RADIUS, me.getY() + Bot.RADIUS);
                        if (graveDistance <= tooClose) {
                            if (closestGrave.getX() <= me.getX()) {
                                if (closestBot.getY() < me.getY()) {
                                    return BattleBotArena.UP;
                                }
                                return BattleBotArena.DOWN;
                            }
                        }
                    }
                    return BattleBotArena.LEFT;
                }
            } else { 
                if (closestBot.getY() > me.getY()) {
                    if (closestBullet.getY() > me.getY() && closestBullet.getY() < me.getY() + speed + radius && closestBullet.getXSpeed() != 0) {
                        return BattleBotArena.STAY;
                    }
                    if (deadBots.length > 0) {
                        BotInfo closestGrave = botHelper.findClosest(me, deadBots);
                        graveDistance = distance(closestGrave.getX() + Bot.RADIUS, closestGrave.getY() + Bot.RADIUS, me.getX() + Bot.RADIUS, me.getY() + Bot.RADIUS);
                        if (graveDistance <= tooClose) {
                            if (closestGrave.getY() >= me.getY()) {
                                if (closestBot.getX() < me.getX()) {
                                    return BattleBotArena.LEFT;
                                }
                                return BattleBotArena.RIGHT;
                            }
                        }
                    }
                    return BattleBotArena.DOWN;
                } else if (closestBot.getY() < me.getY()) {
                    if (closestBullet.getY() > me.getY() - speed && closestBullet.getY() < me.getY() && closestBullet.getXSpeed() != 0) {
                        return BattleBotArena.STAY;
                    }
                    if (deadBots.length > 0) {
                        BotInfo closestGrave = botHelper.findClosest(me, deadBots);
                        graveDistance = distance(closestGrave.getX() + Bot.RADIUS, closestGrave.getY() + Bot.RADIUS, me.getX() + Bot.RADIUS, me.getY() + Bot.RADIUS);
                        if (graveDistance <= tooClose) {
                            if (closestGrave.getY() <= me.getY()) {
                                if (closestBot.getX() < me.getX()) {
                                    return BattleBotArena.LEFT;
                                }
                                return BattleBotArena.RIGHT;
                            }
                        }
                    }
                    return BattleBotArena.UP;
                }
            }
        }
        return 0;
    }

    public int shootBullets(BotInfo me, BotInfo[] deadBots, BotInfo closestBot, boolean vertical, Bullet[] bullets) {
        if (deadBots.length > 0) {
            BotInfo closestGrave = botHelper.findClosest(me, deadBots);
            if (vertical) {
                if ((me.getX() + Bot.RADIUS) >= closestGrave.getX() && (me.getX() + Bot.RADIUS) <= (closestGrave.getX() + radius)) {
                    if ((closestBot.getY() < me.getY() && closestGrave.getY() < closestBot.getY()) || (me.getY() < closestGrave.getY() && closestBot.getY() < me.getY())) {
                        return BattleBotArena.FIREUP;
                    } else if ((closestBot.getY() > me.getY() && closestGrave.getY() > closestBot.getY()) || (me.getY() > closestGrave.getY() && closestBot.getY() > me.getY())) {
                        return BattleBotArena.FIREDOWN;
                    } else {
                        return BattleBotArena.STAY;
                    }
                }
            } else {
                if ((me.getY() + Bot.RADIUS) >= closestGrave.getY() && (me.getY() + Bot.RADIUS) <= (closestGrave.getY() + radius)) {
                    if ((closestBot.getX() < me.getX() && closestGrave.getX() < closestBot.getX()) || (me.getX() < closestGrave.getX() && closestBot.getX() < me.getX())) {
                        return BattleBotArena.FIRELEFT;
                    } else if ((closestBot.getX() > me.getX() && closestGrave.getX() > closestBot.getX()) || (me.getX() > closestGrave.getX() && closestBot.getX() > me.getX())) {
                        return BattleBotArena.FIRERIGHT;
                    } else {
                        return BattleBotArena.STAY;
                    }
                }
            }
        }
        if (vertical) {
            if (closestBot.getY() < me.getY()) {
                return BattleBotArena.FIREUP;
            } else {
                return BattleBotArena.FIREDOWN;
            }
        } else {
            if (closestBot.getX() < me.getX()) {
                return BattleBotArena.FIRELEFT;
            } else {
                return BattleBotArena.FIRERIGHT;
            }
        }
    }

    public double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(Math.abs(x1 - x2), 2) + Math.pow(Math.abs(y1 - y2), 2));
    }

    @Override
    public void draw(Graphics g, int x, int y) {
        // TODO Auto-generated method stub
        g.setColor(Color.red);
        g.fillRect(x + 2, y + 2, RADIUS * 2 - 4, RADIUS * 2 - 4);
        // throw new UnsupportedOperationException("Unimplemented method 'draw'");
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "Cakir";
        // throw new UnsupportedOperationException("Unimplemented method 'getName'");
    }

    @Override
    public String getTeamName() {
        // TODO Auto-generated method stub
        return "Team Davin";
        // throw new UnsupportedOperationException("Unimplemented method
        // 'getTeamName'");
    }

    @Override
    public String outgoingMessage() {
        // TODO Auto-generated method stub
        return "haha";
        // throw new UnsupportedOperationException("Unimplemented method
        // 'outgoingMessage'");
    }

    @Override
    public void incomingMessage(int botNum, String msg) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method
        // 'incomingMessage'");
    }

    @Override
    public String[] imageNames() {
        // TODO Auto-generated method stub
        return null;
        // throw new UnsupportedOperationException("Unimplemented method 'imageNames'");
    }

    @Override
    public void loadedImages(Image[] images) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method
        // 'loadedImages'");
    }

}
