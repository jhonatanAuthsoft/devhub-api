package com.projeto.modelo.configuracao;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;

@Configuration
public class SwaggerConfig {

    private static final String BEARER_AUTH = "bearerAuth";
    private static final String IN_QUERY = "query";
    private static final String TYPE_INTEGER = "integer";
    private static final String TYPE_STRING = "string";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Modelo API")
                        .version("1.0")
                        .description("Documentação da API do xxxx")
                )
                .components(new Components()
                        .addSecuritySchemes(BEARER_AUTH,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)
                                        .name("Authorization")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH));
    }

    @Bean
    public GroupedOpenApi publicApi(OperationCustomizer parametrizarPaginacaoCustomizada) {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/**")
                .addOperationCustomizer(parametrizarPaginacaoCustomizada)
                .build();
    }

    @Bean
    public OperationCustomizer parametrizarPaginacaoCustomizada() {
        return (operation, handlerMethod) -> {
            for (MethodParameter parameter : handlerMethod.getMethodParameters()) {
                if (Pageable.class.isAssignableFrom(parameter.getParameterType())) {
                    operation.addParametersItem(new Parameter()
                            .name("page")
                            .in(IN_QUERY)
                            .description("Número da página (0..N)")
                            .required(false)
                            .schema(new Schema<Integer>().type(TYPE_INTEGER)._default(0)));

                    operation.addParametersItem(new Parameter()
                            .name("size")
                            .in(IN_QUERY)
                            .description("Quantidade de elementos por página")
                            .required(false)
                            .schema(new Schema<Integer>().type(TYPE_INTEGER)._default(10)));

                    operation.addParametersItem(new Parameter()
                            .name("sort")
                            .in(IN_QUERY)
                            .description("Critério de ordenação: propriedade,asc|desc. Pode ser usado múltiplas vezes.")
                            .required(false)
                            .schema(new Schema<String>().type(TYPE_STRING))
                            .style(Parameter.StyleEnum.FORM)
                            .explode(true));
                }
            }
            return operation;
        };
    }
}