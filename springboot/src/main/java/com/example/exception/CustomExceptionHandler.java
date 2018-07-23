package com.example.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: Capturing Global exceptions.
 * @Author: WangY
 * @Date: Created in 2018/7/20 15:28
 * @Modified By：
 */

//@ControllerAdvice
public class CustomExceptionHandler {

    public static final String ERROR_VIEW = "error";

//    @ExceptionHandler(value = Exception.class)
    public Object errorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        e.printStackTrace();

        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", request.getRequestURL());
        mav.setViewName(ERROR_VIEW);
        return mav;
    }

    /**
     * @Desc 使用该注解的方法将在所有应用@RequestMapping注解的方法执行前初始化数据绑定器
     * @param binder
     */
//    @InitBinder
    public void initBinder(WebDataBinder binder) {

    }

    /**
     * @Desc 将值绑定到Model中，使全局应用@RequestMapping注解的方法都可以获取到该值
     * @param model
     */
//    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("author", "WangY");
    }

    /**
     * @Desc 判断是否为Ajax请求
     * @param request
     * @return
     */
    public static boolean isAjax(HttpServletRequest request) {
        Boolean flag = false;
        if (request.getHeader("X-Requested-With") != null &&
                "XMLHttpRequest".equals(request.getHeader("X-Requested-With").toString())) {
            flag = true;
        }
        return flag;
    }
}
