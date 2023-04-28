package bots;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import arena.BattleBotArena;
import arena.BotInfo;
import arena.Bullet;

public class RisticBot extends Bot{

    
    double dodgeDistance = 80.0;
    double shootRange=250.0;
    double botSpace=30.0;

    BotHelper botHelper = new BotHelper();
    Image current, up, down, left, right;

    @Override
    public void newRound(){
    }
    
    @Override
    public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets){
        //check if there are bullets
		if(bullets.length>0){
            BotInfo closestDeadBot = botHelper.findClosest(me, deadBots);
            //find closest bullet
            Bullet closeBullet = botHelper.findClosest(me, bullets);
            if(botHelper.calcDistance(me.getX(), me.getY(), closeBullet.getX(), closeBullet.getY()) <= dodgeDistance){
                //check if horizontal movement
                if(closeBullet.getYSpeed() == 0){
                    //is bullet in line with me
                    if(closeBullet.getY()>=me.getY()+RADIUS || closeBullet.getY()<=me.getY()-RADIUS){
                        //is bullet moving right
                        if(closeBullet.getXSpeed()>0){
                            //check if there are any deadbots directly above me
                            if(closestDeadBot.getX()>= me.getX()+RADIUS || closestDeadBot.getX()<= me.getX()-RADIUS){
                                if(closestDeadBot.getY()<me.getY()-botSpace){
                                    return BattleBotArena.UP;
                                } else{
                                    return BattleBotArena.DOWN;
                                }
                            }
                        } else {
                            if(closestDeadBot.getX()>= me.getX()+RADIUS || closestDeadBot.getX()<= me.getX()-RADIUS){
                                if(closestDeadBot.getY()<me.getY()-botSpace){
                                    return BattleBotArena.UP;
                                } else{
                                    return BattleBotArena.DOWN;
                                }
                            }
                        }
                    }
                } else{
                    if(closeBullet.getX()>=me.getX()+RADIUS || closeBullet.getX()<=me.getX()-RADIUS){
                        if(closeBullet.getYSpeed()>0){
                            //check if there are any deadbots directly left of me
                            if(closestDeadBot.getY()>= me.getY()+RADIUS || closestDeadBot.getY()<= me.getY()-RADIUS){
                                if(closestDeadBot.getX()<me.getX()-botSpace){
                                    return BattleBotArena.LEFT;
                                } else{
                                    return BattleBotArena.RIGHT;
                                }
                            }
                        } else {
                            if(closestDeadBot.getY()>= me.getY()+RADIUS || closestDeadBot.getY()<= me.getY()-RADIUS){
                                if(closestDeadBot.getX()<me.getX()-botSpace){
                                    return BattleBotArena.LEFT;
                                } else{
                                    return BattleBotArena.RIGHT;
                                }
                            }
                        }
                }
            }
            
        }
        
        //shoot da bots
        for(BotInfo bot : liveBots){
            //make sure they not on my team
            if(bot.getTeamName() != me.getTeamName()){
                //check if closest bot is in x or y axis with my center and shoot if they are within a radius
                BotInfo closestBot1 = botHelper.findClosest(me, liveBots);
                if(Math.abs(closestBot1.getX()-me.getX())< RADIUS){
                    if(closestBot1.getY()>me.getY()){
                        if(shotOK){
                            return BattleBotArena.FIREDOWN;
                        }
                    }else{
                        if(shotOK){
                            return BattleBotArena.FIREUP;
                        } 
                    }
                } else if(Math.abs(closestBot1.getY()-me.getY())< RADIUS ){
                    if(closestBot1.getX()>me.getX()){
                        if(shotOK ){
                            return BattleBotArena.FIRERIGHT;
                        }
                    }else{
                        if(shotOK){
                            return BattleBotArena.FIRELEFT;
                        } 
                    }
                }
                
                //change asf to make it shoot uo or down if the xdiff is >= radius * 2 and  vice  versa f or  y
            }
        }
    }
    //dodge bots
        BotInfo closestBot = botHelper.findClosest(me, liveBots);
        if(botHelper.calcDistance(me.getX(), me.getY(),closestBot.getX(), closestBot.getY()) > 50){
        double xDifference = me.getX()- closestBot.getX();
        double yDifference = me.getY()- closestBot.getY();
        if (Math.abs(xDifference) > Math.abs(yDifference)) {
            if (xDifference > 0) {
                return BattleBotArena.RIGHT;
            } else {
                return BattleBotArena.LEFT;
            }
        } else {
            if (yDifference> 0) {
                return BattleBotArena.DOWN;
            } else {
                return BattleBotArena.UP;
            }
        }
    }
    return 0;
    }
   
    
        



    

    @Override
    public void draw(Graphics g, int x, int y) {
        if (current != null)
        g.drawImage(current, x, y, Bot.RADIUS*2, Bot.RADIUS*2, null);
    else
    {
        g.setColor(Color.YELLOW);
        g.fillOval(x, y, Bot.RADIUS*2, Bot.RADIUS*2);
    }
    }

    @Override
    public String getName() {
        return "RisticBot";
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
