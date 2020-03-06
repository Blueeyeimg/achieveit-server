package com.ecnu.achieveit.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

    private Date date;

    private String link;

    private static final long serialVersionUID = 1L;

}