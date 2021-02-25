package erp.web;

import erp.handler.BackupsHandler;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @author Yhaobo
 * @since 2020/10/3
 */
@WebListener
public class SessionListener implements HttpSessionListener{

    @Autowired
    BackupsHandler backupsHandler;

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        backupsHandler.backups();
    }
}
