# HomeThings Project

## Frontend Service (Vue + Vuetify)
- Порт 8080

#### Запуск (требуется установка Node.js)
cmd -> cd frontend-service

#### Скачивание пакетов
npm install

#### Dev-запуск
npm run serve

## Local Service
- Порт 8081

#### Включает в себя
- Weight module
- Reminders module

#### Enviroment variables:
- POSTGRES_USER
- POSTGRES_PASSWORD

## Api Service
- Порт 8082

#### API's
- Погода - https://www.yr.no
- Курс валют ЦБ - https://www.cbr.ru/currency_base/daily/
- Курс валют Тинькофф - https://www.tinkoff.ru/about/exchange/

## TelegramBot Service
- Порт 8083

#### Enviroment variables:
- TELEGRAM_BOT_NAME
- TELEGRAM_BOT_TOKEN
- TELEGRAM_BOT_CHAT_ID

## Eureka Service
- Порт 8761