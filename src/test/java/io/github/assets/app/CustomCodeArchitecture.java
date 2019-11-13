package io.github.assets.app;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class CustomCodeArchitecture {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("io.github.assets");

        noClasses()
            .that()
            .resideInAnyPackage("..service..")
            .or()
            .resideInAnyPackage("..repository..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..io.github.assets.app.resource..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }

    @Test
    void yeomanCodeShouldBeIndependentOfAnyCustomCode() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("io.github.assets");

        noClasses()
            .that()
            .resideInAnyPackage("..io.github.assets.aop..")
            .or()
            .resideInAnyPackage("..io.github.assets.client..")
            .or()
            .resideInAnyPackage("..io.github.assets.config..")
            .or()
            .resideInAnyPackage("..io.github.assets.domain..")
            .or()
            .resideInAnyPackage("..io.github.assets.repository..")
            .or()
            .resideInAnyPackage("..io.github.assets.security..")
            .or()
            .resideInAnyPackage("..io.github.assets.service..")
            .or()
            .resideInAnyPackage("..io.github.assets.web..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..io.github.assets.app..")
            .because("The yeoman code needs to be left ignorant of the existence of " +
                         "custom code, to enable iteration of code generation processes. Hope you understand. Use decorators")
            .check(importedClasses);
    }
}
