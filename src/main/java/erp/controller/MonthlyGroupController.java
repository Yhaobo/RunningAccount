package erp.controller;

import erp.domain.Detail;
import erp.util.ResultInfo;
import erp.service.GroupQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/group")
public class MonthlyGroupController {
    @Autowired
    private GroupQueryService service;

//    private Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/findDepartment")
    @ResponseBody
    public ResultInfo findDepartment(int dep_id) {
        List<Detail> detailList=service.findDepartment(dep_id);
        return new ResultInfo(true, detailList);
    }

    @RequestMapping("/findAccount")
    @ResponseBody
    public ResultInfo findAccount(int a_id) {
        List<Detail> detailList = service.findAccount(a_id);
        return new ResultInfo(true, detailList);
    }
    @RequestMapping("/findProject")
    @ResponseBody
    public ResultInfo findProject(int p_id) {
        List<Detail> detailList = service.findProject(p_id);
        return new ResultInfo(true, detailList);
    }
    @RequestMapping("/findCategory")
    @ResponseBody
    public ResultInfo findCategory(int c_id) {
        List<Detail> detailList = service.findCategory(c_id);
        return new ResultInfo(true, detailList);
    }
    @RequestMapping("/findDetail")
    @ResponseBody
    public ResultInfo findDetail() {
        List<Detail> detailList = service.findDetail();
        return new ResultInfo(true, detailList);
    }
}
