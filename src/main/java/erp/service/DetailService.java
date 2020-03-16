package erp.service;

import erp.dao.DetailDao;
import erp.domain.Detail;
import erp.util.MyException;
import erp.vo.req.DetailFilterVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class DetailService {

    private DetailDao detailDao;

    public DetailService(DetailDao detailDao) {
        this.detailDao = detailDao;
    }

    public List<Detail> findAll(DetailFilterVo vo) {
        List<Detail> details = detailDao.listByFilter(vo);
        //格式化所有数字
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.CHINA);
        for (Detail i : details) {
            i.setFormatEarning(format.format(i.getEarning()));
            i.setFormatExpense(format.format(i.getExpense()));
            i.setFormatBalance(format.format(i.getBalance()));
        }
        return details;
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(Detail form) {

        //处理null值
        if (form.getEarning() == null) {
            form.setEarning(new BigDecimal(0));
        }
        if (form.getExpense() == null) {
            form.setExpense(new BigDecimal(0));
        }
        //设置结存
        handleBalance(form);
        //添加到数据库
        detailDao.add(form);
        //如果是插入以前,就需要调整本次插入记录之后的所有记录的结存
        BigDecimal balanceDifference = form.getEarning().subtract(form.getExpense());
        handleLaterBalance(form.getDate(), balanceDifference);
    }

    public Detail findOne(int id) {
        return detailDao.findOne(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Detail form) throws Exception {
        //判断id是否为表单默认值0
        if (form.getId() == 0) {
            throw new MyException("请刷新页面,重新操作!");
        }
        //处理null值
        if (form.getEarning() == null) {
            form.setEarning(new BigDecimal(0));
        }
        if (form.getExpense() == null) {
            form.setExpense(new BigDecimal(0));
        }
        //获取被修改之前的旧记录
        Detail old = detailDao.findOne(form.getId());
        //处理修改时间之后的不一致
        if (form.getDate().compareTo(old.getDate()) < 0) {
            //时间提前
            handleAlterDate(form, old, true);
        } else if (form.getDate().compareTo(old.getDate()) > 0) {
            //时间推后
            handleAlterDate(form, old, false);
        }
        //处理修改收入支出之后的不一致
        if (form.getEarning().compareTo(old.getEarning()) != 0 || form.getExpense().compareTo(old.getExpense()) != 0) {
            //修改了收入支出,这条记录以后的所有记录的余额都要修改
            BigDecimal earningDifference = form.getEarning().subtract(old.getEarning());
            BigDecimal expenseDifference = form.getExpense().subtract(old.getExpense());
            BigDecimal balanceDifference = earningDifference.subtract(expenseDifference);
            //得到被修改记录后面的所有记录并更新
            handleLaterBalance(form.getDate(), balanceDifference);
        }
        //更新当前记录
        handleBalance(form);
        detailDao.update(form);

    }

    /**
     * 处理detail的结存
     *
     * @param detail
     */
    private void handleBalance(Detail detail) {
        //获取期初
        Detail previous = detailDao.findBeforeOne(detail.getDate());//获取前一条记录
        BigDecimal qi_chu = previous == null ? new BigDecimal(0) : previous.getBalance();
        //设置结存(当前结存=之前结存+当前收入-当前支出)
        detail.setBalance(qi_chu.add(detail.getEarning()).subtract(detail.getExpense()));
    }

    /**
     * 处理修改时间导致的结存不一致
     *
     * @param current   修改后的条目
     * @param previous  被修改之前的条目
     * @param isForward 日期是否往提前
     */
    private void handleAlterDate(Detail current, Detail previous, boolean isForward) {
        //要修改的结存差值
        BigDecimal difference = previous.getEarning().subtract(previous.getExpense());
        if (isForward) {
            //日期提前
            detailDao.updateDuring(difference, current.getDate(), previous.getDate());
        } else {
            //日期推后
            detailDao.updateDuring(difference.negate(), previous.getDate(), current.getDate());
        }
    }

    /**
     * 处理date之后的所有记录的结存不一致
     *
     * @param date              时间
     * @param balanceDifference 要修改的结存差值
     */
    private void handleLaterBalance(Date date, BigDecimal balanceDifference) {
        detailDao.updateLater(balanceDifference, date);
    }

    /**
     * 删除一条记录, 并处理此记录之后的结存不一致
     *
     * @param form
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Detail form) {
        detailDao.delete(form.getId());
        handleLaterBalance(form.getDate(), form.getExpense().subtract(form.getEarning()));
    }

    /**
     * 根据每一笔收入支出来更新所有记录的结存
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateBalance() {
        List<Detail> detailList = detailDao.findAll();
        BigDecimal balance = new BigDecimal(0);
        for (int i = detailList.size() - 1; i >= 0; i--) {
            balance = balance.add(detailList.get(i).getEarning().subtract(detailList.get(i).getExpense()));
            detailList.get(i).setBalance(balance);
            detailDao.update(detailList.get(i));
        }
    }

}
