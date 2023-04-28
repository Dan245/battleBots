package bots;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import arena.BattleBotArena;
import arena.BotInfo;
import arena.Bullet;

public class SlyzysBot extends Bot{
    Image current, up, down, right, left;

    BotHelper bh = new BotHelper();
    BotHelper displaceX = new BotHelper();
    BotHelper displaceY = new BotHelper();
    public int fireDirection = 0; //1=left, 2=right, 3=up, 4=down
    boolean startMove = true;
    boolean doRetaliate = false;
    boolean atLeftEdge = false;
    boolean atRightEdge = false;
    boolean atTopEdge = false;
    boolean atBottomEdge = false;

    @Override
    public void newRound() {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'newRound'");
    }

    public String[] imageNames()
	{
		String[] paths = {"drone_up.png", "drone_down.png", "drone_right.png", "drone_left.png"};
		return paths;
	}

    public void loadedImages(Image[] images)
	{
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

    @Override
    public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) {
        // TODO Auto-generated method stub
        //Strategy: 
        //throw new UnsupportedOperationException("Unimplemented method 'getMove'");
        int move = 0;
        //check if bullets list is greater than zero
        arena.Bullet closestBullet = bh.findClosest(me,bullets);
        double displaceBetweenX = displaceX.calcDisplacement(me.getX(),closestBullet.getX());
        double displaceBetweenY = displaceY.calcDisplacement(me.getY(),closestBullet.getY());
        
            
        //moves out of the way if the bot spawns near the middle of the arena
        if (startMove == true){
            if(me.getY() >= 300.00 && me.getY() < 600.00){
                move = BattleBotArena.DOWN;
                if (me.getY() >= 500.00){
                    startMove = false;
                }
            }
            if (me.getY() < 300.00 && me.getY() > 0){
                move = BattleBotArena.UP;
                if (me.getY() <= 100.00){
                    startMove = false;
                }   
            }
        }
        
        if ((closestBullet.getXSpeed() != 0 || closestBullet.getYSpeed() != 0)){
                //runs when bullet is close and moving to the right
                if(closestBullet.getXSpeed() > 0){
                    if(displaceBetweenX > -(RADIUS*2+30) && displaceBetweenX < -5.00){
                        if (displaceBetweenY >= 0 && displaceBetweenY < (RADIUS*2+30)){
                            //System.out.println("Dodge Up");
                            move = BattleBotArena.UP;
                            current=up;
                            fireDirection = 1;
                            doRetaliate = true;
				            return move;
                        }else if (displaceBetweenY < 0 && displaceBetweenY > -45.00){
                            //System.out.println("Dodge Down");
                            move = BattleBotArena.DOWN;
                            current=down;
                            fireDirection = 1;
                            doRetaliate = true;
				            return move;
                        }
                    //runs when bullet is far away       
                    }else{
                    }
                }       
                //Bullet moving left
                if(closestBullet.getXSpeed() < 0){
                    if (displaceBetweenX > 5.00 && displaceBetweenX < (RADIUS*2+30)){
                        if (displaceBetweenY >= 0 && displaceBetweenY < (RADIUS*2+30)){
                            //System.out.println("Dodge Up");
                            move = BattleBotArena.UP;
                            current=up;
                            fireDirection = 2;
                            doRetaliate = true;
				            return move;
                        }else if (displaceBetweenY < 0 && displaceBetweenY > -45.00){
                            //System.out.println("Dodge Down");
                            move = BattleBotArena.DOWN;
                            current=down;
                            fireDirection = 2;
                            doRetaliate = true;
				            return move;
                        }
                    }else{
                    }
                }
            
                //Bullet moving down
                if(closestBullet.getYSpeed() > 0){
                    if(displaceBetweenY < -5.00 && displaceBetweenY > -(RADIUS*2+30)){
                        if (displaceBetweenX < (RADIUS*2+30) && displaceBetweenX >= 0){
                            //System.out.println("Dodge Left");
                            move = BattleBotArena.LEFT;
                            current=left;
                            fireDirection = 3;
                            doRetaliate = true;
				            return move;
                        }else if (displaceBetweenX > -45.00 && displaceBetweenX < 0){
                            //System.out.println("Dodge Right");
                            move = BattleBotArena.RIGHT;
                            current=right;
                            fireDirection = 3;
                            doRetaliate = true;
				            return move;
                        }
                    }else{
                    }
                }

                //Bullet moving up
                if(closestBullet.getYSpeed() < 0){
                    //when in range
                    if(displaceBetweenY > 5.00 && displaceBetweenY < (RADIUS*2+30)){
                        if (displaceBetweenX < (RADIUS*2+30) && displaceBetweenX >= 0){
                            //System.out.println("Dodge Left");
                            move = BattleBotArena.LEFT;
                            current=left;
                            fireDirection = 4;
                            doRetaliate = true;
				            return move;
                        }else if (displaceBetweenX > -45.00 && displaceBetweenX < 0){
                            //System.out.println("Dodge Right");
                            move = BattleBotArena.RIGHT;
                            current=right;
                            fireDirection = 4;
                            doRetaliate = true;
				            return move;
                        }
                    //when out of range
                    }else{
                    }
                }
                
                    
                }

        //Truns true if the bot is at the arena border to move it away from the edge
        if (me.getX() == BattleBotArena.LEFT_EDGE){
            System.out.println("At left edge");
            atLeftEdge = true;
        }
        if (me.getX() + (RADIUS*2) == BattleBotArena.RIGHT_EDGE){
            System.out.println("At Right Edge");
            atRightEdge = true;
        }
        if (me.getY() == BattleBotArena.TOP_EDGE){
            System.out.println("At Top Edge");
            atTopEdge = true;
        }
        if (me.getY() + (RADIUS*2) == BattleBotArena.BOTTOM_EDGE){
            System.out.println("At Bottom Edge");
            atBottomEdge = true;
        }


        //Moves the bot to prevent it from getting stuck on the arena border
        if (atLeftEdge == true){
            if(me.getX() >= 0 && me.getX() < 35.00){
                move=BattleBotArena.RIGHT;
            }
            if (me.getX() >= 35.00){
                System.out.println("Away from Left Edge");
                atLeftEdge = false;
            }
        }

        if (atRightEdge == true){
            if(me.getX() + (RADIUS*2) <= BattleBotArena.RIGHT_EDGE && me.getX() > BattleBotArena.RIGHT_EDGE - 35.00){
                move=BattleBotArena.LEFT;
            }
            if (me.getX() <= BattleBotArena.RIGHT_EDGE - 35.00){
                System.out.println("Away from Right Edge");
                atRightEdge = false;
            }
        }

        if (atTopEdge == true){
            if(me.getY() >= BattleBotArena.TOP_EDGE && me.getY() < BattleBotArena.TOP_EDGE + 35.00){
                move=BattleBotArena.DOWN;
            }
            if (me.getY() >= BattleBotArena.TOP_EDGE + 35.00){
                System.out.println("Away from Top Edge");
                atTopEdge = false;
            }
        }

        if (atBottomEdge == true){
            if(me.getY()+(RADIUS*2) <= BattleBotArena.BOTTOM_EDGE && me.getY()+(RADIUS*2) > BattleBotArena.BOTTOM_EDGE - 35.00){
                move=BattleBotArena.UP;
            }
            if (me.getY()+(RADIUS*2) <= BattleBotArena.BOTTOM_EDGE - 35.00){
                System.out.println("Away from Left Edge");
                atBottomEdge = false;
            }
        }
    
        
        if (doRetaliate == true){
            return retaliate(shotOK,fireDirection);
        }
        doRetaliate = false;
        return move;
    }


    public int retaliate(boolean shotOK, int fireDirection){
        int move = 0;
        //fires a bullet at the direction of the dodged bullet
        if (doRetaliate == true && shotOK == true){
            if(fireDirection == 1){
                move = BattleBotArena.FIRELEFT;
            }
            if(fireDirection == 2){
                move = BattleBotArena.FIRERIGHT;
            }
            if(fireDirection == 3){
                move = BattleBotArena.FIREUP;
            }
            if(fireDirection == 4){
                move = BattleBotArena.FIREDOWN;
            }
        }
        doRetaliate = false;
        return move;
    }

    @Override
    public void draw(Graphics g, int x, int y) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'draw'");
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
        // TODO Auto-generated method stub
        return "SlyzysBot";
    }

    @Override
    public String getTeamName() {
        // TODO Auto-generated method stub
        return "cool";   }

    @Override
    public String outgoingMessage() {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'outgoingMessage'");
        return "";
    }

    @Override
    public void incomingMessage(int botNum, String msg) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'incomingMessage'");
    }

    // @Override
    // public String[] imageNames() {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'imageNames'");
    // }

    // @Override
    // public void loadedImages(Image[] images) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'loadedImages'");
    // }
    
}
