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
import de.htwg.klaut.backend.model.dto.ModelTestResultDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

public interface IModelService<MODEL_PARAM_TYPE extends IModelParams> {
    Page<Model> list(Pageable pageable);

    Model create(ModelDto modelDto) throws ModelCreationException;

    void update(CompositeId modelId, ModelDto modelDto) throws ModelNotFoundException;

    void train(CompositeId modelId) throws ModelNotFoundException, SourceNotFoundException;

    void addSourceFile(CompositeId modelId, MultipartFile file) throws ModelNotFoundException, SourceCreationException;

    void setParams(CompositeId modelId, MODEL_PARAM_TYPE modelParams) throws ModelNotFoundException;

    void delete(CompositeId modelId) throws ModelNotFoundException, SourceNotFoundException;

    Collection<ModelTrainingData> getTrainingData(CompositeId modelId) throws ModelNotFoundException;

    Collection<String> test(CompositeId modelId, String testWord) throws ModelNotFoundException, SourceNotFoundException;
}
