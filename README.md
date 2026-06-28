# Sistema Integral de Gestión Hotelera

Proyecto académico desarrollado para la materia **Seminario de Práctica de Informática** de la **Universidad Siglo 21**.

El objetivo del proyecto es desarrollar un sistema de escritorio para administrar los principales procesos operativos y administrativos de un hotel, aplicando conceptos de Ingeniería de Software, Programación Orientada a Objetos, Bases de Datos y Java.

---

## Descripción

El sistema permite administrar de forma centralizada las operaciones más importantes de un establecimiento hotelero, facilitando el trabajo de recepcionistas y administradores.

Entre sus principales funcionalidades se encuentran:

* Gestión de huéspedes.
* Gestión de habitaciones.
* Creación, modificación y cancelación de reservas.
* Consulta de disponibilidad.
* Registro de Check-In y Check-Out.
* Registro de pagos.
* Emisión de facturas en PDF.
* Gestión de usuarios.
* Reportes de ocupación e ingresos.

Toda la información se almacena de forma persistente en una base de datos MySQL.

---

## Tecnologías utilizadas

* Java
* Java Swing
* JDBC
* MySQL
* MySQL Connector/J
* NetBeans IDE
* Git
* GitHub

---

## Arquitectura

El proyecto fue desarrollado utilizando una arquitectura organizada por capas para separar las responsabilidades de cada componente.

```text
Interfaz gráfica (Java Swing)
           │
           ▼
Clases de dominio
(CHuesped, CHabitacion, CReserva, CPago, CFactura...)
           │
           ▼
Clases DAO
           │
           ▼
JDBC
           │
           ▼
Base de datos MySQL
```

---

## Programación Orientada a Objetos

Durante el desarrollo se aplicaron los principales conceptos de Programación Orientada a Objetos:

* Encapsulamiento mediante atributos privados y métodos getters y setters.
* Abstracción representando entidades del negocio hotelero mediante clases Java.
* Herencia utilizando una clase base `CPersona` y sus especializaciones (`CHuesped`, `CAdministrador` y `CRecepcionista`).
* Polimorfismo mediante la redefinición de comportamientos según el tipo de usuario.

---

## Base de datos

La aplicación utiliza una base de datos MySQL denominada:

```text
sistema_hotel
```

El modelo de datos incluye las siguientes tablas:

* usuarios
* huespedes
* habitaciones
* reservas
* pagos
* facturas

El script SQL para crear la base de datos se encuentra en:

```text
database/sistema_hotel.sql
```

---

## Instalación

### Requisitos

* JDK 21 o superior.
* NetBeans IDE.
* MySQL Server.
* MySQL Connector/J.
* WampServer (utilizado durante el desarrollo).

### Pasos

Clonar el repositorio:

```bash
git clone https://github.com/erik-ovejero/sistema-gestion-hotel.git
```

Crear la base de datos ejecutando el script SQL ubicado en:

```text
database/sistema_hotel.sql
```

Configurar los parámetros de conexión en la clase `CConexion`.

Abrir el proyecto desde NetBeans.

Ejecutar la aplicación.

---

## Usuarios de prueba

### Administrador

```
Usuario: Admin
Contraseña: admin123
```

### Recepcionista

```
Usuario: Recepcionista
Contraseña: recepcionista123
```

---

## Funcionalidades implementadas

### Recepcionista

* Registrar huéspedes.
* Modificar huéspedes.
* Eliminar huéspedes.
* Crear reservas.
* Modificar reservas.
* Cancelar reservas.
* Consultar disponibilidad.
* Registrar Check-In.
* Registrar Check-Out.
* Registrar pagos.
* Emitir facturas.

### Administrador

Además de todas las funciones del recepcionista, puede:

* Gestionar usuarios.
* Administrar habitaciones.
* Consultar reportes.
* Consultar estadísticas de ocupación e ingresos.

---

## Estructura del proyecto

```text
src/
├── Clases/
├── DAO/
├── Formularios/
├── Conexion/
└── Utilidades/

database/
└── sistema_hotel.sql
```

---

## Capturas

El proyecto incluye los siguientes módulos:

* Inicio de sesión.
* Menú principal.
* Gestión de huéspedes.
* Gestión de habitaciones.
* Gestión de reservas.
* Consulta de disponibilidad.
* Check-In y Check-Out.
* Registro de pagos.
* Emisión de facturas.
* Gestión de usuarios.
* Reportes.

---

## Objetivos del proyecto

Este proyecto fue desarrollado con fines académicos para aplicar los conocimientos adquiridos durante la carrera, integrando:

* Ingeniería de Software.
* Programación Orientada a Objetos.
* Java.
* JDBC.
* MySQL.
* UML.
* Testing de Software.
* Proceso Unificado de Desarrollo (PUD).

---

## Autor

**Erik Maximiliano Ovejero**

Licenciatura en Informática
Universidad Siglo 21

GitHub: https://github.com/erik-ovejero
