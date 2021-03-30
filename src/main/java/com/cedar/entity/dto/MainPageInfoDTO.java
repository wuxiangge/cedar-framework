package com.cedar.entity.dto;

import com.cedar.entity.bo.HeadLine;
import com.cedar.entity.bo.ShopCategory;
import lombok.Data;

import java.util.List;

@Data
public class MainPageInfoDTO {
    private List<HeadLine> headLineList;
    private List<ShopCategory> shopCategoryList;
}
