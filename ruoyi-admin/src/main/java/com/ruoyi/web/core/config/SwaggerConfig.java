package com.ruoyi.web.core.config;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.ruoyi.common.config.RuoYiConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Swagger2的接口配置
 */
@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfig {
    /**
     * 系统基础配置
     */
    @Autowired
    private RuoYiConfig ruoyiConfig;

    /**
     * 是否开启swagger
     */
    @Value("${knife4j.enable}")
    private boolean enabled;

    /**
     * 设置请求的统一前缀
     */
    @Value("${swagger.pathMapping}")
    private String pathMapping;

    private static final String splitor = ";";


    @Bean
    public Docket allApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                // 是否启用Swagger
                .enable(true)
                .groupName("全部")
                // 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
                .apiInfo(apiInfo())
                // 设置哪些接口暴露给Swagger展示
                .select()
                // 指定扫描包的范围
                .apis(RequestHandlerSelectors.basePackage("com"))
                // 扫描包含@Api注解的类
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                // 扫描包含@ApiOperation注解的方法
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();

    }


   /* @Bean
    public Docket sysApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                // 是否启用Swagger
                .enable(true)
                .groupName("系统管理")
                // 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
                .apiInfo(apiInfo())
                // 设置哪些接口暴露给Swagger展示
                .select()
                // 指定扫描包的范围
                .apis(RequestHandlerSelectors.basePackage("com.ruoyi.web.controller"))
                // 扫描包含@Api注解的类
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                // 扫描包含@ApiOperation注解的方法
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();


        // 设置安全模式，swagger可以设置访问token
               *//* .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                .pathMapping(pathMapping);*//*
    }

    *//**
     * 模块：应急处置
     *
     * 注意：若某些接口类不显示在分组中，请优先按以下三点排查
     * 1、是否加注解 @Api @ApiOperation
     * 2、列表接口 集和泛型是否嵌套了其他包装类
     * 3、导出接口 集合泛型是否嵌套了其他包装类
     *//*
    @Bean
    public Docket sysEvent() {
        return new Docket(DocumentationType.SWAGGER_2)
                // 是否启用Swagger
                .enable(true)
                // 分组名称
                .groupName("应急处置")
                // 创建API基本信息
                .apiInfo(apiInfo())
                // 设置哪些接口暴露给Swagger展示
                .select()
                // 指定扫描包的范围
                .apis(
                        basePackage(
                                "com.tunnel.platform.controller.emeResource"
                                        + splitor
                                        + "com.tunnel.platform.controller.event.SdEmergencyDeviceController"
                                        + splitor
                                        + "com.tunnel.platform.controller.event.SdEmergencyPerController"
                                        + splitor
                                        + "com.tunnel.platform.controller.event.SdEventController"
                                        + splitor
                                        + "com.tunnel.platform.controller.event.SdEventFlowController"
                                        + splitor
                                        + "com.tunnel.platform.controller.event.SdEventTypeController"
                                        + splitor
                                        + "com.tunnel.platform.controller.event.SdJoinTypeFlowController"
                                        + splitor
                                        + "com.tunnel.platform.controller.event.SdPlanTypeController"
                                        + splitor
                                        + "com.tunnel.platform.controller.event.SdReservePlanController"
                                        + splitor
                                        + "com.tunnel.platform.controller.event.SdReservePlanFileController"
                                        + splitor
                                        + "com.tunnel.platform.controller.event.SdReserveProcessController"
                                        + splitor
                                        + "com.tunnel.platform.controller.event.SdSafetyIndexController"
                                        + splitor
                                        + "com.tunnel.platform.controller.event.SdStrategyBackController"
                                        + splitor
                                        + "com.tunnel.platform.controller.event.SdStrategyController"
                                        + splitor
                                        + "com.tunnel.platform.controller.event.SdStrategyRlController"
                                        + splitor
                                        + "com.tunnel.platform.controller.event.SdTriggerController"
                                        + splitor
                                        + "com.tunnel.platform.controller.event.SdWarningTypeController"
                                        + splitor
                                        + "com.tunnel.platform.controller.event.SdWarningInfoController"
                                        + splitor
                        )
                )
                // 扫描包含@Api注解的类
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                // 扫描包含@ApiOperation注解的方法
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                // 通过路径过滤，精确分组
                .paths(
                        // 安全预警
                        PathSelectors.ant("/event/**")
                                // 应急预案
                                .or(PathSelectors.ant("/plan/**"))
                                // 联控策略
                                .or(PathSelectors.ant("/strategy/**"))
                                // 应急资源-应急物资
                                .or(PathSelectors.ant("/material/**"))
                                // 应急资源-应急车辆
                                .or(PathSelectors.ant("/system/vehicle/**"))
                                // 应急资源-应急人员
                                .or(PathSelectors.ant("/SdEmergencyPer/**"))
                                // 应急事件-事件类型
                                .or(PathSelectors.ant("/eventType/**"))
                ).build();
    }

    *//**
     * 模块：机电巡查
     *
     * 注意：若某些接口类不显示在分组中，请优先按以下三点排查
     * 1、是否加注解 @Api @ApiOperation
     * 2、列表接口 集和泛型是否嵌套了其他包装类
     * 3、导出接口 集合泛型是否嵌套了其他包装类
     *//*
    @Bean
    public Docket electApis() {
        return new Docket(DocumentationType.SWAGGER_2)
                // 是否启用Swagger
                .enable(true)
                // 分组名称
                .groupName("机电巡查")
                // 创建API基本信息
                .apiInfo(apiInfo())
                // 设置哪些接口暴露给Swagger展示
                .select()
                // 指定扫描包的范围
                .apis(RequestHandlerSelectors.basePackage("com.tunnel.platform.controller.electromechanicalPatrol"))
                // 扫描包含@Api注解的类
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                // 扫描包含@ApiOperation注解的方法
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }

    *//**
     * 模块：隧道配置
     *
     * 注意：若某些接口类不显示在分组中，请优先按以下三点排查
     * 1、是否加注解 @Api @ApiOperation
     * 2、列表接口 集和泛型是否嵌套了其他包装类
     * 3、导出接口 集合泛型是否嵌套了其他包装类
     *//*
    @Bean
    public Docket tunnelConfigAPI() {
        return new Docket(DocumentationType.SWAGGER_2)
                // 是否启用Swagger
                .enable(true)
                // 分组名称
                .groupName("隧道配置")
                // 创建API基本信息
                .apiInfo(apiInfo())
                // 设置哪些接口暴露给Swagger展示
                .select()
                // 指定扫描包的范围
                .apis(RequestHandlerSelectors.basePackage("com.tunnel.platform.controller.dataInfo"))
                // 扫描包含@Api注解的类
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                // 扫描包含@ApiOperation注解的方法
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(
                        // 隧道管理
                        PathSelectors.ant("/tunnels/**")
                                // 设备图标
                                .or(PathSelectors.ant("/type/**"))
                                // 环境配置
                                .or(PathSelectors.ant("/system/configuration/**"))
                                // 隧道分区
                                .or(PathSelectors.ant("/tunnel/subarea/**"))
                )
                .build();
    }

    *//**
     * 模块：设备台账
     *
     * 注意：若某些接口类不显示在分组中，请优先按以下三点排查
     * 1、是否加注解 @Api @ApiOperation
     * 2、列表接口 集和泛型是否嵌套了其他包装类
     * 3、导出接口 集合泛型是否嵌套了其他包装类
     *//*
    @Bean
    public Docket devBookAPI() {
        return new Docket(DocumentationType.SWAGGER_2)
                // 是否启用Swagger
                .enable(true)
                // 分组名称
                .groupName("设备台账")
                // 创建API基本信息
                .apiInfo(apiInfo())
                // 设置哪些接口暴露给Swagger展示
                .select()
                // 指定扫描包的范围
                .apis(RequestHandlerSelectors.basePackage("com.tunnel.platform.controller.dataInfo"))
                // 扫描包含@Api注解的类
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                // 扫描包含@ApiOperation注解的方法
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(
                        // 设备管理
                        PathSelectors.ant("/devices/**")
                                // 设备状态
                                .or(PathSelectors.ant("/eqTypeState/**"))
                                // 设备类型数据项
                                .or(PathSelectors.ant("/eqType/item/**"))
                                // 设备变更
                                .or(PathSelectors.ant("/system/change/**"))
                )
                .build();
    }

    *//**
     * 模块：情报板管理
     *
     * 注意：若某些接口类不显示在分组中，请优先按以下三点排查
     * 1、是否加注解 @Api @ApiOperation
     * 2、列表接口 集和泛型是否嵌套了其他包装类
     * 3、导出接口 集合泛型是否嵌套了其他包装类
     *//*
    @Bean
    public Docket infoBoardAPI() {
        return new Docket(DocumentationType.SWAGGER_2)
                // 是否启用Swagger
                .enable(true)
                // 分组名称
                .groupName("情报板管理")
                // 创建API基本信息
                .apiInfo(apiInfo())
                // 设置哪些接口暴露给Swagger展示
                .select()
                // 指定扫描包的范围
                .apis(RequestHandlerSelectors.basePackage("com.tunnel.platform.controller.informationBoard"))
                // 扫描包含@Api注解的类
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                // 扫描包含@ApiOperation注解的方法
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }



    *//*@Bean
    public Docket ssoApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                // 是否启用Swagger
                .enable(true)
                .groupName("单点登录")
                // 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
                .apiInfo(apiInfo())
                // 设置哪些接口暴露给Swagger展示
                .select()
                // 指定扫描包的范围
                .apis(RequestHandlerSelectors.basePackage("com.tunnel.platform.controller.sso"))
                // 扫描包含@Api注解的类
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                // 扫描包含@ApiOperation注解的方法
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }*//*

     *//**
     * 模块：备品备件
     *
     * 注意：若某些接口类不显示在分组中，请优先按以下三点排查
     * 1、是否加注解 @Api @ApiOperation
     * 2、列表接口 集和泛型是否嵌套了其他包装类
     * 3、导出接口 集合泛型是否嵌套了其他包装类
     *//*
    @Bean
    public Docket sparePartAPI() {
        return new Docket(DocumentationType.SWAGGER_2)
                // 是否启用Swagger/devices/list
                .enable(true)
                // 分组名称
                .groupName("备品备件")
                // 创建API基本信息
                .apiInfo(apiInfo())
                // 设置哪些接口暴露给Swagger展示
                .select()
                // 指定扫描包的范围
                .apis(RequestHandlerSelectors.basePackage("com.tunnel.platform.controller.dataInfo"))
                // 扫描包含@Api注解的类
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                // 扫描包含@ApiOperation注解的方法
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.ant("/system/warehouse/**"))
                .build();
    }*/


    /**
     * 安全模式，这里指定token通过Authorization头请求头传递
     */
    private List<SecurityScheme> securitySchemes() {
        List<SecurityScheme> apiKeyList = new ArrayList<>();
        apiKeyList.add(new ApiKey("Authorization", "Authorization", In.HEADER.toValue()));
        return apiKeyList;
    }

    /**
     * 安全上下文
     */
//    private List<SecurityContext> securityContexts() {
//        List<SecurityContext> securityContexts = new ArrayList<>();
//        securityContexts.add(
//                SecurityContext.builder()
//                        .securityReferences(defaultAuth())
//                        .operationSelector(o -> o.requestMappingPattern().matches("/.*"))
//                        .build());
//        return securityContexts;
//    }

    /**
     * 默认的安全上引用
     */
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
        return securityReferences;
    }

    /**
     * 添加摘要信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 设置标题
                .title("智慧隧道综合管控平台_接口文档")
                // 描述
                .description("描述：用于智慧隧道综合管控平台数据接口信息,具体包括工作台、数字孪生、应急处置、预案管理、设备管理、日志管理、系统管理等模块")
                // 作者信息
                .contact(new Contact(null, null, null))
                // 版本
                .version("版本号:" + ruoyiConfig.getVersion())
                .build();
    }


    /**
     * @param basePackage 所有包路径
     * @return Predicate<RequestHandler>
     * @author luoyu
     * @description 重写basePackage方法，使能够实现多包访问
     */
    public static Predicate<RequestHandler> basePackage(final String basePackage) {
        return input -> declaringClass(input).map(handlerPackage(basePackage)::apply).orElse(true);
    }

    /**
     * @param basePackage 所有包路径
     * @return Function<Class < ?>, Boolean>
     * @author luoyu
     * @description 重写basePackage方法，使能够实现多包访问
     */
    private static Function<Class<?>, Boolean> handlerPackage(final String basePackage) {
        return input -> {
            // 循环判断匹配
            for (String strPackage : basePackage.split(splitor)) {
                assert input != null;
                boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                if (isMatch) {
                    return true;
                }
            }
            return false;
        };
    }

    /**
     * @param input
     * @return Optional<? extends Class < ?>>
     * @author luoyu
     * @description 重写basePackage方法，使能够实现多包访问
     */
    private static Optional<Class<?>> declaringClass(RequestHandler input) {
        return Optional.ofNullable(input.declaringClass());
    }

}
