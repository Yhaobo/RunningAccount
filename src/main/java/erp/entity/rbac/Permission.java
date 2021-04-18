package erp.entity.rbac;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Yhaobo
 * @date 2021-03-04 10:49
 */
@Data
@TableName(value = "rbac_permission")
public class Permission {
    @TableId(type = IdType.AUTO)
    private String id;
    private String perm;
    private String name;
}
