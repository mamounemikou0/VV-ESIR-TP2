# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer
## Answers

### 1) Under what circumstances do Tight Class Cohesion (TCC) and Loose Class Cohesion (LCC) metrics produce the same value for a given Java class?

TCC and LCC will produce the same value under the following circumstances:

- **All Methods Are Directly Connected**: This means that every method in the class shares at least one instance variable with every other method. When this happens, all method pairs are directly connected, and thus, the TCC value will capture all method connections. Since LCC also considers indirect connections but does not need to account for them (because all connections are already direct), the values of TCC and LCC will be identical.
  
- **No Indirect Connections Are Needed**: LCC allows for both direct and indirect connections between method pairs. If no indirect connections are required, LCC will not have any additional method pairs to account for beyond those already counted by TCC. In this case, LCC = TCC.

There's an example:

```java
public class Author {
    private int id;  // Instance variable

    public int getId() {
        return id;  // Accesses instance variable
    }

    public void incrementNumber() {
        id++;  // Accesses instance variable
    }

    public void setId(int id) {
        this.id = id;  // Accesses instance variable
    }
}
```

In this example, all methods (`getId()`, `incrementNumber()`, and `setId()`) directly access the same instance variable, `id`. Therefore, every possible method pair:

- (`getId()`, `incrementNumber()`)
- (`getId()`, `setId()`)
- (`incrementNumber()`, `setId()`)

is directly connected. Since no indirect connections are needed, TCC and LCC will have the same value.
TCC=(Directly connected method pairs​)/(Total method pairs)=3/3=1
LCC=(Directly or indirectly connected method pairs​)/(Total method pairs)=3/3=1
⇒ In this example TCC=LCC
