This directory is shared with the docker-compose service `fixer`, meaning it can access the contents in a 'simulated' directory `data`.

Any files put here (including this one) can be accessed by the service, and outputs can be put here by it. It serves as a sort of 'communication hub'.