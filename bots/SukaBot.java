package bots;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.naming.NameAlreadyBoundException;

import arena.BattleBotArena;
import arena.BotInfo;
import arena.Bullet;

public class SukaBot extends Bot {
    Image current, up, down, right, left;

    //stuff
    int shootDis = 15;
    int testDis = 15;
    int botDist = 100;
    int move;
    int testval =20;

    BotHelper botHelper = new BotHelper();

    int whatMove= BattleBotArena.DOWN;
    //bot move, (up, down, left, right)
    int whatShoot = BattleBotArena.FIREDOWN;
    //what direction is bot gonna shoot, (up, down, left, right)

//really just placeholder moves, will be changed in the code
    

    @Override
    public void newRound() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'newRound'");
    }
    

    @Override
    public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets){
        
        BotHelper bh = new BotHelper();
        arena.Bullet bulletNear = bh.findClosest(me,bullets);
        //finding the closest bullet to me so Sukabot can dodge
        

        // if the bots closest ARENT on my team
    if (me.getTeamName() != bh.findClosest(me, liveBots).getTeamName() && Math.abs(bh.calcDisplacement(bh.findClosest(me, liveBots).getY(), me.getY())) <= shootDis) {

        if (bh.calcDisplacement(bh.findClosest(me, liveBots).getX(), me.getX()) < 0) {
            whatShoot = BattleBotArena.FIRERIGHT;
            //if the bot is closest to me on my right, fire right

        } else if (bh.calcDisplacement(bh.findClosest(me, liveBots).getX(), me.getX()) >= 0) {
            whatShoot = BattleBotArena.FIRELEFT;
            //if the bot is closest to me on my left, fire left
        }

        return whatShoot;
        


        // if the bots closest ARENT on my team
    } else if (me.getTeamName() != bh.findClosest(me, liveBots).getTeamName() && Math.abs(bh.calcDisplacement(bh.findClosest(me, liveBots).getX(), me.getX())) <= shootDis) {

        if (bh.calcDisplacement(bh.findClosest(me, liveBots).getY(), me.getY()) < 0) {    
            whatShoot = BattleBotArena.FIREDOWN;
            //if the bot is closest to me downwards, fire down

        } else if (bh.calcDisplacement(bh.findClosest(me, liveBots).getY(), me.getY()) >= 0) {      
            whatShoot = BattleBotArena.FIREUP;
            //if the bot is closest enough to me upwards, fire up
        }

        return whatShoot;
    

//stuff for the right bullet
    }else if ((bulletNear.getY() > (me.getY() - testval)) && (bulletNear.getY() < (me.getY() + Bot.RADIUS + testval))) {//open
           if (bulletNear.getXSpeed() < 0) {//open
               if ((Math.abs(bulletNear.getX() - me.getX())) < botDist) {
                  //if the bullet too close in the X column 
                
                   if (bulletNear.getY() < (me.getY() + Bot.RADIUS)) {
                    //if the bullet too close in the Y column 

                       whatMove = BattleBotArena.DOWN;
                    
                   } else {

                       whatMove = BattleBotArena.UP;
                   }
                   
                 }

           }else{//open

     //stuff for the left bullet
                   if ((bulletNear.getX() < me.getX())) {
                       if (bulletNear.getXSpeed() > 0) {
                           if ((Math.abs(me.getX() - bulletNear.getX())) < botDist) {
                            //if the bullet too close in the X column 

                               if (bulletNear.getY() < (me.getY() + Bot.RADIUS)) {
                                //if the bullet too close in the Y column 

                                   whatMove = BattleBotArena.DOWN;

                               } else {

                                   whatMove = BattleBotArena.UP;
                               }
                           }
               		     }
                    	}
       }//close

//stuff for the bottom bullet
       if ((bulletNear.getX() > (me.getX() - testval)) && (bulletNear.getX() < (me.getX() + Bot.RADIUS + testval))) {//open
           if (bulletNear.getYSpeed() < 0) {

               if ((Math.abs(bulletNear.getY()) - Math.abs(me.getY())) < botDist) {
                 //if the bullet too close in the Y column 

                   if (bulletNear.getX() < (me.getX() + Bot.RADIUS)) {
                     //if the bullet too close in the X column 

                       whatMove = BattleBotArena.RIGHT;

                   } else {

                       whatMove = BattleBotArena.LEFT;
                   }
                }
                }

        }else {//open

//stuff for the top bullet
           if ((bulletNear.getY() < me.getY())) {
               if (bulletNear.getYSpeed() > 0) {
                   if ((Math.abs(me.getY()) - Math.abs(bulletNear.getY())) < botDist) {
                    //if the bullet too close in the Y column 

                       if (bulletNear.getX() < (me.getX() + Bot.RADIUS)) {
                         //if the bullet too close in the X column 


                           whatMove = BattleBotArena.RIGHT;
                           
                       } else {

                           whatMove = BattleBotArena.LEFT;
                           
                       }
                      }
                     }
                  }
       }//close
   }//close
   
       return whatMove;
    
       
}
    





@Override
public void draw (Graphics g, int x, int y)

	{
		if (current != null)
			g.drawImage(current, x, y, Bot.RADIUS*2, Bot.RADIUS*2, null);
		else
		{
			g.setColor(Color.blue);
			g.fillOval(x, y, Bot.RADIUS*2, Bot.RADIUS*2);
		}
	}

    @Override
    public String getName() {
        return "SukaBot";
    }

    @Override
    public String getTeamName() {
        // TODO Auto-generated method stub
		return "Team Davin";
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
