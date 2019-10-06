# Hibernitto [![Build Status](https://ci.notfab.net/job/Libraries/job/Hibernitto/badge/icon)](https://ci.notfab.net/job/Libraries/job/Hibernitto/)

Hibernitto is a persistence library for Hibernate and Hibernate-JPA.

Check versions here: https://maven.notfab.net/Hosted/net/notfab/lib/Hibernitto/

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
    <artifactId>Hibernitto</artifactId>
    <version>2.0</version>
</dependency>
```
Gradle:
```bash
repositories {
    maven { url "https://maven.notfab.net/Hosted" }
}
```
```bash
compile group: 'net.notfab.lib', name: 'Hibernitto', version: '2.0'
```

### Usage

Hibernate:
```java
public class HibernateUtil {

    // Hibernate SessionFactory
    private static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
                registryBuilder.applySettings(getSettings());
                registry = registryBuilder.build();

                MetadataSources sources = new MetadataSources(registry);
                Set<Class<?>> annotated = new Reflections(scanPackages).getTypesAnnotatedWith(Entity.class);
                annotated.forEach(sources::addAnnotatedClass);
    
                Metadata metadata = sources.getMetadataBuilder().build();
                sessionFactory = metadata.getSessionFactoryBuilder().build();
            } catch (Exception ex) {
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
                logger.error("Error during SessionFactory creation", ex);
            }
        }
        return sessionFactory;
    }

    // Hibernitto (basically pass the Dialect + a session)
    public static HibernateEngine getEngine() {
        return new HibernateEngine(getSessionFactory().openSession(), Dialect.MariaDB);
    }

}
```

JPA:
```java
public class Example {
    
    private JPAFactory jpaFactory;
    
    public Example() {
        // With persistence.xml
        jpaFactory = new JPAFactory("your.persistence.unit.name");
        // With builder
        jpaFactory = JPAFactory.builder()
            .setName("MyDatabase")
            .setIp("127.0.0.1")
            .setDatabase("test")
            .build();
        // Load entity
        try (JPAEngine engine = jpaFactory.getEngine()) {
            User user = engine.get(User.class, 123); // Get user with id 123
            user = engine.get(User.class, new SQLEquals("name", "Jhon")); // Gets the first user with name Jhon
            List<User> users = engine.getList(User.class, new SQLLike("email", "%hotmail%")); // all users with a hotmail
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        // Save (or update) entity
        try (JPAEngine engine = jpaFactory.getEngine()) {
            User user = new User(4, "Jhon", "jhon@example.com");
            engine.save(user);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        // Reload and Delete entity
        try (JPAEngine engine = jpaFactory.getEngine()) { 
            User user = ...;
            engine.refresh(user); // Refresh
            engine.remove(user); // Delete (make sure to delete all relations before!)
            engine.deatch(user); // Detach
            engine.unwrap(user); // Unwrap
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        // Native query (if needed - try avoiding!)
        try (JPAEngine engine = jpaFactory.getEngine()) { 
            engine.createNativeQuery("SELECT * FROM Users");
            engine.createNativeQuery("SELECT * FROM Users", User.class); // With class
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
