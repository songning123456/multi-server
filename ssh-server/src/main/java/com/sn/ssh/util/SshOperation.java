package com.sn.ssh.util;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import com.sn.ssh.entity.Result;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sonin
 * @date 2020/10/2 9:58
 */
@Slf4j
public class SshOperation {

    private Connection connection;

    public SshOperation(Connection connection) {
        this.connection = connection;
    }

    public Result scp(String[] localFiles, String[] remoteFiles, String remoteTargetDirectory, String mode) {
        try {
            SCPClient scpClient = connection.createSCPClient();
            scpClient.put(localFiles, remoteFiles, remoteTargetDirectory, mode);
            return new Result(true);
        } catch (Exception e) {
            log.error("scp fail: {}", e.getMessage());
            return new Result(e);
        }
    }

    public Result scpToDir(String localFile, String remoteTargetDirectory) {
        return scpToDir(localFile, remoteTargetDirectory, "0744");
    }

    public Result scpToDir(String localFile, String remoteTargetDirectory, String mode) {
        return scp(new String[]{localFile}, null, remoteTargetDirectory, mode);
    }

    public Result scpToDir(String[] localFile, String remoteTargetDirectory) {
        return scp(localFile, null, remoteTargetDirectory, "0744");
    }

    public Result scpToFile(String localFile, String remoteFile, String remoteTargetDirectory) {
        return scpToFile(localFile, remoteFile, remoteTargetDirectory, "0744");
    }

    public Result scpToFile(String localFile, String remoteFile, String remoteTargetDirectory, String mode) {
        return scp(new String[]{localFile}, new String[]{remoteFile}, remoteTargetDirectory, "0744");
    }
}
