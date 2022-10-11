# Build
Make sure that Docker is installed and running. Build application's dockerimage with `sudo docker-compose build`.

# Launch
Launch dockerimage with `sudo docker-compose up -d`.

# Operation 
The application operates as an interface to [file system emulator project](https://github.com/Xeuten/Yandex-backend-school-test-project). Type `/start` to view available commands.

# Connection adjustments for local launch and inside Docker
Datasource for local launch:

`spring.datasource.url=jdbc:postgresql://localhost:5432/updates`

Datasource for launch inside Docker:
  
`spring.datasource.url=jdbc:postgresql://tgpostgres:5432/updates`

URLs:

`localhost` corresponds to local launch, `host.docker.internal` corresponds to Docker.
