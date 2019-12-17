package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaGrepLambdaImp extends JavaGrepImp {


  public static void main(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException("USAGE: regex rootPath outFile");
    }
    JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
    javaGrepLambdaImp.setRegex(args[0]);
    javaGrepLambdaImp.setRootPath(args[1]);
    javaGrepLambdaImp.setOutFile(args[2]);

    try {
      javaGrepLambdaImp.process();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public List<File> listFiles(String rootDir) throws IOException {
    Stream<Path> files = Files.walk(Paths.get(rootDir));
    List<File> resultFiles = files
        .filter(Files::isRegularFile)
        .map(Path::toFile)
        .collect(Collectors.toList());
    files.close();
    return resultFiles;
  }

  @Override
  public List<String> readLines(File inputFile) {
    List<String> listLines = new ArrayList<>();
    //read file into stream, try-with-resources
    try (Stream<String> stream = Files.lines(Paths.get(String.valueOf(inputFile)))) {
      listLines=stream.collect(Collectors.toList());

    } catch (IOException e) {
      e.printStackTrace();
    }
    return listLines;
  }

  }

