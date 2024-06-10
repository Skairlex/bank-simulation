# TRAIN-API Project

## Requisitos
- Java 17
- Gradle 7.2

## Documentación de Servicios con Swagger
[Swagger UI](http://localhost:8080/swagger-ui/index.html)

## Pasos para Ejecutar el Proyecto
1. Ejecutar el siguiente comando para limpiar y construir el proyecto:
    ```bash
    ./gradlew clean build
    ```

2. Iniciar la aplicación con el siguiente comando:
    ```bash
    ./gradlew bootRun
    ```
2.1. Ejecutar test solicitados(archivo SolicitedTest):
   ```bash
   ./gradlew test
   ```
3. Importar el archivo TRAINS.postman_collection.json a postman y ejecutar servicios de acuerdo a la necesidad

## Descripción
Este proyecto implementa una API para gestionar rutas y ubicaciones de trenes. Proporciona servicios para calcular distancias, contar rutas, encontrar la ruta más corta y realizar otras operaciones relacionadas con el transporte ferroviario.

1. **Calcular Distancia para una Ruta Específica:**
   - **Método:** `GET`
   - **URL:** `/api/routes/calculateDistance/{routeDescription}`
   - **Descripción:** Calcula la distancia para la ruta especificada.

2. **Contar Viajes con un Número Máximo de Paradas:**
   - **Método:** `GET`
   - **URL:** `/api/routes/countTripsWithMaxStops/{startLocation}/{endLocation}/{maxStops}`
   - **Descripción:** Cuenta el número de viajes desde la ubicación de inicio hasta la ubicación de destino con un máximo de paradas especificado.

3. **Contar Viajes con un Número Exacto de Paradas:**
   - **Método:** `GET`
   - **URL:** `/api/routes/countTripsWithExactStops/{startLocation}/{endLocation}/{exactStops}`
   - **Descripción:** Cuenta el número de viajes desde la ubicación de inicio hasta la ubicación de destino con un número exacto de paradas.

4. **Calcular Ruta Más Corta:**
   - **Método:** `GET`
   - **URL:** `/api/routes/calculateShortestRoute/{startLocation}/{endLocation}`
   - **Descripción:** Calcula la ruta más corta desde la ubicación de inicio hasta la ubicación de destino.

5. **Obtener Nombres de Ubicaciones:**
   - **Método:** `GET`
   - **URL:** `/api/routes/locations`
   - **Descripción:** Obtiene los nombres de todas las ubicaciones disponibles.

6. **Obtener Información de Rutas:**
   - **Método:** `GET`
   - **URL:** `/api/routes/routes`
   - **Descripción:** Obtiene información detallada sobre las rutas disponibles, incluyendo origen, destino y distancia.

7. **Agregar Ubicación:**
   - **Método:** `POST`
   - **URL:** `/api/routes/addLocation/{name}`
   - **Descripción:** Agrega una nueva ubicación con el nombre especificado.

8. **Agregar Ruta:**
   - **Método:** `POST`
   - **URL:** `/api/routes/addRoute/{origin}/{destination}/{distance}`
   - **Descripción:** Agrega una nueva ruta con el origen, destino y distancia especificados.

9. **Inicializar Grafo:**
   - **Método:** `POST`
   - **URL:** `/api/routes/initializeGraph`
   - **Descripción:** Inicializa el grafo con las ubicaciones y rutas de ejemplo descritas de la prueba.

## Información Adicional
Este proyecto utiliza Java 17 y Gradle 7.2. La documentación de servicios está disponible en Swagger para facilitar el consumo de la API.

Para ejecutar el proyecto, sigue los pasos mencionados anteriormente. Visita la [Swagger UI](http://localhost:8080/swagger-ui/index.html) para explorar y probar los servicios proporcionados.

###Se añade el archivo TRAINS.postman_collection.json para importar el listado de servicios disponibles

**Nota:** Asegúrate de tener instalado Java 17 y Gradle 7.2 antes de ejecutar el proyecto.
