package com.example.contactbook.utils;
import org.apache.commons.io.IOUtils;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;

@Service
public class ImageProcessingService implements HasLogger {

    public ByteArrayOutputStream createThumbnail(MultipartFile orginalFile, Integer width) throws IOException {
        ByteArrayOutputStream thumbOutput = new ByteArrayOutputStream();
        BufferedImage img = ImageIO.read(orginalFile.getInputStream());
        BufferedImage thumbImg = Scalr.resize(img, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, width, Scalr.OP_ANTIALIAS);
        ImageIO.write(thumbImg, orginalFile.getContentType().split("/")[1] , thumbOutput);
        return thumbOutput;
    }

    public ByteArrayOutputStream createThumbnail(byte[] bytes, Integer width, String contentType) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(bytes);
        ByteArrayOutputStream thumbOutput = new ByteArrayOutputStream();
        BufferedImage img = ImageIO.read(inputStream);
        BufferedImage thumbImg  = Scalr.resize(img, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, width, Scalr.OP_ANTIALIAS);
        ImageIO.write(thumbImg, contentType.split("/")[1] , thumbOutput);
        return thumbOutput;
    }

    public ByteArrayOutputStream convertToPng(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedImage img = ImageIO.read(inputStream);
        ImageIO.write(img, "png", outputStream);
        return outputStream;
    }

    /**
     * Resize a BufferedImage
     * @param originalImage the image to be resized as a BufferedImage
     * @param targetWidth the target with in pixels
     * @return the resized image as a BufferedImage
     */
    public BufferedImage simpleResizeImage(BufferedImage originalImage, int targetWidth)  {
        return Scalr.resize(originalImage, targetWidth);
    }

    /**
     * Resize a BufferedImage
     * @param originalImage the image to be resized as a BufferedImage

     * @return the resized image as a BufferedImage
     */
    public BufferedImage simpleCropImageToSquare(BufferedImage originalImage)  {

        if (originalImage.getWidth() > originalImage.getHeight()) {
            int x = (originalImage.getWidth() - originalImage.getHeight()) / 2;
            return Scalr.crop(originalImage, x, 0, originalImage.getHeight(), originalImage.getHeight());
        } else {
            int y = (originalImage.getHeight() - originalImage.getWidth()) * 10 / 50;

            return Scalr.crop(originalImage, 0, y, originalImage.getWidth(), originalImage.getWidth());
        }
    }

    /**
     * Resize a Byte Array
     * @param originalImage the image to be resized as a byte array
     * @param targetWidth the target with in pixels
     * @return the resized image in bytes array
     * @throws IOException the exception is through if there is an error at converting the bytes array to an Input Stream
     */
    public  byte[] simpleResizeImage(byte[]  originalImage, int targetWidth)  {
        return bufferedImageToBytes(Scalr.resize(bytesToBufferedImage(originalImage), targetWidth));
    }

    public  byte[] resizeAndCrop(byte[]  originalImage, int targetWidth)  {
        return bufferedImageToBytes(  Scalr.resize(simpleCropImageToSquare(bytesToBufferedImage(originalImage)), targetWidth));
    }

    /**
     * Read file into byte array
     *
     * @param imagePath path to a file
     * @return byte array out of file
     * @throws IOException File not found or could not be read
     */
    public  byte[] getBytesFromFile(String imagePath) throws IOException {
        File file = new File(imagePath);
        return Files.readAllBytes(file.toPath());
    }

    /**
     * Read file into byte array
     *
     * @param resourcePath path to a file
     * @return byte array out of file
     * @throws IOException File not found or could not be read
     */
    public  byte[] getBytesFromResource(Class<?> clazz, String resourcePath) throws IOException, URISyntaxException {

        InputStream in = clazz.getResourceAsStream("/" + resourcePath);
        if (in == null) {
            throw new IllegalArgumentException("Resource not found! " + resourcePath);
        } else {
            return IOUtils.toByteArray(in);
        }
    }


    public  BufferedImage bytesToBufferedImage(byte[] imageArray) {
        ByteArrayInputStream in = new ByteArrayInputStream(imageArray);
        BufferedImage img = null;
        try {
            img = ImageIO.read(in);
        } catch (IOException e) {
            getLogger().info("IOException in bytesToBufferedImage");
        }
        return img;
    }

    public  byte[] bufferedImageToBytes(BufferedImage image)  {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", buffer);
        } catch (IOException e) {
            getLogger().info("Error at the conversion to byte[] from buffered image");
        }
        return buffer.toByteArray();
    }

}
