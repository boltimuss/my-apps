package entity.generator.plugin;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;

@Mojo(name = "version", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class EntityGeneratorMojo {
	
	@Parameter(property = "inputDirectory")
	private String inputDir;
	
	@Parameter(property = "outputDirectory")
	private String outputDir;
	
	public void execute() throws MojoExecutionException, MojoFailureException 
	{
        
    }
	
}
