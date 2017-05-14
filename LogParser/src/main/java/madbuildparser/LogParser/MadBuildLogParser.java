package madbuildparser.LogParser;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class MadBuildLogParser {


    private Map<String,List<String>> errorLinesByKeyword=new LinkedHashMap<>();
    private List<String> processedFiles=new ArrayList<>();
    private List<String> generatedFiles=new ArrayList<>();
    public static final String MISSING="missing ";
    public static final String FAILED="failed ";
    public static final String ERROR="error ";

    public static List<File> getFilesForParsing(String inputDir) throws IOException {

        List<File> filesInFolder = Files.walk(Paths.get(inputDir))
                .filter(p -> p.toString().endsWith(".log"))
                .map(Path::toFile)
                .collect(Collectors.toList());
        return filesInFolder;
    }

    public void process(String inputDir,String outputDir) throws IOException {

        readFiles(inputDir);
        generateFiles(outputDir);

    }


    private void readFiles(String inputDir) throws IOException {
        List<File> filesInInputDir = getFilesForParsing(inputDir);

        // 001. Loop over files

        for (File parseFile : filesInInputDir) {
            String absolutePath=parseFile.getAbsolutePath();
            processedFiles.add(absolutePath);
            try {
                Scanner s = new Scanner(parseFile);
                long lineNo=0;
                String errorType;
                while (s.hasNextLine()) {
                    String line = s.nextLine();
                    lineNo++;
                    String lowerCase=line.toLowerCase();
                    if (lowerCase.contains(MISSING)) {
                        errorType=MISSING;
                        collectError(errorType,String.format("{file:%s, line:%d, error:%s}",absolutePath,lineNo,line));
                    }else if (lowerCase.contains(FAILED)) {
                        errorType=FAILED;
                        collectError(errorType,String.format("{file:%s, line:%d, error:%s}",absolutePath,lineNo,line));
                    }
                    else if (lowerCase.contains(ERROR)) {
                        errorType=ERROR;
                        collectError(errorType,String.format("{file:%s, line:%d, error:%s}",absolutePath,lineNo,line));
                    }

                }
                s.close();
            } // closing try
            catch (IOException ioex) {
                // handle exception...

            }
        } // closing for parseFile
    }

    public void outputIssues() {
        System.out.println("=================== Found issues are =============== ");
        for(Map.Entry<String, List<String>> entry:errorLinesByKeyword.entrySet()){
            entry.getValue().forEach(System.out::println);
        }
    }

    private void collectError(String errorType, String errorMessage) {
        if(!errorLinesByKeyword.containsKey(errorType)){
            errorLinesByKeyword.put(errorType,new ArrayList<>());
        }
        errorLinesByKeyword.get(errorType).add(errorMessage);

    }


    public void generateFiles(String outputDir) throws FileNotFoundException {
        for(Map.Entry<String, List<String>> entry:errorLinesByKeyword.entrySet()){
            String outputFile=outputDir+"/Processed_"+entry.getKey()+".log";
            generatedFiles.add(outputFile);
            PrintWriter processedLog = new PrintWriter(outputFile);
            entry.getValue().forEach(processedLog::println);
            processedLog.close();
        }
    }

    public void showProcessedFiles(){
        System.out.println("=================== Processed files are =============== ");
        processedFiles.forEach(System.out::println);
    }

    public void showGeneratedFiles(){
        System.out.println("=================== Generated files are =============== ");
        generatedFiles.forEach(System.out::println);
    }

}
