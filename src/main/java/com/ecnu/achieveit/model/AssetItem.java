package com.ecnu.achieveit.model;

import java.io.Serializable;

/**
 * assets
 * @author 
 */
public class AssetItem implements Serializable {
    private String assetItem;

    private static final long serialVersionUID = 1L;

    public String getAssetItem() {
        return assetItem;
    }

    public void setAssetItem(String assetItem) {
        this.assetItem = assetItem;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        AssetItem other = (AssetItem) that;
        return (this.getAssetItem() == null ? other.getAssetItem() == null : this.getAssetItem().equals(other.getAssetItem()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getAssetItem() == null) ? 0 : getAssetItem().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", assetItem=").append(assetItem);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}