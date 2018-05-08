package de.htwg.klaut.backend.controller;

import de.htwg.klaut.backend.model.Word2VecParams;
import de.htwg.klaut.backend.model.db.CompositeId;
import de.htwg.klaut.backend.model.db.Model;
import de.htwg.klaut.backend.model.dto.ModelDto;
import de.htwg.klaut.backend.service.IModelService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("{organization}/model")
public class ModelController {

    private IModelService<Word2VecParams> modelService;

    public ModelController(IModelService<Word2VecParams> modelService) {
        this.modelService = modelService;
    }

    @GetMapping
    public ResponseEntity<Page<Model>> getModels(@PathVariable String organization, Pageable pageable) {
        return new ResponseEntity<>(modelService.getModels(organization, pageable), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createModel(@PathVariable String organization, @RequestBody ModelDto modelDto) {
        final Model model = modelService.createModel(modelDto.getName(), modelDto.getDescription(), organization);
        return new ResponseEntity<>(model.getId(), HttpStatus.CREATED);
    }

    @PutMapping(path = "{modelId}/param")
    public ResponseEntity setParameter(@PathVariable String organization, @RequestBody Word2VecParams params, @PathVariable String modelId) {
        modelService.setParams(new CompositeId(organization, modelId), params);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "{modelId}/train")
    public ResponseEntity trainModel(@PathVariable String organization, @PathVariable String modelId) {
        modelService.trainModel(new CompositeId(organization, modelId),organization);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "{modelId}/source")
    public ResponseEntity addSource(@PathVariable String organization, @PathVariable String modelId) {
        // TODO LG copy uploaded file to temp and add as source
        modelService.addSource(new CompositeId(organization, modelId), "fileName", organization);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteModel(@PathVariable String organization, @PathVariable String modelId) {
        modelService.deleteModel(new CompositeId(organization, modelId));
        return ResponseEntity.ok().build();
    }
}
