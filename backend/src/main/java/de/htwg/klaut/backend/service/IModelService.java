package de.htwg.klaut.backend.service;

import de.htwg.klaut.backend.model.IModelParams;
import de.htwg.klaut.backend.model.db.CompositeId;
import de.htwg.klaut.backend.model.db.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IModelService<MODEL_PARAM_TYPE extends IModelParams> {
    Page<Model> getModels(Pageable pageable);

    Model createModel(String modelName, String modelDescription, String organization) throws Exception;

    void trainModel(CompositeId modelId) throws Exception;

    void addSource(CompositeId modelId, String fileName) throws Exception;

    void setParams(CompositeId modelId, MODEL_PARAM_TYPE modelParams) throws Exception;

    void deleteModel(CompositeId modelId) throws Exception;
}
