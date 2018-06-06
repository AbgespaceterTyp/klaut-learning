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
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Collection;

public interface IModelService<MODEL_PARAM_TYPE extends IModelParams> {

    /**
     * Lists the {@Link Model}s for current organization.
     *
     * @return a list of {@Link Collection<Model>}
     */
    Collection<Model> list();

    /**
     * Search for {@Link Model}s with given id in current organization.
     *
     * @param modelId the id to get {@Link Model} for.
     * @return the found model
     * @throws ModelNotFoundException in case of missing {@Link Model}
     */
    Model get(CompositeId modelId) throws ModelNotFoundException;

    /**
     * Creates a new {@Link Model} with values from given {@Link ModelDto}.
     *
     * @param modelDto the dto to get values from
     * @return the saved {@Link Model}
     * @throws ModelCreationException when failed to save/create model
     */
    Model create(ModelDto modelDto) throws ModelCreationException;

    /**
     * Updates the {@Link Model} for given {@Link CompositeId} with values from {@Link ModelDto}.
     *
     * @param modelId  the id to get model for
     * @param modelDto the dto to get values from
     * @return the updated {@Link Model}
     * @throws ModelNotFoundException
     */
    Model update(CompositeId modelId, ModelDto modelDto) throws ModelNotFoundException;

    /**
     * Trains the {@Link Model} for given {@Link CompositeId}.
     * A new {@Link ModelTrainingData} will be created and added.
     *
     * @param modelId the id to get model for
     * @return the updated {@Link Model}
     * @throws ModelNotFoundException  in case of missing {@Link Model}
     * @throws SourceNotFoundException
     */
    Model train(CompositeId modelId) throws ModelNotFoundException, SourceNotFoundException;

    /**
     * Adds a source file to {@Link Model} with given {@Link CompositeId}.
     *
     * @param modelId the id to get model for
     * @param file    the {@Link MultipartFile} to add
     * @return the updated {@Link Model}
     * @throws ModelNotFoundException  in case of missing {@Link Model}
     * @throws SourceCreationException in case of failed to create source file
     */
    Model addSourceFile(CompositeId modelId, MultipartFile file) throws ModelNotFoundException, SourceCreationException;

    /**
     * Get {@Link Model} source file for given url.
     *
     * @param modelSourceUrl
     * @return the {@Link InputStream} for given model source url
     * @throws SourceNotFoundException in case of missing model source file
     */
    InputStream getSourceFile(String modelSourceUrl) throws SourceNotFoundException;

    /**
     * Set params on {@Link Model} with given {@Link CompositeId} and {@Link MODEL_PARAM_TYPE}.
     *
     * @param modelId     the id to get model for
     * @param modelParams the params to set
     * @return the updated {@Link Model}
     * @throws ModelNotFoundException in case of missing {@Link Model}
     */
    Model setParams(CompositeId modelId, MODEL_PARAM_TYPE modelParams) throws ModelNotFoundException;

    /**
     * Deletes the {@Link Model} with given {@Link CompositeId}.
     *
     * @param modelId the id to get model for
     * @return the deleted {@Link Model}
     * @throws ModelNotFoundException  in case of missing {@Link Model}
     * @throws SourceNotFoundException in case of source files of {@Link Model} not found
     */
    Model delete(CompositeId modelId) throws ModelNotFoundException, SourceNotFoundException;

    /**
     * Get {@Link Collection<ModelTrainingData>} for {@Link Model} with given {@Link CompositeId}.
     *
     * @param modelId the id to get model for
     * @return {@Link Collection<ModelTrainingData>}
     * @throws ModelNotFoundException in case of missing {@Link Model}
     */
    Collection<ModelTrainingData> getTrainingData(CompositeId modelId) throws ModelNotFoundException;

    /**
     * Tests the {@Link Model} at given source url with given test word.
     *
     * @param modelSourceUrl the url to get model source from
     * @param testWord       the word to get test results for
     * @return {@Link Collection<String>} with results for test word
     * @throws ModelNotFoundException  in case of missing {@Link Model}
     * @throws SourceNotFoundException
     */
    Collection<String> test(String modelSourceUrl, String testWord) throws ModelNotFoundException, SourceNotFoundException;
}
