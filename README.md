# HomeThings Project

## Frontend Service (Vue + Vuetify)
- Порт 8080

#### Dev запуск (требуется установка Node.js)
cmd -> cd frontend-service

#### Скачивание пакетов
npm install

#### Dev-запуск
npm run serve

### Prod запуск
- Если сервер без HTTPS, то в первый раз требуется сделать следующее:
  - После запуска контейнера зайти в него docker exec -it keycloak bash
  - Команда для начала изменения конфига: bin/kcadm.sh config credentials --server http://IP_ADDR:8900 --realm master --user admin
  - Команда отключения SSL: bin/kcadm.sh update realms/master -s sslRequired=NONE
- Загрузить или настроить свой realm

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
- Twitch - https://www.twitch.tv/

#### Enviroment variables:
- CLIENT_ID (Twitch)
- CLIENT_SECRET (Twitch)
- REDIRECT_URI (Twitch)

## TelegramBot Service
- Порт 8083

#### Enviroment variables:
- TELEGRAM_BOT_NAME
- TELEGRAM_BOT_TOKEN
- TELEGRAM_BOT_CHAT_ID

## Eureka Service
- Порт 8761

## Gateway Service
- Порт 9000

#### Enviroment variables:
- SERVER_HOST - необходим для работы Keycloak

## Keycloak
- Порт 8900

#### Dev запуск (Требуется Docker)
docker run -p 8900:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:22.0.4 start-dev

Далее заходим на http://localhost:8900/ -> Administration Console -> Login -> Create Realm -> Выбрать файл из папки keycloak

#### Default user for auth
email: admin@admin.ru
password: admin