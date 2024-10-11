#### Implemetation de la class [PrivateFields.java](../javaparser-starter/src/main/java/fr/istic/vv/PrivateFields.java)  ###
### changes in main.java
```java
SourceRoot root = new SourceRoot(file.toPath());
       PrivateFields printer = new PrivateFields();// Change here
       root.parse("", (localPath, absolutePath, result) -> {
           result.ifSuccessful(unit -> unit.accept(printer, null));
           return SourceRoot.Callback.Result.DONT_SAVE;
       });
```

### Results: [private-fields-report.csv](./private-fields-report.csv) 

