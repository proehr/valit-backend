# Local Setup

## Database

This backend project requires a postgres server running on port 5432 with an already existing database

* Name: `study_feedback`
* Username: `postgres`
* Password: `postgres`

Please make sure this is set up before starting your application. In case you want to use different credentials, you can
modify those in the
application.yml local properties, but do not push these changes.

### How to set up your postgres db

* [Download a PostgreSQL installer](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads), version
  shouldn't matter, but I use v13
* Follow the instructions in your installer
    * PostgreSQL comes with a tool called pgAdmin, which is very useful. Make sure to install this alongside your
      postgres server
    * PostgreSQL default port ist 5432, so keep the default setting
    * Declare the password for `postgres` User as stated above
* Once the installer has finished, you can use pgAdmin to create a database
    * Upon opening pgAdmin for the first time, you will be prompted to pick a master password. Only you will need access
      to your local server, so make sure to pick a secure password
    * The left sidebar will most likely have your newly installed postgres server set up already
    * Open your postgres server and enter the password for the `postgres` user
    * In your server overview, go to `Object>Create>Database...` and create a db called `study_feedback`

## Run Config

When starting this Application, make sure to enable the `local` profile in your IntelliJ RunConfiguration.

# Conventions

## Git

We want to use a feature branch workflow in this project. In case you haven't worked
with this before, [Atlassian](https://www.atlassian.com/git/tutorials/comparing-workflows/feature-branch-workflow)
has a well summarized description of the workflow. We are not rebasing onto main before executing a merge. In case a
merge conflict arises, we merge the `main` branch into the conflicting feature branch and
resolve any conflicts there.

* Branch Naming: Generated from gitlab issues, e.g. `68-code-conventions`
* Commit message format: `imi-wise-2022/projectdocumentation#XX - Descriptive commit message` with `XX`being the gitlab
  issue number

### Example Flow

When implementing a feature, this may be your workflow:

* Check out the latest `main` version locally
* Create a new branch from `main`, take the branch name from your feature issue in gitlab
* Write your code
* Add all changes for your feature to your commit
* Commit your changes with a descriptive commit message, linking to your gitlab feature issue
  as shown in the commit message format above.
* Push your changes
* Create a merge request to `main`, add a reviewer and give any needed info for a review in the
  merge request description
* Once your changes have been reviewed and accepted, merge them into `main`.

## Code Conventions

All of these are not mandatory and often up for discussion, due to personal preferences.

### SonarLint

IntelliJ has a SonarLint plugin which can be configured to review all your changes before a commit.
Linters can be used to detect code smells and vulnerabilities, while also enforcing specific code conventions
and style choices. If SonarLint is used, all shown issues should at least be reviewed before a commit, and
**all** Blocker and Critical issues should be resolved.

### Other general guidelines

* Entities should not be handed around the application too much. In case you want to
  return an entity from a controller, please use [Mapstruct](https://mapstruct.org/) to create a
  [Data Transfer Object (DTO)](https://www.baeldung.com/java-dto-pattern) and replace all uses of the entity class
  with this DTO.
* Use Lombok to avoid boilerplate code. In most cases, there is no need to write your own getters,
  setters or constructors.
* Please re-format your code, run tests (if we add any) and check any TODOs you've added before you commit.
  IntelliJ can be configured to do this before you commit.
