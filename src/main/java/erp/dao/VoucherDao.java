package erp.dao;

import erp.dao.provider.VoucherDaoProvider;
import erp.domain.Voucher;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Yhaobo
 * @date 2020/5/15
 */
@Repository
public interface VoucherDao {
    @InsertProvider(type = VoucherDaoProvider.class, method = "insertByBatchSql")
    void insertByBatch(List<Voucher> vouchers);

    @Insert("insert into voucher(url,d_id) values(#{url},#{d_id})")
    void insert(Voucher voucher);

    @Select("select id, url from voucher where d_id=#{detailId}")
    List<Voucher> listVoucher(Integer detailId);

    @Delete("delete from voucher where id=#{voucherId}")
    void deleteById(Integer voucherId);

    @Select("select url from voucher where id=#{voucherId}")
    String getUrlById(Integer voucherId);

    @Delete("delete from voucher where d_id=#{detailId}")
    void deleteByDetailId(Integer detailId);

    @Select("select url from voucher where d_id=#{detailId}")
    String[] listUrlByDetailId(Integer detailId);
}
