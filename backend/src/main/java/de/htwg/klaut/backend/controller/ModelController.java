package de.htwg.klaut.backend.controller;


import de.htwg.klaut.backend.model.db.CompositeId;
import de.htwg.klaut.backend.model.db.Model;
import de.htwg.klaut.backend.model.db.ModelTrainingData;
import de.htwg.klaut.backend.model.db.Word2VecParams;
import de.htwg.klaut.backend.model.dto.IdDto;
import de.htwg.klaut.backend.model.dto.ModelDto;
import de.htwg.klaut.backend.model.dto.ModelTrainingDataDto;
import de.htwg.klaut.backend.service.IModelService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("{organization}/model")
@Log4j2
public class ModelController {

    private IModelService<Word2VecParams> modelService;

    public ModelController(IModelService<Word2VecParams> modelService) {
        this.modelService = modelService;
    }

    @GetMapping
    public ResponseEntity<Page<Model>> getModels(@PathVariable String organization, Pageable pageable) {
        return new ResponseEntity<>(modelService.getModels(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "{modelId}/trainData")
    public ResponseEntity<Collection<ModelTrainingDataDto>> getTrainingsData(@PathVariable String organization, @PathVariable String modelId) {
        final Collection<ModelTrainingData> trainingsData = modelService.getTrainingsData(new CompositeId(organization, modelId));
        List<ModelTrainingDataDto> results = new LinkedList<>();
        for (ModelTrainingData trainingData : trainingsData) {
            results.add(new ModelTrainingDataDto(trainingData));
        }
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<IdDto> createModel(@PathVariable String organization, @RequestBody ModelDto modelDto) {
        final Model model = modelService.createModel(modelDto);
        return new ResponseEntity<>(new IdDto(model.getId()), HttpStatus.CREATED);
    }

    @PutMapping(path = "{modelId}/update")
    public ResponseEntity updateModel(@PathVariable String organization, @RequestBody ModelDto modelDto, @PathVariable String modelId) {
        modelService.updateModel(new CompositeId(organization, modelId), modelDto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "{modelId}/param")
    public ResponseEntity setParameter(@PathVariable String organization, @RequestBody Word2VecParams params, @PathVariable String modelId) {
        modelService.setModelParams(new CompositeId(organization, modelId), params);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "{modelId}/train")
    public ResponseEntity trainModel(@PathVariable String organization, @PathVariable String modelId) {
        modelService.trainModel(new CompositeId(organization, modelId));
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "{modelId}/source")
    public ResponseEntity addSource(@PathVariable String organization, @RequestBody MultipartFile fileToUpload, @PathVariable String modelId) {
        if (fileToUpload.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        modelService.addSourceFileToModel(new CompositeId(organization, modelId), fileToUpload);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "{modelId}/delete")
    public ResponseEntity deleteModel(@PathVariable String organization, @PathVariable String modelId) {
        modelService.deleteModel(new CompositeId(organization, modelId));
        return ResponseEntity.ok().build();
    }
}
