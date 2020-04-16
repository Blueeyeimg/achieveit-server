package com.ecnu.achieveit.modelview;

import com.ecnu.achieveit.model.ProjectRisk;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RiskView {

    public String projectId;

    public String riskId;

    public String type;

    public String description;

    public String state;

    public String owner;

    public static List<RiskView> convertFromProjectRisk(List<ProjectRisk> risks){

        List<RiskView> result = new ArrayList<>();

        if(ObjectUtils.isEmpty(risks)){
            return result;
        }

        for(ProjectRisk risk : risks){
            RiskView riskView = new RiskView(risk.getProjectId(),
                    risk.getRiskId(),
                    risk.getType(),
                    risk.getDescription(),
                    risk.getRiskState(),
                    risk.getRiskOwnerId());

            result.add(riskView);
        }

        return result;
    }

}
