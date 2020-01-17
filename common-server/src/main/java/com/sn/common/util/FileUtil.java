package com.sn.common.util;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author songning
 * @create 2019/7/26 13:37
 */
@Slf4j
public class FileUtil {

    public static String readToString(String fileName) throws IOException {
        String encoding = "UTF-8";
        File file = new File(fileName);
        long fileLength = file.length();
        byte[] fileContent = new byte[(int) fileLength];
        try (FileInputStream in = new FileInputStream(file)) {
            in.read(fileContent);
            return new String(fileContent, encoding);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将数据追加写入到文件文件的末尾
     *
     * @param dataList
     * @param fileName
     */
    public static void appendDataToFile(List<String> dataList, String fileName) {
        OutputStreamWriter outputStreamWriter = null;
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(fileName);
            if (!file.getParentFile().isDirectory()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(fileName, true);
            outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
            for (int i = 0; i < dataList.size(); i++) {
                outputStreamWriter.write(dataList.get(i));
                outputStreamWriter.write("\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStreamWriter != null) {
                    outputStreamWriter.flush();
                    outputStreamWriter.close();
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static long getFileSize(File file) {
        FileChannel fileChannel = null;
        try {
            if (file.exists() && file.isFile()) {
                FileInputStream fileInputStream = new FileInputStream(file);
                fileChannel = fileInputStream.getChannel();
                return fileChannel.size();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileChannel != null) {
                try {
                    fileChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    /**
     * 读取excel中的内容
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static List readExcel(File file) throws Exception {
        try {
            // 创建输入流，读取Excel
            InputStream is = new FileInputStream(file.getAbsolutePath());
            // jxl提供的Workbook类
            Workbook wb = Workbook.getWorkbook(is);
            // Excel的页签数量
            int sheet_size = wb.getNumberOfSheets();
            for (int index = 0; index < sheet_size; index++) {
                List<List> outerList = new ArrayList<List>();
                // 每个页签创建一个Sheet对象
                Sheet sheet = wb.getSheet(index);
                // sheet.getRows()返回该页的总行数
                for (int i = 0; i < sheet.getRows(); i++) {
                    List innerList = new ArrayList();
                    // sheet.getColumns()返回该页的总列数
                    for (int j = 0; j < sheet.getColumns(); j++) {
                        String cellinfo = sheet.getCell(j, i).getContents();
                        if (cellinfo.isEmpty()) {
                            continue;
                        }
                        innerList.add(cellinfo);
                    }
                    outerList.add(i, innerList);
                    System.out.println();
                }
                return outerList;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取txt文本内容
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static List readTxt(File file) throws Exception {
        List<String> result = new ArrayList<>();
        if (file.isFile() && file.exists()) {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                result.add(lineTxt);
            }
            bufferedReader.close();
        } else {
            log.info("文件不存在!");
        }
        return result;
    }

    /**
     * 根据条件读取
     *
     * @param file
     * @param condition
     * @return
     * @throws Exception
     */
    public static List<String> readTxtByCondition(File file, String condition) throws Exception {
        List<String> result = new ArrayList<>();
        if (file.isFile() && file.exists()) {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                if (lineTxt.contains(condition)) {
                    String[] temps = lineTxt.split("\"");
                    result.add(temps[3]);
                }
            }
            bufferedReader.close();
        } else {
            log.info("文件不存在!");
        }
        return result;
    }

    /**
     * 创建文件夹
     *
     * @param path
     * @return
     */
    public static Boolean mkDirs(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return file.mkdirs();
        }
        return true;
    }

    /**
     * 将inputStream 流转换成 图片文件
     *
     * @param imageSrc
     * @param inputStream
     * @throws IOException
     */
    public static void streamToImage(String imageSrc, InputStream inputStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        // 创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        // 每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        // 使用一个输入流从buffer里把数据读取出来
        while ((len = inputStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inputStream.close();
        //把outStream里的数据写入内存
        //得到图片的二进制数据，以二进制封装得到数据，具有通用性
        byte[] data = outStream.toByteArray();
        //new一个文件对象用来保存图片，默认保存当前工程根目录
        File imageFile = new File(imageSrc);
        //创建输出流
        FileOutputStream fileOutStream = new FileOutputStream(imageFile);
        //写入数据
        fileOutStream.write(data);
        outStream.close();
        fileOutStream.close();
    }

    /**
     * e.g: C:\Users\songning\simple-blog\avatar\b0f3446e-ba9f-4d41-9611-e83ce677626d.png
     *
     * @param filename 路径 + 文件名
     */
    public static void deleteImage(String filename) {
        File file = new File(filename);
        file.delete();
    }

    /**
     * 获取项目路径
     *
     * @return
     * @throws IOException
     */
    public static String getProjectPath() throws IOException {
        File file = new File("");
        String filePath = file.getCanonicalPath();
        return filePath;
    }
}
