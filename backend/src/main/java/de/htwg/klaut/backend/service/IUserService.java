package de.htwg.klaut.backend.service;

import de.htwg.klaut.backend.model.db.CloudUser;

public interface IUserService {

    /**
     * Saves the given {@Link CloudUser} to Database
     * @param cloudUser the user to save
     * @return the updated user object
     */
    CloudUser save(CloudUser cloudUser);
}
