package com.cedar.controller.frontend;


import com.cedar.entity.dto.MainPageInfoDTO;
import com.cedar.entity.dto.Result;
import com.cedar.service.combine.HeadLineShopCategoryCombineService;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Getter
public class MainPageController {

    private HeadLineShopCategoryCombineService headLineShopCategoryCombineService;

    public Result<MainPageInfoDTO> getMainPageInfo(HttpServletRequest req, HttpServletResponse resp){
        return headLineShopCategoryCombineService.getMainPageInfo();
    }


    public void throwException(){
        throw new RuntimeException("抛出异常测试");
    }
}
