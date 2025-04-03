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

## Instalación y Despliegue

### 1. Clonar el Repositorio

```bash
git clone <URL_DEL_REPOSITORIO>
cd PruebaDetsu
```
