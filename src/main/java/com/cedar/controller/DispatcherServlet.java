package com.cedar.controller;

import com.cedar.controller.frontend.MainPageController;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("request path is " + req.getServletPath());
        System.out.println("request method is " + req.getMethod());

        if (req.getServletPath().equals("/mainPage") && req.getMethod().equals("GET")) {
            new MainPageController().getMainPageInfo(req, resp);
        }
    }
}
