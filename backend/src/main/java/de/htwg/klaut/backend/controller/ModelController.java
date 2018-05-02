package de.htwg.klaut.backend.controller;

import de.htwg.klaut.backend.model.db.Model;
import de.htwg.klaut.backend.service.IModelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("model")
public class ModelController {

    private IModelService modelService;

    public ModelController(IModelService modelService) {
        this.modelService = modelService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<Model>> findAll(Pageable pageable) {
        return ResponseEntity.ok(modelService.getModels(pageable));
    }
}
