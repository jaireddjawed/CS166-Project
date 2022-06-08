#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

DB="cs166_project_db"
PORT=5432
USER="jairedjawed"

# Compile the java files
javac -d $DIR/../classes $DIR/../src/Cafe.java ../src/MenuOptions.java ../src/PrettyPrinter.java ../src/ProfileOptions.java ../src/OrderOptions.java ../src/User.java

# Run the java files
java -cp $DIR/../classes:$DIR/../lib/postgresql-42.3.5.jar Cafe $DB $PORT $USER
