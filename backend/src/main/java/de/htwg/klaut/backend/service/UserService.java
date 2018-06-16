package de.htwg.klaut.backend.service;

import de.htwg.klaut.backend.exception.UserCreationException;
import de.htwg.klaut.backend.model.db.CloudUser;
import de.htwg.klaut.backend.model.db.CreateUserDto;
import de.htwg.klaut.backend.repository.IUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
@Slf4j
public class UserService implements UserDetailsService, IUserService {

    private IUserRepository userRepository;

    private IOrganizationService organizationService;

    private PasswordEncoder passwordEncoder;

    public UserService(IUserRepository userRepository, IOrganizationService organizationService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.organizationService = organizationService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // Username = email
        String currentOrganization = organizationService.getCurrentOrganization();

        Optional<CloudUser> cloudUser = userRepository.findByEmailAndOrganization(s, currentOrganization);

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

    @Override
    public CloudUser create(String email, String firstName, String lastName,
                            String password, String organizationKey) {

        Optional<CloudUser> cloudUser = userRepository.findByEmailAndOrganization(email, organizationKey);

        if (cloudUser.isPresent()) {
            throw new UserCreationException();
        }

        log.debug("creating user", email);

        String passwordHash = passwordEncoder.encode(password);
        CloudUser user = CloudUser.builder()
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .passwordHash(passwordHash)
                .build();
        user.setOrganization(organizationKey);

        return save(user);
    }

    @Override
    public CloudUser create(CreateUserDto createUserDto) {
        String currentOrganization = organizationService.getCurrentOrganization();
        return create(createUserDto.getEmail(), createUserDto.getFirstName(),
                createUserDto.getLastName(), createUserDto.getPassword(), currentOrganization);
    }

    @Override
    public Collection<CloudUser> findAll() {
        return userRepository.findByOrganization(organizationService.getCurrentOrganization());
    }
}
