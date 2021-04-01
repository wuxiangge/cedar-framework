package com.cedar.controller.frontend;


import com.cedar.entity.dto.MainPageInfoDTO;
import com.cedar.entity.dto.Result;
import com.cedar.service.combine.HeadLineShopCategoryCombineService;
import com.cedar.service.combine.impl.HeadLineShopCategoryCombineServiceImpl2;
import lombok.Getter;
import org.framework.core.annotation.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Getter
public class MainPageController {

    private HeadLineShopCategoryCombineService headLineShopCategoryCombineService = new HeadLineShopCategoryCombineServiceImpl2();

    public Result<MainPageInfoDTO> getMainPageInfo(HttpServletRequest req, HttpServletResponse resp){
        return headLineShopCategoryCombineService.getMainPageInfo();
    }


    public void throwException(){
        throw new RuntimeException("抛出异常测试");
    }


}
