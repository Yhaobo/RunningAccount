package erp.entity;

import lombok.Data;

/**
 * @author Yhaobo
 * @since 2020/10/13
 */
@Data
public class Option {
    protected Integer id;
    protected String name;

    /**
     * 为了导出 EXCEL 中设置值的方便
     * @return
     */
    @Override
    public String toString() {
        return name.toString();
    }
}
