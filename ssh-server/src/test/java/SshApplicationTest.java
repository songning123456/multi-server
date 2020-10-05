import com.sn.ssh.SshApplication;
import com.sn.ssh.command.SshTemplate;
import com.sn.ssh.entity.Result;
import com.sn.ssh.entity.SshSession;
import com.sn.ssh.service.ISshCallback;
import com.sn.ssh.sshql.ParserParameter;
import com.sn.ssh.sshql.SshqlParser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sonin
 * @date 2020/10/2 17:29
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SshApplication.class)
public class SshApplicationTest {

    @Autowired
    private SshTemplate sshTemplate;

    @Autowired
    private SshqlParser sshqlParser;

    @Test
    public void testSshql() {
        Map<String, Object> params = new HashMap<>(4);
        params.put("dir", "testSshDir");
        String sshql = sshqlParser.parse(new ParserParameter("testSshql.test1", params)).getExecutableSshql();
        log.info("sshql: {}", sshql);
    }

    @Test
    public void testSsh() throws Exception {
        String ip = "192.168.88.130";
        int port = 22;
        String username = "sonin";
        String password = "772805406sn123.";

        sshTemplate.execute(ip, port, username, password, new ISshCallback() {
            @Override
            public Result call(SshSession sshSession) {
                return sshSession.executeCommand("ls");
            }
        });
    }
}
