package de.htwg.klaut.backend.service.controller;

import de.htwg.klaut.backend.controller.IModelControllerPathConst;
import de.htwg.klaut.backend.controller.ModelController;
import de.htwg.klaut.backend.exception.ModelNotFoundException;
import de.htwg.klaut.backend.exception.SourceNotFoundException;
import de.htwg.klaut.backend.model.db.CompositeId;
import de.htwg.klaut.backend.model.db.Model;
import de.htwg.klaut.backend.model.db.ModelTrainingData;
import de.htwg.klaut.backend.model.db.Word2VecParams;
import de.htwg.klaut.backend.service.IModelService;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class ModelControllerTest {

    private MockMvc mockMvc;

    private String organization = "klaut-testing";
    private String modelId = "080a59eb-869d-43f3-8237-0ad223cbace4";

    @Mock
    private IModelService<Word2VecParams> modelService;
    @InjectMocks
    private ModelController modelController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(modelController).build();
    }

    @Test
    public void shouldReturnListOfModelsForOrganization() throws Exception {
        Mockito.when(modelService.list()).thenReturn(createSampleModels());

        mockMvc.perform(get("/" + IModelControllerPathConst.CONTROLLER_MAPPING, organization))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void shouldReturnTrainingData() throws Exception {
        Mockito.when(modelService.getTrainingData(createSampleModelId())).thenReturn(createSampleTrainingData());

        mockMvc.perform(get("/" + IModelControllerPathConst.CONTROLLER_MAPPING + "/" + IModelControllerPathConst.TRAIN_DATA_MAPPING, organization, modelId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void shouldFailOnReturningTrainingDataWhenModelNotExists() throws Exception {
        Mockito.when(modelService.getTrainingData(createSampleModelId())).thenThrow(ModelNotFoundException.class);

        mockMvc.perform(get("/" + IModelControllerPathConst.CONTROLLER_MAPPING + "/" + IModelControllerPathConst.TRAIN_DATA_MAPPING, organization, modelId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnSynonymsForTestInput() throws Exception {
        final String sourceUrl = "some model url";
        final String testWord = "graf";
        Mockito.when(modelService.test(sourceUrl, testWord)).thenReturn(createSampleSynonyms());

        mockMvc.perform(get("/" + IModelControllerPathConst.CONTROLLER_MAPPING + "/" + IModelControllerPathConst.TEST_MAPPING, organization, modelId)
                .param("modelSourceUrl", sourceUrl)
                .param("testWord", testWord))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.results", hasSize(3)));
    }

    @Test
    public void shouldFailOnReturningSynonymsWhenModelNotExists() throws Exception {
        final String sourceUrl = "some model url";
        final String testWord = "graf";
        Mockito.when(modelService.test(sourceUrl, testWord)).thenThrow(ModelNotFoundException.class);

        mockMvc.perform(get("/" + IModelControllerPathConst.CONTROLLER_MAPPING + "/" + IModelControllerPathConst.TEST_MAPPING, organization, modelId)
                .param("modelSourceUrl", sourceUrl)
                .param("testWord", testWord))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldFailOnReturningSynonymsWhenSourceNotExists() throws Exception {
        final String sourceUrl = "some model url";
        final String testWord = "graf";
        Mockito.when(modelService.test(sourceUrl, testWord)).thenThrow(SourceNotFoundException.class);

        mockMvc.perform(get("/" + IModelControllerPathConst.CONTROLLER_MAPPING + "/" + IModelControllerPathConst.TEST_MAPPING, organization, modelId)
                .param("modelSourceUrl", sourceUrl)
                .param("testWord", testWord))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeleteModel() throws Exception {
        mockMvc.perform(delete("/" + IModelControllerPathConst.CONTROLLER_MAPPING + "/" + IModelControllerPathConst.DELETE_MAPPING, organization, modelId))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldFailOnDeleteWhenModelNotExist() throws Exception {
        Mockito.when(modelService.delete(createSampleModelId())).thenThrow(ModelNotFoundException.class);

        mockMvc.perform(delete("/" + IModelControllerPathConst.CONTROLLER_MAPPING + "/" + IModelControllerPathConst.DELETE_MAPPING, organization, modelId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldFailOnDeleteWhenSourceFilesNotExist() throws Exception {
        Mockito.when(modelService.delete(createSampleModelId())).thenThrow(SourceNotFoundException.class);

        mockMvc.perform(delete("/" + IModelControllerPathConst.CONTROLLER_MAPPING + "/" + IModelControllerPathConst.DELETE_MAPPING, organization, modelId))
                .andExpect(status().isNotFound());
    }

    private CompositeId createSampleModelId() {
        return new CompositeId(organization, modelId);
    }

    private List<Model> createSampleModels() {
        List<Model> results = new LinkedList<>();

        final Model model = new Model();
        model.setId(modelId);
        model.setOrganization(organization);
        model.setName("Test Model");
        model.setDescription("Test Desc");
        model.setAlgorithm("Word2Vec");
        results.add(model);

        return results;
    }

    private List<ModelTrainingData> createSampleTrainingData() {
        List<ModelTrainingData> results = new LinkedList<>();

        final ModelTrainingData modelTrainingData = new ModelTrainingData();
        modelTrainingData.setModelUrl("someSampleUrl");
        modelTrainingData.setLastTrainingStart(DateTime.now().toDate());
        modelTrainingData.setLastTrainingEnd(DateTime.now().toDate());
        results.add(modelTrainingData);

        return results;
    }

    private List<String> createSampleSynonyms() {
        List<String> results = new LinkedList<>();

        results.add("Graf");
        results.add("Herr");
        results.add("Junge");

        return results;
    }
}
