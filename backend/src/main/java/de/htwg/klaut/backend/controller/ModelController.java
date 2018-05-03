package de.htwg.klaut.backend.controller;

import de.htwg.klaut.backend.model.db.Model;
import de.htwg.klaut.backend.service.IModelService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("model")
public class ModelController {

    private Logger logger = LogManager.getLogger(ModelController.class);
    private IModelService modelService;

    public ModelController(IModelService modelService) {
        this.modelService = modelService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<Model>> getModels(Pageable pageable) {
        return ResponseEntity.ok(modelService.getModels(pageable));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> createModel(@RequestParam(value = "modelName") String modelName,
                                                   @RequestParam(value = "modelDescription") String modelDescription) {
        try{
            final Model model = modelService.createModel(modelName, modelDescription);
            return new ResponseEntity<>(model.getSortKey(), HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error(e);
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteModel(@RequestParam(value = "modelId") String modelId){
        try{
            modelService.deleteModel(modelId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error(e);
            return ResponseEntity.badRequest().build();
        }
    }
}
