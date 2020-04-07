package com.ecnu.achieveit.controller;

import ch.qos.logback.core.util.FileUtil;
import com.ecnu.achieveit.constant.ProjectRole;
import com.ecnu.achieveit.model.ProjectFunctionKey;
import com.ecnu.achieveit.model.ProjectMember;
import com.ecnu.achieveit.model.ProjectMemberKey;
import com.ecnu.achieveit.modelview.FunctionItem;
import com.ecnu.achieveit.modelview.FunctionView;
import com.ecnu.achieveit.service.ExcelService;
import com.ecnu.achieveit.service.ProjectFunctionService;
import com.ecnu.achieveit.service.ProjectMemberService;
import com.ecnu.achieveit.util.LogUtil;
import com.ecnu.achieveit.util.RestResponse;
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
public class ProjectFunctionController {

    @Autowired
    private ExcelService excelService;

    @Autowired
    private ProjectFunctionService projectFunctionService;

    @Autowired
    private ProjectMemberService projectMemberService;

    @PostMapping("/upload/function")
    public Object upload(MultipartFile file){

        FunctionView functionView = excelService.readProjectFunction(file);

        if(functionView == null){
            return RestResponse.fail();
        }
        return RestResponse.success(functionView);
    }

    @GetMapping("/download/function/{projectId}")
    public void download(HttpServletResponse response,
                         @PathVariable("projectId") String projectId,
                         @RequestAttribute("userId")String userId){
        try {
            InputStream fis = Thread.currentThread().getContextClassLoader().getResourceAsStream("static/project_function_template.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(fis);

            List<ProjectFunctionKey> functionKeys = projectFunctionService.queryProjectFunction(projectId);
            workbook = excelService.writeProjectFunction(workbook, functionKeys);

            response.setContentType("application/binary;charset=ISO8859-1");
            String fileName = java.net.URLEncoder.encode(projectId + "-功能列表", "UTF-8");
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

    @GetMapping("/function/{projectId}")
    public Object get(@PathVariable("projectId") String projectId){

        FunctionView functionView = projectFunctionService.queryProjectFunctionView(projectId);

        return RestResponse.success(functionView);
    }

    @PostMapping("/function")
    public Object add(@RequestBody FunctionView functionView, @RequestAttribute("userId")String userId){
        ProjectMember user = projectMemberService.queryMemberByKey(new ProjectMemberKey(functionView.getProjectId(),userId));
        if(ObjectUtils.isEmpty(user) || !ProjectRole.MANAGER.in(user.getRole())){
            return RestResponse.noPermission("当前用户不是该项目的项目经理，无法新增项目功能列表");
        }
        LogUtil.i("注入的model： " + functionView.toString());
        if(projectFunctionService.addProjectFunction(functionView)){
            return RestResponse.success();
        }

        return RestResponse.fail();
    }

    @PutMapping("/function")
    public Object update(@RequestBody FunctionView functionView, @RequestAttribute("userId")String userId){
        ProjectMember user = projectMemberService.queryMemberByKey(new ProjectMemberKey(functionView.getProjectId(),userId));
        if(ObjectUtils.isEmpty(user) || !ProjectRole.MANAGER.in(user.getRole())){
            return RestResponse.noPermission("当前用户不是该项目的项目经理，无法修改项目功能列表");
        }
        LogUtil.i("注入的model： " + functionView.toString());
        if(projectFunctionService.updateProjectFunction(functionView)){
            return RestResponse.success();
        }

        return RestResponse.fail();
    }
}
