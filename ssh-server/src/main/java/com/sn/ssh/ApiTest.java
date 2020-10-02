package com.sn.ssh;

import com.sn.ssh.command.SshTemplate;
import com.sn.ssh.entity.Result;
import com.sn.ssh.entity.SshSession;
import com.sn.ssh.service.ISshCallback;
import com.sn.ssh.threadpool.ThreadPool;

/**
 * @author sonin
 * @date 2020/10/1 12:55
 */
public class ApiTest {

    public static void main(String[] args) throws Exception {
        ThreadPool threadPool = new ThreadPool();
        SshTemplate sshTemplate = new SshTemplate();
        sshTemplate.execute("192.168.88.128", 22, "sonin", "772805406sn123.", new ISshCallback() {
            @Override
            public Result call(SshSession sshSession) {
                return sshSession.executeCommand("ls");
            }
        });
    }
}
