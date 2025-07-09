# build stage
FROM node:22 AS build
WORKDIR /app

# Для гибкой установки адреса бэка(для удобства на проде через docker-compose)
ARG REACT_APP_API_BASE_URL
ENV REACT_APP_API_BASE_URL=$REACT_APP_API_BASE_URL

COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build