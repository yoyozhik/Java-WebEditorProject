/* regular image panel extending RegJPanel */

package org.dharmatech.views;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends RegJPanel {
    private BufferedImage image;

    public ImagePanel() {
        super();
    }

    public void setImage(String imagePath) {
        if (imagePath == null) {
            throw new NullPointerException("Null imagePath");
        }
        try {                
            image = ImageIO.read(new File(imagePath));
        } catch (IOException ex) {
            System.out.println("IOException during image setting: "
                + imagePath);
            ex.printStackTrace();
        } catch (Exception ex) {
            System.out.println("Exception during image setting: "
                + imagePath);
            ex.printStackTrace();
        } 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null); 
    }
}