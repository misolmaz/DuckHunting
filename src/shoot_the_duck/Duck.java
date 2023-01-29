package shoot_the_duck;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class Duck {
    
   
    public static long timeBetweenDucks = Framework.secInNanosec ;
    
    public static long lastDuckTime = 0;
    
   
    public static int[][] duckLines = {
                                       {-100, (int)(Framework.frameHeight * 0.60), -2*Game.gameLavel, 20,0},
                                       {-100, (int)(Framework.frameHeight * 0.65), -5*Game.gameLavel, 30,1},
                                       {-100, (int)(Framework.frameHeight * 0.70), -4*Game.gameLavel, 25,2},
                                       {-100, (int)(Framework.frameHeight * 0.78), -8*Game.gameLavel, 50,3}
                                      };
    
    public static int nextDuckLines = 0;
    
    
   
    public int x;
    
    public int y;
    
   
    private int speed;
    
   
    public int score;
    
    
    private BufferedImage duckImg;
    
    
   
    public Duck(int x, int y, int speed, int score, BufferedImage duckImg)
    {
        this.x = x;
        this.y = y;
        
        this.speed = speed;
        
        this.score = score;
        
        this.duckImg = duckImg;        
    }
    
    
   
    public void Update()
    {
        x -= speed;
    }
    
   
    public void Draw(Graphics2D g2d)
    {
        g2d.drawImage(duckImg, x, y, null);
    }
}
