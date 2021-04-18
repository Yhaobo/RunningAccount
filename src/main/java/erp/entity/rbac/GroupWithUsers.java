package erp.entity.rbac;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author Yhaobo
 * @date 2021-03-06 13:58
 */
@Data
@AllArgsConstructor
public class GroupWithUsers {
    private String id;
    private String name;
    private String description;
    private List<GroupWithUsers> children;
}
