/*
TRABAJO PRACTICO 2
SEMINARIO DE PRÁCTICA DE INFORMÁTICA
Alumno: Erik Maximiliano Ovejero
*/

CREATE DATABASE sistema_hotel;
USE sistema_hotel;

CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(50) NOT NULL,
    contrasena VARCHAR(100) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    estado VARCHAR(20) NOT NULL
);

CREATE TABLE huespedes (
    id_huesped INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    dni VARCHAR(20) NOT NULL UNIQUE,
    telefono VARCHAR(30),
    correo VARCHAR(100)
);

CREATE TABLE habitaciones (
    id_habitacion INT AUTO_INCREMENT PRIMARY KEY,
    numero VARCHAR(10) NOT NULL UNIQUE,
    tipo VARCHAR(50) NOT NULL,
    capacidad INT NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    estado VARCHAR(30) NOT NULL
);

CREATE TABLE reservas (
    id_reserva INT AUTO_INCREMENT PRIMARY KEY,
    id_huesped INT NOT NULL,
    id_habitacion INT NOT NULL,
    id_usuario INT NOT NULL,
    fecha_checkin DATE NOT NULL,
    fecha_checkout DATE NOT NULL,
    cantidad_huespedes INT NOT NULL,
    estado VARCHAR(30) NOT NULL,
    observaciones VARCHAR(255),

    FOREIGN KEY (id_huesped) REFERENCES huespedes(id_huesped),
    FOREIGN KEY (id_habitacion) REFERENCES habitaciones(id_habitacion),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

CREATE TABLE pagos (
    id_pago INT AUTO_INCREMENT PRIMARY KEY,
    id_reserva INT NOT NULL,
    fecha_pago DATE NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    medio_pago VARCHAR(50) NOT NULL,
    estado_pago VARCHAR(30) NOT NULL,

    FOREIGN KEY (id_reserva) REFERENCES reservas(id_reserva)
);

CREATE TABLE facturas (
    id_factura INT AUTO_INCREMENT PRIMARY KEY,
    id_reserva INT NOT NULL,
    fecha_emision DATE NOT NULL,
    tipo_factura VARCHAR(20) NOT NULL,
    importe_total DECIMAL(10,2) NOT NULL,
    estado_factura VARCHAR(30) NOT NULL,

    FOREIGN KEY (id_reserva) REFERENCES reservas(id_reserva)
);



/* ---------------------------------------------------------
   INSERTAR USUARIOS
   --------------------------------------------------------- */

INSERT INTO usuarios (
    nombre_usuario,
    contrasena,
    rol,
    estado
)
VALUES
('admin', 'admin123', 'Administrador', 'Activo'),

('recepcion1', 'recep123', 'Recepcionista', 'Activo');


/* ---------------------------------------------------------
   INSERTAR HUÉSPEDES
   --------------------------------------------------------- */

INSERT INTO huespedes (
    nombre,
    apellido,
    dni,
    telefono,
    correo
)
VALUES
('Juan', 'Perez', '30111222', '2944123456', 'juan@gmail.com'),

('Maria', 'Gomez', '28999888', '2944987654', 'maria@gmail.com');


/* ---------------------------------------------------------
   INSERTAR HABITACIONES
   --------------------------------------------------------- */

INSERT INTO habitaciones (
    numero,
    tipo,
    capacidad,
    precio,
    estado
)
VALUES
('101', 'Simple', 1, 45000, 'Disponible'),

('102', 'Doble', 2, 65000, 'Disponible');


/* ---------------------------------------------------------
   INSERTAR RESERVAS
   --------------------------------------------------------- */

INSERT INTO reservas (
    id_huesped,
    id_habitacion,
    id_usuario,
    fecha_checkin,
    fecha_checkout,
    cantidad_huespedes,
    estado,
    observaciones
)
VALUES
(1, 1, 1, '2026-05-20', '2026-05-25', 1,
 'Reservada',
 'Pago en recepción');


/* ---------------------------------------------------------
   INSERTAR PAGOS
   --------------------------------------------------------- */

INSERT INTO pagos (
    id_reserva,
    fecha_pago,
    monto,
    medio_pago,
    estado_pago
)
VALUES
(1, '2026-05-20', 225000,
 'Tarjeta',
 'Aprobado');


/* ---------------------------------------------------------
   INSERTAR FACTURAS
   --------------------------------------------------------- */

INSERT INTO facturas (
    id_reserva,
    fecha_emision,
    tipo_factura,
    importe_total,
    estado_factura
)
VALUES
(1, '2026-05-20',
 'Factura B',
 225000,
 'Emitida');
 
 /* =========================================================
   CONSULTA DE DATOS
   ========================================================= */

/*
   Consulta general de reservas con información
   del huésped, habitación y pago realizado.
*/

SELECT
    r.id_reserva,
    h.nombre,
    h.apellido,
    hb.numero AS habitacion,
    r.fecha_checkin,
    r.fecha_checkout,
    r.estado,
    p.monto,
    p.medio_pago
FROM reservas r

INNER JOIN huespedes h
    ON r.id_huesped = h.id_huesped

INNER JOIN habitaciones hb
    ON r.id_habitacion = hb.id_habitacion

LEFT JOIN pagos p
    ON r.id_reserva = p.id_reserva;
    
    /* ---------------------------------------------------------
   ELIMINAR FACTURAS
   --------------------------------------------------------- */

DELETE FROM facturas
WHERE id_factura = 1;


/* ---------------------------------------------------------
   ELIMINAR PAGOS
   --------------------------------------------------------- */

DELETE FROM pagos
WHERE id_pago = 1;


/* ---------------------------------------------------------
   ELIMINAR RESERVAS
   --------------------------------------------------------- */

DELETE FROM reservas
WHERE id_reserva = 1;


/* ---------------------------------------------------------
   ELIMINAR HUÉSPEDES
   --------------------------------------------------------- */

DELETE FROM huespedes
WHERE id_huesped = 1;


/* ---------------------------------------------------------
   ELIMINAR HABITACIONES
   --------------------------------------------------------- */

DELETE FROM habitaciones
WHERE id_habitacion = 1;


/* ---------------------------------------------------------
   ELIMINAR USUARIOS
   --------------------------------------------------------- */

DELETE FROM usuarios
WHERE id_usuario = 1;