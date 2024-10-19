package com.ilhaha.yueyishou.rules.config;

import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class DroolsConfig {
    // 制定规则文件的路径
    private static final String[] RULES_CUSTOMER_RULES_DRL = {
            "rules/CustomerServiceFeeRule.drl",
            "rules/RecyclerServiceFeeRule.drl",
            "rules/RecyclerOvertimeFeeRule.drl"
    };

    @Bean
    public KieContainer kieContainer() {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();

        for (String rulePath : RULES_CUSTOMER_RULES_DRL) {
            kieFileSystem.write(ResourceFactory.newClassPathResource(rulePath));
        }

        KieBuilder kb = kieServices.newKieBuilder(kieFileSystem);
        kb.buildAll();

        KieModule kieModule = kb.getKieModule();
        KieContainer kieContainer = kieServices.newKieContainer(kieModule.getReleaseId());
        return kieContainer;
    }
}
