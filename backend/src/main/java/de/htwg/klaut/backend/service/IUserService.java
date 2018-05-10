package de.htwg.klaut.backend.service;

import de.htwg.klaut.backend.model.CloudUser;

public interface IUserService {
    CloudUser save(CloudUser cloudUser);
}
