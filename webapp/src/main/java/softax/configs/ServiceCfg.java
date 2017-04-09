package softax.configs;

import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceCfg {

    @Bean
    public Validator beanValidator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

}
