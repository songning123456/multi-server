package com.sn.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author songning
 * @date 2020/5/12
 * description
 */
@Slf4j
public class ReadTxtUtil {

    public static List<Map<String, Object>> readTxt(String filePath) {
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String lineTxt;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    String[] arrStrings = lineTxt.split("\t");
                    Map<String, Object> map = new HashMap<>(2);
                    map.put("codeItem", arrStrings[0]);
                    map.put("codeValue", arrStrings[1]);
                    map.put("codeType", "违法行为");
                    result.add(map);
                }
                bufferedReader.close();
            } else {
                log.error("~~~文件不存在!~~~");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("解析txt 失败: {}", e.getMessage());
        }
        return result;
    }

    public static void main(String[] args) {
        String filePath = "D:\\haiyan-data\\violationDir\\violation.txt";
        List<Map<String, Object>> result = ReadTxtUtil.readTxt(filePath);
        System.out.println("");
    }
}
