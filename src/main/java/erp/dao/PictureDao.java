package erp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import erp.entity.Picture;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author Yhaobo
 * @date 2020/5/15
 */
@Repository
public interface PictureDao extends BaseMapper<Picture> {
    @Select("select id, uri,detail_id detailId from picture where detail_id=#{detailId}")
    List<Picture> list(Integer detailId);

    @Delete("delete from picture where id=#{pictureId}")
    void deleteById(Integer pictureId);

    @Select("select uri from picture where id=#{pictureId}")
    String getUrlById(Integer pictureId);

    @Delete("delete from picture where detail_id=#{detailId}")
    void deleteByDetailId(Integer detailId);

    @Select("select uri from picture where detail_id=#{detailId}")
    String[] listUriByDetailId(Integer detailId);

    /**
     * 返回拥有凭证的记录的id集合
     */
    @Select("SELECT detail_id FROM picture ")
    Set<Integer> listDetailIds();
}
