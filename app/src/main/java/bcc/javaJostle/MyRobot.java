package bcc.javaJostle;
import java.util.ArrayList;
import java.util.Random;

public class MyRobot extends Robot {
    private int lastX = -1;
    private int lastY = -1;
    private int stuckCount = 0;
    private Random rand = new Random();

      public MyRobot(int x, int y){
        super(x, y, 3, 3, 2, 2,"Dove", "myRobotImage.png", "defaultProjectileImage.png");
        // Health: 3, Speed: 3, Attack Speed: 2, Projectile Strength: 2
        // Total = 10
        // Image name is "myRobotImage.png"
    }


    public void think(ArrayList<Robot> robots, ArrayList<Projectile> projectiles, Map map, ArrayList<PowerUp> powerups) {
        xMovement = 0;
        yMovement = 0;
        shoot = false;
        int curX = getX();
        int curY = getY();
        if (curX == lastX && curY == lastY) {
            stuckCount++;
        } else {
            stuckCount = 0;
        }
        lastX = curX;
        lastY = curY;

        Robot closest = null;
        int bestDist = Integer.MAX_VALUE;
        for (Robot r : robots) {
            if (r != this && r.isAlive()) {
                int Xdist = r.getX() - curX;
                int  Ydist = r.getY() - curY;
                int distSq = Xdist*Xdist + Ydist*Ydist;
                if (distSq < bestDist) {
                    bestDist = distSq;
                    closest  = r;
                }
            }
        }

        if (closest != null) {
            int robx = closest.getX();
            int roby = closest.getY();

            if (canAttack()) {
                shootAtLocation(robx, roby);
                return;
            }

            if (stuckCount >= 3) {
                double r = rand.nextDouble();
                if (r < 0.25) {
                    xMovement =  1;
                } else if (r < 0.50) {
                    xMovement = -1;
                } else if (r < 0.75) {
                    yMovement =  1;
                } else {
                    yMovement = -1;
                }
                stuckCount = 0;
                return;
            }


            int tileSize = Utilities.TILE_SIZE;
            int[][] grid = map.getTiles();
            int rows = grid.length;
            int cols = grid[0].length;

            int curCol = (curX + Utilities.ROBOT_SIZE/2) / tileSize;
            int curRow = (curY + Utilities.ROBOT_SIZE/2) / tileSize;

            double bestScore = Double.MAX_VALUE;
            int bestXdist = 0;
            int bestYdist = 0;

          
            int[] robCols = { 1, -1,  0,  0 };
            int[] robRows = { 0,  0,  1, -1 };

            for (int i = 0; i < 4; i++) {
                int nextc = curCol + robCols[i];
                int nextr = curRow + robRows[i];

              
                if (nextr >= 0 && nextr < rows && nextc >= 0 && nextc < cols  && grid[nextr][nextc] != Utilities.WALL) {

                    double centerX = nextc * tileSize + tileSize/2.0;
                    double centerY = nextr * tileSize + tileSize/2.0;
                    double Xdist = robx - centerX;
                    double Ydist = roby - centerY;
                    double score = Xdist*Xdist + Ydist*Ydist;

                    if (score < bestScore) {
                        bestScore = score;
                        bestXdist = robCols[i];
                        bestYdist = robRows[i];
                    }
                }
            }

        
            if (bestScore == Double.MAX_VALUE) {
                double r2 = rand.nextDouble();
                if (r2 < 0.25) {
                    xMovement =  1;
                } else if (r2 < 0.50) {
                    xMovement = -1;
                } else if (r2 < 0.75) {
                    yMovement =  1;
                } else {
                    yMovement = -1;
                }
            } else {
                xMovement = bestXdist;
                yMovement = bestYdist;
            }
        }
        
    }
}
