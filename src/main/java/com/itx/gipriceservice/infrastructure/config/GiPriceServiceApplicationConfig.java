package com.itx.gipriceservice.infrastructure.config;

import com.itx.gipriceservice.application.usecase.GetPriceUseCase;
import com.itx.gipriceservice.domain.port.PriceRepository;
import com.itx.gipriceservice.domain.service.HighestPriorityPricingRule;
import com.itx.gipriceservice.domain.service.PricingRule;
import com.itx.gipriceservice.domain.service.PricingService;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

import java.util.List;

@Configuration
public class GiPriceServiceApplicationConfig {

    @Bean
    ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        Resource initSchema = new ClassPathResource("in/db/schema.sql");
        Resource initData = new ClassPathResource("in/db/import.sql");
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(initSchema, initData));
        return initializer;
    }

    /**
     * Registers the active pricing rules in evaluation order.
     * To add a new rule: implement {@link PricingRule} and add it to this list
     * before {@link HighestPriorityPricingRule} (which is the catch-all fallback).
     */
    @Bean
    PricingService pricingService() {
        List<PricingRule> rules = List.of(
                new HighestPriorityPricingRule()
        );
        return new PricingService(rules);
    }

    @Bean
    GetPriceUseCase getPriceUseCase(PriceRepository priceRepository, PricingService pricingService) {
        return new GetPriceUseCase(priceRepository, pricingService);
    }

    @Bean
    HttpStatus defaultStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

}
