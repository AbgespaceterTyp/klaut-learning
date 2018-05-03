package de.htwg.klaut.backend.config;

import de.htwg.klaut.backend.model.Word2VecParams;
import de.htwg.klaut.backend.repository.ModelRepository;
import de.htwg.klaut.backend.service.IModelService;
import de.htwg.klaut.backend.service.Word2VecModelService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public IModelService<Word2VecParams> word2VecModelService(ModelRepository modelRepository){
        return new Word2VecModelService(modelRepository);
    }
}
