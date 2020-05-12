# mykrobe-atlas-kafka-consumer

## Running Locally

port forward kafka nodes, schema registry and control centre.

```
kubectl port-forward svc/mykrobe-confluent-kafka-0-nodeport 31090:19092
```

```
kubectl port-forward svc/mykrobe-confluent-kafka-1-nodeport 31091:19092
```

```
kubectl port-forward svc/mykrobe-confluent-kafka-2-nodeport 31092:19092
```

```
kubectl port-forward svc/mykrobe-confluent-schema-registry 8081:8081
```

```
kubectl port-forward svc/mykrobe-confluent-cc 9021:9021
```