#!/bin/bash

# shellcheck disable=SC2164
cd "$(dirname "$0")"

echo "Формируем базы данных..."
docker-compose -f ds-docker/docker-compose.yml up -d

sleep 1
echo "Разворачиваем postgres ..."

sleep 2
echo "Разворачиваем elasticsearch ..."

sleep 2

echo "Сборка проекта..."
./gradlew clean build
