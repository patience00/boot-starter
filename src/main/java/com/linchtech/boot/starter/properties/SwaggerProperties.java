package com.linchtech.boot.starter.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 107
 * @date Created by 2020/4/25 14:59
 */
@Data
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {

	private boolean enable = false;

	private List<String> basePackages;
	/**
	 * 自定义header
	 */
	private Map<String, String> headers;

	private ApiInfo apiInfo = new SwaggerApiInfo();

	private String name;
	private String email;
	private String url;
	private String apiVersion;

	private String apiLicense;
	private String apiLicenseUrl;

	@Getter
	@Setter
	class SwaggerApiInfo extends ApiInfo {
		private String title = "在线文档";
		private String description = "仅用于开发和联调测试阶段使用";
		private String version = "v1";
		private String termsOfServiceUrl = "";
		private String license = "";
		private String licenseUrl = "";

		public SwaggerApiInfo() {
			super("在线文档",
					"仅用于开发和联调测试阶段使用",
					apiVersion,
					"",
					new Contact(name, url, email),
					apiLicense,
					apiLicenseUrl,
					new ArrayList<>());
		}
	}

}
