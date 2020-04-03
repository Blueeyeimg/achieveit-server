package com.ecnu.achieveit.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * This class corresponds to the database table review_defect_info
 */
@Getter
@Setter
@ToString
public class ReviewDefectInfo implements Serializable {

    private Integer reviewDefectId;

    private String projectId;

    /**
     * Database Column Remarks:
     *   review, defect
     */
    private String type;

    private String providerId;

    /**
     * Database Column Remarks:
     *   0: 未处理 1: 已处理
     */
    private Integer state;

    private String solverId;

    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date date;

    private String link;

    private static final long serialVersionUID = 1L;

    public boolean valid(){
        return projectId != null
                && type != null
                && (type.equals("defect") || type.equals("review"))
                && providerId != null;
    }

}