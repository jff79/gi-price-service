package com.itx.gipriceservice;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GiPriceServiceApplicationTests {

    public static final String BASE_PACKAGE = "com.itx.gipriceservice";

    private static final String JAVA_STANDARD_LIBRARY = "java..";

    private static final String REACTOR_STANDARD_LIBRARY = "reactor..";

    private static final String JAKARTA_STANDARD_LIBRARY = "jakarta..";

    private static final String LOMBOK_LIBRARY = "lombok..";

    private static final String SLF4J_LIBRARY = "org.slf4j..";

    public static final String APPLICATION_PACKAGE = BASE_PACKAGE + ".application..";

    public static final String DOMAIN_PACKAGE = BASE_PACKAGE + ".domain..";

    private final JavaClasses importedClasses = new ClassFileImporter().importPackages(BASE_PACKAGE);

    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoads() {
        Assertions.assertTrue(context.getBeanDefinitionCount() > 0);
    }

    @Test
    void applicationLayerShouldDependOnDomainLayerAndJavaStandardLibrary() {
        ArchRule rule = noClasses().that()
                .resideInAPackage(APPLICATION_PACKAGE)
                .should()
                .dependOnClassesThat()
                .resideOutsideOfPackages(APPLICATION_PACKAGE, DOMAIN_PACKAGE, REACTOR_STANDARD_LIBRARY, JAVA_STANDARD_LIBRARY, LOMBOK_LIBRARY, SLF4J_LIBRARY);
        rule.check(importedClasses);
    }

    @Test
    void domainLayerShouldDependOnJavaStandardLibrary() {
        ArchRule rule = noClasses().that()
                .resideInAPackage(DOMAIN_PACKAGE)
                .should()
                .dependOnClassesThat()
                .resideOutsideOfPackages(DOMAIN_PACKAGE, REACTOR_STANDARD_LIBRARY, JAKARTA_STANDARD_LIBRARY, JAVA_STANDARD_LIBRARY, LOMBOK_LIBRARY);
        rule.check(importedClasses);
    }

}
