package de.htwg.klaut.backend.controller;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.gson.JsonObject;
import de.htwg.klaut.backend.model.Word2VecParams;
import de.htwg.klaut.backend.model.db.CompositeId;
import de.htwg.klaut.backend.model.db.Model;
import de.htwg.klaut.backend.model.dto.ModelDto;
import de.htwg.klaut.backend.service.IModelService;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
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

    @PostMapping
    public ResponseEntity<String> createModel(@PathVariable String organization, @RequestBody ModelDto modelDto) {
        final Model model = modelService.createModel(modelDto);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String modelIdAsJson = null;
        try {
            modelIdAsJson = ow.writeValueAsString(model.getId());
        } catch (JsonProcessingException e) {
            log.error("failed to write model id as json", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return new ResponseEntity<>(modelIdAsJson, HttpStatus.CREATED);
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
