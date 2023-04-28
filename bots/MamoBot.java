package bots;

import arena.BattleBotArena;
import arena.BotInfo;
import arena.Bullet;

import java.awt.*;
import java.util.*;
import java.util.List;

public class MamoBot extends Bot {

    // CONSTANTS
    private final int ABOVE_INDEX = 0;
    private final int BELOW_INDEX = 1;
    private final int LEFT_INDEX = 2;
    private final int RIGHT_INDEX = 3;
    private final int X_INDEX = 0;
    private final int Y_INDEX = 1;
    private final int DEAD_INDEX = 2;
    private final int TEAM_INDEX = 3;
    
    private final int BOT_DEAD = 0;
    private final int BOT_ALIVE = 1;
    private final int YES = 1;
    private final int NO = 0;
    
    private ArrayList<BotInfo> topScorers = new ArrayList<>();

    private BotInfo currentTarget;
    private boolean newRound = true;

    private BotInfo me;


    Image current, up, down, right, left;

    private final String[] killMessages = {"git gud"};
    private String nextMessage = null;
    private int msgCounter = 0;

    @Override
    public void draw(Graphics g, int x, int y) {
        if (current != null)
            g.drawImage(current, x, y, Bot.RADIUS * 2, Bot.RADIUS * 2, null);
        else {
            g.setColor(Color.blue);
            g.fillOval(x, y, Bot.RADIUS * 2, Bot.RADIUS * 2);
        }
    }

    @Override
    public String getName() {
        return "BIG D";
    }

    @Override
    public String getTeamName() {
        return "Davin";
    }

    @Override
    public String outgoingMessage() {
        String msg = nextMessage;
        nextMessage = null;
        return msg;
    }

    @Override
    public void incomingMessage(int botNum, String msg) {
        if (botNum == BattleBotArena.SYSTEM_MSG && msg.matches(".*destroyed by " + getName() + ".*")) {
            int msgNum = (int) (Math.random() * killMessages.length);
            nextMessage = killMessages[msgNum];
            msgCounter = (int) (Math.random() * 30 + 30);
        }
    }

    @Override
    public String[] imageNames() {
        return new String[]{"MamoDrone_up.png", "MamoDrone_down.png", "MamoDrone_right.png", "MamoDrone_left.png"};
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

    @Override
    public void newRound() {

        newRound = true;
        currentTarget = null;
    }


    @Override
    public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) {
        if (newRound) {
            setTopScorers(liveBots);
            newRound = false;
        }

        if (liveBots != null) {
            this.me = me;
            BotInfo[] allBots = Arrays.copyOf(liveBots, liveBots.length + deadBots.length);
            System.arraycopy(deadBots, 0, allBots, liveBots.length, deadBots.length);
            double[][] allBotsDbl = botInfoToDblArray(allBots);
            double[][] botSuperPositions = getBotSuperPositions(allBotsDbl);


            boolean ezFarm = liveBots.length >= BattleBotArena.NUM_BOTS / 2; // hunt the closest bot if more than half arena still alive
            currentTarget = getTarget(liveBots, ezFarm);

            double[][] enemies = getBotsInSight(allBotsDbl, me.getX(), me.getY());
            Bullet[][] bulletsInSight = getBulletsInSight(getNextBulletPositions(bullets), enemies, me.getX(), me.getY());
            if (shotOK) { // if I can shoot
                int fire = botInKillZone(enemies, me.getX(), me.getY());
                if (fire != 0 && !firedAlready(fire, bulletsInSight)) {
                    updateImage(fire);
                    return fire;
                }
                if (moveIsSafe(BattleBotArena.STAY, allBots, botSuperPositions, bullets)) {
                    fire = potshots(enemies);
                }
                if (fire != 0 && !firedAlready(fire, bulletsInSight)) {
                    updateImage(fire);
                    return fire;
                }
            }
            if (currentTarget == null) {
                return BattleBotArena.STAY;
            }
            int[] movePreference = hunt(currentTarget);
            for (int move : movePreference) {
                if (moveIsSafe(move, allBots, botSuperPositions, bullets)) {
                    updateImage(move);
                    return move;
                }
            }
        }
        return BattleBotArena.STAY;
    }

    private int botInKillZone(double[][] enemies, double x, double y) {
        if (enemies[ABOVE_INDEX] != null) { // enemy that's above
            if (enemies[ABOVE_INDEX][DEAD_INDEX] == BOT_ALIVE &&
                    bulletTooClose(enemies[ABOVE_INDEX][Y_INDEX] + RADIUS * 2,
                            enemies[ABOVE_INDEX][X_INDEX], y - 1, x + RADIUS)) {
                if (enemies[ABOVE_INDEX][TEAM_INDEX] == NO) { // make sure not on team
                    return BattleBotArena.FIREUP;
                }
            }
        }
        if (enemies[BELOW_INDEX] != null) { // enemy that's below
            if (enemies[BELOW_INDEX][DEAD_INDEX] == BOT_ALIVE && bulletTooClose(enemies[BELOW_INDEX][Y_INDEX],
                    enemies[BELOW_INDEX][X_INDEX], (y + RADIUS * 2) + 1, x + RADIUS)) {
                if (enemies[BELOW_INDEX][TEAM_INDEX] == NO) { // make sure not on team
                    return BattleBotArena.FIREDOWN;
                }
            }
        }
        if (enemies[LEFT_INDEX] != null) { // enemy that's to my left
            if (enemies[LEFT_INDEX][DEAD_INDEX] == BOT_ALIVE &&
                    bulletTooClose(enemies[LEFT_INDEX][X_INDEX] + RADIUS * 2,
                            enemies[LEFT_INDEX][Y_INDEX], x - 1, y + RADIUS)) {
                if (enemies[LEFT_INDEX][TEAM_INDEX] == NO) { // make sure not on team
                    return BattleBotArena.FIRELEFT;
                }
            }
        }
        if (enemies[RIGHT_INDEX] != null) { // enemy that's to my right
            if (enemies[RIGHT_INDEX][DEAD_INDEX] == BOT_ALIVE && bulletTooClose(enemies[RIGHT_INDEX][X_INDEX],
                    enemies[RIGHT_INDEX][Y_INDEX], (x + RADIUS * 2) + 1, y + RADIUS)) {
                if (enemies[RIGHT_INDEX][TEAM_INDEX] == NO) { // make sure not on team
                    return BattleBotArena.FIRERIGHT;
                }
            }
        }
        return 0;
    }

    private int[] hunt(BotInfo target) {
        double[] huntPos = getHuntPosition(target);
        double meToPointX = Math.abs(me.getX() - huntPos[X_INDEX]);
        double meToPointY = Math.abs(me.getY() - huntPos[Y_INDEX]);
        int directionX = (me.getX() - huntPos[X_INDEX] < 0) ? BattleBotArena.RIGHT : BattleBotArena.LEFT;
        int badDirectionX = !(me.getX() - huntPos[X_INDEX] < 0) ? BattleBotArena.RIGHT : BattleBotArena.LEFT;
        int directionY = (me.getY() - huntPos[Y_INDEX] < 0) ? BattleBotArena.DOWN : BattleBotArena.UP;
        int badDirectionY = !(me.getY() - huntPos[Y_INDEX] < 0) ? BattleBotArena.DOWN : BattleBotArena.UP;
        double[][] enemy = getBotsInSight(botInfoToDblArray(new BotInfo[]{target}), huntPos[X_INDEX], huntPos[Y_INDEX]);
        if (intersectsY(huntPos[X_INDEX], target)) {
            if (botInKillZone(enemy, huntPos[X_INDEX], me.getY()) != 0) {
                return new int[]{directionY, directionX, BattleBotArena.STAY, badDirectionY, badDirectionX};
            } else if (meToPointX < BattleBotArena.BOT_SPEED) {
                return new int[]{directionY, BattleBotArena.STAY, directionX, badDirectionY, badDirectionX};
            } else {
                return new int[]{directionX, directionY, BattleBotArena.STAY, badDirectionX, badDirectionY};
            }
        } else if (intersectsX(huntPos[Y_INDEX], target)) {
            if (botInKillZone(enemy, me.getX(), huntPos[Y_INDEX]) != 0) {
                return new int[]{directionX, directionY, BattleBotArena.STAY, badDirectionX, badDirectionY};
            } else if (meToPointY < BattleBotArena.BOT_SPEED) {
                return new int[]{directionX, BattleBotArena.STAY, directionY, badDirectionX, badDirectionY};
            } else {
                return new int[]{directionY, directionX, BattleBotArena.STAY, badDirectionY, badDirectionX};
            }
        }
        return new int[]{0};
    }


    private boolean moveIsSafe(int move, BotInfo[] allBots, double[][] botList, Bullet[] bullets) {
        BotHelper helper = new BotHelper();
        BotInfo closestBot = helper.findClosest(me, allBots);
        double x = me.getX();
        double y = me.getY();
        switch (move) {
            case BattleBotArena.UP:
                y -= BattleBotArena.BOT_SPEED;
            case BattleBotArena.DOWN:
                y += BattleBotArena.BOT_SPEED;
            case BattleBotArena.LEFT:
                x -= BattleBotArena.BOT_SPEED;
            case BattleBotArena.RIGHT:
                x += BattleBotArena.BOT_SPEED;
        }
        if (botsOverlap(x, y, closestBot.getX(), closestBot.getY())) {
            return false;
        }
        if (bullets != null) {
            double[][] enemies = getBotsInSight(botList, x, y);
            Bullet[][] bulletsInSight = getBulletsInSight(getNextBulletPositions(bullets), enemies, x, y);
            if (botInKillZone(enemies, x, y) != 0) {
                return false;
            }
            for (Bullet bullet : bullets) {
                if (bulletCollided(bullet, x, y)) {
                    return false;
                }
            }
            for (int i = 0; i < bulletsInSight.length; i++) {
                Bullet[] bulletList = bulletsInSight[i];
                for (Bullet bullet : bulletList) {
                    if (bulletWillCollide(bullet, x, y, i)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private double[][] getBotsInSight(double[][] botSuperPositions, double x, double y) {
        double[][] botsInSight = new double[4][]; // order goes: up, down, left, right

        for (double[] bot : botSuperPositions) {
            if (intersectsX(y + RADIUS, bot)) { // if bot intersects with my X center line
                if (x - bot[X_INDEX] > 0) { // bot is to the left of me
                    if (botsInSight[LEFT_INDEX] == null) {
                        botsInSight[LEFT_INDEX] = bot;
                    } else if (botsInSight[LEFT_INDEX][X_INDEX] < bot[X_INDEX]) { // replace if bot is closer
                        botsInSight[LEFT_INDEX] = bot;
                    }
                } else { // bot is to the right of me
                    if (botsInSight[RIGHT_INDEX] == null) {
                        botsInSight[RIGHT_INDEX] = bot;
                    } else if (botsInSight[RIGHT_INDEX][X_INDEX] > bot[X_INDEX]) {
                        botsInSight[RIGHT_INDEX] = bot;
                    }
                }
            } else if (intersectsY(x + RADIUS, bot)) { // if bot intersects with my Y Center Line
                if (y - bot[Y_INDEX] > 0) { // bot is above me
                    if (botsInSight[ABOVE_INDEX] == null) {
                        botsInSight[ABOVE_INDEX] = bot;
                    } else if (botsInSight[ABOVE_INDEX][Y_INDEX] < bot[Y_INDEX]) {
                        botsInSight[ABOVE_INDEX] = bot;
                    }
                } else { // bot is below me
                    if (botsInSight[BELOW_INDEX] == null) {
                        botsInSight[BELOW_INDEX] = bot;
                    } else if (botsInSight[BELOW_INDEX][Y_INDEX] > bot[Y_INDEX]) {
                        botsInSight[BELOW_INDEX] = bot;
                    }
                }
            }
        }
        return botsInSight;
    }

    private Bullet[][] getBulletsInSight(Bullet[] bullets, double[][] enemies, double x, double y) {
        Bullet[][] bulletsInSight = new Bullet[4][];
        ArrayList<Bullet> bulletsAbove = new ArrayList<>();
        ArrayList<Bullet> bulletsBelow = new ArrayList<>();
        ArrayList<Bullet> bulletsLeft = new ArrayList<>();
        ArrayList<Bullet> bulletsRight = new ArrayList<>();
        if (bullets != null) {
            for (Bullet bullet : bullets) {
                if (intersectsX(y, bullet)) { // if on my X line
                    if (x - bullet.getX() > 0) { // bullet is to the left of me
                        if (enemies[LEFT_INDEX] == null) {
                            bulletsLeft.add(bullet);
                        } else if (bullet.getX() > enemies[LEFT_INDEX][X_INDEX]) { // check if a bot is in the way
                            bulletsLeft.add(bullet);
                        }
                    } else { // bullet is to the right of me
                        if (enemies[RIGHT_INDEX] == null) {
                            bulletsRight.add(bullet);
                        } else if (bullet.getX() < enemies[RIGHT_INDEX][X_INDEX]) {
                            bulletsRight.add(bullet);
                        }
                    }
                } else if (intersectsY(x, bullet)) { // if on my y line
                    if (y - bullet.getY() > 0) { // bullet is above me
                        if (enemies[ABOVE_INDEX] == null) {
                            bulletsAbove.add(bullet);
                        } else if (bullet.getY() > enemies[ABOVE_INDEX][Y_INDEX]) {
                            bulletsAbove.add(bullet);
                        }
                    } else { // bullet is below me
                        if (enemies[BELOW_INDEX] == null) {
                            bulletsBelow.add(bullet);
                        } else if (bullet.getY() < enemies[BELOW_INDEX][Y_INDEX]) {
                            bulletsBelow.add(bullet);
                        }
                    }
                }
            }
            bulletsInSight[ABOVE_INDEX] = bulletsAbove.toArray(new Bullet[0]);
            bulletsInSight[BELOW_INDEX] = bulletsBelow.toArray(new Bullet[0]);
            bulletsInSight[LEFT_INDEX] = bulletsLeft.toArray(new Bullet[0]);
            bulletsInSight[RIGHT_INDEX] = bulletsRight.toArray(new Bullet[0]);
        }
        return bulletsInSight;
    }

    private boolean firedAlready(int fireDirection, Bullet[][] bulletsInSight) {
        int index;
        if (fireDirection == BattleBotArena.FIREUP) {
            index = ABOVE_INDEX;
        } else if (fireDirection == BattleBotArena.FIREDOWN) {
            index = BELOW_INDEX;
        } else if (fireDirection == BattleBotArena.FIRELEFT) {
            index = LEFT_INDEX;
        } else {
            index = RIGHT_INDEX;
        }
        if (bulletsInSight[index] != null) {
            for (Bullet bullet : bulletsInSight[index]) {
                if (fireDirection == BattleBotArena.FIREUP && bullet.getYSpeed() < 0) {
                    return true;
                } else if (fireDirection == BattleBotArena.FIREDOWN && bullet.getYSpeed() > 0) {
                    return true;
                } else if (fireDirection == BattleBotArena.FIRELEFT && bullet.getXSpeed() < 0) {
                    return true;
                } else if (fireDirection == BattleBotArena.FIRERIGHT && bullet.getXSpeed() > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private Bullet[] getNextBulletPositions(Bullet[] bulletList) {
        ArrayList<Bullet> futureBullets = new ArrayList<>();
        for (Bullet bullet : bulletList) {
            futureBullets.add(new Bullet(bullet.getX() + bullet.getXSpeed(), bullet.getY() + bullet.getYSpeed(),
                    bullet.getXSpeed(), bullet.getYSpeed()));
        }
        return futureBullets.toArray(new Bullet[0]);
    }

    private double[][] getBotSuperPositions(double[][] botList) {
        ArrayList<double[]> botSuperPositions = new ArrayList<>();
        double botSpeed = BattleBotArena.BOT_SPEED;
        for (double[] bot : botList) {
            if (bot[DEAD_INDEX] == BOT_ALIVE) { // get all possible bot positions
                botSuperPositions.add(new double[]{bot[X_INDEX], bot[Y_INDEX], BOT_ALIVE, bot[TEAM_INDEX]}); // if bot stays still
                botSuperPositions.add(new double[]{bot[X_INDEX], bot[Y_INDEX] - botSpeed, BOT_ALIVE, bot[TEAM_INDEX]}); // if bot goes up
                botSuperPositions.add(new double[]{bot[X_INDEX], bot[Y_INDEX] + botSpeed, BOT_ALIVE, bot[TEAM_INDEX]}); // goes down
                botSuperPositions.add(new double[]{bot[X_INDEX] - botSpeed, bot[Y_INDEX], BOT_ALIVE, bot[TEAM_INDEX]}); // goes left
                botSuperPositions.add(new double[]{bot[X_INDEX] + botSpeed, bot[Y_INDEX], BOT_ALIVE, bot[TEAM_INDEX]}); // goes right
            } else {
                botSuperPositions.add(new double[]{bot[X_INDEX], bot[Y_INDEX], BOT_DEAD, bot[TEAM_INDEX]});
            }
        }
        return botSuperPositions.toArray(new double[0][]);
    }

    private double[][] botInfoToDblArray(BotInfo[] botList) {
        ArrayList<double[]> botSuperPositions = new ArrayList<>();
        for (BotInfo bot : botList) {
            double onMyTeam = (Objects.equals(bot.getTeamName(), me.getTeamName())) ? YES: NO;
            if (!bot.isDead()) {
                botSuperPositions.add(new double[]{bot.getX(), bot.getY(), BOT_ALIVE, onMyTeam});
            } else {
                botSuperPositions.add(new double[]{bot.getX(), bot.getY(), BOT_DEAD, onMyTeam});
            }
        }
        return botSuperPositions.toArray(new double[0][]);
    }

    private boolean bulletWillCollide(Bullet bullet, double x, double y, int index) {
        double botEdge;
        double botDodgeCoord;
        double bulletDistCoord;
        double bulletDodgeCoord;
        if (index == ABOVE_INDEX) {
            if (!(bullet.getY() < 0)) {
                return false;
            }
            botEdge = y;
            botDodgeCoord = x;
            bulletDistCoord = bullet.getY();
            bulletDodgeCoord = bullet.getX();
        } else if (index == BELOW_INDEX) {
            if (!(bullet.getY() > 0)) {
                return false;
            }
            botEdge = y + RADIUS * 2;
            botDodgeCoord = x;
            bulletDistCoord = bullet.getY();
            bulletDodgeCoord = bullet.getX();
        } else if (index == LEFT_INDEX) {
            if (!(bullet.getX() < 0)) {
                return false;
            }
            botEdge = x;
            botDodgeCoord = y;
            bulletDistCoord = bullet.getX();
            bulletDodgeCoord = bullet.getY();
        } else {
            if (!(bullet.getX() < 0)) {
                return false;
            }
            botEdge = x + RADIUS * 2;
            botDodgeCoord = y;
            bulletDistCoord = bullet.getX();
            bulletDodgeCoord = bullet.getY();
        }
        return bulletTooClose(botEdge, botDodgeCoord, bulletDistCoord, bulletDodgeCoord);
    }

   /*
   DON"T TOUCH, FUNCTIONS FINISHED
    */

    private BotInfo getTarget(BotInfo[] liveBots, boolean ezFarm) {
        if (ezFarm) {
            BotHelper helper = new BotHelper();
           ArrayList<BotInfo> bots = new ArrayList<>(Arrays.asList(liveBots));
            bots.removeIf(bot -> Objects.equals(bot.getTeamName(), me.getTeamName())); // get rid of team members
            return (bots.size() > 0) ? helper.findClosest(me, bots.toArray(new BotInfo[0])): null;
        }
        List<BotInfo> botlist = Arrays.asList(liveBots);
        if (currentTarget != null) {
            if (botlist.contains(currentTarget)) {
                return currentTarget;
            }
        }
        for (BotInfo topScorer : topScorers) {
            if (botlist.contains(topScorer) && !Objects.equals(topScorer.getTeamName(), me.getTeamName())) { // don't target dead or team members
                return topScorer;
            }
        }
        return null;
    }

    private void setTopScorers(BotInfo[] bots) {
        topScorers = new ArrayList<>(Arrays.asList(bots));
        topScorers.sort(Comparator.comparing(BotInfo::getCumulativeScore).reversed());
    }

    private void updateImage(int move) {
        if (move == BattleBotArena.UP || move == BattleBotArena.FIREUP) {
            current = up;
        } else if (move == BattleBotArena.DOWN || move == BattleBotArena.FIREDOWN) {
            current = down;
        } else if (move == BattleBotArena.LEFT || move == BattleBotArena.FIRELEFT) {
            current = left;
        } else if (move == BattleBotArena.RIGHT || move == BattleBotArena.FIRERIGHT) {
            current = right;
        }
    }

    private boolean botsOverlap(double x1, double y1, double x2, double y2) {
        if (y1 + RADIUS * 2 < y2
                || y1 > y2 + RADIUS * 2) {
            return false;
        }
        return !(x1 + RADIUS * 2 < x2)
                && !(x1 > x2 + RADIUS * 2);
    }

    private boolean bulletTooClose(double botEdge, double botDodgeCoord, double bulletDistCoord, double bulletDodgeCoord) {
        return Math.abs(botEdge - bulletDistCoord) / BattleBotArena.BULLET_SPEED <
                (RADIUS - Math.abs(bulletDodgeCoord - (botDodgeCoord + RADIUS))) / BattleBotArena.BOT_SPEED;
    }

    private double[] getHuntPosition(BotInfo target) {
        double[][] huntPositions = new double[4][];
        double[] closestPosition = new double[0];
        BotHelper helper = new BotHelper();
        double safeDist = ((RADIUS / BattleBotArena.BOT_SPEED) * BattleBotArena.BULLET_SPEED) + RADIUS * 2 + 1 + BattleBotArena.BOT_SPEED;
        huntPositions[ABOVE_INDEX] = new double[]{target.getX(), target.getY() - safeDist};
        huntPositions[BELOW_INDEX] = new double[]{target.getX(), target.getY() + safeDist};
        huntPositions[LEFT_INDEX] = new double[]{target.getX() - safeDist, target.getY()};
        huntPositions[RIGHT_INDEX] = new double[]{target.getX() + safeDist, target.getY()};
        for (double[] huntPosition : huntPositions) {
            double posDist = helper.manhattanDist(me.getX(), me.getY(), huntPosition[X_INDEX], huntPosition[Y_INDEX]);
            if (huntPosition[X_INDEX] >= BattleBotArena.LEFT_EDGE && huntPosition[X_INDEX] <= BattleBotArena.RIGHT_EDGE &&
                    huntPosition[Y_INDEX] >= BattleBotArena.TOP_EDGE && huntPosition[Y_INDEX] <= BattleBotArena.BOTTOM_EDGE) { // make sure point is in bounds
                if (closestPosition.length == 0) {
                    closestPosition = huntPosition;
                } else if (posDist < helper.calcDistance(me.getX(), me.getY(), closestPosition[X_INDEX], closestPosition[Y_INDEX])) {
                    closestPosition = huntPosition;
                }
            }
        }
        return closestPosition;
    }

    private boolean bulletCollided(Bullet bullet, double x, double y) {
        return bullet.getX() >= x && bullet.getX() <= x + RADIUS * 2 && bullet.getY() >= y && bullet.getY() <= y + RADIUS * 2;
    }

    private int potshots(double[][] enemies) { // shoot at bots I can see
        BotHelper helper = new BotHelper();
        double maxPotshotDist = ((RADIUS / BattleBotArena.BOT_SPEED) * BattleBotArena.BULLET_SPEED) + RADIUS * 2 + 1 + BattleBotArena.BOT_SPEED; // for safety
        maxPotshotDist *= 3; // minimizes wasting bullets
        if (enemies[ABOVE_INDEX] != null) { // enemy that's above
            if (enemies[ABOVE_INDEX][DEAD_INDEX] == BOT_ALIVE &&
                    helper.manhattanDist(enemies[ABOVE_INDEX][X_INDEX], enemies[ABOVE_INDEX][Y_INDEX], me.getX(), me.getY()) <= maxPotshotDist) {
                if (enemies[ABOVE_INDEX][TEAM_INDEX] == NO) { // make sure not on team
                    return BattleBotArena.FIREUP;
                }            }
        }
        if (enemies[BELOW_INDEX] != null) { // enemy that's below
            if (enemies[BELOW_INDEX][DEAD_INDEX] == BOT_ALIVE &&
                    helper.manhattanDist(enemies[BELOW_INDEX][X_INDEX], enemies[BELOW_INDEX][Y_INDEX], me.getX(), me.getY()) <= maxPotshotDist) {
                if (enemies[BELOW_INDEX][TEAM_INDEX] == NO) { // make sure not on team
                    return BattleBotArena.FIREDOWN;
                }            }
        }
        if (enemies[LEFT_INDEX] != null) { // enemy that's to my left
            if (enemies[LEFT_INDEX][DEAD_INDEX] == BOT_ALIVE &&
                    helper.manhattanDist(enemies[LEFT_INDEX][X_INDEX], enemies[LEFT_INDEX][Y_INDEX], me.getX(), me.getY()) <= maxPotshotDist) {
                if (enemies[LEFT_INDEX][TEAM_INDEX] == NO) { // make sure not on team
                    return BattleBotArena.FIRELEFT;
                }            }
        }
        if (enemies[RIGHT_INDEX] != null) { // enemy that's to my right
            if (enemies[RIGHT_INDEX][DEAD_INDEX] == BOT_ALIVE &&
                    helper.manhattanDist(enemies[RIGHT_INDEX][X_INDEX], enemies[RIGHT_INDEX][Y_INDEX], me.getX(), me.getY()) <= maxPotshotDist) {
                if (enemies[RIGHT_INDEX][TEAM_INDEX] == NO) { // make sure not on team
                    return BattleBotArena.FIRERIGHT;
                }            }
        }
        return 0;
    }

    private boolean intersectsX(double y, BotInfo bot) {
        return y >= bot.getY() && y <= bot.getY() + RADIUS * 2;
    }

    private boolean intersectsX(double y, double[] bot) {
        return y >= bot[Y_INDEX] && y <= bot[Y_INDEX] + RADIUS * 2;
    }

    private boolean intersectsX(double y, Bullet bullet) {
        return bullet.getY() >= y && bullet.getY() <= y + RADIUS * 2;
    }

    private boolean intersectsY(double x, BotInfo bot) {
        return x >= bot.getX() && x <= bot.getX() + RADIUS * 2;
    }

    private boolean intersectsY(double x, double[] bot) {
        return x >= bot[X_INDEX] && x <= bot[X_INDEX] + RADIUS * 2;
    }

    private boolean intersectsY(double x, Bullet bullet) {
        return bullet.getX() >= x && bullet.getX() <= x + RADIUS * 2;
    }

}
