package fi.aalto.amadei.graderapi.controllers;

import fi.aalto.amadei.graderapi.beans.UserSessionBean;
import fi.aalto.amadei.graderapi.beans.responses.SuccessBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class TestController {

    @GetMapping("/hello")
    public SuccessBean<String> hello(HttpServletRequest request, HttpServletResponse response) {
        return new SuccessBean<>(UserSessionBean.getSessionFromRequest(request), "Hello world!");
    }
}
