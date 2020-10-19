package erp.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * 重新封装了 Page, 为了使此对象转换为json字符串时, 自定义有哪些属性
 *
 * @author Yhaobo
 * @since 2020/9/24
 */
@JsonIgnoreProperties({"optimizeCountSql", "hitCount", "searchCount", "maxLimit", "countId", "orders",
        "records", "current", "size"})
public class MyPage<T> extends Page<T> {

    public MyPage(long current, long size) {
        super(current, size);
    }

    public MyPage(long current, long size, long total) {
        super(current, size, total);
    }

    public MyPage(long current, long size, boolean isSearchCount) {
        super(current, size, isSearchCount);
    }

    public MyPage(long current, long size, long total, boolean isSearchCount) {
        super(current, size, total, isSearchCount);
    }

    public MyPage() {
    }

    /**
     * 是否有下一页
     */
    public Boolean getHasNext() {
        return super.hasNext();
    }

    /**
     * 是否有上一页
     */
    public Boolean getHasPrevious() {
        return super.hasPrevious();
    }

    public List<T> getList() {
        return super.getRecords();
    }

    public long getCurrentPage() {
        return super.getCurrent();
    }

    public long getPageSize() {
        return super.getSize();
    }
}
