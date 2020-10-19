package erp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author Yhaobo
 * @date 2020/5/15
 */
@Data
public class Picture {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String uri;
    private Integer detailId;

    public Picture() {
    }

    public Picture(String uri, Integer detailId) {
        this.uri = uri;
        this.detailId = detailId;
    }
}
