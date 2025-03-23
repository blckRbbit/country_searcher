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

echo "Запуск тестов..."
./gradlew test


echo "Формирование отчета о тестах..."

# shellcheck disable=SC2034
report_path="build/reports/tests/test/index.html"

if command -v xdg-open &> /dev/null; then
    xdg-open "$report_path"
elif command -v open &> /dev/null; then
    open "$report_path"
elif [[ "$OS" == "Windows_NT" ]]; then
         start "" "$report_path"
else
    echo "Не удалось открыть отчет. Пожалуйста, откройте его вручную: build/reports/tests/test/index.html"
fi