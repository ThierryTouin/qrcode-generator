package com.touin.thierry.qrgenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class SimpleQrcodeGenerator {

    private static BitMatrix generateMatrix(final String data, final int size) throws WriterException {
        final BitMatrix bitMatrix = new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, size, size);
        return bitMatrix;
    }

    private static void writeImage(final String outputFileName, final String imageFormat, final BitMatrix bitMatrix) throws FileNotFoundException,
            IOException {
        final FileOutputStream fileOutputStream = new FileOutputStream(new File(outputFileName));
        MatrixToImageWriter.writeToStream(bitMatrix, imageFormat, fileOutputStream);
        fileOutputStream.close();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("SimpleQrcodeGenerator DEBUT");

        try {
            final String data = "Je suis Thierry et je suis gentil.";
            final String imageFormat = "png";
            final String outputFileName = "c:/code/qrcode-01." + imageFormat;
            final int size = 400;

            // encode
            final BitMatrix bitMatrix = generateMatrix(data, size);

            // write in a file
            writeImage(outputFileName, imageFormat, bitMatrix);

            System.out.println("SimpleQrcodeGenerator FIN");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}