package com.cedar.service.combine;

import com.cedar.entity.dto.MainPageInfoDTO;
import com.cedar.entity.dto.Result;

public interface HeadLineShopCategoryCombineService {
    Result<MainPageInfoDTO> getMainPageInfo();
}
