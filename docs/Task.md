# HW-13 Kotlin Gradle DSL

Необходимо реализовать небольшой микросервис на Ktor, который будет реализовать API по необходимым методам. 
Для каждого метода будут прописаны условия его работы:

Ваша задача - реализовать небольшой сервер на Ktor для получения информации о maven зависимостях (они и используются в
gradle)
Далее описаны запросы, их нужно реализовать не изменяя структуру запроса и ответа.

Сервер должен сохранять только ссылки на репозитории, где должны искаться зависимости:

### Add custom repository url

Запрос на добавление репозитория, где будет искаться зависимость. Ответ должен возвращать саму модель репозитория

Изначальные репозитории (присутствуют при запуске сервера без запросов):

- https://repo.maven.apache.org/maven2
- https://dl.google.com/dl/android/maven2

POST `/api/v1/repository/add`

Request Schema:

```json5
{
  name: "<rep_name>",
  url: "<rep_url>",
}
```

Response Schema:

```json5
{
  id: "<rep_id>",
  name: "<rep_name>",
  url: "<rep_url>",
}
```

#### Get custom repository urls

Запрос на получение всех репозиториев. Ответ должен возвращать список моделей репозиториев

GET `/api/v1/repository/list`

Response Schema:

```json5
{
  repositories: [
    {
      id: "<rep_id>",
      name: "<rep_name>",
      url: "<rep_url>",
    }
  ]
}
```

#### Delete custom repository url

Запрос на удалене репозитория по id. Ответ должен возвращать id удаленного репозитория

POST `/api/v1/repository/delete`

Request Schema:

```json5
{
  id: "<repository_id>",
}
```

Response Schema:

```json5
{
  id: "<repository_id>",
}
```

#### Get metadata for dependency

Запрос на получение методанных зависимости.
Есть 2 способа (как в Gradle) - передать каждый параметр отдельно или совместить их через `:`.
Также есть возможность указания LATEST/RELEASE констант версий, они должны браться из файла `maven-metadata.xml`
Ответ должен содержать название, ссылку на версию и название лицензии

GET `/api/v1/dependency/metadata`

Request Schema:

```json5
{
  group: "<dep_group>",
  artifact: "<dep_artifact>",
  version: "<dep_version or one of two constants: LATEST/RELEASE>",
}
```

Request Schema:

```json5
{
  dependency: "<dep_group:dep_artifact:dep_version>",
}
```

Response Schema:

```json5
{
  name: "<dep_name>",
  url: "<dep_version_root_url>",
  license: "<dep_license>",
}
```

#### Get list of versions for dependency

Запрос на получение списка версий зависимости.
Есть 2 способа (как в Gradle) - передать каждый параметр отдельно или совместить их через `:`.
Данные должны браться из файла `maven-metadata.xml`
Ответ должен содержать список из всевозможных версий зависимости, отсортированных в изначальном порядке

GET `/api/v1/dependency/versions`

Request Schema:

```json5
{
  group: "<dep_group>",
  artifact: "<dep_artifact>",
}
```

Request Schema:

```json5
{
  dependency: "<dep_group:dep_artifact>",
}
```

Response Schema:

```json5
{
  name: "<dep_name>",
  versions: [
    "<dep_version_1>",
    "<dep_version_2>",
    ...
  ]
}
```

### Пример:

- Добавляем новый репозиторий (https://dl.bintray.com/kotlin/kotlin-eap)
- Получаем список версий для `org.jetbrains.kotlin:kotlin-reflect:1.7.20`
- Получаем список версий для этой же зависимости
    - Список версий и `maven-metadata.xml` можно получаем
      отсюда https://repo.maven.apache.org/maven2/org/jetbrains/kotlin/kotlin-reflect/
- Получаем информацию про последнюю версию (на данный момент это - `1.8.10`)
    - https://repo.maven.apache.org/maven2/org/jetbrains/kotlin/kotlin-reflect/1.8.10/

### Полезные ссылки

- Структура зависимостей всегда одинаковая, можно посмотреть, например,
  тут https://repo.maven.apache.org/maven2/org/jetbrains/kotlin/kotlin-reflect/
- Для парсинга xml можно использовать https://github.com/pdvrieze/xmlutil
- Как работает такой поиск в Gradle/Maven https://www.tutorialspoint.com/maven/maven_repositories.htm
