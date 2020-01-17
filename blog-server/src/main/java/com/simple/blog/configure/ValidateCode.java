package com.simple.blog.configure;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Random;

/**
 * 生成随机验证码
 *
 * @author songning
 * @date 2019/9/17
 * description
 */
public class ValidateCode {
    private static Random random = new Random();
    /**
     * 宽
     */
    private int width = 160;
    /**
     * 高
     */
    private int height = 40;
    /**
     * 干扰线数量
     */
    private int lineSize = 30;
    /**
     * 随机产生字符数量
     */
    private int stringNum = 4;
    /**
     * 随机数范围
     */
    private String randomString = "0123456789abcdefghijklmnopqrstuvwxyz";
    private final String sessionKey = "RANDOMKEY";

    /**
     * 设置字体
     *
     * @return
     */
    private Font getFont() {
        return new Font("Times New Roman", Font.ROMAN_BASELINE, 40);
    }

    /**
     * 获取随机颜色
     *
     * @param fc
     * @param bc
     * @return
     */
    private static Color getRandomColor(int fc, int bc) {
        fc = Math.min(fc, 255);
        bc = Math.min(bc, 255);

        int r = fc + random.nextInt(bc - fc - 16);
        int g = fc + random.nextInt(bc - fc - 14);
        int b = fc + random.nextInt(bc - fc - 12);

        return new Color(r, g, b);
    }

    /**
     * 绘制干扰线
     *
     * @param graphics
     */
    private void drawLine(Graphics graphics) {
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        int x1 = random.nextInt(20);
        int y1 = random.nextInt(10);
        graphics.drawLine(x, y, x + x1, y + y1);
    }

    /**
     * 获取随机字符
     *
     * @param num
     * @return
     */
    private String getRandomString(int num) {
        num = num > 0 ? num : randomString.length();
        return String.valueOf(randomString.charAt(random.nextInt(num)));
    }

    /**
     * 绘制字符串
     *
     * @param graphics
     * @param randomStr
     * @param i
     * @return
     */
    private String drawString(Graphics graphics, String randomStr, int i) {
        graphics.setFont(getFont());
        graphics.setColor(getRandomColor(108, 190));
        String rand = getRandomString(random.nextInt(randomString.length()));
        randomStr += rand;
        graphics.translate(random.nextInt(3), random.nextInt(6));
        graphics.drawString(rand, 40 * i + 10, 25);
        return randomStr;
    }

    /**
     * 生成随机图片
     *
     * @param request
     * @param response
     */
    public void getRandomCodeImage(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        // BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics graphics = image.getGraphics();
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(getRandomColor(105, 189));
        graphics.setFont(getFont());

        // 绘制干扰线
        for (int i = 0; i < lineSize; i++) {
            drawLine(graphics);
        }
        // 绘制随机字符
        String randomString = "";
        for (int i = 0; i < stringNum; i++) {
            randomString = drawString(graphics, randomString, i);
        }
        graphics.dispose();
        session.removeAttribute(sessionKey);
        session.setAttribute(sessionKey, randomString);
        String base64String = "";
        try {
            //  直接返回图片
            ImageIO.write(image, "PNG", response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回base64
     *
     * @param request
     * @param response
     * @return
     */
    public String getRandomCodeBase64(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        // BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics graphics = image.getGraphics();
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(getRandomColor(105, 189));
        graphics.setFont(getFont());
        // 绘制干扰线
        for (int i = 0; i < lineSize; i++) {
            drawLine(graphics);
        }
        // 绘制随机字符
        String randomString = "";
        for (int i = 0; i < stringNum; i++) {
            randomString = drawString(graphics, randomString, i);
        }
        graphics.dispose();
        session.removeAttribute(sessionKey);
        session.setAttribute(sessionKey, randomString);
        String base64String = "";
        try {
            //返回 base64
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", bos);
            byte[] bytes = bos.toByteArray();
            Base64.Encoder encoder = Base64.getEncoder();
            base64String = encoder.encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64String;
    }
}
