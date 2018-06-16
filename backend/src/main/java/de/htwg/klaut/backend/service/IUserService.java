package de.htwg.klaut.backend.service;

import de.htwg.klaut.backend.model.db.CloudUser;
import de.htwg.klaut.backend.model.db.CreateUserDto;

public interface IUserService {

    /**
     * Saves the given {@Link CloudUser} to Database
     *
     * @param cloudUser the user to save
     * @return the updated user object
     */
    CloudUser save(CloudUser cloudUser);

    /**
     * Creates a new CloudUser and saves it to the Database
     *
     * @param email           The user's email
     * @param firstName       The user's first name
     * @param lastName        The user's last name
     * @param password        The user's password (plain text, not hashed)
     * @param organizationKey The Key of the existing organization
     * @return the newly created CloudUser object
     */
    CloudUser create(String email, String firstName, String lastName, String password, String organizationKey);

    /**
     * @param createUserDto The users information
     * @return the newly created CloudUser object
     */
    CloudUser create(CreateUserDto createUserDto);
}
