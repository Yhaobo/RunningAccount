package erp.dao;

import erp.entity.Voucher;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Yhaobo
 * @date 2020/5/15
 */
@Repository
public interface VoucherDao {

    @Insert("insert into voucher(uri,detail_id) values(#{uri},#{detailId})")
    void insert(Voucher voucher);

    @Select("select id, uri,detail_id detailId from voucher where detail_id=#{detailId}")
    List<Voucher> listVoucher(Integer detailId);

    @Delete("delete from voucher where id=#{voucherId}")
    void deleteById(Integer voucherId);

    @Select("select uri from voucher where id=#{voucherId}")
    String getUrlById(Integer voucherId);

    @Delete("delete from voucher where detail_id=#{detailId}")
    void deleteByDetailId(Integer detailId);

    @Select("select uri from voucher where detail_id=#{detailId}")
    String[] listUriByDetailId(Integer detailId);
}
