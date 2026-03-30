package com.stadium;
 
import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import java.awt.image.BufferedImage;
 
public class QRGenerator {
 
    public static BufferedImage generateQR(String text){
        try{
            int size = 220;
 
            BitMatrix matrix =
                    new MultiFormatWriter()
                            .encode(text,
                                    BarcodeFormat.QR_CODE,
                                    size,size);
 
            BufferedImage img =
                    new BufferedImage(size,size,
                            BufferedImage.TYPE_INT_RGB);
 
            for(int x=0;x<size;x++){
                for(int y=0;y<size;y++){
                    img.setRGB(x,y,
                            matrix.get(x,y)
                                    ? 0x000000
                                    : 0xFFFFFF);
                }
            }
 
            return img;
 
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
 