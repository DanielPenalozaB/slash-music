#!/bin/bash

# ================================
# SLASH MUSIC - Run Script
# ================================

export SPRING_APPLICATION_NAME=music
export SERVER_PORT=8080

# Database
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=slash_db
export POSTGRESQL_USER=slash_db_user
export POSTGRESQL_PASSWORD='SlashMusic2025*.'
export POSTGRESQL_URL=jdbc:postgresql://$DB_HOST:$DB_PORT/$DB_NAME

# JWT
export JWT_SECRET=0728da49593493141f1fb994db6feb134e488e49b80e7ec1b19d19c9c1129acf
export JWT_EXPIRATION=86400000
export JWT_REFRESH_EXPIRATION=604800000

# Environment
export ENVIRONMENT=dev

# ================================
# Run the app
# ================================
java -jar build/libs/music-0.0.1-SNAPSHOT.jar --spring.profiles.active=$ENVIRONMENT
