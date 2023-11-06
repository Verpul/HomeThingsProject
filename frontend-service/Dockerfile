FROM node:lts-alpine as build-stage
WORKDIR /app
COPY frontend-service/package*.json .
RUN npm install

COPY frontend-service/ .

ARG SERVER_HOST
ARG TWITCH_REDIRECT_URI
RUN sed -i 's/VUE_APP_KEYCLOAK_URL=/VUE_APP_KEYCLOAK_URL=${SERVER_HOST}/g' .env.production
RUN sed -i 's/VUE_APP_TWITH_REDIRECT_URI=/VUE_APP_TWITH_REDIRECT_URI=${TWITCH_REDIRECT_URI}/g' .env.production

RUN npm run build

FROM nginx:stable-alpine as production-stage
COPY frontend-service/.docker/nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build-stage /app/dist /usr/share/nginx/html
EXPOSE 8080
CMD ["nginx", "-g", "daemon off;"]
