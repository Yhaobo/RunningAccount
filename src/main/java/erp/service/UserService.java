package erp.service;

import erp.dao.UserDao;
import erp.domain.User;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @Transactional(readOnly = true)
    public User findOneByUser(User form) {
        return userDao.getByUser(form);
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userDao.getByUsername(username);
    }

    public void updateByUser(User user) {
        // 密码加密
        SimpleHash result = new SimpleHash("md5", user.getU_password(), user.getU_username(), 7);
        user.setU_password(result.toString());
        userDao.updateByUser(user);
    }

    @Transactional(readOnly = true)
    public List<String> listUsername() {
        return userDao.listUsername();
    }

    @Transactional(readOnly = true)
    public Integer getLevelByUsername(String username) {
        return userDao.getLevelByUsername(username);
    }

    @Transactional(readOnly = true)
    public String getUsernameByLevel(Integer level) {
        return userDao.getUsernameByLevel(level);
    }
}
