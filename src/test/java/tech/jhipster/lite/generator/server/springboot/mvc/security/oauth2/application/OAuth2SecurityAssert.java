package tech.jhipster.lite.generator.server.springboot.mvc.security.oauth2.application;

import static tech.jhipster.lite.TestUtils.assertFileContent;
import static tech.jhipster.lite.TestUtils.assertFileExist;
import static tech.jhipster.lite.common.domain.FileUtils.getPath;
import static tech.jhipster.lite.generator.project.domain.Constants.*;
import static tech.jhipster.lite.generator.server.springboot.mvc.security.jwt.domain.JwtSecurityDomainService.SECURITY_JWT_PATH;
import static tech.jhipster.lite.generator.server.springboot.mvc.security.oauth2.domain.OAuth2Security.DEFAULT_PROVIDER;
import static tech.jhipster.lite.generator.server.springboot.mvc.security.oauth2.domain.OAuth2Security.getDockerKeycloakImage;
import static tech.jhipster.lite.generator.server.springboot.mvc.security.oauth2.domain.OAuth2SecurityDomainService.SECURITY_OAUTH2_PATH;

import java.util.List;
import java.util.Optional;
import tech.jhipster.lite.generator.project.domain.DefaultConfig;
import tech.jhipster.lite.generator.project.domain.Project;
import tech.jhipster.lite.generator.server.springboot.mvc.security.oauth2.domain.OAuth2Provider;
import tech.jhipster.lite.generator.server.springboot.mvc.security.oauth2.domain.OAuth2Security;

public class OAuth2SecurityAssert {

  private OAuth2SecurityAssert() {}

  public static void assertSecurityDependencies(Project project) {
    assertFileContent(project, POM_XML, securityDependency());
    assertFileContent(project, POM_XML, securityTestDependency());
  }

  public static void assertOAuth2ClientDependencies(Project project) {
    assertFileContent(project, POM_XML, oauth2ClientDependency());
  }

  public static void assertOAuth2ResourceServerDependencies(Project project) {
    assertFileContent(project, POM_XML, oauth2ResourceServerDependency());
  }

  public static void assertDockerKeycloak(Project project) {
    assertFileExist(project, "src/main/docker/keycloak.yml");
    assertFileExist(project, "src/main/docker/keycloak-realm-config/jhipster-realm.json");
    assertFileExist(project, "src/main/docker/keycloak-realm-config/jhipster-users-0.json");

    assertFileContent(project, "src/main/docker/keycloak.yml", getDockerKeycloakImage());
  }

  public static void assertJavaFiles(Project project) {
    String oauth2Path = getPath(project.getPackageNamePath().orElse(DefaultConfig.PACKAGE_PATH), SECURITY_OAUTH2_PATH);

    // main java files
    OAuth2Security
      .oauth2SecurityFiles()
      .forEach((javaFile, destination) -> assertFileExist(project, getPath(MAIN_JAVA, oauth2Path, destination, javaFile)));
    OAuth2Security
      .oauth2TestSecurityFiles()
      .forEach((javaFile, destination) -> assertFileExist(project, getPath(TEST_JAVA, oauth2Path, destination, javaFile)));
  }

  public static void assertOAuth2ClientProperties(Project project, OAuth2Provider provider, String issuerUri) {
    OAuth2Provider providerFallback = Optional.ofNullable(provider).orElse(DEFAULT_PROVIDER);
    String issuerUriFallback = Optional.ofNullable(issuerUri).orElse(providerFallback.getDefaultIssuerUri());

    List<String> properties =
      switch (providerFallback) {
        case GOOGLE -> List.of(
          "spring.security.oauth2.client.registration.google.client-id=web_app",
          "spring.security.oauth2.client.registration.google.client-secret=web_app"
        );
        case FACEBOOK -> List.of(
          "spring.security.oauth2.client.registration.facebook.client-id=web_app",
          "spring.security.oauth2.client.registration.facebook.client-secret=web_app"
        );
        case GITHUB -> List.of(
          "spring.security.oauth2.client.registration.github.client-id=web_app",
          "spring.security.oauth2.client.registration.github.client-secret=web_app"
        );
        case OKTA -> List.of(
          "spring.security.oauth2.client.provider.okta.issuer-uri=" + issuerUriFallback,
          "spring.security.oauth2.client.registration.okta.client-id=web_app",
          "spring.security.oauth2.client.registration.okta.client-secret=web_app"
        );
        case KEYCLOAK, AUTH0, OTHER -> {
          String providerId = providerFallback.name().toLowerCase();
          yield List.of(
            "spring.security.oauth2.client.provider." + providerId + ".issuer-uri=" + issuerUriFallback,
            "spring.security.oauth2.client.registration." + providerId + ".client-name=" + providerId,
            "spring.security.oauth2.client.registration." + providerId + ".client-id=web_app",
            "spring.security.oauth2.client.registration." + providerId + ".client-secret=web_app"
          );
        }
      };

    assertFileContent(project, getPath(MAIN_RESOURCES, "config/application.properties"), properties);
    assertFileContent(project, getPath(TEST_RESOURCES, "config/application.properties"), properties);
  }

  public static void assertUpdateExceptionTranslatorIT(Project project) {
    String basePath = project.getPackageNamePath().orElse(getPath(DefaultConfig.PACKAGE_PATH));
    String exceptionTestPath = getPath(TEST_JAVA, basePath, "technical/infrastructure/primary/exception");
    assertFileContent(project, getPath(exceptionTestPath, "ExceptionTranslatorIT.java"), "@WithMockUser");
  }

  public static List<String> securityDependency() {
    return List.of(
      "<dependency>",
      "<groupId>org.springframework.boot</groupId>",
      "<artifactId>spring-boot-starter-security</artifactId>",
      "</dependency>"
    );
  }

  public static List<String> securityTestDependency() {
    return List.of(
      "<dependency>",
      "<groupId>org.springframework.security</groupId>",
      "<artifactId>spring-security-test</artifactId>",
      "<scope>test</scope>",
      "</dependency>"
    );
  }

  public static List<String> oauth2ClientDependency() {
    return List.of(
      "<dependency>",
      "<groupId>org.springframework.boot</groupId>",
      "<artifactId>spring-boot-starter-oauth2-client</artifactId>",
      "</dependency>"
    );
  }

  public static List<String> oauth2ResourceServerDependency() {
    return List.of(
      "<dependency>",
      "<groupId>org.springframework.boot</groupId>",
      "<artifactId>spring-boot-starter-oauth2-resource-server</artifactId>",
      "</dependency>"
    );
  }
}
