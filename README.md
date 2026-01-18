# XTI Solutions

## Kafka Container

>> Running Kafka Container:

docker run -d `
  --name kafka `
  -p 9092:9092 `
  -e KAFKA_PROCESS_ROLES=broker,controller `
  -e KAFKA_NODE_ID=1 `
  -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092,CONTROLLER://0.0.0.0:9093 `
  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 `
  -e KAFKA_CONTROLLER_LISTENER_NAMES=CONTROLLER `
  -e KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=PLAINTEXT:PLAINTEXT,CONTROLLER:PLAINTEXT `
  -e KAFKA_CONTROLLER_QUORUM_VOTERS=1@localhost:9093 `
  confluentinc/cp-kafka:7.6.0

docker rm -f kafka

docker run --rm confluentinc/cp-kafka:7.6.0 kafka-storage random-uuid

>> Take note of the output of the last command, for example PJD_L-wPSeml0gtxkDKPNA

>> Replace PASTE_CLUSTER_ID_HERE with the value you generated on next command

docker run -d `
  --name kafka `
  -p 9092:9092 `
  -e CLUSTER_ID=PASTE_CLUSTER_ID_HERE `
  -e KAFKA_PROCESS_ROLES=broker,controller `
  -e KAFKA_NODE_ID=1 `
  -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092,CONTROLLER://0.0.0.0:9093 `
  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 `
  -e KAFKA_CONTROLLER_LISTENER_NAMES=CONTROLLER `
  -e KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=PLAINTEXT:PLAINTEXT,CONTROLLER:PLAINTEXT `
  -e KAFKA_CONTROLLER_QUORUM_VOTERS=1@localhost:9093 `
  confluentinc/cp-kafka:7.6.0

>> Create a topic

docker exec -it kafka kafka-topics `
  --bootstrap-server localhost:9092 `
  --create `
  --topic pix-transactions `
  --partitions 1 `
  --replication-factor 1

>> Verify topic

docker exec -it kafka kafka-topics `
  --bootstrap-server localhost:9092 `
  --list

>> Verify messages on topic (* DOES NOT WORK)

>> Get into the container with below command

docker exec -it kafka /bin/bash

>> and then check messages on the topic

kafka-console-consumer.sh \
  --bootstrap-server 0.0.0.0:9092 \
  --topic pix-transactions \
  --from-beginning




