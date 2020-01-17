package com.sn.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 命令行执行工具类
 *
 * @author songning
 * @date 2019/11/14
 * description
 */
public class CommandUtil {
    /**
     * 执行控制台命令
     *
     * @param command
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static String execute(String command) throws IOException, InterruptedException {
        String result;
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(command);
        StreamCaptureThread errorStream = new StreamCaptureThread(process.getErrorStream());
        StreamCaptureThread outputStream = new StreamCaptureThread(process.getInputStream());
        new Thread(errorStream).start();
        new Thread(outputStream).start();
        process.waitFor();

        result = command + "\n" + outputStream.output.toString() + errorStream.output.toString();
        return result;
    }

    private static class StreamCaptureThread implements Runnable {
        InputStream inputStream;
        StringBuilder output;

        public StreamCaptureThread(InputStream inputStream) {
            this.inputStream = inputStream;
            this.output = new StringBuilder();
        }

        //TODO:考虑死锁问题
        @Override
        public void run() {
            try {
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(this.inputStream, "UTF-8"));
                    String line = br.readLine();
                    while (line != null) {
                        if (line.trim().length() > 0) {
                            output.append(line).append("\n");
                        }
                        line = br.readLine();
                    }
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
