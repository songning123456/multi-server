package com.sn.ssh.sshql;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * @author sonin
 * @date 2020/10/05
 */
@Slf4j
public class ParsedSshql extends Sshql {

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

    public String getExecutableSshql() {
        String sshqlStr = this.getParsed();
        try {
            Map<String, Object> params = this.getParameterMap();
            for (int i = params.keySet().size() - 1; i >= 0; i--) {
                String key = "argument" + i;
                Object obj = params.get(key);
                if (obj instanceof Date) {
                    sshqlStr = sshqlStr.replaceAll(":" + key, "\'" + toDateTimeStr((Date) obj) + "\'");
                } else if (obj instanceof String) {
                    sshqlStr = sshqlStr.replaceAll(":" + key, "\'" + obj + "\'");
                } else if (obj instanceof Collection) {
                    StringBuilder list = new StringBuilder();
                    if (((Collection) obj).size() > 0) {
                        if (((Collection) obj).iterator().next() instanceof String) {
                            for (String o : ((Collection<String>) obj)) {
                                list.append("\'" + o + "\',");
                            }
                            list.deleteCharAt(list.length() - 1);
                        } else {
                            list.append(obj.toString(), 1, obj.toString().length() - 1);
                        }
                    }
                    sshqlStr = sshqlStr.replaceAll(":" + key, list.toString());
                } else {
                    sshqlStr = sshqlStr.replaceAll(":" + key, obj.toString());
                }
            }
            if (sshqlStr.contains("like") && (sshqlStr.contains(".") || sshqlStr.contains("*"))) {
                this.setParsed(sshqlStr);
            }
            log.info("{\"ModuleId\": \"{}\"," + "\"Sshql\": \"{}\"}", this.getId(), sshqlStr.replaceAll("\\s+", " "));
        } catch (Exception e) {
            log.error("Sshql转换失败：" + e.getMessage());
        }
        return sshqlStr.replaceAll("\\s+", " ");
    }
}
