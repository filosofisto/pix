



# XTI Solutions

## Kafka Container

### Start Zookeeper

docker run -d `
  --name zookeeper `
  -p 2181:2181 `
  -e ZOOKEEPER_CLIENT_PORT=2181 `
  -e ZOOKEEPER_TICK_TIME=2000 `
  confluentinc/cp-zookeeper:7.6.0


### Start Kafka broker

docker run -d `
  --name kafka `
  -p 9092:9092 `
  --link zookeeper `
  -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 `
  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 `
  -e KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=PLAINTEXT:PLAINTEXT `
  -e KAFKA_BROKER_ID=1 `
  -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 `
  confluentinc/cp-kafka:7.6.0


### Create Topic

docker exec -it kafka kafka-topics --bootstrap-server localhost:9092 --create --topic pix-transactions --partitions 1 --replication-factor 1


## MQ Container

### Create Queues

docker exec -it ibm-mq bash
runmqsc QM1
DEFINE QLOCAL(REQUEST.Q)
DEFINE QLOCAL(RESPONSE.Q)
END

### IBMMQ Console

https://localhost:9443/ibmmq/console
