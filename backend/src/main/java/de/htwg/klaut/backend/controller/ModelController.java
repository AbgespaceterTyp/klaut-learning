package de.htwg.klaut.backend.controller;


import de.htwg.klaut.backend.model.db.CompositeId;
import de.htwg.klaut.backend.model.db.Model;
import de.htwg.klaut.backend.model.db.ModelTrainingData;
import de.htwg.klaut.backend.model.db.Word2VecParams;
import de.htwg.klaut.backend.model.dto.IdDto;
import de.htwg.klaut.backend.model.dto.ModelDto;
import de.htwg.klaut.backend.model.dto.ModelTestResultDto;
import de.htwg.klaut.backend.model.dto.ModelTrainingDataDto;
import de.htwg.klaut.backend.service.IModelService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static de.htwg.klaut.backend.controller.IModelControllerPathConst.CONTROLLER_MAPPING;

@Controller
@RequestMapping(CONTROLLER_MAPPING)
@Log4j2
public class ModelController implements IModelControllerPathConst {

    private IModelService<Word2VecParams> modelService;

    public ModelController(IModelService<Word2VecParams> modelService) {
        this.modelService = modelService;
    }

    @GetMapping
    public ResponseEntity<Collection<Model>> list(@PathVariable String organization) {
        return new ResponseEntity<>(modelService.list(), HttpStatus.OK);
    }

    @GetMapping(path = GET_MAPPING)
    public ResponseEntity<Model> get(@PathVariable String organization, @PathVariable String modelId) {
        return new ResponseEntity<>(modelService.get(new CompositeId(organization, modelId)), HttpStatus.OK);
    }

    @GetMapping(path = TRAIN_DATA_MAPPING)
    public ResponseEntity<Collection<ModelTrainingDataDto>> trainingData(@PathVariable String organization, @PathVariable String modelId) {
        final Collection<ModelTrainingData> trainingsData = modelService.getTrainingData(new CompositeId(organization, modelId));
        List<ModelTrainingDataDto> results = new LinkedList<>();
        for (ModelTrainingData trainingData : trainingsData) {
            results.add(new ModelTrainingDataDto(trainingData));
        }
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @GetMapping(path = TEST_MAPPING)
    public ResponseEntity<ModelTestResultDto> test(@PathVariable String organization, @RequestParam String modelSourceUrl, @RequestParam String testWord) {
        final Collection<String> testResults = modelService.test(modelSourceUrl, testWord);
        return new ResponseEntity<>(new ModelTestResultDto(testResults), HttpStatus.OK);
    }

    @GetMapping(path = DOWNLOAD_MAPPING)
    public void download(@RequestParam String modelSourceUrl, HttpServletResponse response) {
        try {
            InputStream is = modelService.getSourceFile(modelSourceUrl);
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            log.error("Failed to download model file for training data {}", modelSourceUrl);
        }
    }

    @PostMapping
    public ResponseEntity<IdDto> create(@PathVariable String organization, @RequestBody ModelDto modelDto) {
        final Model model = modelService.create(modelDto);
        return new ResponseEntity<>(new IdDto(model.getId()), HttpStatus.CREATED);
    }

    @PutMapping(path = UPDATE_MAPPING)
    public ResponseEntity update(@PathVariable String organization, @RequestBody ModelDto modelDto, @PathVariable String modelId) {
        modelService.update(new CompositeId(organization, modelId), modelDto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = PARAM_MAPPING)
    public ResponseEntity setParameter(@PathVariable String organization, @RequestBody Word2VecParams params, @PathVariable String modelId) {
        modelService.setParams(new CompositeId(organization, modelId), params);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = TRAIN_MAPPING)
    public ResponseEntity train(@PathVariable String organization, @PathVariable String modelId) {
        modelService.train(new CompositeId(organization, modelId));
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = SOURCE_MAPPING)
    public ResponseEntity addSource(@PathVariable String organization, @RequestBody MultipartFile fileToUpload, @PathVariable String modelId) {
        if (fileToUpload.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        modelService.addSourceFile(new CompositeId(organization, modelId), fileToUpload);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = DELETE_MAPPING)
    public ResponseEntity delete(@PathVariable String organization, @PathVariable String modelId) {
        modelService.delete(new CompositeId(organization, modelId));
        return ResponseEntity.ok().build();
    }
}
