package erp.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 备份数据库的数据
 */
@Slf4j
@Component
public class BackupsHandler {
    // 数据库帐号
    @Value("${spring.datasource.username}")
    private String username;
    // 数据库密码
    @Value("${spring.datasource.password}")
    private String password;
    // 需要备份的数据库名
    @Value("${erp.datasource.database}")
    private String database;
    @Value("${erp.home.location}")
    private String location;

    public synchronized void backups() {
        // 数据库导出
        InputStream in = null;
        FileOutputStream out = null;
        try {
            //cmd命令拼接
            String cmd = "mysqldump -u" + username + " -p" + password + " " + database;
            //执行命令
            Process process;
            try {
                process = Runtime.getRuntime().exec(cmd);
            } catch (IOException e) {
                log.error("请先配置mysql服务的环境变量，否则无法自动备份数据", e);
                return;
            }

            // 生成的文件名
            String fileName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            // 需要备份到的路径
            String filepath = location + "sql\\" + fileName + ".sql";

            File file = new File(filepath);
            // 自动创建父目录
            file.getParentFile().mkdirs();

            in = process.getInputStream();
            out = new FileOutputStream(file);
            byte[] data = new byte[1024 * 4];
            int len;
            while ((len = in.read(data)) != -1) {
                out.write(data, 0, len);
            }
            System.out.println(LocalDateTime.now() + "\t数据已备份:" + filepath);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }
}
