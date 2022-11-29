package fi.aalto.amadei.graderapi.controllers.filters;

import fi.aalto.amadei.graderapi.DatabaseConnectionInitializer;
import fi.aalto.amadei.graderapi.beans.UserSessionBean;
import fi.aalto.amadei.graderapi.daos.AuthenticationDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Order(1)
public class AuthenticationFilter implements Filter {
    private final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        UserSessionBean newSession;
        newSession = AuthenticationDAO.createOrUpdateSession(
                DatabaseConnectionInitializer.getConnection(),
                httpRequest.getHeader("Authorization"));

        httpRequest.setAttribute("session", newSession);

        chain.doFilter(request, response);
    }
}
