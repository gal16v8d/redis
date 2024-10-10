# redis

Basic sample of Redis usage.

For the sample you will need:

* Java 21
* Maven
* Docker

For get started with the docker image execute:

- docker pull redis
- redis instance: docker run -p 6379:6379 -d redis
- redis with persistent storage: docker run -p 6379:6379 -d redis redis-server --appendonly yes