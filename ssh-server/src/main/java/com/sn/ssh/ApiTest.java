package com.sn.ssh;

import com.sn.ssh.command.SshTemplate;
import com.sn.ssh.entity.Result;
import com.sn.ssh.entity.SshSession;
import com.sn.ssh.service.ISshCallback;
import com.sn.ssh.service.impl.AbstractDefaultLineProcessor;
import com.sn.ssh.threadpool.ThreadPool;

/**
 * @author sonin
 * @date 2020/10/1 12:55
 */
public class ApiTest {

    public static void main(String[] args) throws Exception {
        ThreadPool threadPool = new ThreadPool();
        SshTemplate sshTemplate = new SshTemplate();
        String ip = "192.168.88.128";
        int port = 22;
        String username = "sonin";
        String password = "772805406sn123.";

        sshTemplate.execute(ip, port, username, password, new ISshCallback() {
            @Override
            public Result call(SshSession sshSession) {
                return sshSession.executeCommand("mkdir testb");
            }
        });
//        sshTemplate.execute(ip, port, username, password, new ISshCallback() {
//            @Override
//            public Result call(SshSession sshSession) {
//                sshSession.executeCommand("df -lh", new AbstractDefaultLineProcessor() {
//                    @Override
//                    public void process(String line, int lineNum) throws Exception {
//                        System.out.println(line);
//                    }
//                });
//                return null;
//            }
//        });
    }
}
