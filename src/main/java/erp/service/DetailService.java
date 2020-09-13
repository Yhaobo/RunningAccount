package erp.service;

import erp.dao.DetailDao;
import erp.dao.ForeignTableDao;
import erp.dao.VoucherDao;
import erp.entity.Account;
import erp.entity.Detail;
import erp.entity.Voucher;
import erp.util.MyException;
import erp.util.MyUtils;
import erp.entity.vo.req.DetailConditionQueryVO;
import erp.entity.vo.resp.DetailRespVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class DetailService {

    @Autowired
    private DetailDao detailDao;
    @Autowired
    private VoucherDao voucherDao;
    @Autowired
    private ForeignTableDao foreignTableDao;

    @Value("${home.location}")
    private String location;

    private String parentPath = "voucher\\";

    @Transactional(readOnly = true)
    public List<DetailRespVO> findAll(DetailConditionQueryVO vo) {
        // 所有收支记录DO
        List details = detailDao.listByFilter(vo);
        // 拥有凭证的收支记录的id集合
        Set<Integer> DetailIdFromVouchers = detailDao.listDetailIdFromVouchers();
        for (int i = 0; i < details.size(); i++) {
            Detail detail = (Detail) details.get(i);
            // 用于返回前端的VO
            DetailRespVO detailRespVo = new DetailRespVO();
            // 将DO的数据传递给VO
            BeanUtils.copyProperties(detail, detailRespVo);

            if (DetailIdFromVouchers.contains(detail.getId())) {
                // 有凭证
                detailRespVo.setHasVoucher(true);
            } else {
                // 没有凭证
                detailRespVo.setHasVoucher(false);
            }
            details.set(i, detailRespVo);
        }

        //格式化所有数字
        MyUtils.formatNumber(details);
        return details;
    }

    @Transactional(readOnly = true)
    public Detail findOneById(int id) {
        return detailDao.getById(id);
    }

    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void insert(Detail form) {
        //处理空值
        if (form.getDescription().length() < 1) {
            form.setDescription("无");
        }
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
        detailDao.insert(form);
        //如果是插入时间是以前,就需要调整本次插入记录之后的所有记录的结存
        handleLaterBalance(form.getDate(), form.getEarning().subtract(form.getExpense()), form.getAccount().getId());
    }

    @Transactional(rollbackFor = Exception.class, timeout = 30)
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
        Detail old = detailDao.getById(form.getId());
        //处理修改时间之后的结存不一致
        if (form.getDate().compareTo(old.getDate()) < 0) {
            //时间提前
            handleAlterDate(form, old, true);
        } else if (form.getDate().compareTo(old.getDate()) > 0) {
            //时间推后
            handleAlterDate(form, old, false);
        }
        //处理修改收入支出之后的结存不一致
        if (form.getEarning().compareTo(old.getEarning()) != 0 || form.getExpense().compareTo(old.getExpense()) != 0) {
            //修改了收入支出,这条记录以后的所有记录的余额都要修改
            BigDecimal earningDifference = form.getEarning().subtract(old.getEarning());
            BigDecimal expenseDifference = form.getExpense().subtract(old.getExpense());
            BigDecimal balanceDifference = earningDifference.subtract(expenseDifference);
            //得到被修改记录后面的所有记录并更新
            handleLaterBalance(form.getDate(), balanceDifference, form.getAccount().getId());
        }
        //更新当前记录
        handleBalance(form);
        detailDao.update(form);
    }

    /**
     * 删除一条记录, 并处理此记录之后的结存不一致
     *
     * @param form
     */
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void delete(Detail form) throws MyException {
        // 删除本地凭证文件
        String[] urls = voucherDao.listUrlByDetailId(form.getId());
        deleteLocalVoucherFiles(urls);
        // 删除凭证记录
        voucherDao.deleteByDetailId(form.getId());
        // 删除明细记录
        detailDao.delete(form.getId());
        //处理目标记录之后的记录的Balance
        handleLaterBalance(form.getDate(), form.getExpense().subtract(form.getEarning()), form.getAccount().getId());
    }

    /**
     * 根据每一笔收入支出来更新所有记录的结存
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateAllBalance() {
        List<Account> accounts = foreignTableDao.listAccount();
        for (Account account : accounts) {
            List<Detail> detailList = detailDao.listAllByAccountId(account.getId());
            BigDecimal balance = new BigDecimal(0);
            for (int i = detailList.size() - 1; i >= 0; i--) {
                balance = balance.add(detailList.get(i).getEarning().subtract(detailList.get(i).getExpense()));
                detailList.get(i).setBalance(balance);
                detailDao.update(detailList.get(i));
            }
        }
    }

    /**
     * 上传图片凭证的保存和记录
     *
     * @param file 图片凭证文件
     * @param id   对应的一条记录的id
     */
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void insertVoucher(MultipartFile file, Integer id) throws Exception {
        String uuid = MyUtils.getUUID();
        String url = uuid + "_" + LocalDate.now().toString() + "_" + file.getOriginalFilename();
        String filePath = getLocalVoucherFilePath(url);

        // 数据库记录关联
        voucherDao.insert(new Voucher(url, id));

        // 保存文件到磁盘
        File localFile = new File(filePath);
        // 自动创建上级目录
        if (!localFile.getParentFile().exists()) {
            if (!localFile.getParentFile().mkdirs()) {
                throw new MyException("目录创建失败");
            }
        }
        file.transferTo(localFile);
    }

    /**
     * @param dId detail的id
     * @return 返回对应d_id的所有voucher图片的地址
     */
    public List<Voucher> listVoucherByDetailId(Integer dId) {
        return voucherDao.listVoucher(dId);
    }

    /**
     * @param fileName 对应voucher图片的文件名
     * @param response 直接返回文件流
     */
    public void listVoucherByUrl(String fileName, HttpServletResponse response) throws Exception {
        String filePath = getLocalVoucherFilePath(fileName);
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(filePath));
             OutputStream outputStream = response.getOutputStream()) {
            byte[] data = new byte[4096];
            int len;
            while ((len = inputStream.read(data)) != -1) {
                outputStream.write(data, 0, len);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 删除凭证本地文件和数据库记录
     *
     * @param voucherId
     */
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void deleteVoucher(Integer voucherId) throws MyException {
        // 获取文件名
        String fileName = voucherDao.getUrlById(voucherId);

        // 删除数据库记录
        voucherDao.deleteById(voucherId);

        // 删除本地文件
        deleteLocalVoucherFiles(fileName);
    }

    /**
     * 处理detail的结存
     *
     * @param detail
     */
    private void handleBalance(Detail detail) {
        //获取期初
        Detail previous = detailDao.findBeforeOne(detail.getDate(),detail.getAccount().getId());
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
            detailDao.updateDuring(difference, current.getDate(), previous.getDate(), current.getAccount().getId());
        } else {
            //日期推后
            detailDao.updateDuring(difference.negate(), previous.getDate(), current.getDate(), current.getAccount().getId());
        }
    }

    /**
     * 处理date之后的所有记录的结存不一致
     *
     * @param date              时间
     * @param balanceDifference 要修改的结存差值
     * @param accountId 账户ID
     */
    private void handleLaterBalance(Date date, BigDecimal balanceDifference, Integer accountId) {
        detailDao.updateLater(balanceDifference, date, accountId);
    }

    /**
     * @param fileName
     * @return 返回凭证文件的本地路径
     */
    private String getLocalVoucherFilePath(String fileName) {
        return location + parentPath + fileName.charAt(0) + "\\" + fileName;
    }

    /**
     * 删除多个本地凭证文件
     *
     * @param fileNames 文件名
     * @throws MyException
     */
    private void deleteLocalVoucherFiles(String... fileNames) throws MyException {
        File file = null;
        for (String fileName : fileNames) {
            file = new File(getLocalVoucherFilePath(fileName));
            if (file.exists()) {
                if (!file.delete()) {
                    throw new MyException("凭证文件删除失败");
                }
            }
        }
    }
}
