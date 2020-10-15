package erp.service;

import erp.dao.UserDao;
import erp.entity.User;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @Value("${erp.password.hashIterations}")
    private int hashIterations;
    @Value("${erp.password.algorithmName}")
    private String algorithmName;

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userDao.getByUsername(username);
    }

    public void updateByUser(User user) {
        // 密码加密
        SimpleHash result = new SimpleHash(algorithmName, user.getPassword(), user.getUsername(), hashIterations);
        user.setPassword(result.toString());
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
    public List<User> getUsernameByLevel(Integer level) {
        return userDao.getUsernameByLevel(level);
    }

    public void insert(User user) {
        userDao.insert(user);
    }

    public void delete(User user) {
        userDao.delete(user.getUsername());
    }
}
