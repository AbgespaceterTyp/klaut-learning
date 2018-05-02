package de.htwg.klaut.backend.service;

import de.htwg.klaut.backend.model.ModelParams;
import de.htwg.klaut.backend.model.db.Model;
import de.htwg.klaut.backend.repository.ModelRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class Word2VecModelService implements IModelService{

    private final ModelRepository modelRepository;

    public Word2VecModelService(ModelRepository modelRepository){
        this.modelRepository = modelRepository;
    }

    @Override
    public Page<Model> findAll(Pageable pageable) {
        return modelRepository.findAll(pageable);
    }
}
