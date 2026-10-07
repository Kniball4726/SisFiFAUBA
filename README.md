# Sistema de fichado FAUBA

Proyecto Java con Maven para administrar empleados y registrar sus fichadas de ingreso y egreso.

## Requisitos

- JDK 17 o superior.
- Apache Maven 3.8 o superior, o Apache NetBeans con soporte para proyectos Maven.

## Abrir en Apache NetBeans

1. Selecciona **File > Open Project**.
2. Elige la carpeta raiz del proyecto, donde se encuentra `pom.xml`.
3. Espera a que NetBeans cargue las dependencias y el proyecto.
4. Para ejecutar la aplicacion, usa **Run Project**. La clase principal configurada es `Main`.

## Ejecutar desde la consola

Desde la carpeta raiz del proyecto:

```text
mvn clean test
mvn exec:java
```

La fecha y hora de cada fichada se toman automaticamente del reloj local del sistema y se
muestran como `DD/MM/AAAA HH:MM:SS`. El programa sigue solicitando el legajo del empleado
y la confirmacion de presencia en FAUBA.

Para crear un JAR ejecutable:

```text
mvn clean package
java -jar target/sistema-fichado-1.0-SNAPSHOT.jar
```

## Pruebas

Las pruebas unitarias y funcionales se ejecutan con:

```text
mvn test
```

## Estructura

```text
src/
  main/java/       Codigo de la aplicacion
  test/java/       Pruebas automatizadas con JUnit 5
pom.xml            Configuracion Maven del proyecto
```

Las clases se mantienen en el paquete por defecto para conservar la estructura original del sistema.

## Reglas de fichado

- La primera fichada de un empleado es un ingreso.
- Despues de un egreso, la siguiente fichada es un ingreso.
- Desde un ingreso, hasta 12 horas inclusive se registra un egreso normal.
- Si transcurren mas de 12 y hasta 20 horas inclusive, se registra un egreso observado.
- Si transcurren mas de 20 horas, el ingreso anterior queda sin egreso y se registra un nuevo ingreso.
- La fichada debe realizarse presencialmente en FAUBA y su fecha/hora debe ser posterior a la ultima fichada.

Los empleados y sus fichadas se conservan en memoria solo mientras el programa esta abierto.
