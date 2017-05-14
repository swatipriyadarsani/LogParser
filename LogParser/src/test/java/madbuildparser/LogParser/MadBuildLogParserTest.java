package madbuildparser.LogParser;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.junit.Assert.fail;

import java.io.IOException;



public class MadBuildLogParserTest {

@Test
public void readSampleLogFile() throws IOException {

   	String inputDir=System.getProperty("user.dir")+"\\src\\test\\resources\\input";
    String outputDir=System.getProperty("user.dir")+"\\src\\test\\resources\\output";

    MadBuildLogParser madBuildLogParser=new MadBuildLogParser();
    madBuildLogParser.process(inputDir,outputDir);

    madBuildLogParser.showProcessedFiles();
    madBuildLogParser.showGeneratedFiles();
}


@Rule
public TemporaryFolder temporaryFolder = new TemporaryFolder();

@Test
public void checkIfOutputFolderExist() throws IOException {
	
	temporaryFolder.newFolder("output");
	
    fail("Output folder alredy exist");
}


@Test
public void checkIfInputFolderExist() throws IOException {
	
	temporaryFolder.newFolder("input");
	
    fail("Input folder alredy exist");
}



}