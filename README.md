### About this project

Graded security assignment. Building a full-stack community-based blog site with Vue, Java Spring, Neo4j and OAuth2.

### How to run locally

1. **Download or clone the project**


2. **Configure and start the Server App:**
    - Add the provided credentials to the `application.properties` file
    - Execute the `ServerApplication.java` file to start the app


3. **Prepare and run the Client:**

```
    - cd client
    - npm i
    - npm run dev
```

4. **Log in using the existing mock user accounts**
```
   Admin account:
   { username: "admin", password: "password" }

   User account:
   { username: "user", password: "password" }

   For mock user configuration, refer to the "MockUsersConfig.java" in the neo4j server package
```

### Feature Overview

- Viewing, searching and filtering all blogs as an unauthenticated user
- User login with role-based access control
- JWT token authentication
- Google login with code authorization flow and PKCE
- Publishing blogs as an authenticated user
- Deleting blogs as an authenticated admin
- Privacy & vulnerability disclosure policy
    
