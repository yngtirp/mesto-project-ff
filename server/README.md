# Mesto Server

Java REST API сервер для проекта Mesto.

## Требования

- Java 11 или выше
- Maven 3.6+

## Установка и запуск

1. Перейдите в директорию сервера:
```bash
cd server
```

2. Соберите проект:
```bash
mvn clean package
```

3. Запустите сервер:
```bash
java -jar target/mesto-server-1.0-SNAPSHOT.jar
```

Или запустите напрямую:
```bash
mvn exec:java -Dexec.mainClass="ru.mesto.server.Server"
```

## Конфигурация

Настройки сервера находятся в файле `src/main/resources/config.properties`:

- `server.port` - порт сервера (по умолчанию 3000)
- `server.host` - хост сервера (по умолчанию localhost)
- `db.url` - URL базы данных H2
- `log.level` - уровень логирования (DEBUG, INFO, ERROR)

## API Endpoints

### Пользователи

- `GET /users/me` - получить информацию о текущем пользователе
- `PATCH /users/me` - обновить информацию о пользователе
- `PATCH /users/me/avatar` - обновить аватар пользователя

### Карточки

- `GET /cards` - получить все карточки
- `POST /cards` - создать новую карточку
- `DELETE /cards/{cardId}` - удалить карточку

### Лайки

- `PUT /cards/likes/{cardId}` - поставить лайк карточке
- `DELETE /cards/likes/{cardId}` - убрать лайк с карточки

## Администрирование

После запуска сервера доступна консоль администрирования с командами:

- `stats` - показать статистику сервера
- `users` - список всех пользователей
- `cards` - список всех карточек
- `help` - показать справку
- `exit` - остановить сервер

## База данных

Используется встроенная база данных H2. Файлы базы данных сохраняются в директории `./data/`.

## Логирование

Логи сохраняются в файл `./logs/server.log`.

