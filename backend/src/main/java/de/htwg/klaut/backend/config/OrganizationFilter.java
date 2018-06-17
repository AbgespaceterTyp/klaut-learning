package de.htwg.klaut.backend.config;

import de.htwg.klaut.backend.controller.IUserControllerPathConst;
import de.htwg.klaut.backend.service.IOrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class OrganizationFilter extends OncePerRequestFilter {


    private static final String ORGANIZATION_SESSION_KEY = "ORGANIZATION";
    private static final String WRONG_ORGANIZATION_MESSAGE = "Not authenticated for this organization";

    @Autowired
    private IOrganizationService organizationService;

    private List<AntPathRequestMatcher> requestMatchers;


    @PostConstruct
    private void createMatchers() {
        requestMatchers = new ArrayList<>();
        for (String pattern : SecurityConfig.AUTH_WHITELIST) {
            requestMatchers.add(new AntPathRequestMatcher(pattern));
        }
        AntPathRequestMatcher userMeAntPathRequestMatcher = new AntPathRequestMatcher("/".concat(IUserControllerPathConst.CONTROLLER_MAPPING
                .concat(IUserControllerPathConst.ME_MAPPING)), HttpMethod.DELETE.name());
        requestMatchers.add(userMeAntPathRequestMatcher);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        HttpSession httpSession = httpServletRequest.getSession(false);
        boolean clearContext = false;

        String organization = httpServletRequest.getRequestURI().split("/")[1];
        if (organization != null) {
            log.debug("Setting organization to {}", organization);
            organizationService.setCurrentOrganization(organization);
        }
        if (httpSession != null) {
            Object orgAttribute = httpSession.getAttribute(ORGANIZATION_SESSION_KEY);
            String currentOrganization = organizationService.getCurrentOrganization();
            if (orgAttribute != null && StringUtils.isNotEmpty(currentOrganization)) {
                // User was already authenticated before
                if (!orgAttribute.toString().equals(currentOrganization)) {
                    httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, WRONG_ORGANIZATION_MESSAGE);
                    clearContext = true;
                }
            } else {
                httpSession.setAttribute(ORGANIZATION_SESSION_KEY, currentOrganization);
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
        // Clears the context after the request is done
        organizationService.setCurrentOrganization(null);
        if (clearContext) {
            httpSession.invalidate();
            SecurityContextHolder.clearContext();
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // Filter if the request path is not on the white list
        return requestMatchers.stream()
                .anyMatch(pattern -> pattern.matches(request));
    }
}
