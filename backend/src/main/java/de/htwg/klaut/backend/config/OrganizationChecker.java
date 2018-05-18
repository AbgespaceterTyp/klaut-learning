package de.htwg.klaut.backend.config;

import de.htwg.klaut.backend.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
@Slf4j
public class OrganizationChecker extends GenericFilterBean {

    private static final String ORGANIZATION_SESSION_KEY = "ORGANIZATION";
    private static final String WRONG_ORGANIZATION_MESSAGE = "Not authenticated for this organization";

    @Autowired
    private OrganizationService organizationService;

    @Override
    // Is never called if the authentication was not successful
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpSession httpSession = request.getSession(false);
        if (httpSession != null) {
            Object orgAttribute = httpSession.getAttribute(ORGANIZATION_SESSION_KEY);
            String currentOrganization = organizationService.getCurrentOrganization();
            // TODO JD Review, fixed possible NPE when current organization was null
            if (orgAttribute != null && StringUtils.isNotEmpty(currentOrganization)) {
                // User was already authenticated before
                if (!orgAttribute.toString().equals(currentOrganization)) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, WRONG_ORGANIZATION_MESSAGE);
                }
            } else {
                httpSession.setAttribute(ORGANIZATION_SESSION_KEY, currentOrganization);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
