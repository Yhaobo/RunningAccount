package erp.entity;

import lombok.Data;

/**
 * @author Yhaobo
 * @date 2020/5/15
 */
@Data
public class Voucher {
    private Integer id;
    private String url;
    private Integer d_id;

    public Voucher() {
    }

    public Voucher(String filePath, Integer id) {
        url = filePath;
        d_id = id;
    }
}
