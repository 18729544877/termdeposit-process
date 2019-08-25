package com.simnectzbank.lbs.processlayer;
 
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.simnectzbank.lbs.processlayer.termdeposit.config.PathConfig;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
 
/**
 * Swagger2配置类
 * 在与spring boot集成时，放在与Application.java同级的目录下。
 * 通过@Configuration注解，让Spring来加载该类配置。
 * 再通过@EnableSwagger2注解来启用Swagger2。
 */
@Configuration
@EnableSwagger2
public class Swagger2 {
	
	
	@Resource
	private PathConfig pathConfig;
    
    /**
     * 创建API应用
     * apiInfo() 增加API相关信息
     * 通过select()函数返回一个ApiSelectorBuilder实例,用来控制哪些接口暴露给Swagger来展现，
     * 本例采用指定扫描的包路径来定义指定要建立API的目录。
     * 
     * @return
     */
    @Bean
    public Docket createRestApi() {
    	ParameterBuilder parameterBuilder = new ParameterBuilder();
    	List<Parameter> parameters = new ArrayList<Parameter>();
        parameterBuilder.name("token")
        .modelRef(new ModelRef("string"))
        .parameterType("header")
        .description("The most recent customer authorization token to third party app to access customer data from a bank, in JWT format.")
        .defaultValue("eyJhbGciOiJIUzUxMiIsInppcCI6IkRFRiJ9.eNo8y00OwiAQhuG7zNoFkBLUpbqwadI7ADNWEn4aWozGeHchNs7yme99A9KDfJop9xc4gkTaG6XtTQrRCWEPhMKojhnDLVdKwg5sKnHNr3NCqsF1aORJZxenzRhjvKrJOtr73xr5NLk46tBkdqGlZVlToDyWYCj_hmy7Viw64ik9e6wv-HwBAAD__w.alc0ibAbJotnPxSQL2wtt9Qo8h0YYzl4WkxOK65PnGy1fK4SDmNRRVEohqOya_K7qOXJOt5Cjdm10cejK3PViA")
        .required(true).build();
        parameters.add(parameterBuilder.build());
        
        parameterBuilder.name("messageid")
        .modelRef(new ModelRef("string"))
        .parameterType("header")
        .description("Message ID in 128 bit random UUID format generated uniquely for every request by F/E side.")
        .defaultValue("006f7113e5fa48559549c4dfe74e2cd6")
        .required(true).build();
        parameters.add(parameterBuilder.build());
        
        parameterBuilder.name("clientid")
        .modelRef(new ModelRef("string"))
        .parameterType("header")
        .description("client_id generated during consumer onboarding. ")
        .defaultValue("devin")
        .required(true).build();
        parameters.add(parameterBuilder.build());
        
        
        return new Docket(DocumentationType.SWAGGER_2)
        		.globalOperationParameters(parameters)
                .host(pathConfig.getGateWayUrl())
                .groupName("deposit-api")
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.simnectzbank.lbs.processlayer.termdeposit.controller"))
                .paths(PathSelectors.any())
                .build()
                .directModelSubstitute(java.sql.Timestamp.class, java.sql.Date.class)
                .enableUrlTemplating(false);
    }
    
    /**
     * 创建该API的基本信息（这些基本信息会展现在文档页面中）
     * 访问地址：http://项目实际地址/swagger-ui.html
     * @return
     */
    @SuppressWarnings("deprecation")
	private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("term deposit service")
                //.description("Deposit service")
                //.termsOfServiceUrl(SysConstant.LOCAL_SERVICE_URL)
                .contact("Pim li:lihuacheng@chinasofti.com")
                .version("1.0")
                .build();
    }
}
