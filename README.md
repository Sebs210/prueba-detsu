# PruebaDetsu - Sistema de Microservicios Bancarios

## Descripción del Proyecto

Este proyecto implementa un sistema de microservicios para la gestión bancaria, dividido en dos microservicios principales:

- **microservice-cliente**: Gestiona la información de clientes (`Persona` y `Cliente`).
- **microservice-cuenta**: Gestiona cuentas y movimientos bancarios (`Cuenta` y `Movimiento`).

El sistema utiliza **Spring Boot** para los microservicios, **PostgreSQL** como base de datos, y **RabbitMQ** para la comunicación asincrónica entre los microservicios. Todo está desplegado en contenedores Docker usando `docker-compose`.

### Características Principales

- **Microservicio Cliente**:
  - CRUD completo para clientes (`Persona` y `Cliente`).
  - Almacena datos en una base de datos PostgreSQL (`cliente_db`).
  - Envía un mensaje asincrónico a `microservice-cuenta` cuando se crea un cliente.
- **Microservicio Cuenta**:
  - CRU para cuentas y movimientos.
  - Almacena datos en una base de datos PostgreSQL (`cuenta_db`).
  - Genera reportes de estado de cuenta por cliente y rango de fechas.
  - Recibe mensajes asincrónicos de `microservice-cliente` para crear cuentas automáticamente.
- **Comunicación Asincrónica**:
  - Usa RabbitMQ para enviar mensajes entre microservicios.
  - Cuando se crea un cliente, se genera automáticamente una cuenta asociada.
- **Despliegue**:
  - Todo el sistema (microservicios, bases de datos, RabbitMQ) se despliega con Docker y `docker-compose`.

### Tecnologías Utilizadas

- **Backend**: Spring Boot 3.4.4, Java 17
- **Bases de Datos**: PostgreSQL 15
- **Mensajería**: RabbitMQ 3 (con interfaz de gestión)
- **Contenedores**: Docker, Docker Compose
- **Pruebas**: JUnit 5 (pruebas unitarias e integración), Postman (pruebas de API)

---

## Requisitos

- **Docker** y **Docker Compose** instalados.
- **Git** (para clonar el repositorio).
- **Postman** (para probar los endpoints).
- **PostgreSQL** (para ejecutar el script de base de datos, si no usas Docker).

---

## Estructura del Proyecto

- `microservice-cliente/`: Microservicio para gestionar clientes (puerto `8081`).
- `microservice-cuenta/`: Microservicio para gestionar cuentas y movimientos (puerto `8082`).
- `docker-compose.yml`: Configuración para desplegar los microservicios, PostgreSQL y RabbitMQ.
- `BaseDatos.sql`: Script SQL para crear las bases de datos, tablas y datos iniciales.
- `PruebaDetsu.postman_collection.json`: Colección de Postman para probar los endpoints.
- `README.md`: Documentación del proyecto.

---


### 1. Clonar el Repositorio

\`\`\`bash
git clone https://github.com/Sebs210/prueba-detsu.git
cd PruebaDetsu
\`\`\`

### 2. (Opcional) Inicializar las Bases de Datos Manualmente

Si no usas Docker:

\`\`\`bash
psql -U postgres
\i BaseDatos.sql
\`\`\`

### 3. Ejecutar con Docker Compose

\`\`\`bash
docker-compose up --build
\`\`\`

Servicios disponibles:

| Servicio               | URL                                  |
|------------------------|--------------------------------------|
| microservice-cliente   | http://localhost:8081/clientes       |
| microservice-cuenta    | http://localhost:8082/cuentas        |
| RabbitMQ UI            | http://localhost:15672 (guest/guest) |

### 4. Probar los Servicios

Con Postman:
1. Importar \`PruebaDetsu.postman_collection.json\`
2. Ejecutar las peticiones en orden:
   - Crear cliente
   - Verificar cuenta creada automáticamente
   - Crear movimientos
   - Generar reporte

Con \`curl\`:

\`\`\`bash
curl -X GET http://localhost:8081/clientes
curl -X GET http://localhost:8082/cuentas
\`\`\`

### 5. Detener los Contenedores

\`\`\`bash
docker-compose down
# Para eliminar volúmenes y datos persistentes
docker-compose down -v
\`\`\`

---

## Endpoints Principales

### Microservicio Cliente (\`http://localhost:8081\`)

| Método | Endpoint         | Descripción                  |
|--------|------------------|------------------------------|
| POST   | /clientes        | Crear un cliente             |
| GET    | /clientes        | Listar todos los clientes    |
| GET    | /clientes/{id}   | Obtener cliente por ID       |
| PUT    | /clientes/{id}   | Actualizar cliente           |
| DELETE | /clientes/{id}   | Eliminar cliente             |

**Ejemplo JSON - Crear Cliente**

\`\`\`json
{
  "nombre": "Marianela Montalvo",
  "genero": "F",
  "edad": 25,
  "identificacion": "987654321",
  "direccion": "Amazonas y NNUU",
  "telefono": "097548965",
  "clienteId": "C002",
  "contrasena": "5678",
  "estado": true
}
\`\`\`

---

### Microservicio Cuenta (\`http://localhost:8082\`)

| Método | Endpoint             | Descripción                       |
|--------|----------------------|-----------------------------------|
| POST   | /cuentas             | Crear una cuenta                  |
| GET    | /cuentas             | Listar todas las cuentas          |
| GET    | /cuentas/{id}        | Obtener cuenta por ID             |
| PUT    | /cuentas/{id}        | Actualizar cuenta                 |
| POST   | /movimientos         | Crear movimiento                  |
| GET    | /movimientos         | Listar movimientos                |
| GET    | /movimientos/{id}    | Obtener movimiento por ID         |
| PUT    | /movimientos/{id}    | Actualizar movimiento             |
| GET    | /movimientos/reportes| Reporte por cliente y fechas      |

**Ejemplo JSON - Crear Cuenta**

\`\`\`json
{
  "numeroCuenta": "478758",
  "tipoCuenta": "Ahorros",
  "saldoInicial": 2000,
  "estado": true,
  "clienteId": "C002"
}
\`\`\`

**Ejemplo JSON - Crear Movimiento**

\`\`\`json
{
  "fecha": "2022-02-10T10:00:00",
  "tipoMovimiento": "Retiro",
  "valor": -575,
  "cuenta": {
    "id": 1
  }
}
\`\`\`

**Reporte de movimientos**

\`\`\`http
GET /movimientos/reportes?fecha=2022-02-01-2022-02-15&cliente=C002
\`\`\`

---

## Comunicación Asincrónica

- Cuando se crea un cliente (\`clienteId: C002\`), se envía un mensaje a RabbitMQ.
- El microservicio de cuenta recibe este mensaje y crea automáticamente una cuenta con:
  - \`numeroCuenta\`: "AUTO-C002"
  - \`tipoCuenta\`: "Ahorros"
  - \`saldoInicial\`: 1000.0

---

## Pruebas

### Unitarias e Integración

- **microservice-cliente**:
  - Unitarias: \`ClienteRepository\`
  - Integración: \`ClienteController\`
- **microservice-cuenta**: pruebas similares

### Ejecutar Pruebas

\`\`\`bash
cd microservice-cliente
mvn test

cd ../microservice-cuenta
mvn test
\`\`\`

---

## Pruebas con Postman

1. Abrir Postman
2. Importar \`PruebaDetsu.postman_collection.json\`
3. Ejecutar en orden:
   - Crear cliente
   - Verificar creación automática de cuenta
   - Crear movimientos
   - Generar reporte

---

## Autor

Sebastián [@Sebs210](https://github.com/Sebs210)
