package com.ecnu.achieveit.service.impl;

import com.ecnu.achieveit.dao.ReviewDefectInfoMapper;
import com.ecnu.achieveit.model.ReviewDefectInfo;
import com.ecnu.achieveit.service.ReviewDefectService;
import com.ecnu.achieveit.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class ReviewDefectServiceImpl implements ReviewDefectService {

    @Autowired(required = false)
    private ReviewDefectInfoMapper reviewDefectInfoMapper;

    @Override
    public boolean reportReviewDefect(ReviewDefectInfo reviewDefectInfo) {
        if(!reviewDefectInfo.valid()){
            return false;
        }

        reviewDefectInfo.setDate(new Date());
        reviewDefectInfo.setState(0);

        return reviewDefectInfoMapper.insertSelective(reviewDefectInfo) == 1;
    }

    @Override
    public boolean solveReviewDefect(ReviewDefectInfo reviewDefectInfo) {
        if(!reviewDefectInfo.valid() || reviewDefectInfo.getSolverId() == null){
            return false;
        }

        reviewDefectInfo.setState(1);

        return reviewDefectInfoMapper.updateByPrimaryKeySelective(reviewDefectInfo) == 1;
    }

    @Override
    public List<ReviewDefectInfo> queryListByProjectId(String projectId) {
        return reviewDefectInfoMapper.selectListByProjectId(projectId);
    }

    @Override
    public List<ReviewDefectInfo> queryListByProviderId(String providerId) {
        return reviewDefectInfoMapper.selectListByProviderId(providerId);
    }

    @Override
    public List<ReviewDefectInfo> queryListBySolverId(String solverId) {
        return reviewDefectInfoMapper.selectListBySolverId(solverId);
    }

    @Override
    public List<ReviewDefectInfo> queryListByProjectIdAndState(String projectId, Integer state) {

        boolean stateIsValid = state != null && (state.equals(0) || state.equals(1));
        if(!stateIsValid){
            return null;
        }
        return reviewDefectInfoMapper.selectListByProjectIdAndState(projectId,state);
    }

    @Override
    public List<ReviewDefectInfo> queryListByProjectIdAndType(String projectId, String type) {

        boolean typeIsValid = type != null && (type.equals("defect") || type.equals("review"));
        if(!typeIsValid){
            return null;
        }
        return reviewDefectInfoMapper.selectListByProjectIdAndType(projectId,type);
    }
}
