# Code of your exercise

package fr.istic.vv;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;
import java.io.FileWriter;
import java.io.IOException;
public class PrivateFields extends VoidVisitorWithDefaults<Void> {
   private StringBuilder report = new StringBuilder();
   public PrivateFields() {
       report.append("Package|Class|Field\n");
   }
   @Override
   public void visit(CompilationUnit unit, Void arg) {
       for (TypeDeclaration<?> type : unit.getTypes()) {
           type.accept(this, arg);
       }
       saveReport(unit.getPackageDeclaration().map(pd -> pd.getNameAsString()).orElse("default"));
   }
   @Override
   public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
       if (!declaration.isPublic()) return;
       String packageName = declaration.findCompilationUnit()
               .flatMap(CompilationUnit::getPackageDeclaration)
               .map(pd -> pd.getNameAsString())
               .orElse("default");
      
       for (FieldDeclaration field : declaration.getFields()) {
           if (field.isPrivate() && !hasPublicGetter(declaration, field)) {
               // ajout l'infos du champ au rapport
               report.append(String.format("%s|%s|%s\n", packageName, declaration.getNameAsString(), field.getVariable(0).getNameAsString()));
           }
       }
      
       super.visit(declaration, arg);
   }
   private boolean hasPublicGetter(ClassOrInterfaceDeclaration declaration, FieldDeclaration field) {
       String fieldName = field.getVariable(0).getNameAsString();
       String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
       for (MethodDeclaration method : declaration.getMethods()) {
           if (method.isPublic() && method.getNameAsString().equals(getterName) && method.getParameters().isEmpty()) {
               return true;
           }
       }
       return false;
   }
   private void saveReport(String packageName) {
       try (FileWriter writer = new FileWriter("private-fields-report.csv")) {
           writer.write(report.toString());
           System.out.println("Report saved to private-fields-report.csv");
       } catch (IOException e) {
           e.printStackTrace();
       }
   }
}
