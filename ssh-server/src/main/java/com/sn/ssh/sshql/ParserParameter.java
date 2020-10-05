package com.sn.ssh.sshql;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * @author sonin
 * @date 2020/10/05
 */
@Data
@AllArgsConstructor
public class ParserParameter {
    private String id;
    private Map<String, Object> parameter;
}