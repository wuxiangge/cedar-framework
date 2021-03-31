package org.framework.core;

import com.cedar.controller.DispatcherServlet;
import com.cedar.controller.frontend.MainPageController;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BeanContainerTest {


    private static BeanContainer beanContainer;


    @BeforeAll
    static void init() {
        beanContainer =BeanContainer.getInstance();
    }


    @DisplayName("加载目标类及其实例到BeanContainer：loadBeansTest")
    @Order(1)
    @Test
    public void loadBeansTest(){
        Assertions.assertEquals(false, beanContainer.isLoaded());
        beanContainer.loadBeans("com.cedar");
        Assertions.assertEquals(7, beanContainer.size());
        Assertions.assertEquals(true, beanContainer.isLoaded());
    }


    @DisplayName("根据类获取其实例：getBeanTest")
    @Order(2)
    @Test
    public void getBeanTest(){
        MainPageController controller = (MainPageController)beanContainer.getBean(MainPageController.class);
        Assertions.assertEquals(true, controller instanceof MainPageController);
        DispatcherServlet dispatcherServlet = (DispatcherServlet) beanContainer.getBean(DispatcherServlet.class);
        Assertions.assertEquals(null, dispatcherServlet);
    }

}