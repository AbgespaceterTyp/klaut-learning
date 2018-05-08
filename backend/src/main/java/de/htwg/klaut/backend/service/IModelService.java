package de.htwg.klaut.backend.service;

import de.htwg.klaut.backend.exception.ModelNotFoundException;
import de.htwg.klaut.backend.exception.SourceNotFoundException;
import de.htwg.klaut.backend.model.IModelParams;
import de.htwg.klaut.backend.model.db.CompositeId;
import de.htwg.klaut.backend.model.db.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface IModelService<MODEL_PARAM_TYPE extends IModelParams> {
    Page<Model> getModels(String organization, Pageable pageable);

    Model createModel(String modelName, String modelDescription, String organization) throws ModelNotFoundException;

    void trainModel(CompositeId modelId, String organization) throws ModelNotFoundException, SourceNotFoundException;

    void addSource(CompositeId modelId, String fileName, String organization) throws ModelNotFoundException, SourceNotFoundException;

    void setParams(CompositeId modelId, MODEL_PARAM_TYPE modelParams) throws ModelNotFoundException;

    void deleteModel(CompositeId modelId) throws ModelNotFoundException, SourceNotFoundException;
}
