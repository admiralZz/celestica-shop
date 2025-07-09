# build client
FROM node:22 AS client-build
WORKDIR /client

ARG REACT_APP_API_BASE_URL_CLIENT
ENV REACT_APP_API_BASE_URL=$REACT_APP_API_BASE_URL_CLIENT

COPY client-front-module/package*.json ./
RUN npm install
COPY client-front-module ./
RUN npm run build

# build admin
FROM node:22 AS admin-build
WORKDIR /admin

ARG REACT_APP_API_BASE_URL_ADMIN
ENV REACT_APP_API_BASE_URL=$REACT_APP_API_BASE_URL_ADMIN

COPY admin-front-module/package*.json ./
RUN npm install
COPY admin-front-module ./
RUN npm run build

# production nginx
FROM nginx:alpine
COPY --from=client-build /client/dist /usr/share/nginx/html/client
COPY --from=admin-build /admin/dist /usr/share/nginx/html/admin
COPY nginx.conf /etc/nginx/conf.d/default.conf