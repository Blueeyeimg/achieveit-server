package com.ecnu.achieveit.controller;

import com.ecnu.achieveit.constant.ProjectRole;
import com.ecnu.achieveit.model.*;
import com.ecnu.achieveit.service.ExcelService;
import com.ecnu.achieveit.service.ProjectMemberService;
import com.ecnu.achieveit.service.ProjectRiskService;
import com.ecnu.achieveit.util.RestResponse;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
public class RiskController {

    @Autowired
    private ExcelService excelService;

    @Autowired
    private ProjectRiskService riskService;

    @Autowired
    private ProjectMemberService projectMemberService;

    @PostMapping("/upload/risk")
    public Object upload(MultipartFile file){

        List<ProjectRisk> risks = excelService.readProjectRisk(file);

        if(risks == null){
            return RestResponse.fail();
        }

        return RestResponse.success(risks);
    }

    @GetMapping("/download/risk/{projectId}")
    public void download(HttpServletResponse response,
                            @PathVariable("projectId") String projectId){
        try {
            InputStream fis = Thread.currentThread().getContextClassLoader().getResourceAsStream("static/project_risk_template.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(fis);

            List<ProjectRisk> risks = riskService.queryByProjectId(projectId);
            workbook = excelService.writeProjectRisk(workbook, risks);

            response.setContentType("application/binary;charset=ISO8859-1");
            String fileName = java.net.URLEncoder.encode(projectId + "-风险列表", "UTF-8");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");
            ServletOutputStream out = null;
            out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭文件输出流

        }
        return;
    }

    @GetMapping("/risk/project/{projectId}")
    public Object listByProjectId(@PathVariable("projectId") String projectId){

        List<ProjectRisk> projectRisks = riskService.queryByProjectId(projectId);

        if(projectRisks == null){
            return RestResponse.fail();
        }

        return RestResponse.success(projectRisks);
    }

    /**
     * 之所以不直接用token里的userId，是考虑到项目经理可能需要查看项目成员相关的风险
     * */
    @GetMapping("/risk/related/{relatedId}")
    public Object listByRelatedId(@PathVariable("relatedId") String relatedId){
        List<ProjectRisk> projectRisks = riskService.queryByRelatedId(relatedId);

        if(projectRisks == null){
            return RestResponse.fail();
        }

        return RestResponse.success(projectRisks);
    }

    @GetMapping("/risk/owner/{ownerId}")
    public Object listByOwnerId(@PathVariable("ownerId") String ownerId){
        List<ProjectRisk> projectRisks = riskService.queryByOwnerId(ownerId);

        if(projectRisks == null){
            return RestResponse.fail();
        }

        return RestResponse.success(projectRisks);
    }

    /**
     * 项目经理
     * 批量新增，任何时刻都可以批量新增*/
    @PostMapping("/risks")
    public Object addList(@RequestBody List<ProjectRisk> risks,
                          @RequestAttribute("userId") String userId){

        if(ObjectUtils.isEmpty(risks)){
            return RestResponse.fail("批量新增的Project Risk 不能为空");
        }

        ProjectMember user = projectMemberService.queryMemberByKey(new ProjectMemberKey(risks.get(0).getProjectId(),userId));
        if(ObjectUtils.isEmpty(user) || !ProjectRole.MANAGER.in(user.getRole())){
            return RestResponse.noPermission("当前用户不是该项目的项目经理，无法批量新增项目风险");
        }

        if(riskService.add(risks)){
            return RestResponse.success();
        }

        return RestResponse.fail();
    }

    /**
    * 项目经理
    * */
    @PostMapping("/risk")
    public Object add(@RequestBody ProjectRisk risk,
                      @RequestAttribute("userId") String userId){
        ProjectMember user = projectMemberService.queryMemberByKey(new ProjectMemberKey(risk.getProjectId(),userId));
        if(ObjectUtils.isEmpty(user) || !ProjectRole.MANAGER.in(user.getRole())){
            return RestResponse.noPermission("当前用户不是该项目的项目经理，无法新增项目风险");
        }

        if(riskService.add(risk)){
            return RestResponse.success();
        }

        return RestResponse.fail();
    }

    /**
     * 项目经理、风险相关人、风险责任人
     * */
    @PutMapping("/risk")
    public Object update(@RequestBody ProjectRisk risk,
                         @RequestAttribute("userId") String userId){
        ProjectMember user = projectMemberService.queryMemberByKey(new ProjectMemberKey(risk.getProjectId(),userId));
        if(ObjectUtils.isEmpty(user) || !ProjectRole.MANAGER.in(user.getRole())){
            return RestResponse.noPermission("当前用户不是该项目的项目经理，无法修改项目风险");
        }

        if(riskService.modify(risk)){
            return RestResponse.success();
        }

        return RestResponse.fail();
    }

    /**
     * 项目经理
     * */
    @DeleteMapping("/risk")
    public Object delete(@RequestBody ProjectRiskKey riskKey,
                         @RequestAttribute("userId") String userId){
        ProjectMember user = projectMemberService.queryMemberByKey(new ProjectMemberKey(riskKey.getProjectId(),userId));
        if(ObjectUtils.isEmpty(user) || !ProjectRole.MANAGER.in(user.getRole())){
            return RestResponse.noPermission("当前用户不是该项目的项目经理，无法删除项目风险");
        }

        if(riskService.remove(riskKey)){
            return RestResponse.success();
        }

        return RestResponse.fail();
    }

    @GetMapping("/risk/related")
    public Object getRelated(@RequestParam("projectId") String projectId,
                             @RequestParam("riskId") String riskId){
        List<RiskRelatedKey> riskRelatedKeys = riskService.queryRelatesByProjectIdAndRiskId(projectId, riskId);

        if(riskRelatedKeys == null){
            return RestResponse.fail();
        }

        return RestResponse.success(riskRelatedKeys);
    }

    /**
     * 项目经理
     * */
    @PostMapping("/risk/related")
    public Object addRelated(@RequestBody RiskRelatedKey relatedKey,
                             @RequestAttribute("userId") String userId){
        ProjectMember user = projectMemberService.queryMemberByKey(new ProjectMemberKey(relatedKey.getProjectId(),userId));
        if(ObjectUtils.isEmpty(user) || !ProjectRole.MANAGER.in(user.getRole())){
            return RestResponse.noPermission("当前用户不是该项目的项目经理，无法为项目风险新增相关人");
        }

        if(riskService.addRelated(relatedKey)){
            return RestResponse.success();
        }
        return RestResponse.fail();
    }

    /**
     * 项目经理
     * */
    @DeleteMapping("/risk/related")
    public Object deleteRelated(@RequestBody RiskRelatedKey relatedKey,
                                @RequestAttribute("userId") String userId){
        ProjectMember user = projectMemberService.queryMemberByKey(new ProjectMemberKey(relatedKey.getProjectId(),userId));
        if(ObjectUtils.isEmpty(user) || !ProjectRole.MANAGER.in(user.getRole())){
            return RestResponse.noPermission("当前用户不是该项目的项目经理，无法删除项目风险相关人");
        }

        if(riskService.removeRelated(relatedKey)){
            return RestResponse.success();
        }
        return RestResponse.fail();
    }

    /**
     * 项目经理
     * 批量修改，也可用作批量新增，会将原有数据删除再全部新增
     * 请保证relatedKey中的projectId一致
     * */
    @PutMapping("/risk/relates")
    public Object updateRelates(@RequestBody List<RiskRelatedKey> relatedKey,
                                @RequestAttribute("userId") String userId){

        if(ObjectUtils.isEmpty(relatedKey)){
            return RestResponse.fail("不能将风险相关人修改为空");
        }

        ProjectMember user = projectMemberService.queryMemberByKey(new ProjectMemberKey(relatedKey.get(0).getProjectId(),userId));
        if(ObjectUtils.isEmpty(user) || !ProjectRole.MANAGER.in(user.getRole())){
            return RestResponse.noPermission("当前用户不是该项目的项目经理，无法修改项目风险相关人");
        }

        if(riskService.modifyRelates(relatedKey)){
            return RestResponse.success();
        }
        return RestResponse.fail();
    }

}
