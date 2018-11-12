# PersistEngine [![Build Status](https://ci.notfab.net/job/Libraries/job/PersistEngine/badge/icon)](https://ci.notfab.net/job/Libraries/job/PersistEngine/)

PersistEngine is a persistence library for Hibernate.

Check versions here: https://maven.notfab.net/Hosted/net/notfab/lib/PersistEngine/

### Installation

**Note**: Requires Hibernate >= 5.2.17

Maven:
```xml
<repositories>
    <repository>
        <id>NotFab</id>
        <url>https://maven.notfab.net/Hosted</url>
    </repository>
</repositories>
```
```xml
<dependency>
    <groupId>net.notfab.lib</groupId>
    <artifactId>PersistEngine</artifactId>
    <version>1.0.2</version>
</dependency>
```
Gradle:
```bash
repositories {
    maven { url "https://maven.notfab.net/Hosted" }
}
```
```bash
compile group: 'net.notfab.lib', name: 'PersistEngine', version: '1.0.2'
```

### Usage

```java
public class Example {
    
    private PersistenceFactory persistenceFactory;
    
    public Example() {
        persistenceFactory = new PersistenceFactory("your.persistence.unit.name");
        // Load entity
        try (PersistEngine engine = persistenceFactory.getEngine()) {
            User user = engine.get(User.class, 123); // Get user with id 123
            user = engine.get(User.class, new SQLEquals("name", "Jhon")); // Gets the first user with name Jhon
            List<User> users = engine.getList(User.class, new SQLLike("email", "%hotmail%")); // all users with a hotmail
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        // Save (or update) entity
        try (PersistEngine engine = persistenceFactory.getEngine()) {
            User user = new User(4, "Jhon", "jhon@example.com");
            engine.save(user);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        // Reload and Delete entity
        try (PersistEngine engine = persistenceFactory.getEngine()) { 
            User user = ...;
            engine.refresh(user); // Refresh
            engine.delete(user); // Delete (make sure to delete all relations before!)
            engine.deatch(user); // Detach
            engine.unwrap(user); // Unwrap
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
```

### Contributors

- Fabricio20 [Maintainer]

### License
This project is licensed under the MIT License, for more information, please check the LICENSE file.
