package bots;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import arena.BattleBotArena;
import arena.BotInfo;
import arena.Bullet;
import java.lang.Math;
import java.util.ArrayList;

public class BatthBot extends Bot {

    BotHelper bh = new BotHelper();
    Image current, up, down, right, left;
    int Centre = 13;

    public int Radius() {

        return 13;
    }

    @Override
    public void newRound() {
        // TODO Auto-generated method stub

        
    }

    @Override
    public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) {
        // TODO Auto-generated method stub
        int move = 0;         
        
        if (bullets.length > 0) {

        
        Bullet closestBullet = bh.findClosest(me, bullets);
        }
        BotInfo closestBot = bh.findClosest(me, liveBots);

        double range = RADIUS*1.5;

        if(closestBot.getX() > me.getX()) {

            move = BattleBotArena.RIGHT;

        } else if(closestBot.getX() < me.getX()) {

            move = BattleBotArena.LEFT;

        } else if(closestBot.getY() > me.getY()) {

            move = BattleBotArena.DOWN;

        } else if(closestBot.getY() < me.getY()) {

            move = BattleBotArena.UP;

        }


        if(Math.abs(closestBot.getY() - me.getY()) < RADIUS && shotOK) {
            if (closestBot.getX() >= me.getX()) {
            
                move = BattleBotArena.FIRERIGHT;
            
            } else if (closestBot.getX() <= me.getX()) {

                move = BattleBotArena.FIRELEFT;

            }
    
        }   else if(Math.abs(closestBot.getX() - me.getX()) < RADIUS && shotOK) {
                if (closestBot.getY() >= me.getY()) {

                move = BattleBotArena.FIREDOWN;

                }   else if (closestBot.getY() <= me.getY()) {

                    move = BattleBotArena.FIREUP;

            } 

        
        } 

    //     if (closestBullet.getXSpeed() != 0) {
            
            
    //         if (closestBullet.getY() - RADIUS*0.5 >= me.getY() && closestBullet.getY() <= me.getY() + RADIUS) {

    //             move = BattleBotArena.DOWN;

    //         } else if (closestBullet.getY() >= me.getY() -+ RADIUS && closestBullet.getY() <= me.getY() + RADIUS*2.5) {

    //             move = BattleBotArena.UP;
            
    //         }
    //     }
        
    //    else if (closestBullet.getYSpeed() != 0) {
    //         if (closestBullet.getX() - RADIUS*0.5 >= me.getX() && closestBullet.getX() <= me.getX() + RADIUS) {

    //             move = BattleBotArena.RIGHT;
            
    //         } else if (closestBullet.getX() >= me.getX() + RADIUS && closestBullet.getX() <= me.getX() + RADIUS*2.5) {

    //             move = BattleBotArena.LEFT;
           
    //         }
    //     } 

        ArrayList <Bullet> BadBullets = new ArrayList<Bullet>();

        for (int i = 0; bullets.length > i; i++) {
            if (Math.abs(bullets[i].getX() - me.getX() - RADIUS) + Math.abs(bullets[i].getY() - me.getY() - RADIUS) > range)  {

                if (bullets[i].getXSpeed() != 0) {
                
                
                    if (bullets[i].getY() - RADIUS*1.5 >= me.getY() && bullets[i].getY() <= me.getY() + RADIUS) {
        
                        BadBullets.add(bullets[i]);
        
                    } else if (bullets[i].getY() >= me.getY() -+ RADIUS && bullets[i].getY() <= me.getY() + RADIUS*3.5) {
        
                        BadBullets.add(bullets[i]);
                    
                    }
                }
                
            else if (bullets[i].getYSpeed() != 0) {
                    if (bullets[i].getX() - RADIUS*1.5 >= me.getX() && bullets[i].getX() <= me.getX() + RADIUS) {
        
                        BadBullets.add(bullets[i]);
                    
                    } else if (bullets[i].getX() >= me.getX() + RADIUS && bullets[i].getX() <= me.getX() + RADIUS*3.5) {
        
                        BadBullets.add(bullets[i]);
                
                    }
                } 

            }  
        }   
        

    if (BadBullets.size() > 0) {

    
    Bullet closestBullet1 = bh.findClosest(me, BadBullets.toArray(new Bullet[BadBullets.size()]));

    if (closestBullet1.getXSpeed() != 0) {
            
            
        if (closestBullet1.getY() - RADIUS*0.5 >= me.getY() && closestBullet1.getY() <= me.getY() + RADIUS) {

            move = BattleBotArena.DOWN;

        } else if (closestBullet1.getY() >= me.getY() -+ RADIUS && closestBullet1.getY() <= me.getY() + RADIUS*2.5) {

            move = BattleBotArena.UP;
        
        }
    }
    
   else if (closestBullet1.getYSpeed() != 0) {
        if (closestBullet1.getX() - RADIUS*0.5 >= me.getX() && closestBullet1.getX() <= me.getX() + RADIUS) {

            move = BattleBotArena.RIGHT;
        
        } else if (closestBullet1.getX() >= me.getX() + RADIUS && closestBullet1.getX() <= me.getX() + RADIUS*2.5) {

            move = BattleBotArena.LEFT;
       
        }
    } 
}


        return move;
    
    }

    @Override
    public void draw(Graphics g, int x, int y) {
        // TODO Auto-generated method stub
        if (current != null) 

            g.drawImage(current, x, y, Bot.RADIUS*2, Bot.RADIUS*2, null);

        else {

            g.setColor(Color.lightGray);
            g.fillOval(x, y, Bot.RADIUS*2, Bot.RADIUS*2);
        
        }
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub

        return("BatthBot");
        
    }

    @Override
    public String getTeamName() {
        // TODO Auto-generated method stub
        return "Alex";
    }

    @Override
    public String outgoingMessage() {
        // TODO Auto-generated method stub
        return "";
        }

    @Override
    public void incomingMessage(int botNum, String msg) {
        // TODO Auto-generated method stub
    }

    @Override
    public String[] imageNames() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'imageNames'");
    }

    @Override
    public void loadedImages(Image[] images) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadedImages'");
    } 
    


    
}
