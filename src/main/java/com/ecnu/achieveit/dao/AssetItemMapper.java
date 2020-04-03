package com.ecnu.achieveit.dao;

import com.ecnu.achieveit.model.AssetItem;

import java.util.List;

public interface AssetItemMapper {
    int deleteByPrimaryKey(String assetItem);

    int insert(AssetItem record);

    int insertSelective(AssetItem record);

    List<AssetItem> selectAll();
}