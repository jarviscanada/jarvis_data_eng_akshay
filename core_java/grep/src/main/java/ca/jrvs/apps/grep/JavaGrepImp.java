package ca.jrvs.apps.grep;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class JavaGrepImp implements JavaGrep {

  private String regex;
  private String rootPath;
  private String outFile;

  public static void main(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException("USAGE: regex rootPath outFile");
    }
    JavaGrepImp javaGrepImp = new JavaGrepImp();
    javaGrepImp.setRegex(args[0]);
    javaGrepImp.setRootPath(args[1]);
    javaGrepImp.setOutFile(args[2]);

    try {
      javaGrepImp.process();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void process() throws IOException {
//    matchedLines = []
//    for file in listFilesRecursively(rootDir)
//    for line in readLines(file)
//    if containsPattern(line)
//    matchedLines.add(line)
//    writeToFile(matchedLines)
  }

  @Override
  public List<File> listFiles(String rootDir)  {
    File rootFile = new File(rootDir);
    File[] list = rootFile.listFiles();
    List<File> filesList = new ArrayList<>();

//    if (list==null){
//      System.out.println("we here");
//      return null;
//    }

    for (File file : list) {
      if (file.isFile()) {
        filesList.add(file);
      } else if (file.isDirectory()) {
        filesList.addAll(listFiles(file.getPath()));
      }
    }
    return filesList;
  }

  @Override
  public List<String> readLines(File inputFile) {
    Scanner s = null;
    try {
      s = new Scanner(inputFile);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    ArrayList<String> list = new ArrayList<String>();
    while (s.hasNextLine()){
      list.add(s.nextLine());
    }
    s.close();
    return list;
  }

  @Override
  public boolean containsPattern(String line) {
    return false;
  }

  @Override
  public void writeToFile(List<String> lines) throws IOException {

  }

  @Override
  public String getRootPath() {
    return null;
  }

  @Override
  public void setRootPath(String rootPath) {

  }

  @Override
  public String getRegex() {
    return regex;
  }

  @Override
  public void setRegex(String regex) {
    this.regex = regex;
  }

  @Override
  public String getOutFile() {
    return outFile;
  }

  @Override
  public void setOutFile(String outFile) {
    this.outFile = outFile;
  }
}
