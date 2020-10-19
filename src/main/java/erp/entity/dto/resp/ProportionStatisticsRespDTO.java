package erp.entity.dto.resp;

import lombok.Data;

/**
 * @author Yhaobo
 * @since 2020/10/16
 */
@Data
public class ProportionStatisticsRespDTO {
    private String name;
    private String value;

    public ProportionStatisticsRespDTO() {
    }

    public ProportionStatisticsRespDTO(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
