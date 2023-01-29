package shoot_the_duck;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.text.AttributeSet.ColorAttribute;



public class Game {
    
   
    private Random random;
    
    
    private Font font;
    
   
    private ArrayList<Duck> ducks;
    private ArrayList<Integer> scoreTable = new ArrayList<>();
    boolean isScoreShowed;
    
    
    private int runawayDucks;
    
  
    private int killedDucks;
    
   
    private int score;
    

    private int shoots;
    
    
    private long lastTimeShoot;    
  
    private long timeBetweenShots;

  
    private BufferedImage backgroundImg;
    
  
    private BufferedImage grassImg;
    
   
    private BufferedImage yellowDuckImage;
    private BufferedImage blueDuckImage;
    private BufferedImage greenDuckImage;
    private BufferedImage orangeDuckImage;
    private BufferedImage selectedDuck;
  
    private BufferedImage sightImg;
    
    
    private int sightImgMiddleWidth;
  
    private int sightImgMiddleHeight;
    
    public static int gameLavel=1;
    

    public Game()
    {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;
        
        Thread threadForInitGame = new Thread() {
            @Override
            public void run(){
                // Sets variables and objects for the game.
                Initialize();
                // Load game files (images, sounds, ...)
                LoadContent();
                
                Framework.gameState = Framework.GameState.PLAYING;
            }
        };
        threadForInitGame.start();
    }
    
  
    private void Initialize()
    {
        random = new Random();        
        font = new Font("calibri", Font.BOLD, 18);
        
        ducks = new ArrayList<Duck>();
        
        runawayDucks = 0;
        killedDucks = 0;
        score = 0;
        shoots = 0;
        
        lastTimeShoot = 0;
        timeBetweenShots = Framework.secInNanosec / 3;
    }
    
   
    private void LoadContent()
    {
        try
        {
            URL backgroundImgUrl = this.getClass().getResource("/shoot_the_duck/resources/images/gamebg.jpg");
            backgroundImg = ImageIO.read(backgroundImgUrl);
            
            URL grassImgUrl = this.getClass().getResource("/shoot_the_duck/resources/images/grass.png");
            grassImg = ImageIO.read(grassImgUrl);
            
            URL yellowDuckImageUrl = this.getClass().getResource("/shoot_the_duck/resources/images/duck.png");
            yellowDuckImage = ImageIO.read(yellowDuckImageUrl);
            URL blueDuckImageUrl = this.getClass().getResource("/shoot_the_duck/resources/images/duckb.png");
            blueDuckImage = ImageIO.read(blueDuckImageUrl);
            URL greenDuckImageUrl = this.getClass().getResource("/shoot_the_duck/resources/images/duckg.png");
            greenDuckImage = ImageIO.read(greenDuckImageUrl);
            URL orangeDuckImageUrl = this.getClass().getResource("/shoot_the_duck/resources/images/ducko.png");
            orangeDuckImage = ImageIO.read(orangeDuckImageUrl);
            
            
            URL sightImgUrl = this.getClass().getResource("/shoot_the_duck/resources/images/sight.png");
            sightImg = ImageIO.read(sightImgUrl);
            sightImgMiddleWidth = sightImg.getWidth() / 2;
            sightImgMiddleHeight = sightImg.getHeight() / 2;
        }
        catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
 public void showScore( ) {
    	
    	JFrame frame = new JFrame("High Scores");
    	frame.setBounds(1200, 200, 140, 300);
    	frame.setBackground(Color.CYAN);
    	//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.getContentPane().setLayout(null);
    	scoreTable.add(score);
    	Collections.sort(scoreTable,Collections.reverseOrder());
    	JList<Integer> list = new JList<Integer>();
    	list.setModel(new AbstractListModel<Integer>() {
    		
    		public int getSize() {
    			return scoreTable.size();
    		}
    		public Integer getElementAt(int index) {
    			return scoreTable.get(index);
    		}
    	});
         
    	list.setBounds(10, 10, 100, 240);
    	frame.getContentPane().add(list);
    	list.setBackground(Color.CYAN);
    	frame.getContentPane().setBackground(Color.CYAN);
    	
    	
    	frame.setVisible(true);
    }
   
    public void RestartGame()
    {
        
        ducks.clear();
        
      
        Duck.lastDuckTime = 0;
        
        runawayDucks = 0;
        killedDucks = 0;
        score = 0;
        shoots = 0;
        gameLavel=0;
        lastTimeShoot = 0;
        isScoreShowed=false;
        
    }
    
    
    
    public void UpdateGame(long gameTime, Point mousePosition)
    {
       
        if(System.nanoTime() - Duck.lastDuckTime >= Duck.timeBetweenDucks)
        {
           
        	switch(Duck.duckLines[Duck.nextDuckLines][4]) {
        	case 0:
        		selectedDuck=yellowDuckImage;
        		break;
        	case 1:
        		selectedDuck=blueDuckImage;
        		break;
        	case 2:
        		selectedDuck=greenDuckImage;
        		break;
        	case 3:
        		selectedDuck=orangeDuckImage;
        		break;
        	
        	}
        	
            ducks.add(new Duck(Duck.duckLines[Duck.nextDuckLines][0] + random.nextInt(200), Duck.duckLines[Duck.nextDuckLines][1], Duck.duckLines[Duck.nextDuckLines][2], 
            		Duck.duckLines[Duck.nextDuckLines][3], selectedDuck));
            
            // Here we increase nextDuckLines so that next duck will be created in next line.
            Duck.nextDuckLines++;
            if(Duck.nextDuckLines >= Duck.duckLines.length)
                Duck.nextDuckLines = 0;
            
            Duck.lastDuckTime = System.nanoTime();
        }
        
        
        for(int i = 0; i < ducks.size(); i++)
        {
            
            ducks.get(i).Update();
            
           
            if(ducks.get(i).x > Framework.frameWidth)
            {
                ducks.remove(i);
                runawayDucks++;
            }
        }
        
       
        if(Canvas.mouseButtonState(MouseEvent.BUTTON1))
        {
           
            if(System.nanoTime() - lastTimeShoot >= timeBetweenShots)
            {
                shoots++;
                
               
                for(int i = 0; i < ducks.size(); i++)
                {
                  
                    if(new Rectangle(ducks.get(i).x + 18, ducks.get(i).y     , 27, 30).contains(mousePosition) ||
                       new Rectangle(ducks.get(i).x + 30, ducks.get(i).y + 30, 88, 25).contains(mousePosition))
                    {
                        killedDucks++;
                        score += ducks.get(i).score;
                        
                       
                        ducks.remove(i);
                        
                       
                        break;
                    }
                }
                
                lastTimeShoot = System.nanoTime();
            }
        }
        
        // When 10 ducks runaway, the game ends.
        if(runawayDucks >= 10)
            Framework.gameState = Framework.GameState.GAMEOVER;
        
        if (killedDucks>20) {
        	gameLavel=2;
        }
        if (killedDucks>30) {
        	gameLavel=3;
        }
    }
    

    public void Draw(Graphics2D g2d, Point mousePosition)
    {
        g2d.drawImage(backgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        
        for(int i = 0; i < ducks.size(); i++)
        {
            ducks.get(i).Draw(g2d);
        }
        
        g2d.drawImage(grassImg, 0, Framework.frameHeight - grassImg.getHeight(), Framework.frameWidth, grassImg.getHeight(), null);
        
        g2d.drawImage(sightImg, mousePosition.x - sightImgMiddleWidth, mousePosition.y - sightImgMiddleHeight, null);
        
        g2d.setFont(font);
        g2d.setColor(Color.black);
        
        g2d.drawString("Lavel   \t: " + gameLavel, 50, 30);
        g2d.drawString("RUNAWAY \t: " + runawayDucks, 1250, 30);
        g2d.drawString("KILLS   \t: " + killedDucks, 1250, 70);
        g2d.drawString("SHOOTS  \t: " + shoots, 1250, 110);
        g2d.drawString("SCORE   \t: " + score, 1250, 150);
    }
    
    
   
    public void DrawGameOver(Graphics2D g2d, Point mousePosition)
    {
        Draw(g2d, mousePosition);
        
       
        g2d.setColor(Color.black);
        g2d.drawString("Game Over", Framework.frameWidth / 2 - 39, (int)(Framework.frameHeight * 0.65) + 1);
        g2d.drawString("Press space or enter to restart.", Framework.frameWidth / 2 - 149, (int)(Framework.frameHeight * 0.70) + 1);
        
        
        if (!isScoreShowed) showScore(); 
        isScoreShowed=true;
    }
}
