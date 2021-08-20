package com.example.contactbook.utils;
import org.imgscalr.Scalr;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageProcessing {

    public static ByteArrayOutputStream createThumbnail(MultipartFile orginalFile, Integer width) throws IOException {
        ByteArrayOutputStream thumbOutput = new ByteArrayOutputStream();
        BufferedImage img = ImageIO.read(orginalFile.getInputStream());
        BufferedImage thumbImg = Scalr.resize(img, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, width, Scalr.OP_ANTIALIAS);
        ImageIO.write(thumbImg, orginalFile.getContentType().split("/")[1] , thumbOutput);
        return thumbOutput;
    }

    public static ByteArrayOutputStream createThumbnail(byte[] bytes, Integer width, String contentType) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(bytes);
        ByteArrayOutputStream thumbOutput = new ByteArrayOutputStream();
        BufferedImage img = ImageIO.read(inputStream);
        BufferedImage thumbImg  = Scalr.resize(img, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, width, Scalr.OP_ANTIALIAS);
        ImageIO.write(thumbImg, contentType.split("/")[1] , thumbOutput);
        return thumbOutput;
    }

    public static ByteArrayOutputStream convertToPng(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedImage img = ImageIO.read(inputStream);
        ImageIO.write(img, "png", outputStream);
        return outputStream;
    }
}
