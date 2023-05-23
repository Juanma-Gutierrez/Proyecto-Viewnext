# Proyecto Viewnext Lista facturas
Practica DAM 1º y 2º – Lista facturas MVC JAVA

1. Introducción

    Esta es una práctica de iniciación en la cual se pretende emular el trabajo diario en un equipo de desarrollo. A la hora de realizar una tarea es tan importante la capacidad de desarrollo como uso de herramientas para el trabajo en equipo y la comunicación.

2. Arquitectura

    Esta práctica es de un nivel para introducir a los alumnos al desarrollo Android por los cual se establece la arquitectura MVC por defecto.
    El lenguaje utilizado será Java o Kotlin a elección personal. 

3. Listado de facturas

    Consiste en la creación de la siguiente pantalla en la cual se cargarán los datos a través de un servicio.

    Especificaciones gráficas de la pantalla:
    - La zona superior de la pantalla usara el componente Toolbar nativo con la configuración para mostrar el título en grande.
    - Para el listado de facturas se usará el componente apropiado.
    - Al pulsar sobre una celda se mostrará un popup nativo:
        - Título: “Información”
        - Mensaje: “Esta funcionalidad aún no está disponible”
        - Botón: “Cerrar”
    - Al pulsar el botón de filtros se mostrará la pantalla de los filtros. En caso de no obtener ninguna factura mostrar un texto informativo en el centro de la pantalla.
    - Los recursos para los iconos son:
    - Al cargar la pantalla y cuando se cambien los filtros se deben recargar los datos de las facturas. Para obtener el listado de facturas se usará la librería Retrofit mediante una consulta GET a la url:
    https://viewnextandroid.mocklab.io/facturas

    Los filtros se aplicarán de forma local, es decir se cargan los datos de la url y a continuación se filtran desde la app.

4. Pantalla de filtros
   
    Esta pantalla se encargará de configurar los filtros para las facturas, las especificaciones de la pantalla son las siguientes:
    - La zona superior de la pantalla usara el componente Toolbar nativo con la configuración para mostrar el título en grande.
    - La X cierra la pantalla sin hacer ningún cambio en los filtros.
    - Sección de fechas, se visualizará un selector de fecha del sistema en cada campo
    - Sección de importes: No será un slider doble, será simple e irá de 0 al máximo de valor de las facturas (campo de entrada de la pantalla 1)
    - Sección de checks
    - Botón verde con fondo en blanco con texto “Aplicar”
    - Botón gris sin fondo con texto “Eliminar filtros” que borra los filtros.
 
5. Proyecto

    Para el desarrollo de la tarea se creará un proyecto desde cero conectado a un repositorio git personal.
    Las llamadas a los servicios deberán de hacerse mediante la librería Retrofit.
