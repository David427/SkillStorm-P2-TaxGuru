# SkillStorm-Project2-TaxGuru
Project to build a Federal tax preparation service deployed on AWS.

# **Objective**
- Create a tax preparation website styled like the IRS website.
    - Only worry about Federal taxes.
- Microservices architecture? TBD...

# **Functional Requirements**
- User administration | Create an account, login.
    - We probably need at least two users to showcase full functionality.
- User information | Enter personal info.
- Financial information | Enter financial info.
- Tax information | Show a user’s Federal tax breakdown, how much their return is or how much they owe, etc.
- Technology stack (as of 4/10/24; subject to change):
    - Spring Boot
    - JUnit
    - React with TypeScript
    - Jest
    - Redux
    - Trussworks
    - i18n
    - MongoDB and DocumentDB with AWS
    - AWS
    - Spring Security
    - OAuth2
    - Docker
    - Kubernetes
    - GitHub

# **Non-Functional Requirements**
- Professional UI built using Trussworks.
- Create an account page where users can view and update account, personal, and financial info.

# **Notes**
- Use TurboTax as inspiration (though hopefully, not as complex in terms of finding tax breaks, etc.)
- Create a landing page with company information and a link for account creation.
- Create account creation & login pages.
- Suggested process flow: Four stages/pages.

## **Process Flow**
1. Personal information | Full name, date of birth, SSN, email, phone, address, etc.
2. Financial information | Income, employment details, etc.
3. Review page | Display all saved info and allow the user to make corrections. Submit the tax return.
4. Results page | Show the final refund or amount owed and the breakdown of the taxes.
