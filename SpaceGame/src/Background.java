import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;

class Background {
    private BufferedImage background1;
    private int bg1Y;
    private BufferedImage background2;
    private int bg2Y;

    public Background() {
        try {
            background1 = ImageIO.read(new FileImageInputStream(new File("Images/background.jpeg")));
        } catch (IOException e) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, e);
        }

        try {
            background2 = ImageIO.read(new FileImageInputStream(new File("Images/background2.jpeg")));
        } catch (IOException e) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, e);
        }

        bg1Y = 0;
        bg2Y = -600;
    }

    public void move() {
        if (bg1Y == 600) {
            bg1Y = -600;
        }
        if (bg2Y == 600) {
            bg2Y = -600;
        }
        bg1Y += 3;
        bg2Y += 3;
    }

    public void draw(Graphics g) {
        g.drawImage(background1, 0, bg1Y, GamePanel.WIDTH, 600, null);
        g.drawImage(background2, 0, bg2Y, GamePanel.WIDTH, 600, null);
    }
}
