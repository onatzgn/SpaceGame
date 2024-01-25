import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;

class Spaceship {
    private BufferedImage imageShip;
    private BufferedImage hitImage;
    private int PositionX;
    private int PositionY;
    private int MoveX;
    private int MoveY;
    private int lives;
    private boolean moveLeft;
    private boolean moveRight;
    private boolean moveUp;
    private boolean moveDown;
    private boolean hit;
    private int hitCount;
    private long hitTime;

    public Spaceship() {
        try {
            imageShip = ImageIO.read(new FileImageInputStream(new File("Images/spaceship.png")));
        } catch (IOException e) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, e);
        }
        
        try {
            hitImage = ImageIO.read(new FileImageInputStream(new File("Images/hitShip.png")));
        } catch (IOException e) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, e);
        }

        PositionX = 0;
        PositionY = 490;
        MoveX = 3;
        MoveY = 3;
        lives = 3;
        
        moveLeft = false;
        moveRight = false;
        moveUp = false;
        moveDown = false;
        
        hit = false;
        
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

	public int getHitCount() {
		return hitCount;
	}

	public void setHitCount(int hitCount) {
		this.hitCount = hitCount;
	}

	public long getHitTime() {
		return hitTime;
	}

	public void setHitTime(long hitTime) {
		this.hitTime = hitTime;
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public void moveUp() {
        if (PositionY > 0) {
            PositionY -= MoveY;
        }
    }

    public void moveDown() {
        if (PositionY < 490) {
            PositionY += MoveY;
        }
    }

    public BufferedImage getImageShip() {
		return imageShip;
	}

	public void setImageShip(BufferedImage imageShip) {
		this.imageShip = imageShip;
	}

	public int getPositionX() {
		return PositionX;
	}

	public void setPositionX(int positionX) {
		PositionX = positionX;
	}

	public int getPositionY() {
		return PositionY;
	}

	public void setPositionY(int positionY) {
		PositionY = positionY;
	}

	public int getMoveX() {
		return MoveX;
	}

	public void setMoveX(int moveX) {
		MoveX = moveX;
	}

	public int getMoveY() {
		return MoveY;
	}

	public void setMoveY(int moveY) {
		MoveY = moveY;
	}

	public void moveLeft() {
        if (PositionX > 0) {
            PositionX -= MoveX;
        }
    }

    public void moveRight() {
        if (PositionX < 730) {
            PositionX += MoveX;
        }
    }
    
    public Rectangle getBounds() {
    	return new Rectangle(PositionX, PositionY, imageShip.getWidth()/5, imageShip.getHeight()/5);
    }
    
    public void decreaseLives() {
    	lives--;
    }

    public void draw(Graphics g) {
    	if(hitCount>0) {
    		long currentTime = System.currentTimeMillis();
    		if(currentTime - hitTime <500) {
                g.drawImage(hitImage, PositionX, PositionY, hitImage.getWidth() / 4, hitImage.getHeight() / 4, null);
    		}else {
    			hitCount--;
    			hit = false;
    	        g.drawImage(imageShip, PositionX, PositionY, imageShip.getWidth() / 4, imageShip.getHeight() / 4, null);
    		}
    	}else {
            g.drawImage(imageShip, PositionX, PositionY, imageShip.getWidth() / 4, imageShip.getHeight() / 4, null);
    	}
    }

    public void setMoveLeft(boolean moveLeft) {
        this.moveLeft = moveLeft;
    }

    public void setMoveRight(boolean moveRight) {
        this.moveRight = moveRight;
    }

    public void setMoveUp(boolean moveUp) {
        this.moveUp = moveUp;
    }

    public void setMoveDown(boolean moveDown) {
        this.moveDown = moveDown;
    }

    public boolean isMoveLeft() {
        return moveLeft;
    }

    public boolean isMoveRight() {
        return moveRight;
    }

    public boolean isMoveUp() {
        return moveUp;
    }

    public boolean isMoveDown() {
        return moveDown;
    }
}



