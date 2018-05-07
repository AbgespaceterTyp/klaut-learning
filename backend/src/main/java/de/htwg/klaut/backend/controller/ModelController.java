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
        try {
            // TODO JD set company/tenant here
            final Model model = modelService.createModel(modelDto.getName(), modelDto.getDescription(), "klaut-learning");
            return new ResponseEntity<>(model.getId(), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(path = "{modelId}/param")
    public ResponseEntity setParameter(@PathVariable String organization, @RequestBody Word2VecParams params, @PathVariable String modelId) {
        try {
            // TODO JD set company/tenant here
            modelService.setParams(new CompositeId("klaut-learning", modelId), params);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(path = "{modelId}/train")
    public ResponseEntity trainModel(@PathVariable String organization, @PathVariable String modelId) {
        try {
            // TODO JD set company/tenant here
            modelService.trainModel(new CompositeId("klaut-learning", modelId));
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(path = "{modelId}/source")
    public ResponseEntity addSource(@PathVariable String organization, @PathVariable String modelId) {
        try {
            // TODO JD set company/tenant here
            // TODO LG copy uploaded file to temp and add as source
            //modelService.addSource(new CompositeId("klaut-learning", modelId));
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteModel(@PathVariable String organization, @PathVariable String modelId) {
        try {
            // TODO JD set company/tenant here
            modelService.deleteModel(new CompositeId("klaut-learning", modelId));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.badRequest().build();
        }
    }
}
