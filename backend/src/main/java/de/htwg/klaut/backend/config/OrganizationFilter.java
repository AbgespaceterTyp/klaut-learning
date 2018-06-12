package de.htwg.klaut.backend.config;

import de.htwg.klaut.backend.service.IOrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class OrganizationFilter extends OncePerRequestFilter {

    @Autowired
    private IOrganizationService organizationService;

    private List<AntPathRequestMatcher> requestMatchers;

    @PostConstruct
    private void createMatchers() {
        requestMatchers = new ArrayList<>();
        for (String pattern : SecurityConfig.AUTH_WHITELIST) {
            requestMatchers.add(new AntPathRequestMatcher(pattern));
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        String organization = httpServletRequest.getRequestURI().split("/")[1];
        if (organization != null) {
            log.info("Setting organization to {}", organization);
            organizationService.setCurrentOrganization(organization);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        // Clears the context after the request is done
        organizationService.setCurrentOrganization(null);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // Filter if the request path is not on the white list
        return requestMatchers.stream()
                .anyMatch(pattern -> pattern.matches(request));
    }
}
