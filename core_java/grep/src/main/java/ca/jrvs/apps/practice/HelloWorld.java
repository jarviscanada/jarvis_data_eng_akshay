package ca.jrvs.apps.practice;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class HelloWorld {

  // Your program begins with a call to main().
  // Prints "Hello, World" to the terminal window.
  public static void main(String args[]) throws IOException {
    System.out.println("Hello, World");
    int[][] test = {{1, 7}, {1, 2}};
//    File[] listedFiles = new File(
//        "/home/centos/dev/jarvis_data_eng_akshay/core_java/grep/src/main/java").listFiles();
//    Stream<Path> files = Files
//        .walk(Paths.get("/home/centos/dev/jarvis_data_eng_akshay/core_java/grep/src/main/java"));
//
//
//    List<File> result = files.filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
//    files.close();
    String rootDir="/home/centos/dev/jarvis_data_eng_akshay/core_java/grep/src/main/java";
//listFiles(rootDir);

    Scanner s = new Scanner(new File("/home/centos/dev/jarvis_data_eng_akshay/core_java/grep/src/main/java/ca/jrvs/apps/practice/HelloWorld.java"));
    ArrayList<String> list = new ArrayList<String>();
    while (s.hasNextLine()){
      list.add(s.nextLine());
    }
    s.close();

  }
  static public List<File> listFiles(String rootDir) {
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
    int test=5;
    return filesList;
  }
}