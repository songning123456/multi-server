package com.sn.jpql;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * 解析后的Jpql信息
 *
 * @author gehoubao
 * @create 2020-01-18 11:01
 **/
@Slf4j
public class ParsedJpql extends Jpql {
    private String parsed;
    private Map<String, Object> parameterMap;

    private static String toDateTimeStr(Date date) {
        if (date == null) {
            throw new NullPointerException("Please provide a valid Date.");
        }

        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public Map<String, Object> getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(Map<String, Object> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public String getParsed() {
        return parsed;
    }

    public void setParsed(String parsed) {
        this.parsed = parsed;
    }

    public String getExecutableSql() {
        String jpqlStr = this.getParsed();
        try {
            Map<String, Object> params = this.getParameterMap();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String key = entry.getKey();
                Object obj = entry.getValue();
                if (obj instanceof Date) {
                    jpqlStr = jpqlStr.replaceAll(":" + key, "\'" + toDateTimeStr((Date) obj) + "\'");
                } else if (obj instanceof String) {
                    jpqlStr = jpqlStr.replaceAll(":" + key, "\'" + obj + "\'");
                } else if (obj instanceof Collection) {
                    StringBuilder list = new StringBuilder();
                    if (((Collection) obj).size() > 0) {
                        if (((Collection) obj).iterator().next() instanceof String) {
                            for (String o : ((Collection<String>) obj)) {
                                list.append("\'").append(o).append("\',");
                            }
                            list.deleteCharAt(list.length() - 1);
                        } else {
                            list.append(obj.toString(), 1, obj.toString().length() - 1);
                        }
                    }
                    jpqlStr = jpqlStr.replaceAll(":" + key, list.toString());
                } else {
                    jpqlStr = jpqlStr.replaceAll(":" + key, obj.toString());
                }
            }
            if (jpqlStr.contains("like") && (jpqlStr.contains(".") || jpqlStr.contains("*"))) {
                this.setParsed(jpqlStr);
            }
            log.info("{\"QueryId\": \"{}\"," + "\"SQL\": \"{}\"}", this.getId(), jpqlStr.replaceAll("\\s+", " "));
        } catch (Exception e) {
            log.error("SQL转换失败：" + e.getMessage());
        }
        return jpqlStr.replaceAll("\\s+", " ");
    }
}