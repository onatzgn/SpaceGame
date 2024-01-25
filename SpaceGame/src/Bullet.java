import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;

public class Bullet {
    private BufferedImage imageBullet;
    private int x;
    private int y;

    public Bullet(int x, int y) {
		try {
		setImageBullet(ImageIO.read(new FileImageInputStream(new File("Images/bullet.png")) ));
		}catch(IOException e) {
			Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE,null,e);
		}
		
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void draw(Graphics g) {
        g.fillRect(x, y, 5, 10);
    }

	public BufferedImage getImageBullet() {
		return imageBullet;
	}

	public void setImageBullet(BufferedImage imageBullet) {
		this.imageBullet = imageBullet;
	}
}

