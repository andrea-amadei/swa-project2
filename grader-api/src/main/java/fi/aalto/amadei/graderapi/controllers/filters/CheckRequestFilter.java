package fi.aalto.amadei.graderapi.controllers.filters;

import fi.aalto.amadei.graderapi.beans.UserSessionBean;
import fi.aalto.amadei.graderapi.exceptions.InvalidUserSessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Connection;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class CheckRequestFilter implements Filter {
    private final Logger logger = LoggerFactory.getLogger(CheckRequestFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        UserSessionBean session = UserSessionBean.getSessionFromRequest(request);

        if(session == null)
            throw new InvalidUserSessionException("Unable to confirm user session");

        chain.doFilter(request, response);
    }
}
