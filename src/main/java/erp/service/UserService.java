package erp.service;

import erp.dao.UserDao;
import erp.domain.User;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public User findOne(User form) {
        return userDao.findOne(form);
    }

    public User findByUsername(String username) {
        User user = userDao.findUsername(username);
        return user;
    }

    public void updateByUser(User user) {
        // 密码加密
        SimpleHash result = new SimpleHash("md5", user.getU_password(), user.getU_username(), 7);
        user.setU_password(result.toString());
        userDao.updateByUser(user);
    }
}
