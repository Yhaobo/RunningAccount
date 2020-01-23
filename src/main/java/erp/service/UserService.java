package erp.service;

import erp.dao.UserDao;
import erp.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public User findOne(User form) {
        return userDao.findOne(form);
    }
}
