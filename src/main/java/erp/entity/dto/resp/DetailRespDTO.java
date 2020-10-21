package erp.entity.dto.resp;

import erp.entity.Detail;
import lombok.Data;

/**
 * @author Yhaobo
 * @date 2020/5/16
 */
@Data
public class DetailRespDTO extends Detail {
    private Boolean hasPicture;
}
