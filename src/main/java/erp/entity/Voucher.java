package erp.entity;

import lombok.Data;

/**
 * @author Yhaobo
 * @date 2020/5/15
 */
@Data
public class Voucher {
    private Integer id;
    private String uri;
    private Integer detailId;

    public Voucher() {
    }

    public Voucher(String uri, Integer detailId) {
        this.uri = uri;
        this.detailId = detailId;
    }
}
