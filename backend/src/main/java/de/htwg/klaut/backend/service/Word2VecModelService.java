package de.htwg.klaut.backend.service;

import de.htwg.klaut.backend.model.ModelParams;
import de.htwg.klaut.backend.model.db.Model;
import de.htwg.klaut.backend.repository.ModelRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public class Word2VecModelService implements IModelService {

    private final ModelRepository modelRepository;

    public Word2VecModelService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    @Override
    public Page<Model> getModels(Pageable pageable) {
        return modelRepository.findAll(pageable);
    }

    @Override
    public Model createModel(String modelName, String modelDescription, ModelParams modelParams, Set<String> sourceUrls) {
        // TODO LG implement
        return null;
    }

    @Override
    public void trainsModel(String modelId, ModelParams modelParams, Set<String> sourceUrls) {
        // TODO LG implement
    }

    @Override
    public void deleteModel(String modelId) {
        // TODO LG implement
    }
}
