package erp.service.rbac;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import erp.dao.rbac.UserMapper;
import erp.entity.rbac.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Yhaobo
 * @date 2021-03-05 10:51
 */
@Service
public class UserService {
    @Autowired
    private UserMapper mapper;

    public List<User> listAll() {
        return mapper.selectList(null);
    }

    public User getByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return mapper.selectOne(queryWrapper);
    }

    public void add(User user) {
        mapper.insert(user);
    }

    public boolean update(User user) {
        return mapper.updateById(user) == 1;
    }

    public boolean delete(String userId) {
        return mapper.deleteById(userId) == 1;
    }

    public User getById(String id) {
        return mapper.selectById(id);
    }

    public int countByGroupId(String groupId) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = Wrappers.lambdaQuery(User.class);
        userLambdaQueryWrapper.eq(User::getGroupId, groupId);
        return mapper.selectCount(userLambdaQueryWrapper);
    }
}
