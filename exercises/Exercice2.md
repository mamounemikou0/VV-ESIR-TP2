
# Using PMD


Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset (see the [pmd install instruction](./pmd-help.md)). Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.


## Answer

### Projet utilisé:
https://github.com/apache/commons-cli.git

### True positive: Retourner une collection vide plutôt que null.
```java
public String[] getOptionValues(final Option option) {
       if (option == null) {
           return null; // Return an empty collection rather than null.
       }
}
```
### Changes
```java
public String[] getOptionValues(final Option option) {
       if (option == null) {
           return new String[0]; // change
       }
}
```
### Explanation of the change in the code: 
This code change will help us avoid potential issues when using code that calls getOptionValues. Instead of returning null, the method will return an empty array, preventing errors that might occur if we forget to handle the possibility of a null return value.

### False positive: Comment is too large: Line too long

/**
 * @return Value of the argument if option is set, and has an argument, otherwise {@code defaultValue}.
 */

Cela ne vaut pas la peine d'être corrigé parce que sa reformulation pour respecter une limite de longueur ne change pas significativement la clarté du code, le commentaire est compréhensible même s'il dépasse légèrement la longueur de ligne recommandée.


