# GitHub Activity tracker

## Features:
- Rest API to fetch user activity `/github/<username>`
- CLI optionn to fecth user activity `.\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=manas10001"`
- Authentication supported using GitHub Action Token

## Setup:
1. Clone the repo:
    ```
    git clone https://github.com/manas10001/github-activity-tracker.git
2. Create a GitHub Personal Access Token at the following URL [https://github.com/settings/personal-access-tokens](https://github.com/settings/personal-access-tokens)
3. Add the token to the `application.properties` file and assign it to `github.token` variable.

## Usage:
### Rest API: 

Run the application: `.\mvnw.cmd spring-boot:run`

Then open the browser / Postman and do a api call to: [http://localhost:8080/github/manas10001](http://localhost:8080/github/manas10001)

### CLI:

Run the application and pass the username as a argument as follows
```
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=manas10001"
