package com.sn.ssh.service;

import com.sn.ssh.entity.Result;
import com.sn.ssh.entity.SshSession;

/**
 * @author sonin
 * @date 2020/10/1 10:44
 */
public interface ISshCallback {

    /**
     * 执行命令回调
     *
     * @param sshSession
     * @return
     */
    Result call(SshSession sshSession);
}
