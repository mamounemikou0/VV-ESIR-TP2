

```java
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
        report.append("Package|Class|Method|Cyclomatic Complexity\n");
    }

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for (TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, arg);
        }
        // pour sauvegarder le rapport une fois l'analyse terminée
        saveReport(unit.getPackageDeclaration().map(pd -> pd.getNameAsString()).orElse("default"));
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if (!declaration.isPublic()) return;
        for (MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }
        // Gére les types imbriqués
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
        String methodName = declaration.getNameAsString();
        String packageName = declaration.findCompilationUnit()
                .flatMap(CompilationUnit::getPackageDeclaration)
                .map(pd -> pd.getName().toString())
                .orElse("[No Package]");
        // pour obtenir le nom de la classe sans le package
        String className = declaration.findAncestor(ClassOrInterfaceDeclaration.class)
                .map(ClassOrInterfaceDeclaration::getNameAsString)
                .orElse("[Anonymous Class]");

        // Ajouter l'information au rapport
        report.append(String.format("%s|%s|%s|%d\n", packageName, className, methodName, complexity));
    }

 // méthode pour calculer la complexité cyclomatique
    private int calculateCyclomaticComplexity(MethodDeclaration method) {
        int n = 0; // Nombre de nœuds (structures de contrôle)
        int e = 0; // Nombre d'arêtes (chemins possibles)
        int p = 1; // Nombre de composantes connectées (1 pour les méthodes simples)

        // on compte les nœuds pour chaque type de structure de contrôle
        int ifCount = method.findAll(IfStmt.class).size();
        int forCount = method.findAll(ForStmt.class).size();
        int whileCount = method.findAll(WhileStmt.class).size();
        int doCount = method.findAll(DoStmt.class).size();
        int switchCount = method.findAll(SwitchStmt.class).size();
        int breakCount = method.findAll(BreakStmt.class).size();
        int continueCount = method.findAll(ContinueStmt.class).size();

        // Nœuds: chaque structure de contrôle est un nœud
        n += ifCount;
        n += forCount;
        n += whileCount;
        n += doCount;
        n += switchCount;
        n += breakCount;
        n += continueCount;

        // arrtes:chaque structure de contrôle ajoute des chemins possibles
        e += ifCount * 2; // Chaque if crée 2 chemins (vrai/faux)
        e += forCount * 2; // Chaque for crée 2 chemins (continuer/fin)
        e += whileCount * 2; // Chaque while crée 2 chemins (continuer/fin)
        e += doCount * 2; // Chaque do crée 2 chemins (continuer/fin)

        // Les switch créent un chemin par case de plus un chemin par défaut
        e += method.findAll(SwitchStmt.class).stream()
                    .mapToInt(switchStmt -> switchStmt.getEntries().size() + 1)
                    .sum();

        // Si on trouve pas une structure de contrôle, la méthode est triviale
        if (n == 0 && e == 0) {
            return 1; // Complexité cyclomatique minimale
        }

        // calculer la complexité cyclomatique avec cette formule
        return e - n + 2 * p;
    }

    // Sauvegarder le rapport dans un fichier csv
    private void saveReport(String packageName) {
        try (FileWriter writer = new FileWriter("cyclomatic-complexity-report.csv")) {
            writer.write(report.toString());
            System.out.println("Report saved to cyclomatic-complexity-report.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

```
