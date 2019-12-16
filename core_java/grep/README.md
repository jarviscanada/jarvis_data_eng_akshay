# Introduction
This application replicates some of the core features of the linux GREP command. This app is built in Java. It uses recursion to go
through all files in a given directory (even sub directories), and outputs any lines from files matching the input pattern into a temprary
folder. I learned Java I/O, lambda, streams, how to use intellij IDE to my advantage, how to modularize code into functions.

# Usage

Program USAGE: regex rootPath outFile

Search `.*IllegalArgumentException.*` pattern from `./grep/src` folder recursively and output the result to `/tmp/grep.out` file

Program arguments `.*IllegalArgumentException.* ./grep/src /tmp/grep.out`

# Pseudocode
```
matchedLines = []
for file in listFilesRecursively(rootDir)
  for line in readLines(file)
      if containsPattern(line)
        matchedLines.add(line)
writeToFile(matchedLines)
```

# Performance Issues
(30-60 words)
Generally, since the files are stored in memory, they can cause memory/resource issues.
To overcome this, `JavaGrepLambdaImp.java` implements Lambda and Stream APIs in `listFiles()` and `readLines()` methods 
that do not run the same issues. Java Streams supports functional-style operations on streams of elements, such as map-reduce 
transformations on collections without actually storing the data. These are processed byte by byte.

# Improvements
1. I'd like to improve the output file so that it contains more information such as which file the results are from, which line #'s
they are from etc.
2. If there are multiple instances of the exact same match, then instead of duplicating the lines, I would just show which line numbers
they are from
3. Make a GUI to run this app rather than having to run it though an IDE
