package de.htwg.klaut.backend.config;

import de.htwg.klaut.backend.service.IOrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class OrganizationFilter extends OncePerRequestFilter {

    @Autowired
    private IOrganizationService organizationService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String organization = httpServletRequest.getRequestURI().split("/")[1];
        log.debug("Setting organization to {}", organization);
        organizationService.setCurrentOrganization(organization);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        // Clears the context after the request is done
        organizationService.setCurrentOrganization(null);
    }
}
