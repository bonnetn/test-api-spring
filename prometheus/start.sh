#/bin/bash
docker run \
    -p 9090:9090 \
    -v "$(pwd)/prometheus.yml":"/etc/prometheus/prometheus.yml" \
    --network host \
    prom/prometheus
