Для сборки модуля необходимо соблюдение следующих условий:

1. Должны быть заданы переменные среды окружения:

- **env.ASB_PATH** - путь к директории где расположены JKS хранилища сертификатов
   - keystore.jks - хранилище с пользовательским сертификатом пользователя ESBD
   - castore.jks - хранилище с доверенными корневыми центрами сертификации в котором помимо всего должен быть импортирован корневой сертификат KISC
- **ASB_KEYSTORE_PASSWORD** - пароль к файлу JKS хранилища сертификатов
- **ASB_USER** - имя пользователя ASB ESBD
- **ASB_PASSWORD** - пароль пользователя ASB ESBD

2. В хранилище доверенных корневых центров сертификации должен быть проимпортирован доверенный корневой сертификат KISC

- Путь по умолчанию к хранилищу доверенных корневых центров сертификации JRE - [PATH_TO_JDK]/jre/lib/security/cacerts
