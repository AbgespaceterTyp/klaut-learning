package de.htwg.klaut.backend.service;

import de.htwg.klaut.backend.model.ModelParams;
import de.htwg.klaut.backend.model.db.Model;
import de.htwg.klaut.backend.repository.ModelRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
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
        // store source files
        // create model, link with source files and set model params if set
        // return created model
        return null;
    }

    @Override
    public void trainModel(String modelId, Set<String> sourceUrls) {
        trainModel(modelId, null, Collections.emptySet());
    }

    @Override
    public void trainModel(String modelId, ModelParams modelParams) {
        trainModel(modelId, modelParams, Collections.emptySet());
    }

    @Override
    public void trainModel(String modelId, ModelParams modelParams, Set<String> sourceUrls) {
        // TODO LG implement
        // store source files
        // load model from db and link new source urls
        // update model for new params if set
        // start training
        // store model again
    }

    @Override
    public void deleteModel(String modelId) {
        // TODO LG implement
        // load model from db
        // delete files from s3
        // delete model
    }
}
