package com.sn.ssh.entity;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import com.sn.ssh.enums.ConstantEnum;
import com.sn.ssh.exception.SshException;
import com.sn.ssh.service.ILineProcessor;
import com.sn.ssh.threadpool.ThreadPool;
import com.sn.ssh.util.Stream;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author sonin
 * @date 2020/10/1 10:45
 */
@Data
@Slf4j
public class SshSession {

    private Connection connection;

    private String address;

    public SshSession(Connection connection, String address) {
        this.connection = connection;
        this.address = address;
    }

    /**
     * 执行命令并返回结果，可以执行多次
     *
     * @param cmd
     * @return 执行成功Result为true，并携带返回信息,返回信息可能为null
     * 执行失败Result为false，并携带失败信息
     * 执行异常Result为false，并携带异常
     */
    public Result executeCommand(String cmd) {
        return executeCommand(cmd, (Integer) ConstantEnum.getValue("ssh", "cmdTimeout"));
    }

    public Result executeCommand(String cmd, int timoutMillis) {
        return executeCommand(cmd, null, timoutMillis);
    }

    public Result executeCommand(String cmd, ILineProcessor iLineProcessor) {
        return executeCommand(cmd, iLineProcessor, ConstantEnum.getValue("ssh", "cmdTimeout"));
    }

    /**
     * 执行命令并返回结果，可以执行多次
     *
     * @param cmd
     * @param iLineProcessor 回调处理行
     * @return 如果lineProcessor不为null, 那么永远返回Result.true
     */
    public Result executeCommand(String cmd, ILineProcessor iLineProcessor, int timeoutMillis) {
        Session session = null;
        try {
            session = connection.openSession();
            return executeCommand(session, cmd, timeoutMillis, iLineProcessor);
        } catch (Exception e) {
            return new Result(e);
        } finally {
            close(session);
        }
    }

    public Result executeCommand(Session session, String cmd, int timeoutMillis, ILineProcessor iLineProcessor) throws Exception {
        Future<Result> future = ThreadPool.getThreadPoolExecutor().submit(() -> {
            session.execCommand(cmd);
            Stream stream = new Stream();
            // 如果客户端需要进行行处理，则直接进行回调
            if (iLineProcessor != null) {
                new Stream().processStream(session.getStdout(), iLineProcessor);
            } else {
                // 获取标准输出
                String stdout = stream.getResult(session.getStdout());
                if (stdout != null) {
                    return new Result(true, stdout);
                }
                String errorInfo = stream.getResult(session.getStderr());
                if (StringUtils.isNotEmpty(errorInfo)) {
                    log.error("tet {}", errorInfo);
                    log.error("address: {} execute cmd: ({}) error", address, cmd);
                    return new Result(false, errorInfo);
                }
            }
            return new Result(true, null);
        });
        Result result;
        try {
            result = future.get(timeoutMillis, TimeUnit.MILLISECONDS);
            future.cancel(true);
        } catch (TimeoutException e) {
            throw new SshException(e);
        }
        return result;
    }

    private void close(Session session) {
        if (session != null) {
            try {
                session.close();
            } catch (Exception e) {
                log.error("close session error: {}", e.getMessage());
            }
        }
    }
}
