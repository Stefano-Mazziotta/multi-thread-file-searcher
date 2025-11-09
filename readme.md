# MultiThreadFileSearcher

A Java application that searches for a specific file in the file system using multiple threads for faster performance.

## Description

This project implements a multi-threaded file search utility that traverses directories recursively to find a target file. It uses Java's ExecutorService to manage a thread pool, allowing concurrent directory exploration for improved search speed.

## Features

- Multi-threaded file searching using available CPU cores
- Recursive directory traversal
- Configurable starting search path
- Displays file location when found
- Reports elapsed search time

## Requirements

- Java 8 or higher
- Linux/Unix system (or modify paths for Windows)

## Usage

### Compile

```bash
javac multiThreadFileSearcher/*.java
```

### Run

Search from root directory:
```bash
java multiThreadFileSearcher.Main <fileName>
```

Search from specific directory:
```bash
java multiThreadFileSearcher.Main <fileName> <startPath>
```

### Examples

```bash
# Search for a file starting from root
java multiThreadFileSearcher.Main go-system-analyst.sh

# Search for a file starting from home directory
java multiThreadFileSearcher.Main go-system-analyst.sh /home/smzt

# Search for a configuration file
java multiThreadFileSearcher.Main config.properties /etc
```

## Output

```
Starting search using 8 threads...
File found: /home/smzt/go-system-analyst.sh
Elapsed time: 0.15 seconds
```

If the file is not found:
```
Starting search using 8 threads...
File not found: nonexistent.txt
Elapsed time: 2.34 seconds
```

## Project Structure

```
MultiThreadFileSearcher/
└── src/
    └── multiThreadFileSearcher/
        ├── Main.java           # Entry point
        ├── FileSearcher.java   # Manages thread pool and search
        └── SearchTask.java     # Runnable task for directory searching
```

## How It Works

1. **Main**: Parses command-line arguments and initiates the search
2. **FileSearcher**: Creates a thread pool and submits the initial search task
3. **SearchTask**: Recursively explores directories, submitting new tasks for subdirectories and checking files for matches
4. When a file is found, the search terminates and displays the file path

## Performance

The application automatically uses all available CPU cores to parallelize the search, significantly reducing search time on systems with multiple processors.

## Author

Created as a university project for Programming Workshop 3

## License

Educational use only