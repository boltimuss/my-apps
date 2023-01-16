package entity.generator.plugin;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class EntityGeneratorMojo extends AbstractMojo {
	
	@Parameter(property = "inputDirectory")
	private String inputDir;
	
	@Parameter(property = "outputDirectory")
	private String outputDir;
	
	public void execute() throws MojoExecutionException, MojoFailureException 
	{
		
		LinkedList<String> fields = new LinkedList<>();
		
        /* check if the input directory exists, if not throw a failure exception */
		File inputDirectory = new File(inputDir);
		if (!inputDirectory.exists()) throw new MojoFailureException("Input Directory: '" + inputDir + "' does not exist");
		
		/* create the output directory if it doesn't exist */
		File outputDirectory = new File(inputDir);
		if (!outputDirectory.exists())
		{
			outputDirectory.mkdirs();
		}
		
		/* iterate through each file in the input dir */
		for (File file:inputDirectory.listFiles())
		{
			/* get the class name */
			String className = file.getName();
			
			/* gleam the fields from the file */
			FileReader fr;
			try {
				fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr); 
				String line;  
				int expectedSize = 3;
				while((line=br.readLine())!=null)  
				{  
					line = line.trim();
					int size = line.split(" ", -1).length;
					if (size < 3 || 
						 size > 4 || 
						 !line.contains("public") ||
						 line.contains("{") || 
						 line.contains("}") | 
						 line.contains("(") || 
						 line.contains(")")) continue;
					
					if (line.startsWith("@")) expectedSize = 4;
					else expectedSize = 3;
					
					if (expectedSize != size) continue;
					
					fields.add(line.split(" ", -1)[expectedSize-1]);
					System.out.println("field: "+fields.getLast());
				} 
				
				fr.close();  
			} catch (IOException e) {
				throw new MojoFailureException(e.getMessage());
			}   
			
			/* create the new class file, and copy the template */
			/* write into the new class's place holders */
		}
    }
	
}
