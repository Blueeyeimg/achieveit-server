package com.ecnu.achieveit.service;

import com.ecnu.achieveit.model.ReviewDefectInfo;

import java.util.List;

public interface ReviewDefectService {


    /**
     * @description 设置提交时间
     * @param reviewDefectInfo
     * @return
     */
    boolean reportReviewDefect(ReviewDefectInfo reviewDefectInfo);

    boolean solveReviewDefect(ReviewDefectInfo reviewDefectInfo);

    List<ReviewDefectInfo> queryListByProjectId(String projectId);

    List<ReviewDefectInfo> queryListByProviderId(String providerId);

    List<ReviewDefectInfo> queryListBySolverId(String solverId);

    /**
     * @description state 为0/1，0表示未解决，1表示已解决
     */
    List<ReviewDefectInfo> queryListByProjectIdAndState(String projectId, Integer state);

    /**
     * @description type 为defect或者review
     */
    List<ReviewDefectInfo> queryListByProjectIdAndType(String projectId, String type);



}
