package de.htwg.klaut.backend.service;

import de.htwg.klaut.backend.model.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IModelService {
    Page<Model> findAll(Pageable pageable);
}
