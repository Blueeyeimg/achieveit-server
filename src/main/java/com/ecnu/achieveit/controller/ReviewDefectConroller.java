package com.ecnu.achieveit.controller;

import com.ecnu.achieveit.constant.ProjectRole;
import com.ecnu.achieveit.model.ProjectMember;
import com.ecnu.achieveit.model.ProjectMemberKey;
import com.ecnu.achieveit.model.ReviewDefectInfo;
import com.ecnu.achieveit.service.ProjectMemberService;
import com.ecnu.achieveit.service.ReviewDefectService;
import com.ecnu.achieveit.util.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReviewDefectConroller {

    @Autowired
    private ReviewDefectService reviewDefectService;

    @Autowired
    private ProjectMemberService projectMemberService;

    @GetMapping("/review_defect/project/{projectId}")
    public Object listByProjectId(@PathVariable("projectId") String projectId){
        return RestResponse.success(reviewDefectService.queryListByProjectId(projectId));
    }

    @GetMapping("/review_defect/state")
    public Object listByProjectIdAndState(@RequestParam("projectId") String projectId,
                                          @RequestParam("state") String state){
        Integer stateInt = Integer.parseInt(state);

        List<ReviewDefectInfo> reviewDefectInfos = reviewDefectService.queryListByProjectIdAndState(projectId,stateInt);
        if(reviewDefectInfos == null){
            return RestResponse.fail("state只能为0或1，0表示未处理，1表示已处理");
        }
        return RestResponse.success(reviewDefectInfos);
    }

    @GetMapping("/review_defect/type")
    public Object listByProjectIdAndType(@RequestParam("projectId") String projectId,
                                         @RequestParam("type") String type){
        List<ReviewDefectInfo> reviewDefectInfos = reviewDefectService.queryListByProjectIdAndType(projectId,type);
        if(reviewDefectInfos == null){
            return RestResponse.fail("type只能为review或者defect");
        }
        return RestResponse.success(reviewDefectInfos);
    }

    @GetMapping("/review_defect/provider/{providerId}")
    public Object listByProviderId(@PathVariable("providerId") String providerId,
                                   @RequestAttribute("userId") String userId){
        return RestResponse.success(reviewDefectService.queryListByProviderId(providerId));
    }

    @GetMapping("/review_defect/solver/{solverId}")
    public Object listBySolverId(@PathVariable("solverId") String solverId,
                                 @RequestAttribute("userId") String userId){
        return RestResponse.success(reviewDefectService.queryListBySolverId(solverId));
    }

    @PostMapping("/review_defect")
    public Object add(@RequestBody ReviewDefectInfo reviewDefectInfo, @RequestAttribute("userId") String userId){
        ProjectMember user = projectMemberService.queryMemberByKey(new ProjectMemberKey(reviewDefectInfo.getProjectId(),userId));
        if(ObjectUtils.isEmpty(user)
                || (!ProjectRole.TEST.in(user.getRole())
                    && !ProjectRole.TEST_LEADER.in(user.getRole()))){
            return RestResponse.noPermission("当前用户不是该项目的测试或测试Leader，无法为项目提交评审/缺陷信息" + user.getRole());
        }
        reviewDefectInfo.setProviderId(userId);

        if(reviewDefectService.reportReviewDefect(reviewDefectInfo)){
            return RestResponse.success();
        }

        return RestResponse.fail();
    }

    @PutMapping("/review_defect")
    public Object update(@RequestBody ReviewDefectInfo reviewDefectInfo, @RequestAttribute("userId") String userId){
        ProjectMember user = projectMemberService.queryMemberByKey(new ProjectMemberKey(reviewDefectInfo.getProjectId(),userId));
        if(ObjectUtils.isEmpty(user)
                || (!ProjectRole.DEVELOPER.in(user.getRole())
                && !ProjectRole.DEVELOP_LEADER.in(user.getRole()))){
            return RestResponse.noPermission("当前用户不是该项目的开发或开发Leader，无法为项目处理评审/缺陷信息");
        }

        reviewDefectInfo.setSolverId(userId);

        if(reviewDefectService.solveReviewDefect(reviewDefectInfo)){
            return RestResponse.success();
        }

        return RestResponse.fail();
    }
}
