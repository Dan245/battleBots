package bots;

import java.awt.Color;
import java.util.TimerTask;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Timer;
import java.util.TimerTask;

import arena.BattleBotArena;
import arena.BotInfo;
import arena.Bullet;

public class RayanBot extends Bot {

    BotHelper bothelper = new BotHelper();
   
    private String[] killStrings = { "ez pz", "skill diff", "unlucky tbh", "ugandan bruce lee" };
    Image current, up, down, right, left;
    private boolean overheat = false;
    private String nextMessage = null;
    private String name = null;
    private int msgCounter = 0;
    private int shoot = BattleBotArena.FIREDOWN;
    private int timerCounter = 0;

    private int move = BattleBotArena.UP;
    private final int fourmin = 4000;

   

    @Override
    public void newRound() {
        overheat = false;
        nextMessage = null;
    }

    @Override
    

        public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) {

            Bullet closestBullet = bothelper.findClosest(me, bullets);
            
            if (me.getY() - BattleBotArena.TOP_EDGE < me.getY() - BattleBotArena.BOTTOM_EDGE) {
                move = BattleBotArena.DOWN;
                shoot = BattleBotArena.FIREDOWN;
                return move;
            } else {
                while (me.getY() != BattleBotArena.TOP_EDGE) {
                    move = BattleBotArena.UP;
                    return move;
                }
            }

            
                
            
            Timer timer = new Timer();
            

           
                
            
                
            while (me.getY() == BattleBotArena.TOP_EDGE || me.getY() == BattleBotArena.BOTTOM_EDGE) {


                timer.schedule(new TimerTask() {
                   
                    public void run() {
                        timerCounter++;
                        if (timerCounter % 2 == 0) {
                            
                            move = BattleBotArena.LEFT;
                            shoot = BattleBotArena.FIREDOWN;
                        } else {
                            move = BattleBotArena.RIGHT;
                            shoot = BattleBotArena.FIREDOWN;
                            
                        }
                    }
                }, fourmin, fourmin);
                return move | shoot;
            }




              //  BotInfo closestBot = bothelper.findClosest(me, liveBots);
               // move = BattleBotArena.RIGHT;
                //shoot = BattleBotArena.FIREDOWN;
            
            
        
            
                
            return move | shoot;
        }
    

    @Override
    public void draw(Graphics g, int x, int y) {
        if (current != null)
            g.drawImage(current, x, y, Bot.RADIUS * 2, Bot.RADIUS * 2, null);
        else {
            g.setColor(Color.lightGray);
            g.fillOval(x, y, Bot.RADIUS * 2, Bot.RADIUS * 2);
        }

    }

    @Override
    public String getName() {
        if (name == null)
            name = "Rayan" + (botNumber < 10 ? "0" : "") + botNumber;
        return name;
    }

    @Override
    public String getTeamName() {
        return "Arena";
    }

    @Override
    public String outgoingMessage() {
        String msg = nextMessage;
        nextMessage = null;
        return msg;
    }

    @Override
    public void incomingMessage(int botNum, String msg) {

        // if (botNum == BattleBotArena.SYSTEM_MSG && msg.matches(".*destroyed by
        // "+getName()+".*"))
        // {
        // int msgNum = (int)(Math.random()*killMessages.length);
        // nextMessage = killMessages[msgNum];
        // msgCounter = (int)(Math.random()*30 + 30);
        // }
    }

    @Override
    public String[] imageNames() {
        String[] paths = { "pikachu_up.png", "pikachu_down.png", "pickachu_right.png", "pikachu_left.png" };
        return paths;
    }

    @Override
    public void loadedImages(Image[] images) {
        if (images != null) {
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
