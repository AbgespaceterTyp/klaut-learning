package de.htwg.klaut.backend.service;

import de.htwg.klaut.backend.model.CloudUser;
import de.htwg.klaut.backend.repository.IUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService, IUserService {

    private IUserRepository userRepository;

    private IOrganizationService organizationService;

    public UserService(IUserRepository userRepository, IOrganizationService organizationService) {
        this.userRepository = userRepository;
        this.organizationService = organizationService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // Username = email
        String currentOrganization = organizationService.getCurrentOrganization();

        Optional<CloudUser> cloudUser = userRepository.readByEmailAndOrganization(s, currentOrganization);

        if (cloudUser.isPresent()) {
            CloudUser user = cloudUser.get();
            return new User(user.getEmail(), user.getPasswordHash(),
                    Collections.singleton(new SimpleGrantedAuthority("USER")));
        } else {
            throw new UsernameNotFoundException(s);
        }
    }

    @Override
    public CloudUser save(CloudUser cloudUser) {
        return userRepository.save(cloudUser);
    }
}
