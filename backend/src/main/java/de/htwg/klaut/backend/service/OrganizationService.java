package de.htwg.klaut.backend.service;

import de.htwg.klaut.backend.model.Organization;
import de.htwg.klaut.backend.repository.IOrganizationRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@Slf4j
public class OrganizationService extends OncePerRequestFilter implements IOrganizationService {

    private IOrganizationRepository organizationRepository;

    @Getter
    private static ThreadLocal<String> currentOrganization = new ThreadLocal<>();

    public OrganizationService(IOrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Override
    public Organization createOrganization(String name, String iconUrl) {
        return organizationRepository.save(new Organization(name, iconUrl));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String organization = httpServletRequest.getRequestURI().split("/")[1];
        log.debug("Setting organization to {}", organization);
        currentOrganization.set(organization);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        // Clears the context after the request is done
        currentOrganization.set(null);
    }
}
