import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;

class Alien {
    private BufferedImage imageAlien;
    private BufferedImage hitImage;
    private int posX;
    private int posY;
    private int alienMoveX;
    private int alienMoveY;
    private int lives;
    private boolean hit;
    private int hitCount;
    private long hitTime;
    private long lastBulletTime;
    private long bulletDelay;
    private Random random = new Random();

    public Alien() {
        posX = (int)(Math.random()*(GamePanel.WIDTH-40));
        posY = -40;
        alienMoveX = 1;
        alienMoveY = 1;
        lives = 3;
        hit=false;
        lastBulletTime = System.currentTimeMillis();
        bulletDelay = generateBulletDelay();
        
        try {
            imageAlien = ImageIO.read(new FileImageInputStream(new File("Images/alien.png")));
        } catch (IOException e) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, e);
        }
        
        try {
            hitImage = ImageIO.read(new FileImageInputStream(new File("Images/alienhit.png")));
        } catch (IOException e) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    public long getLastBulletTime() {
		return lastBulletTime;
	}

	public void setLastBulletTime(long lastBulletTime) {
		this.lastBulletTime = lastBulletTime;
	}

	public long getBulletDelay() {
		return bulletDelay;
	}

	public void setBulletDelay(long bulletDelay) {
		this.bulletDelay = bulletDelay;
	}
	
	public long generateBulletDelay() { 
		return (long)(Math.random()*(3000-1000+1)+5000);
	}

	public BufferedImage getHitImage() {
		return hitImage;
	}

	public void setHitImage(BufferedImage hitImage) {
		this.hitImage = hitImage;
	}

	public boolean isHit() {
		return hit;
	}

	public void setHit(boolean hit) {
		this.hit = hit;
		if(hit) {
			hitCount++;
			hitTime = System.currentTimeMillis();
		}
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public int getAlienMoveY() {
		return alienMoveY;
	}

	public void setAlienMoveY(int alienMoveY) {
		this.alienMoveY = alienMoveY;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public void move() {
	    posY += alienMoveY;

	    if (posY >= GamePanel.HEIGHT) {
	        posY = -(imageAlien.getHeight()/ 3); 
	        posX = random.nextInt(GamePanel.WIDTH - (imageAlien.getWidth() / 3)); 
	    }
		posX += alienMoveX;
		
		if(posX>= 750) {
			alienMoveX = -alienMoveX;
		}
		if(posX<= 0) {
			alienMoveX = -alienMoveX;
		}
	}


    public void draw(Graphics g) {
    	if(hitCount>0) {
    		long currentTime = System.currentTimeMillis();
    		if(currentTime - hitTime <500) {
                g.drawImage(hitImage, posX, posY, hitImage.getWidth() / 3, hitImage.getHeight() / 3, null);
    		}else {
    			hitCount--;
    			hit = false;
    			g.drawImage(imageAlien, posX, posY, imageAlien.getWidth() / 3, imageAlien.getHeight() / 3, null);
    		}
    	}else {
    		g.drawImage(imageAlien, posX, posY, imageAlien.getWidth() / 3, imageAlien.getHeight() / 3, null);
    	}
    }

	public BufferedImage getImageAlien() {
		return imageAlien;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(posX,posY,imageAlien.getWidth()/5,imageAlien.getHeight()/5);
		
	}
	
	public void decreaseLives() {
		lives--;
	}
	public int getLives() {
		return lives;
	}

	public void setImageAlien(BufferedImage imageAlien) {
		this.imageAlien = imageAlien;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getAlienMoveX() {
		return alienMoveX;
	}

	public void setAlienMoveX(int alienMoveX) {
		this.alienMoveX = alienMoveX;
	}
}