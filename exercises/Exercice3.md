# Extending PMD

Use XPath to define a new rule for PMD to prevent complex code. The rule should detect the use of three or more nested `if` statements in Java programs so it can detect patterns like the following:

```Java
if (...) {
    ...
    if (...) {
        ...
        if (...) {
            ....
        }
    }

}
```
Notice that the nested `if`s may not be direct children of the outer `if`s. They may be written, for example, inside a `for` loop or any other statement.
Write below the XML definition of your rule.

You can find more information on extending PMD in the following link: https://pmd.github.io/latest/pmd_userdocs_extending_writing_rules_intro.html, as well as help for using `pmd-designer` [here](./designer-help.md).

Use your rule with different projects and describe you findings below. See the [instructions](../sujet.md) for suggestions on the projects to use.

## Answer
we worked with the **commons-cli-master** project to solve the problem of detecting complex code due to multiple nested `if` statements. To address this, we created a custom PMD rule using XPath to catch any instances where there are three or more nested `if` statements.
[thr rule](../Exercise3/ruletest.xml)

We used the following XPath expression in the rule to detect nested `if` statements:

```xml
/IfStatement[descendant::IfStatement[descendant::IfStatement]]
```
This expression looks for an if statement that contains another if, which in turn contains a third one, ensuring that it detects when there are three or more nested if statements.

To test this rule, we ran PMD on the commons-cli-master project using the following command:
```
pmd check -f text -R C:/Users/Lenovo/Downloads/ruletest.xml -d C:/Users/Lenovo/Downloads/commons-cli-master.java -r C:/Users/Lenovo/Downloads/reportttt.txt
```
#### Results 
The rule worked as expected, and PMD identified several occurrences of nested if statements. These results were saved in the file reportttt.txt. Here are some examples from the report:

![image](https://github.com/user-attachments/assets/0fe671cd-d521-4c18-aeba-2ddf8ff44cb8)


