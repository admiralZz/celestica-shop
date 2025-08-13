## Celestica Shop

Проект онлайн магазина

На данный момент состоит клиентской и админовской части. Каждая часть делиться на бэк и фронт.
- Бэкенд на базе Spring Boot
- Фронт на базе React

Все вместе собирается через docker-compose. На выходе 3 образа:
- admin-backend
- client-backend
- nginx-frontend

### CI/CD

Пока в зачаточном состоянии. Чтобы собрать готовые образы для деплоя, нужно:

1. Собрать бэкенд часть 

```shell
./gradlew clean build
```

2. Сборка образов и фронта(сборка внутри контейнера) 
```shell
docker-compose build
```

3. Закидываем образы на прод 

```shell
docker-compose push
```

4. На проде чтобы залить обновления

```shell
cd packages/celestica-shop
docker-compose pull
ddocker-compose up
```

### Обновление SSL сертификата

1. Создать сертификат

```shell
docker run -it --rm \
  -v /etc/letsencrypt:/etc/letsencrypt \
  certbot/certbot certonly --standalone \
  -d celestica-shop.com \
  -d www.celestica-shop.com \
  -d admin.celestica-shop.com \
  -d www.admin.celestica-shop.com
```
После этого в /etc/letsencrypt/live/celestica-shop.com/ будут:

**fullchain.pem** — цепочка сертификатов

**privkey.pem** — приватный ключ

2. Убедиться что папка с сертификатами прокинута в nginx-контейнер

```yaml
  nginx:
    image: nginx:alpine
    volumes:
      - /etc/letsencrypt:/etc/letsencrypt:ro
    ports:
      - "80:80"
      - "443:443"
```

3. Чтобы обновить сертификат(он свежий в течении 90 дней), также обновляем через certbot

```shell
docker run --rm \
  -v /etc/letsencrypt:/etc/letsencrypt \
  certbot/certbot renew
```
И после обновления:

```shell
docker exec nginx nginx -s reload
```