### Implementation of the class [CyclomaticComplexity.java](./CyclomaticComplexity.java) 

### Changes in the main.java
```java
SourceRoot root = new SourceRoot(file.toPath());
       CyclomaticComplexity printer = new CyclomaticComplexity(); // Change
       root.parse("", (localPath, absolutePath, result) -> {
           result.ifSuccessful(unit -> unit.accept(printer, null));
           return SourceRoot.Callback.Result.DONT_SAVE;
       });
```

### Results: [report](./cyclomatic-complexity-report.csv) 
to show the histogram we used python: [cyclomatic.py](./cyclomatic.py) 

### Histograms
Projet used:
https://github.com/apache/commons-collections.git
<img width="742" alt="CC2" src="https://github.com/user-attachments/assets/eb48e67d-a2d1-430c-b350-f1b45af13632">

Projet used:
https://github.com/apache/commons-cli.git
<img width="749" alt="CC1" src="https://github.com/user-attachments/assets/817f82b8-c282-403e-af42-b72247a16864">

### comparison between the 2 histograms
The two histograms show that both projects have the majority of methods with a cyclomatic complexity of 1, it means that most methods are simple.The difference is that the Project 1 is significantly larger, with around 2400 methods compared to 240 in Project 2.Additionally, Project 1 has some methods reaching up to 24, whereas Project 2 tops out at 14. This suggests that Project 1 contains more complex logic than Project 1.
