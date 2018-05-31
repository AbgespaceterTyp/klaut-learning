package de.htwg.klaut.backend.controller;

public interface IModelControllerPathConst {
    static String CONTROLLER_MAPPING = "{organization}/model";
    static String TRAIN_DATA_MAPPING = "{modelId}/trainData";
    static String TEST_MAPPING = "{modelId}/test";
    static String DOWNLOAD_MAPPING = "{modelId}/download";
    static String UPDATE_MAPPING = "{modelId}/update";
    static String PARAM_MAPPING = "{modelId}/param";
    static String TRAIN_MAPPING = "{modelId}/train";
    static String SOURCE_MAPPING = "{modelId}/source";
    static String DELETE_MAPPING = "{modelId}/delete";
}
