Usage:
```java
public class Example {
    PersistEngine engine = new PerisistEngine("your.package.on.jpa");
    
    public void main(Person person) {
        pesistengine.refresh(person); // Loads it's data
        person.setName("some name");
        engine.persist(person); // Saves the object
        Person other = engine.get(Person.class, new SQLLike("name", "%Jhon%"));
        // ^ Loads object with custom filter.
    }
}
```