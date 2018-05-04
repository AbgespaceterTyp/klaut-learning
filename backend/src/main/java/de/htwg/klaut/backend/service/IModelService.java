package de.htwg.klaut.backend.service;

import de.htwg.klaut.backend.model.IModelParams;
import de.htwg.klaut.backend.model.db.Model;
import de.htwg.klaut.backend.model.db.ModelCompositeId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface IModelService<MODEL_PARAM_TYPE extends IModelParams> {
    Page<Model> getModels(Pageable pageable);

    Model createModel(String modelName, String modelDescription) throws Exception;

    void trainModel(ModelCompositeId modelId, Set<String> sourceUrls) throws Exception;

    void trainModel(ModelCompositeId modelId, MODEL_PARAM_TYPE modelParams) throws Exception;

    void trainModel(ModelCompositeId modelId, MODEL_PARAM_TYPE modelParams, Set<String> sourceUrls) throws Exception;

    void addSource(ModelCompositeId modelId, String fileName) throws Exception;

    void setParams(ModelCompositeId modelId, MODEL_PARAM_TYPE modelParams) throws Exception;

    void deleteModel(ModelCompositeId modelId) throws Exception;
}
