package bots;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;


import arena.BattleBotArena;
import arena.BotInfo;
import arena.Bullet;






public class KimBot extends Bot {
	BotHelper bh = new BotHelper() ;

	
	
	public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets)
	{
		
		if(bullets.length != 0){

		Bullet closestBullet = bh.findClosest(me, bullets);
		BotInfo closestBot = bh.findClosest(me, liveBots);




		if (closestBot.getX() != 0 ){
			if(closestBot.getX() == me.getX() && closestBot.getY() > me.getY()){
			    return BattleBotArena.FIREUP;
			}



			if(closestBot.getX() == me.getX() && closestBot.getY() < me.getY()){
				return BattleBotArena.FIREDOWN;
			}
		}




        if (closestBot.getY() != 0 ){
			if(closestBot.getY() == me.getY() && closestBot.getX() > me.getX()){
			    return BattleBotArena.FIRERIGHT;
			}


			if(closestBot.getX() == me.getX() && closestBot.getX() < me.getX()){
				return BattleBotArena.FIRELEFT;
			}
		}




			if (closestBullet.getXSpeed() != 0 ){
				if(closestBullet.getX() == me.getX() && closestBullet.getY() > me.getY()){
					return BattleBotArena.LEFT;
				}


				if(closestBullet.getX() == me.getX() && closestBullet.getY() < me.getY()){
					return BattleBotArena.RIGHT;
				}
			}


			if (closestBullet.getYSpeed() != 0 ){
				if(closestBullet.getY() == me.getY() && closestBullet.getX() > me.getX()){
					return BattleBotArena.DOWN;
				}


				if(closestBullet.getX() == me.getX() && closestBullet.getX() < me.getX()){
					return BattleBotArena.UP;
				}
				}
		}
	}
	
