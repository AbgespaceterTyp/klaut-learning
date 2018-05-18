package de.htwg.klaut.backend.service;

import de.htwg.klaut.backend.exception.ModelCreationException;
import de.htwg.klaut.backend.exception.ModelNotFoundException;
import de.htwg.klaut.backend.exception.SourceCreationException;
import de.htwg.klaut.backend.exception.SourceNotFoundException;
import de.htwg.klaut.backend.model.db.CompositeId;
import de.htwg.klaut.backend.model.db.IModelParams;
import de.htwg.klaut.backend.model.db.Model;
import de.htwg.klaut.backend.model.db.ModelTrainingData;
import de.htwg.klaut.backend.model.dto.ModelDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

public interface IModelService<MODEL_PARAM_TYPE extends IModelParams> {
    Page<Model> getModels(Pageable pageable);

    Model createModel(ModelDto modelDto) throws ModelCreationException;

    void updateModel(CompositeId modelId, ModelDto modelDto) throws ModelNotFoundException;

    void trainModel(CompositeId modelId) throws ModelNotFoundException, SourceNotFoundException;

    void addSourceFileToModel(CompositeId modelId, MultipartFile file) throws ModelNotFoundException, SourceCreationException;

    void setModelParams(CompositeId modelId, MODEL_PARAM_TYPE modelParams) throws ModelNotFoundException;

    void deleteModel(CompositeId modelId) throws ModelNotFoundException, SourceNotFoundException;

    Collection<ModelTrainingData> getTrainingsData(CompositeId modelId) throws ModelNotFoundException;
}
