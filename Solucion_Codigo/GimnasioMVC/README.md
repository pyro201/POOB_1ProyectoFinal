# Sistema de Monitoreo de Gimnasios y Planes de Suscripción
## POO - Arreglos, Estructuras de Control y Serialización de Archivos
## Arquitectura MVC

---

## CÓMO ABRIR EN NETBEANS

1. Abre NetBeans
2. Ve a **File > Open Project**
3. Navega y selecciona la carpeta **GimnasioMVC**
4. NetBeans detectará automáticamente el proyecto (verás el ícono de taza de café)
5. Haz clic derecho en el proyecto > **Run** (o F6)

> ⚠️ **IMPORTANTE**: Si NetBeans muestra error de "platform", ve a:
> Project Properties > Libraries > Java Platform y selecciona tu JDK instalado.

---

## ESTRUCTURA DEL PROYECTO (MVC)

```
GimnasioMVC/
├── src/
│   ├── modelo/          ← M: Clases entidad de puros datos
│   │   ├── Cliente.java
│   │   ├── ClaseGrupal.java
│   │   └── Rutina.java
│   ├── controlador/     ← C: Maneja arreglos y serialización
│   │   └── GimnasioControlador.java
│   └── vista/           ← V: Interfaz gráfica, menú interactivo
│       ├── Main.java              (clase ejecutora)
│       ├── VentanaPrincipal.java  (JFrame principal)
│       ├── PanelClientes.java
│       ├── PanelClases.java
│       ├── PanelRutinas.java
│       └── PanelEstadisticas.java
├── dat/                 ← Archivos .dat de serialización
├── nbproject/           ← Configuración NetBeans
└── build.xml
```

---

## FUNCIONALIDADES IMPLEMENTADAS

### Modelado de Clientes y Planes
- Registrar clientes vinculados a un plan (Básico/Plus/VIP)
- Fechas de inicio y caducidad de membresía

### Control de Acceso y Clases
- Validar si un cliente puede ingresar según su membresía
- Clases grupales: CrossFit, Spinning, Yoga
- Aforo máximo controlado con **arreglos** de reservas

### Estadísticas de Uso
- Horas pico con mayor afluencia (ordenamiento burbuja sobre arreglos)
- Membresías próximas a vencer en los siguientes 5 días

### Rutinas de Entrenamiento
- Un cliente puede tener **una rutina por día**
- Asociadas a la clase correspondiente

### Serialización de Objetos
- Todos los datos se guardan en archivos `.dat`
- Se cargan automáticamente al iniciar la aplicación

---

## REQUISITOS
- Java 11 o superior
- NetBeans 12+ (o cualquier IDE compatible con proyectos Ant)
