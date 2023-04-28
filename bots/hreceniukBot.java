//hreceniuk bot
package bots;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;


import arena.BattleBotArena;
import arena.BotInfo;
import arena.Bullet;

public class hreceniukBot extends Bot {

	private int move = BattleBotArena.STAY;
	private int resume;
	private boolean cocked = true;
	public double coordX = 0;
	public double coordY = 0;
	private String msg = null;
	public int mates[] = {6,16,2,1,20};
	
	
	@Override
	public void draw(Graphics g, int x, int y) {
		// TODO Auto-generated method stub
		g.setColor(Color.green);
		g.fillRect(x+2, y+2, RADIUS*2-4, RADIUS*2-4);
		if (!cocked)
		{
			g.setColor(Color.red);
			g.fillRect(x+3, y+3, RADIUS*2-6, RADIUS*2-6);
		}
	}
	public double getDistanceBetween(double x1, double y1, double x2, double y2){
		double dx = x2-x1;
		double dy = y2-y1;
		return Math.sqrt(dx*dx + dy*dy);
	}
	public boolean canMove(BotInfo me, BotInfo[] liveBots, BotInfo[] deadBots) {
		
		for (BotInfo bot : liveBots) {
			if (bot.equals(me)) {
				continue; 
			}
			double distance = Math.sqrt(Math.pow(bot.getX() - me.getX(), 2) + Math.pow(bot.getY() - me.getY(), 2));
			if (distance <= (me.getX() + Bot.RADIUS*2)) {
				return true;
			}
		}
		if (me.getX() <= (me.getX() + Bot.RADIUS*2) || me.getX() >= 800 - (me.getX()+ Bot.RADIUS*2) || 
        me.getY() <= (me.getX()+ Bot.RADIUS*2) || me.getY() >= 800 - (me.getX()+ Bot.RADIUS*2)) {
        return true;
   		}
		for (BotInfo deadBot : deadBots) {
			if ((deadBot.getX() == me.getX() + 1 && deadBot.getY() == me.getY()) || 
				(deadBot.getX() == me.getX() - 1 && deadBot.getY() == me.getY()) || 
				(deadBot.getX() == me.getX() && deadBot.getY() == me.getY() + 1) || 
				(deadBot.getX() == me.getX() && deadBot.getY() == me.getY() - 1)) { 
				return true; 
			} else {
				return false; 
			}
		}
		
		return false;
	}
	public boolean teammate(BotInfo[] liveBots){
		for (BotInfo bot : liveBots){
			if(bot.getTeamName() == "Davin" || bot.getTeamName() == "davin") {
				return true;
			}
			for (int i = 0; i < mates.length; i++){
				if (bot.getBotNumber() == mates[i]){
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots,
		BotInfo[] deadBots, Bullet[] bullets) {
		
		
		
        // coordX = me.getX();
        // coordY = me.getY();
        
        BotHelper bh = new BotHelper();
        Bullet nearBullet = bh.findClosest(me,bullets);
		BotInfo nearBot = bh.findClosest(me,liveBots);
		BotInfo nearGrave = bh.findClosest(me, deadBots);
		//bug to fix: only fires if nearBot is firing.
		if(teammate(liveBots) != true){
			if (nearBot.getX() == me.getX()) { 
				if (nearBot.getY() < me.getY()) {
					return BattleBotArena.FIREUP; 
				} else {
					return BattleBotArena.FIREDOWN; 
				}
			} else if (nearBot.getY() == me.getY()) { 
				if (nearBot.getX() > me.getX()) {
					return BattleBotArena.FIRERIGHT; 
				} else {
					return BattleBotArena.FIRELEFT; 
				}
			}
		}

		if (liveBots.length > 0){
			if (bullets.length >0){
				if(nearBullet != null){
					//800x800
					if(getDistanceBetween(me.getX(), me.getY(), nearBullet.getX(), nearBullet.getY()) < 100){
						if (nearBullet.getXSpeed() > 0){
							if (canMove(me, liveBots, deadBots) == true){
								return BattleBotArena.UP;
							} else {
								return BattleBotArena.DOWN;
							}
						} else if (nearBullet.getXSpeed() < 0){
							if (canMove(me, liveBots, deadBots) == true){
								return BattleBotArena.DOWN;
							} else {
								return BattleBotArena.UP;
							}
						} else if (nearBullet.getYSpeed() > 0){
							if (canMove(me, liveBots, deadBots) == true){
								return BattleBotArena.RIGHT;
							} else {
								return BattleBotArena.LEFT;
							}
						} else if (nearBullet.getYSpeed() < 0){
							if (canMove(me, liveBots, deadBots) == true){
								return BattleBotArena.LEFT;
							} else {
								return BattleBotArena.RIGHT;
							}
						}
					}
					
				}
			}
			
		}
		
		
		
		
	
		
		return 0;
	}


	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "HreceniukBot";
	}

	@Override
	public String getTeamName() {
		// TODO Auto-generated method stub
		return "Davin";
	}

	@Override
	public String[] imageNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void incomingMessage(int botNum, String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadedImages(Image[] images) {
		// TODO Auto-generated method stub

	}

	@Override
	public void newRound() {
		msg="GLHF";
       
        

	}

	@Override
	public String outgoingMessage() {
		// TODO Auto-generated method stub
		String x = msg;
		msg = null;
		return x;
	}
	
}
