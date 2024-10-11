### changes in main.java
```java
SourceRoot root = new SourceRoot(file.toPath());
       **PrivateFields printer = new PrivateFields();** 
       root.parse("", (localPath, absolutePath, result) -> {
           result.ifSuccessful(unit -> unit.accept(printer, null));
           return SourceRoot.Callback.Result.DONT_SAVE;
       });
```

### Results: private-fields-report.csv###
Package|Class|Field
org.apache.commons.cli|AlreadySelectedException|serialVersionUID
org.apache.commons.cli|AlreadySelectedException|group
.
.
.
org.apache.commons.cli|UnrecognizedOptionException|serialVersionUID
