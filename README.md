# aquiletour.ca

A simple ticketing system tailored for students and teachers

## Installation

1. Copy dummy configuration files

        $ sh scripts/init.sh

1. Build and run with `gradle`:

        $ ./gradlew run

1. Browse to <a href="http://localhost:1024" target="_blank">http://localhost:1024</a>


## Configuration

1. Configuration files are as follows:
    * `webapp/students.json`: list of stucents
    * `webapp/server/conf/Conf.json`: server settings
    * `webapp/server/conf/teachers.json`: list of teachers

## Deploying

1. You need to copy the following `.jar` files to `webapp/server/libs/`

        * `gson-2.8.5.jar`
        * `guava-27.0.1-jre.jar`
        * `Java-WebSocket-1.4.0.jar`
        * `slf4j-api-1.7.25.jar`

    NOTE: you'll find these the gradle cache at `~/.gradle`, e.g.

                $ find ~/.gradle -name "gson.*jar"

1. Build `aquiletour.jar`:

        $ sh scripts/refresh_jar.sh

1. Run `aquiletour.jar`

        $ cd webapp
        $ sh server/scripts/run.sh

## Multiple teachers

1. TODO: script `generate_conf_files.py` and explain how to configure Apache
