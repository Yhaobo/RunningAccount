package erp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import erp.dao.DetailDao;
import erp.dao.OptionDao;
import erp.dao.PictureDao;
import erp.entity.Account;
import erp.entity.Detail;
import erp.entity.Picture;
import erp.entity.dto.req.DetailQueryConditionDTO;
import erp.entity.dto.resp.DetailRespDTO;
import erp.util.MyException;
import erp.util.MyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
    private PictureDao pictureDao;
    @Autowired
    private OptionDao optionDao;

    @Value("${erp.home.location}")
    private String homeLocation;

    private final String parentPath = "picture\\";

    @Transactional(readOnly = true)
    public void listByCondition(DetailQueryConditionDTO conditionDTO, Page<List<DetailRespDTO>> page) {
        // 所有收支记录DO
        List details = detailDao.listByCondition(page, conditionDTO);
        // 拥有图片信息的收支记录的id集合
        Set<Integer> detailIdFromVoucher = pictureDao.listDetailIds();
        if (detailIdFromVoucher.size() > 0) {
            for (int i = 0; i < details.size(); i++) {
                Detail detail = (Detail) details.get(i);
                // 用于返回前端的VO
                DetailRespDTO detailRespDTO = new DetailRespDTO();
                // 将DO的数据传递给VO
                BeanUtils.copyProperties(detail, detailRespDTO);

                if (detailIdFromVoucher.contains(detail.getId())) {
                    // 有图片信息
                    detailRespDTO.setHasPicture(true);
                } else {
                    // 没有图片信息
                    detailRespDTO.setHasPicture(false);
                }
                details.set(i, detailRespDTO);
            }
        }

        page.setRecords(details);
    }

    @Transactional(readOnly = true)
    public DetailRespDTO findOneById(int id) {
        final Detail detail = detailDao.selectById(id);
        final DetailRespDTO detailRespDTO = new DetailRespDTO();
        BeanUtils.copyProperties(detail, detailRespDTO);
        return detailRespDTO;
    }

    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void insert(Detail form) {
        //处理空值
        if (form.getDigest() == null || form.getDigest().length() < 1) {
            form.setDigest("无");
        }
        //处理null值
        if (form.getEarning() == null) {
            form.setEarning(new BigDecimal(0));
        }
        if (form.getExpense() == null) {
            form.setExpense(new BigDecimal(0));
        }
        //设置结存
        setBalance(form);
        //添加到数据库
        detailDao.insert(form);
        //如果是插入时间是以前,就需要调整本次插入记录之后的所有记录的结存
        handleLaterBalance(form.getDate(), form.getEarning().subtract(form.getExpense()), form.getAccount().getId());
    }

    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void update(Detail form) throws Exception {
        //获取被修改之前的旧记录
        Detail old = detailDao.selectById(form.getId());
        //处理修改时间之后的结存不一致
        if (form.getDate().compareTo(old.getDate()) < 0) {
            //时间提前
            handleModifyDate(form, old, true);
        } else if (form.getDate().compareTo(old.getDate()) > 0) {
            //时间推后
            handleModifyDate(form, old, false);
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
        setBalance(form);
        detailDao.update(form);
    }

    /**
     * 删除一条记录, 并处理此记录之后的结存不一致
     *
     * @param form
     */
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void delete(Detail form) throws MyException {
        // 删除本地图片文件
        String[] urls = pictureDao.listUriByDetailId(form.getId());
        deleteLocalVoucherFiles(urls);
        // 删除图片信息记录
        pictureDao.deleteByDetailId(form.getId());
        // 删除明细记录
        detailDao.deleteById(form.getId());
        //处理目标记录之后的记录的Balance
        handleLaterBalance(form.getDate(), form.getExpense().subtract(form.getEarning()), form.getAccount().getId());
    }

    /**
     * 根据每一笔收入支出来更新所有记录的结存
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateAllBalance() {
        List<Account> accounts = optionDao.listAccount();
        for (Account account : accounts) {
            List<Detail> detailList = detailDao.listByAccountId(account.getId());
            updateBalance(detailList, new BigDecimal(0));
        }
    }

    /**
     * 更新指定时间及之后后的所有记录的结存
     *
     * @param date 指定时间(包含)
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateBalance(Date date) {
        List<Account> accounts = optionDao.listAccount();
        for (Account account : accounts) {
            List<Detail> detailList = detailDao.listByAccountIdAndDate(account.getId(), date);
            updateBalance(detailList, getPreviousBalance(date, account.getId()));
        }
    }

    /**
     * 上传图片的保存和记录
     *
     * @param file     图片文件
     * @param detailId 对应的一条记录的id
     */
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void insertVoucher(MultipartFile file, Integer detailId) throws Exception {
        String uuid = MyUtils.getUUID();
        String fileName = LocalDate.now().toString() + "_" + uuid + "_" + file.getOriginalFilename();
        String filePath = getLocalVoucherFilePath(fileName);

        // 数据库记录关联
        pictureDao.insert(new Picture(fileName, detailId));

        // 保存文件到磁盘
        File localFile = new File(filePath);

        // 自动创建上级目录
        localFile.getParentFile().mkdirs();

        final FileOutputStream fileOutputStream = new FileOutputStream(localFile);
        StreamUtils.copy(file.getInputStream(), fileOutputStream);
        fileOutputStream.close();
    }

    /**
     * @param dId detail的id
     * @return 返回对应d_id的所有图片的地址
     */
    public List<Picture> listVoucherByDetailId(Integer dId) {
        return pictureDao.list(dId);
    }

    /**
     * @param fileName 对应图片的文件名
     * @param response 直接返回文件流
     */
    public void getImg(String fileName, HttpServletResponse response) throws Exception {
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
     * 删除图片信息本地文件和数据库记录
     */
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void deleteVoucher(Integer pictureId) throws MyException {
        // 获取文件名
        String fileName = pictureDao.getUrlById(pictureId);

        // 删除数据库记录
        pictureDao.deleteById(pictureId);

        // 删除本地文件
        deleteLocalVoucherFiles(fileName);
    }

    /**
     * 处理detail的结存
     */
    private void setBalance(Detail detail) {
        //获取期初
        final BigDecimal previousBalance = getPreviousBalance(detail.getDate(), detail.getAccount().getId());
        //设置结存(当前结存=之前结存+当前收入-当前支出)
        detail.setBalance(previousBalance.add(detail.getEarning()).subtract(detail.getExpense()));
    }

    /**
     * 获取指定时间之前的一条记录的结存
     *
     * @param date      指定时间
     * @param accountId 银行账户id
     * @return 如果不存在, 则返回 new BigDecimal(0)
     */
    private BigDecimal getPreviousBalance(Date date, Integer accountId) {
        BigDecimal previousBalance = detailDao.getPreviousBalance(date, accountId);
        if (previousBalance == null) {
            previousBalance = new BigDecimal(0);
        }
        return previousBalance;
    }

    /**
     * 处理修改时间导致的结存不一致
     *
     * @param current   修改后的条目
     * @param previous  被修改之前的条目
     * @param isForward 日期是否往提前
     */
    private void handleModifyDate(Detail current, Detail previous, boolean isForward) {
        //要修改的结存差值
        BigDecimal difference = previous.getEarning().subtract(previous.getExpense());
        if (isForward) {
            //日期提前
            detailDao.updateDuringBalance(difference, current.getDate(), previous.getDate(), current.getAccount().getId());
        } else {
            //日期推后
            detailDao.updateDuringBalance(difference.negate(), previous.getDate(), current.getDate(), current.getAccount().getId());
        }
    }

    /**
     * 处理date之后的所有记录的结存不一致
     *
     * @param date              时间
     * @param balanceDifference 要修改的结存差值
     * @param accountId         账户ID
     */
    private void handleLaterBalance(Date date, BigDecimal balanceDifference, Integer accountId) {
        detailDao.updateLaterBalance(balanceDifference, date, accountId);
    }

    /**
     * @param fileName
     * @return 返回图片文件的本地路径
     */
    private String getLocalVoucherFilePath(String fileName) {
        return homeLocation + parentPath + fileName.substring(0, fileName.indexOf("-")) + "\\" + fileName;
    }

    /**
     * 删除多个本地图片文件
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
                    throw new MyException("图片文件删除失败");
                }
            }
        }
    }

    /**
     * 批量计算并更新结存
     *
     * @param detailList 按时间倒序的记录集合
     * @param balance    期初
     */
    private void updateBalance(List<Detail> detailList, BigDecimal balance) {
        for (int i = detailList.size() - 1; i >= 0; i--) {
            final Detail detail = detailList.get(i);
            balance = balance.add(detail.getEarning().subtract(detail.getExpense()));
            detail.setBalance(balance);
            detailDao.updateBalance(detail.getId(), balance);
        }
    }
}
