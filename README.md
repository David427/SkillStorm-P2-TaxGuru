# SkillStorm-Project2-TaxGuru
Project to build a Federal tax preparation service deployed on AWS.

# **Objective**
- Create a tax preparation website styled like the IRS website.

# **Functional Requirements**
- User administration | Create an account, login.
- User information | Enter personal info.
- Financial information | Enter financial info.
- Tax information | Show a user’s Federal tax breakdown, how much their return is or how much they owe, etc.
- 80% line code coverage at the Service layer (backend) and in the frontend.
- Technology stack:
    - Spring Boot (monolithic app architecture)
    - JUnit
    - React with TypeScript
    - Jest
    - Trussworks
    - i18n
    - Aurora v1 (Amazon's RDB)
    - AWS
    - Spring Security
    - OAuth2 (JWT)
    - Docker
    - GitHub

# **Non-Functional (Bonus) Requirements**
- Create an account page where users can view and update account, personal, and financial info.
- Configure OAuth2 social login (using Google as an authorization server).

## **Process Flow**
1. Personal information | Full name, date of birth, SSN, email, phone, address, etc.
2. Financial information | Claim dependents, enter spouse income/tax withheld if filing jointly, etc.
3. W2 info | Optional. Enter W2 details.
4. 1099-MISC info | Optional. Enter 1099-MISC details to represent a self-employed individual.
5. Credits & deductions | Enter possible adjustments to taxable income.
6. Review page | Display all saved info and allow the user to make corrections. Submit the tax return.
7. Results page | Show the final refund or amount owed and the breakdown of the taxes.

# How to Build and Start the Project Containers

> All commands are run from the root project directory

1. Create a `.env` file in the root project directory
   - Update the values found in `.env.example` and docker will set those values as environment variables for the containers

2. Package the java project using the following command:
```bash
mvn clean package -DskipTests
```

3. Start the containers using docker compose:
```bash
# start all containers (--build is only necessary the first time)
docker compose up -d --build

# start only the frontend / backend / db
docker compose up -d frontend

# stop all containers
docker compose down

# stop specific container
docker compose down backend
```
