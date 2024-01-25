import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
public class GamePanel extends JPanel implements KeyListener, ActionListener, MouseMotionListener, MouseListener {

    private Timer timer;

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
	public static final int BULLET_SPEED = 5;

    private int gameTime = 0;
    private int gameScore = 0;
    private BufferedImage imageLives;

    private ArrayList<Bullet> bullets;
    private ArrayList<AlienBullet> bulletsA;
    private int spentBullets = 0;
    
    private ArrayList<Alien> aliens;

    private Spaceship spaceship;
    private Alien alien;
    private Background background;
    
    private boolean gameRunning;
    private long lastAlienSpawnTime;
    private long lastBulletTime;
    private long lastAlienBulletTime;
    
    private HighScoreManager highScoreManager;
    private UserManager userManager;
    
    private String username;
    
    private int level = 1;
    private int levelProgress = 0;
    private int requiredScore = 1000;
    
    private int alienBulletSpeed = 4;
    private int alienDelay = 1700;
    private int alienBulletDelay = 300;
    
    private boolean isPaused = false;


    public GamePanel() {
    	//LIVES-PNG
        try {
            imageLives = ImageIO.read(new FileImageInputStream(new File("Images/shiplives.png")));
        } catch (IOException e) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, e);
        }
        highScoreManager = new HighScoreManager();
        userManager = new UserManager();
        
        spaceship = new Spaceship();
        aliens = new ArrayList<>();
        background = new Background();
        bullets = new ArrayList<>();
        bulletsA = new ArrayList<>();
        initializeAliens();

        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setFocusable(true);
        
        timer = new Timer(12,this);

    }
    
    public void startGame(String username) { 
        gameRunning = true;
        lastAlienSpawnTime = System.currentTimeMillis();
        lastBulletTime = System.currentTimeMillis();
        lastAlienBulletTime = System.currentTimeMillis();

    	this.username = username;
        timer.start();

        
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

	        background.draw(g);
	        spaceship.draw(g);
	        
	        //BULLET
	        ArrayList<Bullet> bulletsToRemove = new ArrayList<>();
	
	        for (Bullet bullet : bullets) {
	            if (bullet.getY() < -50) {
	                bulletsToRemove.add(bullet);
	            } else {
	                g.drawImage(bullet.getImageBullet(), bullet.getX(), bullet.getY(), bullet.getImageBullet().getWidth() / 3, bullet.getImageBullet().getHeight() / 3, null);
	            }
	        }
	        bullets.removeAll(bulletsToRemove);
	
	        for (Alien alien : aliens) {
	        	alien.draw(g);
	        }
	        //ALIEN BULLET
	        ArrayList<AlienBullet> bulletsAToRemove = new ArrayList<>();
	        
	        for (AlienBullet bullet : bulletsA) {
	            if (bullet.getY() > HEIGHT) {
	                bulletsAToRemove.add(bullet);
	            } else {
	                g.drawImage(bullet.getImageBullet(), bullet.getX(), bullet.getY(), bullet.getImageBullet().getWidth() / 5, bullet.getImageBullet().getHeight() / 5, null);
	            }
	        }
	        
	        bulletsA.removeAll(bulletsAToRemove);
	        //GAMESCORE
	        String s = Integer.toString(gameScore);
	        g.setColor(Color.WHITE);
	        g.setFont(new Font("Arial", Font.BOLD, 18));
	        g.drawString(s, 380, 30);
	        //LIVES
	        g.drawImage(imageLives, 10, 10, imageLives.getWidth() / 15, imageLives.getHeight() / 15, null);
	        g.drawString("x" + spaceship.getLives(), (imageLives.getWidth() / 15)+13, (imageLives.getHeight() / 15)+4);
	
	        // LEVEL
	        String s2 = Integer.toString(level);
	        String levelText = "Level: " + s2;
	        g.setColor(Color.WHITE);
	        g.setFont(new Font("Arial", Font.BOLD, 18));
	        g.drawString(levelText, 90, 30);
	
	        // LEVEL PROGRESS
	        String s3 = Integer.toString(levelProgress);
	        String levelProgressText = "%" + s3;
	        g.setColor(Color.WHITE);
	        g.setFont(new Font("Arial", Font.BOLD, 18));
	        g.drawString(levelProgressText, 170, 30);
	        
	        if (!gameRunning) {
	            displayGameOverScreen(g);
	        }
	        
	        if (isPaused) {
	        	displayPauseScreen(g);
	        }
        }

    
    
    private void displayPauseScreen(Graphics g) {
        
        g.setColor(new Color(0, 0, 0, 150)); 
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT); 
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        String pausedText = "Paused";
        int pausedWidth = g.getFontMetrics().stringWidth(pausedText);
        g.drawString(pausedText, (GamePanel.WIDTH - pausedWidth) / 2, GamePanel.HEIGHT / 2);
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	
        	gameLoop();
            repaint();
    	
    }

    public void gameLoop() {
        if(gameRunning) {
        	gameTime += 5; 
        	
    		background.move();

            spaceshipMove();
            spawnAlien();
            moveAlien();
    		moveBullets();
            //alienFire();
            checkCollisions();
            
            levelProgress = 100 - ((requiredScore - gameScore) /10) ;
            if (gameScore >= requiredScore) {
                level++;
                Sound.playSound("Sounds/newlevel.wav");
                requiredScore += 1000;
                alienDelay -= 200;
                alienBulletDelay -= 100;
                alienBulletSpeed += 1;
            }
        }

        else {
            removeKeyListener(this);
            removeMouseListener(this);
            removeMouseMotionListener(this);
        }
        
    }
    
    private void spaceshipMove() {
        if (spaceship.isMoveLeft()) {
            spaceship.moveLeft();
        }

        if (spaceship.isMoveRight()) {
            spaceship.moveRight();
        }

        if (spaceship.isMoveUp()) {
            spaceship.moveUp();
        }

        if (spaceship.isMoveDown()) {
            spaceship.moveDown();
        }
    }
    
    private void initializeAliens() {
    	aliens.clear();
    	
    	for(int i =0; i<4; i++) {
    		Alien alien = new Alien();
    		aliens.add(alien);
    	}
    }
    
    private void spawnAlien() {
    	long currentTime = System.currentTimeMillis();
        	if(currentTime - lastAlienSpawnTime > alienDelay) {
        		Alien newAlien = new Alien();
        		aliens.add(newAlien);
        		lastAlienSpawnTime = currentTime;

        	}


    }
    
    private void moveAlien() {
    	long currentTime = System.currentTimeMillis();
        for (Alien alien : aliens) {
            alien.move();
            alien.setPosY(alien.getPosY()+alien.getAlienMoveY());
        
        
        if (currentTime - alien.getLastBulletTime() > alien.getBulletDelay()) {
                bulletsA.add(new AlienBullet(alien.getPosX() - 10, alien.getPosY()+30));
                Sound.playSound("Sounds/fireAlien.wav");
                alien.setLastBulletTime(currentTime);
        }
        
        }
    }
 /* NON USED CODE
    private void alienFire() {
    	long currentTime = System.currentTimeMillis();

        for (Alien alien : aliens) {
            if(currentTime - lastAlienBulletTime > alienBulletDelay){
                Sound.playSound("fireAlien.wav");
                bulletsA.add(new AlienBullet(alien.getPosX()+30  , alien.getPosY()+30));
                lastAlienBulletTime = currentTime;
            }
        }
    }
  */
    private void checkCollisions() {
        // BULLET - ALIEN
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            Rectangle bulletBounds = new Rectangle(bullet.getX(), bullet.getY(), 10, 20);

            Iterator<Alien> alienIterator = aliens.iterator();
            while (alienIterator.hasNext()) {
                Alien alien = alienIterator.next();
                Rectangle alienBounds = alien.getBounds();
                if (bulletBounds.intersects(alienBounds)) {
                    Sound.playSound("Sounds/damageAlien.wav");
                    alien.setHit(true);
                    alien.decreaseLives(); 
                    gameScore += 10;
                    if (alien.getLives() <= 0) {
                        alienIterator.remove(); 
                    }
                    bulletIterator.remove(); 
                    break;
                }
                alien.setHit(false);

            }
        }

        // SPACESHIP - ALIEN
        Rectangle spaceshipBounds = spaceship.getBounds();
        Iterator<Alien> alienIterator = aliens.iterator();
        while (alienIterator.hasNext()) {
            Alien alien = alienIterator.next();
            Rectangle alienBounds = alien.getBounds();

            if (spaceshipBounds.intersects(alienBounds)) {
                Sound.playSound("Sounds/damageShip.wav");
            	spaceship.setHit(true);
                spaceship.decreaseLives(); 

                if (spaceship.getLives() <= 0) {
    				gameRunning = false;
                    Sound.playSound("Sounds/gameover.wav");
                    highScoreManager.saveHighScore(username, gameScore);
                }
                else {
                	alienIterator.remove();
                	break;
                }
                spaceship.setHit(false);
                break;
            }
        }
        
        // SPACESHIP - ALIENBULLET
        Iterator<AlienBullet> alienBulletIterator = bulletsA.iterator();
        while (alienBulletIterator.hasNext()) {
            AlienBullet alienBullet = alienBulletIterator.next();
            Rectangle alienBulletBounds = new Rectangle(alienBullet.getX(), alienBullet.getY(), alienBullet.getImageBullet().getWidth() / 7, alienBullet.getImageBullet().getHeight() / 7);
  
            if (spaceshipBounds.intersects(alienBulletBounds)) {
                Sound.playSound("Sounds/damageShip.wav");
            	spaceship.setHit(true);
                spaceship.decreaseLives(); 

                if (spaceship.getLives() <= 0) {
                    gameRunning = false;
                    Sound.playSound("Sounds/gameover.wav");
                    highScoreManager.saveHighScore(username, gameScore);
                } else {
                    alienBulletIterator.remove();  
                    break;
                }
                spaceship.setHit(false);
                break;
            }
        }

    }
    
    private void moveBullets() {
    	//BULLET
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            bullet.setY(bullet.getY() - BULLET_SPEED);

            if (bullet.getY() < -50) {
                bulletIterator.remove();
            }
        }
        
        //ALIEN BULLET
        Iterator<AlienBullet> bulletAIterator = bulletsA.iterator();
        while (bulletAIterator.hasNext()) {
            AlienBullet bullet = bulletAIterator.next();
            bullet.setY(bullet.getY() + alienBulletSpeed);
            
            if (bullet.getY() > HEIGHT) {
                bulletAIterator.remove();
            }
            
        }
        
    }
    
    public void displayGameOverScreen(Graphics g) {
    	
        g.setColor(new Color(250, 0, 0, 60)); 
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT); 

        //GAME OVER
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        String gameOverText = "Game Over";
        int gameOverWidth = g.getFontMetrics().stringWidth(gameOverText);
        g.drawString(gameOverText, (GamePanel.WIDTH - gameOverWidth) / 2, GamePanel.HEIGHT / 2 - 50);
        //SCORE
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        String scoreText = "Score: " + gameScore;
        int scoreWidth = g.getFontMetrics().stringWidth(scoreText);
        g.drawString(scoreText, (GamePanel.WIDTH - scoreWidth) / 2, GamePanel.HEIGHT / 2);
        //USER
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD,20));
        String userText = username;
        int userWidth = g.getFontMetrics().stringWidth(userText);
        g.drawString(userText, (GamePanel.WIDTH - userWidth) / 2, GamePanel.HEIGHT / 2 + 30);
        
    }
    
    private void togglePause() {
        isPaused = !isPaused;
        if (isPaused) {
            timer.stop();
        } else {
            timer.start();
        }
        repaint();


    }


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override 
	public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            spaceship.setMoveLeft(true);
        }

        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            spaceship.setMoveRight(true);
        }

        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            spaceship.setMoveUp(true);
        }

        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
            spaceship.setMoveDown(true);
        }

        if (key == KeyEvent.VK_SPACE) {
        	if(!isPaused && gameRunning) {
        		long currentTime = System.currentTimeMillis();
        		if (currentTime - lastBulletTime > 200) {
        			bullets.add(new Bullet(spaceship.getPositionX()+25, spaceship.getPositionY() - 30));
        			Sound.playSound("Sounds/laser.wav");
        			spentBullets++;
        			lastBulletTime = currentTime;
            }
        	}
        }
        
        if (key == KeyEvent.VK_P) {
            togglePause();
        }
        

	}

	@Override
	public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            spaceship.setMoveLeft(false);
        }

        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            spaceship.setMoveRight(false);
        }

        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            spaceship.setMoveUp(false);
        }

        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
            spaceship.setMoveDown(false);
        }

        
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
	    int mouseButton = e.getButton();

	    if (mouseButton == MouseEvent.BUTTON1) {
	        long currentTime = System.currentTimeMillis();
	        if (currentTime - lastBulletTime > 200) {
	            bullets.add(new Bullet(spaceship.getPositionX() + 25, spaceship.getPositionY() - 30));
	            Sound.playSound("Sounds/laser.wav");
	            spentBullets++;
	            lastBulletTime = currentTime;
	        }
	    }
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	    int mouseButton = e.getButton();

	    if (mouseButton == MouseEvent.BUTTON1) { 
	    }
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	    int mouseX = e.getX();
	    int mouseY = e.getY();
	    int spaceshipX = spaceship.getPositionX();
	    int spaceshipY = spaceship.getPositionY();
	    int spaceshipWidth = spaceship.getImageShip().getWidth()/4;
	    int spaceshipHeight = spaceship.getImageShip().getHeight()/4;
	    int movementSpeed = 5; 

	    int horizontalDistance = mouseX - (spaceshipX + spaceshipWidth / 2);
	    int verticalDistance = mouseY - (spaceshipY + spaceshipHeight / 2);

	    if (horizontalDistance < -movementSpeed) {
	        spaceship.setMoveLeft(true);
	        spaceship.setMoveRight(false);
	    } else if (horizontalDistance > movementSpeed) {
	        spaceship.setMoveLeft(false);
	        spaceship.setMoveRight(true);
	    } else {
	        spaceship.setMoveLeft(false);
	        spaceship.setMoveRight(false);
	    }

	    if (verticalDistance < -movementSpeed) {
	        spaceship.setMoveUp(true);
	        spaceship.setMoveDown(false);
	    } else if (verticalDistance > movementSpeed) {
	        spaceship.setMoveUp(false);
	        spaceship.setMoveDown(true);
	    } else {
	        spaceship.setMoveUp(false);
	        spaceship.setMoveDown(false);
	    }

	    // MOVEMENT SPEED 
	    if (spaceship.isMoveLeft()) {
	        spaceship.setPositionX(spaceship.getPositionX() - movementSpeed);
	    } else if (spaceship.isMoveRight()) {
	        spaceship.setPositionX(spaceship.getPositionX() + movementSpeed);
	    }

	    if (spaceship.isMoveUp()) {
	        spaceship.setPositionY(spaceship.getPositionY() - movementSpeed);
	    } else if (spaceship.isMoveDown()) {
	        spaceship.setPositionY(spaceship.getPositionY() + movementSpeed);
	    }
	}





}
