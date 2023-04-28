package bots;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.Image;
import arena.BattleBotArena;
import arena.BotInfo;
import arena.Bullet;



public class Qiao_Bot extends Bot{
	


    // Move variables
    private int currentMove = 0;
    private int moveCount = 0;
    private int msgCounter = 0;
    private int current = BattleBotArena.STAY;
	private Image up, down, left, right;

    // Firing variables
    private boolean canFire = false;
    
    //Bot Helper
 	private final BotHelper BH = new BotHelper();
 	

    // Message variables
    private static final String MESSAGE = "Hello, other bots!";
	private static final double DODGE_DIST = 55;
	private static final double FIRE_RANGE = 400;
	
    @Override
    public void newRound() {
        // Reset move and firing variables
        currentMove = 0;
        moveCount = 0;
        canFire = true;
    }

    //See if something can be implemented so that if more then 10 bullets shot from same place move.
    @Override
    public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) {
        BotInfo closestBot = BH.findClosest(me, liveBots);
        double xDist = me.getX() - closestBot.getX();
        double yDist = me.getY() - closestBot.getY();
        
        if (closestBot.getTeamName().equals(getTeamName())) {
            return BattleBotArena.STAY;
        }

        
        if (Math.abs(xDist) <= FIRE_RANGE && Math.abs(yDist) <= FIRE_RANGE && shotOK) {
            if (Math.abs(yDist) < Math.abs(xDist)) {

                // shoot left or right
                if (xDist < 0) {
                    return BattleBotArena.FIRERIGHT;
                } else {
                    return BattleBotArena.FIRELEFT;
                }

            }  else {

                if (yDist > 0) {
                    return BattleBotArena.FIREUP;
                } else {
                    return BattleBotArena.FIREDOWN;
                }
            }
        }else if (bullets.length > 0) {
            Bullet closestBullet = BH.findClosest(me, bullets);
            // Check if bullet is on track to hitting us
   
            double botCenterX = me.getX() + RADIUS;
            double botCenterY = me.getY() + RADIUS;

            // Check if the bullet is too close on the x-axis
            if (Math.abs(botCenterX - closestBullet.getX()) < DODGE_DIST) {
                // Check if the bullet is approaching the bot from the correct direction on the y-axis
                if ((botCenterY < closestBullet.getY() && closestBullet.getYSpeed() < 0) ||
                        (botCenterY > closestBullet.getY() && closestBullet.getYSpeed() > 0)) {
                	 // Determine which side of the bot the bullet is on
                    if (closestBullet.getX() < botCenterX) {
                        // Bullet is on the left side of the bot
                    	
                        return BattleBotArena.RIGHT;
                    } else {
                        // Bullet is on the right side of the bot
                        return BattleBotArena.LEFT;
                    }
                }
            }

            // Check if the bullet is too close on the y-axis
            else if (Math.abs(botCenterY - closestBullet.getY()) < DODGE_DIST) {
                // Check if the bullet is approaching the bot from the correct direction on the x-axis
                if ((botCenterX < closestBullet.getX() && closestBullet.getXSpeed() < 0) ||
                        (botCenterX > closestBullet.getX() && closestBullet.getXSpeed() > 0)) {
                	 // Determine which side of the bot the bullet is on
                    if (closestBullet.getY() < botCenterY) {
                        // Bullet is on the top side of the bot
                        return BattleBotArena.DOWN;
                    } else {
                        // Bullet is on the bottom side of the bot
                        return BattleBotArena.UP;
                    }
                }
            }else{
            	
        	BotInfo closestBots = BH.findClosest(me, liveBots);
            double mxDist = me.getX() - closestBots.getX();
            double myDist = me.getY() - closestBots.getY();

            // Move towards the closest bot
            if (Math.abs(mxDist) < Math.abs(myDist)) {
                // Move in the x direction
                if (mxDist < botCenterX) {
                	current = BattleBotArena.RIGHT;
                    return BattleBotArena.RIGHT;
                } else {
                	current = BattleBotArena.LEFT;
                    return BattleBotArena.LEFT;
                }
            } else {
                // Move in the y direction
                if (myDist > botCenterY) {
                	current = BattleBotArena.UP;
                    return BattleBotArena.UP;
                } else {
                	current = BattleBotArena.DOWN;
                    return BattleBotArena.DOWN;
                }
            }
        }
        }
        return current; // default return value in case none of the conditions are met
    }


           
           
    
	

   
 


	private void fire(int fireright) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void draw(Graphics g, int x, int y) {
		// TODO Auto-generated method stub
		// Set the color of the robot
	    g.setColor(Color.RED);
	    g.fillRect(x, y, 25, 25);
	
		}
	
	


	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Qiao Bot";
	}

	@Override
	public String getTeamName() {
		// TODO Auto-generated method stub
		return "The Big Steppers";
	}
                                                            
	@Override
	public String outgoingMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void incomingMessage(int botNum, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] imageNames() {
		return null;
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadedImages(Image[] images) {

		
	}
	  public static void main(String[] args) {
		  
	  }

}
