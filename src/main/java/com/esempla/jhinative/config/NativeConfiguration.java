package com.esempla.jhinative.config;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

public class NativeConfiguration {

    public static class JHipsterNativeRuntimeHints implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            hints.resources().registerPattern("config/liquibase/*");
            hints
                .reflection()
                .registerType(liquibase.ui.LoggerUIService.class, hint -> hint.withMembers(MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS));
            hints
                .reflection()
                .registerType(
                    liquibase.database.LiquibaseTableNamesFactory.class,
                    hint -> hint.withMembers(MemberCategory.INVOKE_DECLARED_CONSTRUCTORS)
                );
            hints
                .reflection()
                .registerType(
                    liquibase.report.ShowSummaryGeneratorFactory.class,
                    hint -> hint.withMembers(MemberCategory.INVOKE_DECLARED_CONSTRUCTORS)
                );
            hints
                .reflection()
                .registerType(
                    org.hibernate.binder.internal.BatchSizeBinder.class,
                    hint -> hint.withMembers(MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS)
                );
            // jhipster-needle-add-native-hints - JHipster will add native hints here
        }
    }
}
