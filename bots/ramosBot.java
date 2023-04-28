package bots;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.RadialGradientPaint;
import java.awt.Color;

import arena.BotInfo;
import arena.Bullet;

import arena.BattleBotArena;



public class ramosBot extends Bot{

    BotHelper bh;
    int offset =25;
    int minDistance = 100;
    private String msg = null;
    private boolean loaded = true;
    public ramosBot(){
        bh = new BotHelper();
    }
    @Override
    public void newRound() {
        // TODO Auto-generated method stub
         
    }

    @Override
    public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) {
        // TODO Auto-generated methBod stub
        int move = 0;
        BotHelper bh = new BotHelper();
        //closest bullet value
        arena.Bullet closestBullet = bh.findClosest(me,bullets);
        //closest bot value
        arena.BotInfo closestBot = bh.findClosest(me,liveBots);

        if ((closestBullet.getX() > (me.getX() - offset)) && (closestBullet.getX() < (me.getX() + Bot.RADIUS + offset))) {
            // BULLET COMING FROM BOTTOM OF SCREEN
            if (closestBullet.getYSpeed() < 0) {
                if ((Math.abs(closestBullet.getY()) - Math.abs(me.getY())) < minDistance) {
                    if (closestBullet.getX() < (me.getX() + Bot.RADIUS)) {
                        return BattleBotArena.RIGHT;
                    } else {
                        return BattleBotArena.LEFT;
                    }
                }
            }
        }else {
            // BULLET COMING FROM TOP OF SCREEN
            if ((closestBullet.getY() < me.getY())) {
                if (closestBullet.getYSpeed() > 0) {
                    if ((Math.abs(me.getY()) - Math.abs(closestBullet.getY())) < minDistance) {
                        if (closestBullet.getX() < (me.getX() + Bot.RADIUS)) {
                            return BattleBotArena.RIGHT;
                        } else {
                            return BattleBotArena.LEFT;
                        }
                    }
                }
             }
        }
        if ((closestBullet.getY() > (me.getY() - offset)) && (closestBullet.getY() < (me.getY() + Bot.RADIUS + offset))) {
            // BULLET COMING FROM RIGHT OF SCREEN
            if (closestBullet.getXSpeed() < 0) {
                if ((Math.abs(closestBullet.getX() - me.getX())) < minDistance) {
                    if (closestBullet.getY() < (me.getY() + Bot.RADIUS)) {
                        return BattleBotArena.DOWN;
                    } else {
                        return BattleBotArena.UP;
                    }
                }
            } else{
                    // BULLET COMING FROM LEFT OF SCREEN
                    if ((closestBullet.getX() < me.getX())) {
                        if (closestBullet.getXSpeed() > 0) {
                            if ((Math.abs(me.getX() - closestBullet.getX())) < minDistance) {
                                if (closestBullet.getY() < (me.getY() + Bot.RADIUS)) {
                                    return BattleBotArena.DOWN;
                                } else {
                                    return BattleBotArena.UP;
                                }
                            }
                }
            }
        }
    }
    double fireDistance = 275.0;
    for(BotInfo bot : liveBots){
        //IF ABSOLUTE VALUE OF DISTANCE BETWEEN BOT AND BOT IS LESS THAN OR EQUAL TO FIREDISTANCE AND IF BOT ISNT DEAD
        if((Math.abs(me.getY()-bot.getY()) <=fireDistance && !bot.isDead()) || (Math.abs(me.getX()-bot.getX())<=fireDistance && !bot.isDead())){
            //IF THE BOT IS
            double xDistanceBtwn = Math.abs(me.getX()-bot.getX());
            double yDistanceBtwn = Math.abs(me.getY()-bot.getY());
            if(yDistanceBtwn<xDistanceBtwn){
                if(me.getX()>bot.getX() || me.getX()+RADIUS > bot.getX()+RADIUS || me.getX()+RADIUS*2 > bot.getX()+RADIUS*2){
                    if(shotOK && bot.getTeamName() != me.getTeamName()){
                        return BattleBotArena.FIRELEFT;
                    }
                } else {
                    if(shotOK && bot.getTeamName() != me.getTeamName()){
                        return BattleBotArena.FIRERIGHT;
                    }
                }
            }else{
                if(bot.getY()<me.getY() || bot.getY()+RADIUS < me.getY()+RADIUS || bot.getY()+RADIUS*2 < me.getY()+RADIUS*2){
                    if(shotOK && bot.getTeamName() != me.getTeamName()){
                        return BattleBotArena.FIREUP;
                    }
                } else{
                    if(shotOK && bot.getTeamName() != me.getTeamName()){
                        return BattleBotArena.FIREDOWN;
                    }
                }
            }
        }
    }

   
        return move;
    }

    @Override
    public void draw(Graphics g, int x, int y) {
        // TODO Auto-generated method stub
        g.setColor(Color.white);
        g.fillRect(x+2, y+2, RADIUS*2-4, RADIUS*2-4);
        if(!loaded){
            g.setColor(Color.red);
            g.fillRect(x+3, y+3, RADIUS*2-6, RADIUS*2-6);
        }


    }

    @Override
    public String getName() {
        return "Jakob";
    }

    @Override
    public String getTeamName() {
        // TODO Auto-generated method stub
        return "Team Jay"; 
    }

    @Override
    public String outgoingMessage() {
        // TODO Auto-generated method stub
        String x = msg;
        msg = null;
        return x;
        

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
        // TODO Auto-generated method stub
    }
    
}
