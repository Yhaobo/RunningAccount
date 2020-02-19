package erp.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * 当服务器关闭时, 自动备份所有财务数据
 */
public class BackupsListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 数据库导出
        InputStream in = null;
        FileOutputStream out = null;
        Properties properties = new Properties();
        try {
            properties.load(this.getClass().getResourceAsStream("/druid.properties"));
            // 数据库帐号
            String user = properties.getProperty("jdbc.username");
            // 数据库密码
            String password = properties.getProperty("jdbc.password");
            // 需要备份的数据库名
            String database = "erp";
            // 生成的文件名
            String fileName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            // 需要备份到的路径
            String filepath = "D:\\cai_wu_shu_jv\\" + fileName + ".sql";

            String cmd = "cmd /c mysqldump -u" + user + " -p" + password + " " + database;

            Process process = Runtime.getRuntime().exec(cmd);
            in = process.getInputStream();
            out = new FileOutputStream(new File(filepath));
            byte[] data = new byte[1024 * 4];
            int len;
            while ((len = in.read(data)) != -1) {
                out.write(data, 0, len);
            }
            System.out.println("数据已备份到文件 " + filepath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
