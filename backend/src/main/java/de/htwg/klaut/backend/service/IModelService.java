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
import de.htwg.klaut.backend.model.dto.ModelTrainingDataDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IModelService<MODEL_PARAM_TYPE extends IModelParams> {
    Collection<Model> list();

    Model create(ModelDto modelDto) throws ModelCreationException;

    Model update(CompositeId modelId, ModelDto modelDto) throws ModelNotFoundException;

    Model train(CompositeId modelId) throws ModelNotFoundException, SourceNotFoundException;

    Model addSourceFile(CompositeId modelId, MultipartFile file) throws ModelNotFoundException, SourceCreationException;

    InputStream getSourceFile(ModelTrainingDataDto modelId) throws SourceNotFoundException;

    Model setParams(CompositeId modelId, MODEL_PARAM_TYPE modelParams) throws ModelNotFoundException;

    Model delete(CompositeId modelId) throws ModelNotFoundException, SourceNotFoundException;

    Collection<ModelTrainingData> getTrainingData(CompositeId modelId) throws ModelNotFoundException;

    Collection<String> test(ModelTrainingDataDto trainingDataDto, String testWord) throws ModelNotFoundException, SourceNotFoundException;
}
