# SongSearcher Setup

[SongSearcher Description](./description.md)

## Git LFS Note
This repository utilizes Git Large File Storage (Git lfs) to track the Doc2VecEmbedder pretrained model. 
Please ensure that you have 'git lfs' installed and configured before cloning/downloading to properly retrieve 
the pretrained model. Installation Documentation can be found [here](https://git-lfs.com/).

After 'git lfs' is installed. You can clone this repository as normal.

## Database Setup

This application uses a Postgres Database with a PGVector extension. To use this application as intended, please follow the below instructions to setup/intialize the database. Included below are Docker instructions to pull/run the database, however other options and information can be found [here](https://github.com/pgvector/pgvector).

- Install Docker. Instructions can be found [here](https://www.docker.com/get-started/).
- Run ```docker pull pgvector/pgvector:pg17```
- Start pgvector container through Docker Desktop or command line
- Once Postgres is running, create a new database called songsearcherdb. 
    - ```CREATE DATABASE songsearcherdb```
- Inside of new database `songsearcherdb`, add the vector extension
    - ```CREATE EXTENSION vector```

To use the database with the Spring Boot Backend, update the `application.properties` file inside 'SongSearcherBackend/src/main/resources/' by changing the `spring.datasource.url` variable as needed for your database.

# Spring Boot Backend

SongSearcher employs a Spring Boot Application for the backend. 

- Package the Spring Boot Application
    ```
    cd SongSearcherBackend/
    ./mvnw package
    ```
- Start it
    ```
    java -jar ./target/SongSearcher-0.0.1-SNAPSHOT.jar
    ```

# React Frontend 

SongSearcher uses the React Library for the frontend.

- Install dependencies for React Application
    ```
    cd SongSearcherFrontend
    npm install 
    ```
- Run it
    ```
    npm run dev
    ```
