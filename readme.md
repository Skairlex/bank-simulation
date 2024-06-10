# BANK-API Project

## Requisitos
- Java 17
- Gradle 7.2

## Documentación de Servicios con Swagger
[Swagger UI](http://localhost:8080/swagger-ui/index.html)
[Swagger UI](http://localhost:8081/swagger-ui/index.html)

## Pasos para Ejecutar el Proyecto

1. **Configurar la base de datos (MariaDB)**:
    - Ejecutar el archivo `BaseDatos.sql`.
    - Configurar el archivo `properties` con la clave y contraseña del servidor.

2. **Configurar servidor de RabbitMQ**:
    - Tener instalado Docker.
    - Ejecutar el siguiente comando en una línea de comandos:
      ```bash
      docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
      ```
    - Abrir una página web e ingresar a la URL: [http://localhost:15672](http://localhost:15672).
    - Ingresar al dashboard con las credenciales:
      - **Usuario**: `guest`
      - **Contraseña**: `guest`
    - Finalmente, crear la cola con el nombre `clienteQueue`.

3. **Limpiar y construir el proyecto**:
    - Ejecutar el siguiente comando:
      ```bash
      ./gradlew clean build
      ```

4. **Iniciar la aplicación**:
    - Ejecutar el siguiente comando:
      ```bash
      ./gradlew bootRun
      ```

5. **Ejecutar test solicitados**:
    - Ejecutar los tests del archivo `SolicitedTest` con el siguiente comando:
      ```bash
      ./gradlew test
      ```

6. **Importar y ejecutar servicios en Postman**:
    - Importar el archivo `ntt.postman_collection.json` a Postman.
    - Ejecutar los servicios según sea necesario.



## Información Adicional
Este proyecto utiliza Java 17 y Gradle 7.2. La documentación de servicios está disponible en Swagger para facilitar el consumo de la API.

Para ejecutar el proyecto, sigue los pasos mencionados anteriormente. Visita la [Swagger UI](http://localhost:8080/swagger-ui/index.html) para explorar y probar los servicios proporcionados.

###Se añade el archivo ntt.postman_collection.json para importar el listado de servicios disponibles

**Nota:** Asegúrate de tener instalado Java 17 y Gradle 7.2 antes de ejecutar el proyecto.
