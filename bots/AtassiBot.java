package bots;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import arena.BotInfo;
import arena.Bullet;
import arena.BattleBotArena;

public class AtassiBot extends Bot{

   // constants
    private final int shootingRange = Bot.RADIUS-1;

    Image current, up, down, right, left;

    BotHelper botHelper = new BotHelper();

    int move;

    private final int shootingDist = RADIUS - 1;

    private int botDist;

    private BotInfo closestBot;

    boolean blockingRight = false;
    boolean blockingLeft = false;
    boolean blockingUp = false;
    boolean blockingDown = false;

    public int allies[] = {15,8,12,9,4};

    //teammate method
    public boolean teammate(BotInfo[] liveBots){
		for (BotInfo bot : liveBots){
			if(bot.getTeamName() == "Team Jay" || bot.getTeamName() == "team jay") {
				return true;
			}
			for (int i = 0; i < allies.length; i++){
				if (bot.getBotNumber() == allies[i]){
					return true;
				}
			}
		}
		
		return false;
	}

    @Override
    public void newRound() {
        // TODO Auto-generated method stub
    }

    @Override
    public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) {
       if(liveBots.length>0){
        BotInfo closestBot = botHelper.findClosest(me, liveBots);
        double botDist = botHelper.calcDistance(me.getX(), me.getY(), closestBot.getX(), closestBot.getY());

        //DODGING BULLETS//
        if(bullets.length > 0){
            Bullet closestBullet = botHelper.findClosest(me, bullets);
            double dist = 100.0;
           
            //bullet coming from left 
            if ((closestBullet.getY() > me.getY()) && (closestBullet.getY() < (me.getY() + (double)Bot.RADIUS*2))){
                if((closestBullet.getX() < me.getX())){
                    if(closestBullet.getXSpeed()>0){
                        if((Math.abs(me.getX()) - Math.abs(closestBullet.getX())) < dist){
                            if (me.getY() > Bot.RADIUS){
                                return BattleBotArena.UP;
                            } else {
                                return BattleBotArena.DOWN;
                            }
                            
                        }
                    }
                } else {
                    //bullet coming from right
                    if (closestBullet.getXSpeed() < 0){
                        if((Math.abs(closestBullet.getX()) - Math.abs(me.getX())) < dist){
                                return BattleBotArena.DOWN;
                            
                            
                        }
                    }
                }

            }

        if ((closestBullet.getX() > me.getX()) && (closestBullet.getX() < (me.getX() + (double)Bot.RADIUS*2))){
            //Bullet coming from top
            if(closestBullet.getY() < me.getY()){
                if(closestBullet.getYSpeed()>0){
                    if((Math.abs(me.getY()) - Math.abs(closestBullet.getY())) < dist){
                        return BattleBotArena.LEFT;
                    }
                }

            }else{
                //bullet coming from bottom
                if(closestBullet.getYSpeed()<0){
                    if((Math.abs(closestBullet.getY()) - Math.abs(me.getY())) < dist){
                        return BattleBotArena.RIGHT;
                    }
                }
            }
        }
        
    }

        //EVADING OTHER BOTS//
        if(botDist < 50.0){
        double xDiff = me.getX() - closestBot.getX();
        double yDiff = me.getY() - closestBot.getY();

        if (Math.abs(xDiff)>Math.abs(yDiff)){
            return (xDiff > 0) ? BattleBotArena.RIGHT : BattleBotArena.LEFT;
        }else{
            return (yDiff > 0) ? BattleBotArena.DOWN : BattleBotArena.UP;
        }
        
        }


        //SHOOTING OPPONENT BOTS DOES NOT SHOOT AT TEAMMATE BOT//
		double xDist = me.getX() - closestBot.getX();
        double yDist = me.getY() - closestBot.getY();

        if (Math.abs(xDist) <= shootingDist && closestBot.getTeamName() != me.getTeamName()) {
            if (yDist < 0) {
                current = down;
                return BattleBotArena.FIREDOWN;
            } else {
                current = up;
                return BattleBotArena.FIREUP;
            }
        } else if (Math.abs(yDist) <= shootingDist && closestBot.getTeamName() != me.getTeamName()) {
            if (xDist < 0) {
                current = right;
                return BattleBotArena.FIRERIGHT;
            } else {
                current = left;
                return BattleBotArena.FIRELEFT;
            }
        }

        // if ((botHelper.findClosest(me, deadBots).getY() >= botHelper.findClosest(me, liveBots).getY() && botHelper.findClosest(me, deadBots).getY() <= botHelper.findClosest(me, liveBots).getY() + 2 * RADIUS) || (botHelper.findClosest(me, deadBots).getY() + 2 * RADIUS >= botHelper.findClosest(me, liveBots).getY() && botHelper.findClosest(me, deadBots).getY() + 2 * RADIUS <= botHelper.findClosest(me, liveBots).getY() + 2 * RADIUS)) {
        //     if (botHelper.findClosest(me, deadBots).getX() < botHelper.findClosest(me, liveBots).getX() && botHelper.findClosest(me, deadBots).getX() > me.getX()) {
        //         blockingRight = true;
        //     }
        // } else if ((botHelper.findClosest(me, deadBots).getY() >= botHelper.findClosest(me, liveBots).getY() && botHelper.findClosest(me, deadBots).getY() <= botHelper.findClosest(me, liveBots).getY() + 2 * RADIUS) || (botHelper.findClosest(me, deadBots).getY() + 2 * RADIUS >= botHelper.findClosest(me, liveBots).getY() && botHelper.findClosest(me, deadBots).getY() + 2 * RADIUS <= botHelper.findClosest(me, liveBots).getY() + 2 * RADIUS)) {
        //     if (botHelper.findClosest(me, deadBots).getX() > botHelper.findClosest(me, liveBots).getX() && botHelper.findClosest(me, deadBots).getX() < me.getX()) {
        //         blockingLeft = true;
        //     }
        // } else if ((botHelper.findClosest(me, deadBots).getX() >= botHelper.findClosest(me, liveBots).getX() && botHelper.findClosest(me, deadBots).getX() <= botHelper.findClosest(me, liveBots).getX() + 2 * RADIUS) || (botHelper.findClosest(me, deadBots).getX() + 2 * RADIUS >= botHelper.findClosest(me, liveBots).getX() && botHelper.findClosest(me, deadBots).getX() + 2 * RADIUS <= botHelper.findClosest(me, liveBots).getX() + 2 * RADIUS)) {
        //     if (botHelper.findClosest(me, deadBots).getY() > botHelper.findClosest(me, liveBots).getY() && botHelper.findClosest(me, deadBots).getY() < me.getY()) {
        //         blockingUp = true;
        //     }
        // } else if ((botHelper.findClosest(me, deadBots).getX() >= botHelper.findClosest(me, liveBots).getX() && botHelper.findClosest(me, deadBots).getX() <= botHelper.findClosest(me, liveBots).getX() + 2 * RADIUS) || (botHelper.findClosest(me, deadBots).getX() + 2 * RADIUS >= botHelper.findClosest(me, liveBots).getX() && botHelper.findClosest(me, deadBots).getX() + 2 * RADIUS <= botHelper.findClosest(me, liveBots).getX() + 2 * RADIUS)) {
        //     if (botHelper.findClosest(me, deadBots).getY() < botHelper.findClosest(me, liveBots).getY() && botHelper.findClosest(me, deadBots).getY() > me.getY()) {
        //         blockingDown = true;
        //         }
        //     } else {
        //         blockingRight = false;
        //         blockingLeft = false;
        //         blockingUp = false;
        //         blockingDown = false;
        //     }
        
    }   
    return 0;
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
        return "AtassiBot";
    }

    @Override
    public String getTeamName() {
        return "Team Jay";
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
        // TODO Auto-generated method stub
		return null;
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
