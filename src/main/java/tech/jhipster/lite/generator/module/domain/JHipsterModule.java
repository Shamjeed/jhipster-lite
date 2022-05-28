package tech.jhipster.lite.generator.module.domain;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import tech.jhipster.lite.error.domain.Assert;
import tech.jhipster.lite.generator.module.domain.JHipsterModuleContext.JHipsterModuleContextBuilder;
import tech.jhipster.lite.generator.module.domain.JHipsterModuleFiles.JHipsterModuleFilesBuilder;
import tech.jhipster.lite.generator.module.domain.JHipsterModulePreActions.JHipsterModulePreActionsBuilder;
import tech.jhipster.lite.generator.module.domain.javadependency.ArtifactId;
import tech.jhipster.lite.generator.module.domain.javadependency.GroupId;
import tech.jhipster.lite.generator.module.domain.javadependency.JHipsterModuleJavaDependencies;
import tech.jhipster.lite.generator.module.domain.javadependency.JHipsterModuleJavaDependencies.JHipsterModuleJavaDependenciesBuilder;
import tech.jhipster.lite.generator.module.domain.javadependency.JavaDependency;
import tech.jhipster.lite.generator.module.domain.javadependency.JavaDependency.JavaDependencyGroupIdBuilder;
import tech.jhipster.lite.generator.module.domain.javadependency.VersionSlug;

public class JHipsterModule {

  private final JHipsterProjectFolder projectFolder;
  private final Collection<JHipsterModuleFile> files;
  private final JHipsterModuleContext context;
  private final JHipsterModuleJavaDependencies javaDependencies;
  private final JHipsterModulePreActions preActions;

  private JHipsterModule(JHipsterModuleBuilder builder) {
    projectFolder = builder.projectFolder;

    files = builder.files.build().get();
    context = builder.context.build();
    javaDependencies = builder.javaDependencies.build();
    preActions = builder.preActions.build();
  }

  public static JHipsterModuleBuilder moduleForProject(JHipsterProjectFolder project) {
    return new JHipsterModuleBuilder(project);
  }

  public static JavaDependencyGroupIdBuilder javaDependency() {
    return JavaDependency.builder();
  }

  public static JHipsterSource from(String source) {
    Assert.notBlank("source", source);

    return new JHipsterSource(Paths.get("/generator", source));
  }

  public static JHipsterDestination to(String destination) {
    return new JHipsterDestination(destination);
  }

  public static JHipsterDestination toSrcMainJava() {
    return JHipsterDestination.SRC_MAIN_JAVA;
  }

  public static JHipsterDestination toSrcTestJava() {
    return JHipsterDestination.SRC_TEST_JAVA;
  }

  public static GroupId groupId(String groupId) {
    return new GroupId(groupId);
  }

  public static ArtifactId artifactId(String artifactId) {
    return new ArtifactId(artifactId);
  }

  public static VersionSlug versionSlug(String versionSlug) {
    return new VersionSlug(versionSlug);
  }

  public JHipsterProjectFolder projectFolder() {
    return projectFolder;
  }

  public TemplatedFiles templatedFiles() {
    List<TemplatedFile> templatedFiles = files.stream().map(file -> TemplatedFile.builder().file(file).context(context).build()).toList();

    return new TemplatedFiles(templatedFiles);
  }

  public JHipsterModuleJavaDependencies javaDependencies() {
    return javaDependencies;
  }

  public JHipsterModulePreActions preActions() {
    return preActions;
  }

  public static class JHipsterModuleBuilder {

    private final JHipsterProjectFolder projectFolder;
    private final JHipsterModuleContextBuilder context = JHipsterModuleContext.builder(this);
    private final JHipsterModuleFilesBuilder files = JHipsterModuleFiles.builder(this);
    private final JHipsterModuleJavaDependenciesBuilder javaDependencies = JHipsterModuleJavaDependencies.builder(this);
    private final JHipsterModulePreActionsBuilder preActions = JHipsterModulePreActions.builder(this);

    private JHipsterModuleBuilder(JHipsterProjectFolder projectFolder) {
      Assert.notNull("projectFolder", projectFolder);

      this.projectFolder = projectFolder;
    }

    public JHipsterModuleContextBuilder context() {
      return context;
    }

    public JHipsterModuleFilesBuilder files() {
      return files;
    }

    public JHipsterModuleJavaDependenciesBuilder javaDependencies() {
      return javaDependencies;
    }

    public JHipsterModulePreActionsBuilder preActions() {
      return preActions;
    }

    public JHipsterModule build() {
      return new JHipsterModule(this);
    }
  }
}
