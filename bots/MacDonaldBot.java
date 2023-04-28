package bots;

import arena.BattleBotArena;
import arena.BotInfo;
import arena.Bullet;

import java.awt.*;

public class MacDonaldBot extends Bot {

    private Image BOTIMAGE;

    private int rememberShot = 0;
    private int lastShootDirection;

    private int botNumber;

    private final String TEAMNAME = "cool";

    @Override
    public void newRound() {
    }

    @Override
    public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) {
        double x = me.getX() + RADIUS;
        double y = me.getY() + RADIUS;

        botNumber = me.getBotNumber();

        int currentDanger = positionSafety(x, y, bullets, deadBots);

        if (currentDanger != 10000) {
            int safestMovement = findSafestMovement(x, y, bullets, liveBots, deadBots, currentDanger);
            if (safestMovement != 0) return safestMovement;
        }

        int shootDirection = shotOK ? shootInDirection(x, y, liveBots) : 0;

        rememberShot = Math.max(rememberShot - 1, 0);

        if (shootDirection == lastShootDirection && rememberShot != 0) {
            shootDirection = 0;
        } else {
            lastShootDirection = shootDirection;
            rememberShot = 8;
        }

        return shootDirection;
    }

    private boolean positionFree(double x, double y, BotInfo[] allBots) {
        for (BotInfo b : allBots) {
            if (b.getBotNumber() != botNumber) {
                if (Math.sqrt(Math.pow(b.getX() - x, 2) + Math.pow(b.getY() - y, 2)) <= RADIUS * 2) return false;
            }
        }
        return true;
    }

    private int findSafestMovement(double x, double y, Bullet[] bullets, BotInfo[] liveBots, BotInfo[] deadBots, int d) {
        int safestMove = 0;
        int currentDanger = d;

        BotInfo[] allBots = new BotInfo[liveBots.length + deadBots.length];

        System.arraycopy(liveBots, 0, allBots, 0, liveBots.length);
        System.arraycopy(deadBots, 0, allBots, liveBots.length, deadBots.length);

        for (int i = 0; i < 4; i++) {
            int xM = i >= 2 ? (-1 + ((i % 2) * 2)) * 15 : 0;
            int yM = i <= 1 ? (-1 + (i * 2)) * 15 : 0;

            if (x + xM != Math.min(Math.max(x + xM, BattleBotArena.LEFT_EDGE), BattleBotArena.RIGHT_EDGE) || y + yM != Math.min(Math.max(y + yM, BattleBotArena.TOP_EDGE), BattleBotArena.BOTTOM_EDGE)) {
                break;
            }

            int moveDanger = positionSafety(x + xM, y + yM, bullets, deadBots);

            if (((currentDanger < moveDanger) || (safestMove == 0 && currentDanger == moveDanger)) && positionFree(x + xM, y + yM, allBots)) {
                currentDanger = moveDanger;
                safestMove = i + 1;
            }
        }

        return safestMove;
    }

    private int positionSafety(double x, double y, Bullet[] bullets, BotInfo[] deadBots) {
        int danger = 10000;

        for (Bullet b : bullets) {
            if (isThreat(x, y, b, deadBots)) {
                danger = Math.min(danger, (int) (Math.abs(b.getYSpeed() == 0 ? b.getX() - x : b.getY() - y)));
            }
        }

        return danger;
    }

    private int shootInDirection(double x, double y, BotInfo[] bots) {
        for (BotInfo bot : bots) {
            if (!bot.getTeamName().equals(TEAMNAME)) {
                int d = targetInDirection(x, y, bot);
                if (d != 0) return d;
            }
        }

        return 0;
    }

    private int targetInDirection(double x, double y, BotInfo bot) {
        double bX = bot.getX() + RADIUS;
        double bY = bot.getY() + RADIUS;

        if (Math.abs(bX - x) < 11) {
            return bY < y ? 5 : 6;
        } else if (Math.abs(bY - y) < 11) {
            return bX < x ? 7 : 8;
        } else {
            return 0;
        }
    }

    private boolean isThreat(double x, double y, Bullet bullet, BotInfo[] deadBots) {
        boolean bA = bullet.getYSpeed() == 0;
        int bD = (int)(Math.signum(bullet.getXSpeed() + bullet.getYSpeed()));
        double bS = bA ? bullet.getY() : bullet.getX();
        double bM = bA ? bullet.getX() : bullet.getY();

        return Math.abs(bS - (bA ? y : x)) <= Bot.RADIUS && Math.signum((bA ? x : y) - bM) == bD && willReachTarget(x, y, bullet, deadBots);
    }

    private boolean willReachTarget(double x, double y, Bullet bullet, BotInfo[] deadBots) {
        for (BotInfo deadBot : deadBots) {
            int t = Math.max(0, targetInDirection(x, y, deadBot) - 4);
            if (bulletDirection(bullet) == t) {
                double dT = (t <= 2) ? Math.abs(bullet.getY() - y) : Math.abs(bullet.getX() - x);
                double dB = (t <= 2) ? Math.abs(bullet.getY() - deadBot.getY()) : Math.abs(bullet.getX() - deadBot.getX());
                if (dB < dT) return false;
            }
        }
        return true;
    }

    private int bulletDirection(Bullet bullet) {
        int d = (Math.signum(bullet.getXSpeed() + bullet.getYSpeed())) == 1 ? 1 : 0;
        return (bullet.getYSpeed() == 0 ? 1 : 3) + d;
    }

    @Override
    public void draw(Graphics g, int x, int y) {
        g.drawImage(BOTIMAGE, x, y, Bot.RADIUS*2, Bot.RADIUS*2, null);
    }

    @Override
    public String getName() {
        return "Cool Bot";
    }

    @Override
    public String getTeamName() {
        return TEAMNAME;
    }

    @Override
    public String outgoingMessage() {
        return null;
    }

    @Override
    public void incomingMessage(int botNum, String msg) {

    }

    @Override
    public String[] imageNames() {
        return new String[]{"coolbot.png"};
    }

    @Override
    public void loadedImages(Image[] images) {
        BOTIMAGE = images[0];
    }
}
