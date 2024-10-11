package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.io.FileWriter;
import java.io.IOException;

public class CyclomaticComplexity extends VoidVisitorWithDefaults<Void> {

    private StringBuilder report = new StringBuilder();

    public CyclomaticComplexity() {
        en-tête du rapport CSV
        report.append("Package|Class|Method|Cyclomatic Complexity\n");
    }

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for (TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, arg);
        }
        saveReport(unit.getPackageDeclaration().map(pd -> pd.getNameAsString()).orElse("default"));
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if (!declaration.isPublic()) return;
        for (MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }
        // Gérer les types imbriqués
        for (BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration) {
                member.accept(this, arg);
            }
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        int complexity = calculateCyclomaticComplexity(declaration);

        // Obtenir le nom de la méthode
        String methodName = declaration.getNameAsString();

        // Obtenir le package
        String packageName = declaration.findCompilationUnit()
                .flatMap(CompilationUnit::getPackageDeclaration)
                .map(pd -> pd.getName().toString())
                .orElse("[No Package]");

        // Obtenir le nom de la classe
        String className = declaration.findAncestor(ClassOrInterfaceDeclaration.class)
                .map(ClassOrInterfaceDeclaration::getNameAsString)
                .orElse("[Anonymous Class]");

        // Ajouter la ligne au rapport
        report.append(String.format("%s|%s|%s|%d\n", packageName, className, methodName, complexity));
    }

    private int calculateCyclomaticComplexity(MethodDeclaration method) {
        int n = 0; // Nombre de nœuds
        int e = 0; // Nombre d'arêtes 
        int p = 1; // Nombre de composantes connectées 

        // Compter les nœuds pour chaque type de structure de contrôle
        int ifCount = method.findAll(IfStmt.class).size();
        int forCount = method.findAll(ForStmt.class).size();
        int whileCount = method.findAll(WhileStmt.class).size();
        int doCount = method.findAll(DoStmt.class).size();
        int switchCount = method.findAll(SwitchStmt.class).size();
        int breakCount = method.findAll(BreakStmt.class).size();
        int continueCount = method.findAll(ContinueStmt.class).size();

        n += ifCount;
        n += forCount;
        n += whileCount;
        n += doCount;
        n += switchCount;
        n += breakCount;
        n += continueCount;

        // Arêtes
        e += ifCount * 2; 
        e += forCount * 2; 
        e += whileCount * 2; 
        e += doCount * 2; 
        e += method.findAll(SwitchStmt.class).stream()
                    .mapToInt(switchStmt -> switchStmt.getEntries().size() + 1)
                    .sum();

        if (n == 0 && e == 0) {
            return 1; // Complexité cyclomatique minimale
        }

        return e - n + 2 * p;
    }

    // Sauvegarde le rapport dans un fichier CSV
    private void saveReport(String packageName) {
        try (FileWriter writer = new FileWriter("cyclomatic-complexity-report.csv")) {
            writer.write(report.toString());
            System.out.println("Report saved to cyclomatic-complexity-report.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
