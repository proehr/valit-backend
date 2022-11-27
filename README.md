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


