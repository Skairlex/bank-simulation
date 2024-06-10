# BANK-API Project

## Requisitos
- Java 17
- Gradle 7.2

## Documentación de Servicios con Swagger
[Swagger UI](http://localhost:8080/swagger-ui/index.html)
[Swagger UI](http://localhost:8081/swagger-ui/index.html)

## Pasos para Ejecutar el Proyecto
1. Configurar la base de datos(María DB), para ello ejecutar el archivo BaseDatos.sql, y configurar el archivo properties con la clave y contraseña de donde del servidor

2. Ejecutar el siguiente comando para limpiar y construir el proyecto:
    ```bash
    ./gradlew clean build
    ```

3. Iniciar la aplicación con el siguiente comando:
    ```bash
    ./gradlew bootRun
    ```
3.1. Ejecutar test solicitados(archivo SolicitedTest):
   ```bash
   ./gradlew test
   ```
4. Importar el archivo ntt.postman_collection.json a postman y ejecutar servicios de acuerdo a la necesidad


## Información Adicional
Este proyecto utiliza Java 17 y Gradle 7.2. La documentación de servicios está disponible en Swagger para facilitar el consumo de la API.

Para ejecutar el proyecto, sigue los pasos mencionados anteriormente. Visita la [Swagger UI](http://localhost:8080/swagger-ui/index.html) para explorar y probar los servicios proporcionados.

###Se añade el archivo ntt.postman_collection.json para importar el listado de servicios disponibles

**Nota:** Asegúrate de tener instalado Java 17 y Gradle 7.2 antes de ejecutar el proyecto.
