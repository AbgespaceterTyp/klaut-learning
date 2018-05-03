package de.htwg.klaut.backend.service;

import de.htwg.klaut.backend.model.ModelParams;
import de.htwg.klaut.backend.model.db.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface IModelService<MODEL_PARAM_TYPE extends ModelParams> {
    Page<Model> getModels(Pageable pageable);

    Model createModel(String modelName, String modelDescription) throws Exception;

    void trainModel(String modelId, Set<String> sourceUrls) throws Exception;

    void trainModel(String modelId, MODEL_PARAM_TYPE modelParams) throws Exception;

    void trainModel(String modelId, MODEL_PARAM_TYPE modelParams, Set<String> sourceUrls) throws Exception;

    void addSource(String modelId, String fileName) throws Exception;

    void setParams(String modelId, MODEL_PARAM_TYPE modelParams) throws Exception;

    void deleteModel(String modelId) throws Exception;
}
