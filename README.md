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