package com.ecnu.achieveit.modelview;

import com.ecnu.achieveit.model.ProjectFunctionKey;
import com.ecnu.achieveit.util.LogUtil;
import lombok.*;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FunctionView implements Serializable{

    private String projectId;

    private List<FunctionItem> functions = new ArrayList<>();

    private static final long serialVersionUID = 1L;

    public static FunctionView functionListToView(List<ProjectFunctionKey> projectFunctionKeys){

        FunctionView view = new FunctionView();

        if(ObjectUtils.isEmpty(projectFunctionKeys)){
            return view;
        }

        view.setProjectId(projectFunctionKeys.get(0).getProjectId() == null ? "" : projectFunctionKeys.get(0).getProjectId());
        view.setFunctions(new ArrayList<>());

        Map<String, List<String>> functionMap = projectFunctionKeys.stream().collect(
                Collectors.groupingBy(ProjectFunctionKey::getPrimaryFunction,
                        Collectors.mapping(ProjectFunctionKey::getSecondaryFunction, Collectors.toList())));


        for(String key : functionMap.keySet()){
            view.getFunctions().add(new FunctionItem(key, functionMap.get(key)));
        }
        LogUtil.i("in FunctionVIew.functionListToView" + view.toString());

        return view;
    }

    public static List<ProjectFunctionKey> functionViewToList(FunctionView functionView){

        List<ProjectFunctionKey> projectFunctionKeys = new ArrayList<>();

        if(ObjectUtils.isEmpty(functionView.getProjectId())){
            return projectFunctionKeys;
        }
        String projectId = functionView.getProjectId();

        for(FunctionItem functionItem : functionView.getFunctions()){
            String primaryFunction = functionItem.getPrimaryFunction();
            for(String secondaryFunction : functionItem.getSecondaryFunction()){
                projectFunctionKeys.add(new ProjectFunctionKey(projectId,primaryFunction,secondaryFunction));
            }
        }
        LogUtil.i("in FunctionVIew.functionViewToList" + projectFunctionKeys.toString());

        return projectFunctionKeys;
    }

}
