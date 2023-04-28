package bots;

import java.awt.Graphics;
import java.awt.Image;
//import java.util.Random;

import arena.BattleBotArena;
import arena.BotInfo;
import arena.Bullet;

public class BellBot extends Bot {

    private int move = BattleBotArena.UP;

    Image up, down, left, right, current;

    String nextMessage = "He He";

    BotHelper bh = new BotHelper();

    @Override
    public void newRound() {
    }

    @Override
    public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) {
        double x = me.getX();
        double y = me.getY();
        int leftEdge = BattleBotArena.LEFT_EDGE;
        int rightEdge = BattleBotArena.RIGHT_EDGE;
        int topEdge = BattleBotArena.TOP_EDGE;
        int bottomEdge = BattleBotArena.BOTTOM_EDGE;
        int shoot = 0;
        if (bullets.length > 0) {

        Bullet bullet = bh.findClosest(me, bullets);
        double bx = bullet.getX();
        double by = bullet.getY();
        double xSpeed = bullet.getXSpeed();
        double ySpeed = bullet.getYSpeed();

        if (xSpeed < 0 || xSpeed > 0) {
            if (xSpeed < 0 && bx < x) {
            } else if (xSpeed > 0 && bx > x) {
            } else if (by < y + 15 && by > y - 20) {
                move = BattleBotArena.DOWN;
                return move;
            } else if (by > y + 14 && by < y + 47 ) {
                move = BattleBotArena.UP;
                return move;
            }
        } else if (ySpeed < 0 || ySpeed > 0) {
            if (ySpeed < 0 && by < y) {
                
            } else if (ySpeed > 0 && by > y) {
                
            } else if (bx < x + 15 && bx > x - 20) {
                move = BattleBotArena.RIGHT;
                return move;
            } else if (bx > x + 14 && bx < x + 47) {
                move = BattleBotArena.LEFT;
                return move;
            }
        }
    }
        // for (int i = 0; i < bullets.length; i++) {
        //    if (bullets[i].getX() >= x && bullets[i].getX() <= x + 26) {
        //         if (bullets[i].getY() > y - 50 && bullets[i].getY() < y + 50) {
        //             if (bullets[i].getX() <= x + 13) {
        //                 move = BattleBotArena.RIGHT;
        //             } else if (bullets[i].getX() >= x + 14 && bullets[i].getX() <= x + 26 ) {
        //                 move = BattleBotArena.LEFT;
        //             }
        //             return move;
        //         }

        //    } else if (bullets[i].getY() >= y && bullets[i].getY() <= y + 26) {
        //     if (bullets[i].getX() > x - 50 && bullets[i].getX() < x + 50) {
        //         if (bullets[i].getY() <= y + 13) {
        //             move = BattleBotArena.DOWN;
        //         } else if (bullets[i].getY() >= y + 14 && bullets[i].getY() <= y + 26 ) {
        //             move = BattleBotArena.UP;
        //         }
        //         return move;
        //     }
        //     }
        // }

        // MOVEMENT TYPE 1 MOVE TO CORNER


    //     if (x <= leftEdge|| x >= rightEdge - 26) {
    //        move = 52;
    //    } else if (x <= 500) {
    //        move = BattleBotArena.LEFT;
    //    } else if (x > 500) {
    //        move = BattleBotArena.RIGHT;
    //    }
    //    if (move == 52) {
    //        if (y <= topEdge || y >= bottomEdge - 26) {
    //            move = 53;
    //        } else if (y <= 345) {
    //            move = BattleBotArena.UP;
    //        } else if (y > 345) {
    //            move = BattleBotArena.DOWN;
    //        }
    //    }
    //    if (move == 53) {
    //        if (x <= 0) {
    //            if (y <= 0) {
    //                shoot = 1;
    //            } else if (y >= bottomEdge - 26) {
    //                shoot = 2;
    //            }
    //        } else if (x >= rightEdge - 26) {
    //            if (y <= 0) {
    //                shoot = 3;
    //            } else if (y >= bottomEdge - 26) {
    //                shoot = 4;
    //            }
    //         }
    //    }
    //     int random_int = (int)Math.floor(Math.random() * (2 - 1 + 1) + 1);
    //    if (shoot == 1) {
    //        if (random_int == 1) {
    //            move = BattleBotArena.FIREDOWN;
    //        } else if (random_int == 2) {
    //            move = BattleBotArena.FIRERIGHT;
    //        }
    //    } else if (shoot == 2) {
    //        if (random_int == 1) {
    //            move = BattleBotArena.FIREUP;
    //        } else if (random_int == 2) {
    //            move = BattleBotArena.FIRERIGHT;
    //        }
    //    } else if (shoot == 3) {
    //        if (random_int == 1) {
    //            move = BattleBotArena.FIREDOWN;
    //        } else if (random_int == 2) {
    //            move = BattleBotArena.FIRELEFT;  
    //        }
    //    } else if (shoot == 4) {
    //        if (random_int == 1) {
    //            move = BattleBotArena.FIREUP;
    //        } else if (random_int == 2) {
    //            move = BattleBotArena.FIRELEFT;
    //        }
    //    }

        double bx = bh.findClosest(me,liveBots).getX();
        double by = bh.findClosest(me,liveBots).getY();
        if (by > y + 26) {
            move = BattleBotArena.DOWN;
        } else if (by < y) {
            move = BattleBotArena.UP;
        } else if (bx > x + 26) {
            move = BattleBotArena.RIGHT;
        } else if (bx < x) {
            move = BattleBotArena.LEFT;
        }
        if (bh.findClosest(me,liveBots).getTeamName().equals("Team Davin")) {
            if (by > y + 26) {
                move = BattleBotArena.UP;
            } else if (by < y) {
                move = BattleBotArena.DOWN;
            } else if (bx > x + 26) {
                move = BattleBotArena.LEFT;
            } else if (bx < x) {
                move = BattleBotArena.RIGHT;
            }
        } else {
            if (by > y - 13 && by < y + 14){
                if (bx > x) {
                    move = BattleBotArena.FIRERIGHT;
                } else if (bx < x) {
                    move = BattleBotArena.FIRELEFT;
                }
            } else if (bx > x - 13 && bx < x + 14){
                if (by > y) {
                    move = BattleBotArena.FIREDOWN;
                } else if (by < y) {
                    move = BattleBotArena.FIREUP;
                }
            }
        }

        for (int i = 0; i <deadBots.length ; i++) {
            double dx = deadBots[i].getX();
            double dy = deadBots[i].getY();
            // if (bh.manhattanDist(x, y, dx, dy) >= 0 && bh.manhattanDist(x, y, dx, dy) <= 54){
            //     move = BattleBotArena.UP;
            // } else if (bh.manhattanDist(x, y, dx, dy) < 0 && bh.manhattanDist(x, y, dx, dy) >= -54) {
            //     move = BattleBotArena.DOWN;
            // }
        //     if (dy <= y - 25 && dy >= y - 27) {
        //         if (dx >= x - 26 && dx <= x) {
        //             move = BattleBotArena.LEFT;
        //         } else if (dx <= x + 26) {
        //             move = BattleBotArena.RIGHT;
        //         }
            
        //     } else if (dy >= y + 25 && dy <= y + 27) {
        //         if (dx >= x - 26 && dx <= x) {
        //             move = BattleBotArena.LEFT;
        //         } else if (dx <= x + 26) {
        //             move = BattleBotArena.RIGHT;
        //         }
        //     } else if (dx <= x - 25 && dx >= x - 27) {
        //         if (dy >= y - 26 && dy <= y) {
        //             move = BattleBotArena.UP;
        //         } else if (dy <= y + 26) {
        //             move = BattleBotArena.DOWN;
        //         }
            
        //     } else if (dx >= x + 25 && dx <= x + 27) {
        //         if (dy >= y - 26 && dy <= y) {
        //             move = BattleBotArena.UP;
        //         } else if (dy <= y + 26) {
        //             move = BattleBotArena.DOWN;
        //         }
        //}
            if (move == BattleBotArena.FIREUP) {
                if (dx > x - 15 && dx < x + 15) {
                if (dy < y && dy > y - 100) {
                    move = BattleBotArena.FIRELEFT;
                }
            }

            } else if (move == BattleBotArena.FIREDOWN) {
                if (dx > x - 15 && dx < x + 15) {
                    if (dy > y && dy < y + 100) {
                        move = BattleBotArena.FIRERIGHT;
                    }
                }
                
            } else if (move == BattleBotArena.FIRELEFT) {
                if (dy > y - 15 && dy < y + 15) {
                    if (dx < x && dx > x - 100) {
                        move = BattleBotArena.FIREUP;
                    }
                }
            } else if (move == BattleBotArena.FIRERIGHT) {
                if (dy > y - 15 && dy < y + 15) {
                    if (dx > x && dx < x + 100) {
                        move = BattleBotArena.FIREDOWN;
                    }
                }
            }
        }
        

       


        // MOVEMENT TYPE 2 RANDOM


    //    int random_int = (int)Math.floor(Math.random() * (12 - 1 + 1) + 1);
    //        if (random_int == 1) {
    //            move = BattleBotArena.FIREUP;
    //        } else if (random_int == 2) {
    //            move = BattleBotArena.FIREDOWN;
    //        } else if (random_int == 3) {
    //            move = BattleBotArena.FIRELEFT;
    //        } else if (random_int == 4) {
    //            move = BattleBotArena.FIRERIGHT;
    //        } else if (random_int >= 5 && random_int <= 6) {
    //            move = BattleBotArena.UP;
    //        } else if (random_int >= 7 && random_int <= 8) {
    //            move = BattleBotArena.DOWN;
    //        } else if (random_int >= 9 && random_int <= 10) {
    //            move = BattleBotArena.LEFT;
    //        } else if (random_int >= 11 && random_int <= 12) {
    //            move = BattleBotArena.RIGHT;
    //        }
        return move;
    }

    @Override
    public void draw(Graphics g, int x, int y) {
        g.drawImage(current, x, y, Bot.RADIUS*2, Bot.RADIUS*2, null);
    }

    @Override
    public String getName() {
        String name = "BellBot";
        return name;
    }

    @Override
    public String getTeamName() {
        return "Team Davin";
    }

    @Override
    public String outgoingMessage() {
        String msg = nextMessage;
		nextMessage = "He He";
		return msg;
    }

     @Override
    public void incomingMessage(int botNum, String msg) {

    }

    @Override
    public String[] imageNames() {
        String[] images = {"smile.png","smile.png","smile.png","smile.png"};
		return images;
    }

    @Override
    public void loadedImages(Image[] images) {
        if (images != null)
		{
			current = up = images[0];
			down = images[1];
			left = images[2];
			right = images[3];
		}
    }

    public double manhattanDistX(double x1, double x2) {
		return Math.abs(x2 - x1);
	}

	public double manhattanDistY(double y1,double y2) {
		return Math.abs(y2 - y1);
	}
    
}

    

