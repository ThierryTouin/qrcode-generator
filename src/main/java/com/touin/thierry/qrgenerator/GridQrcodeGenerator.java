package com.touin.thierry.qrgenerator;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;

public class GridQrcodeGenerator {
    private static ByteMatrix generateMatrix(final String data, final ErrorCorrectionLevel level) throws WriterException {
        final QRCode qr = new QRCode();
        Encoder.encode(data, level, qr);
        final ByteMatrix matrix = qr.getMatrix();
        return matrix;
    }

    private static void writeImage(final String outputFileName, final String imageFormat, final ByteMatrix matrix, final int size)
            throws FileNotFoundException, IOException {

        /**
         * Java 2D Traitement de Area
         */
        Area a = new Area(); // les futurs modules
        Area module = new Area(new Rectangle2D.Float(0.05f, 0.05f, 0.9f, 0.9f));

        AffineTransform at = new AffineTransform(); // pour dplacer le module
        int width = matrix.getWidth();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                if (matrix.get(j, i) == 1) {
                    a.add(module); // on ajoute le module
                }
                at.setToTranslation(1, 0); // on decale vers la droite
                module.transform(at);
            }
            at.setToTranslation(-width, 1); // on saute une ligne on revient au
                                            // 
            module.transform(at);
        }

        // agrandissement de l'Area pour le remplissage de l'image
        double ratio = size / (double) width;
        // il faut respecter la Quietzone : 4 modules de bordures autour du QR
        // Code
        double adjustment = width / (double) (width + 8);
        ratio = ratio * adjustment;

        at.setToTranslation(4, 4); // *
        a.transform(at);

        at.setToScale(ratio, ratio); // on agrandit
        a.transform(at);

        /**
         * Java 2D Traitement l'image
         */
        BufferedImage im = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) im.getGraphics();
        //Color couleur1 = new Color(0xFF0000);
        Color couleur1 = Color.decode("#B61E22");
        
        
        
        
        g.setPaint(couleur1);

        g.setBackground(new Color(0xFFFFFF));
        g.clearRect(0, 0, size, size); // pour le fond blanc
        g.fill(a); // remplissage des modules

        // Ecriture sur le disque
        File f = new File(outputFileName);
        f.setWritable(true);
        try {
            ImageIO.write(im, imageFormat, f);
            f.createNewFile();
        } catch (Exception e) {
        }

    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("GridQrcodeGenerator DEBUT");

        try {
            //final String data = "Je suis Thierry et je suis gentil.";
        	final String data = "https://play.google.com/store/apps/details?id=com.ionicframework.wineexplorer461338";
        	
            final String imageFormat = "png";
            final String outputFileName = "c:/Test/qrcode-ignition." + imageFormat;
            final int size = 400;
            final ErrorCorrectionLevel level = ErrorCorrectionLevel.Q;

            // encode
            final ByteMatrix matrix = generateMatrix(data, level);

            // write in a file
            writeImage(outputFileName, imageFormat, matrix, size);

            System.out.println("GridQrcodeGenerator FIN");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
