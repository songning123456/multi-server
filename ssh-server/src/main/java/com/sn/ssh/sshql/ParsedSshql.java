package com.sn.ssh.sshql;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author sonin
 * @date 2020/10/05
 */
@Slf4j
public class ParsedSshql extends Sshql {

    private String parsed;
    private Map<String, Object> parameterMap;

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
                sshqlStr = sshqlStr.replaceAll(":" + key, String.valueOf(obj));
            }
            log.info("{\"ModuleId\": \"{}\"," + "\"Sshql\": \"{}\"}", this.getId(), sshqlStr.replaceAll("\\s+", " "));
        } catch (Exception e) {
            log.error("Sshql转换失败：" + e.getMessage());
        }
        return sshqlStr.replaceAll("\\s+", " ");
    }
}
