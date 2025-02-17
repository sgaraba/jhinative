package com.esempla.jhinative.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import jakarta.persistence.Basic;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.lang.annotation.Annotation;
import java.util.function.Function;
import org.hibernate.Hibernate;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonNativeConfiguration {

    /*
     * Filter to allow Jackson serializer to detect lazy-loaded properties when using hibernate-enhance-maven-plugin
     * see: https://github.com/FasterXML/jackson-datatype-hibernate/issues/148#issuecomment-1383923857
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizeJackson() {
        return builder -> {
            String filterName = "lazyPropertyFilter";
            builder.filters(new SimpleFilterProvider().addFilter(filterName, new LazyPropertyFilter()));
        };
    }

    private class LazyPropertyFilter implements PropertyFilter {

        public static boolean isPropertyInitialized(BeanPropertyWriter prop, Object bean) throws Exception {
            return (
                Hibernate.isPropertyInitialized(bean, prop.getName()) &&
                isInitialized(prop, bean, ManyToOne.class, ManyToOne::fetch) &&
                isInitialized(prop, bean, ElementCollection.class, ElementCollection::fetch) &&
                isInitialized(prop, bean, OneToMany.class, OneToMany::fetch) &&
                isInitialized(prop, bean, Basic.class, Basic::fetch)
            );
        }

        public static <A extends Annotation> boolean isInitialized(
            BeanPropertyWriter prop,
            Object bean,
            Class<A> type,
            Function<A, FetchType> fetch
        ) throws Exception {
            var ann = prop.getAnnotation(type);
            if (ann == null) {
                return true;
            }
            var fetchType = fetch.apply(ann);
            if (fetchType == FetchType.EAGER) {
                return true;
            }
            var value = prop.get(bean);
            return Hibernate.isInitialized(value);
        }

        @Override
        public void serializeAsField(Object pojo, JsonGenerator gen, SerializerProvider prov, PropertyWriter writer) throws Exception {
            var initialized = isPropertyInitialized((BeanPropertyWriter) writer, pojo);
            if (initialized) {
                writer.serializeAsField(pojo, gen, prov);
            } else if (!gen.canOmitFields()) { // since 2.3
                writer.serializeAsOmittedField(pojo, gen, prov);
            }
        }

        @Override
        public void serializeAsElement(Object elementValue, JsonGenerator gen, SerializerProvider prov, PropertyWriter writer) {
            throw new RuntimeException("LazyPropertyFilter.serializeAsElement() currently unsupported");
        }

        @SuppressWarnings("deprecation")
        @Override
        public void depositSchemaProperty(PropertyWriter writer, ObjectNode propertiesNode, SerializerProvider provider)
            throws JsonMappingException {
            writer.depositSchemaProperty(propertiesNode, provider);
        }

        @Override
        public void depositSchemaProperty(PropertyWriter writer, JsonObjectFormatVisitor objectVisitor, SerializerProvider provider)
            throws JsonMappingException {
            writer.depositSchemaProperty(objectVisitor, provider);
        }
    }
}
