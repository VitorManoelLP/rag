package com.rag.demo.configuration;

import org.hibernate.boot.model.TypeContributor;
import org.hibernate.vector.PGVectorTypeContributor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfiguration {
    @Bean
    public TypeContributor pgVectorTypeContributor() {
        return new PGVectorTypeContributor();
    }
}