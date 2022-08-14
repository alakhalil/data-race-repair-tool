# data-race-repair-tool


## Requirements

The project requires `docker` and `docker-compose`, as well as an `sh` command, which can be executed from the command-line within the docker container.


## Installation

`sudo docker-compose build`


## Running the tool

Put input data in the `./in` directory. It must contain `.java` files to be processed (in the correct data-structure java expects).
A `@ThreadSafe` annotation will be added to every file in the project, in order to test it, and datarace bugs found by RacerD will be fixed.
The fixed files will be put in the `./out` directory.

Run the command using: `sudo docker-compose run fixer`

