# pruebaTecnica-doublevpartners

## Requisitos Previos

Antes de comenzar, asegurar tener instalados los siguientes programas:

- [Java 17](https://adoptopenjdk.net/)
- [Maven](https://maven.apache.org/download.cgi)
- [Git](https://git-scm.com/)
- [MySQL](https://www.mysql.com/downloads/) 

## Clonando el Repositorio

1. Abre la terminal o línea de comandos.
2. Ejecuta el siguiente comando para clonar el repositorio:

   ```bash
   git@github.com:franciscorestrepo/pruebaTecnica-doublevpartners.git

## Clonando el Repositorio
Construir y Ejecutar la Aplicacion

1. Asegurar  tener Maven instalado en la máquina.

2. En la raíz del proyecto, ejecuta el siguiente comando para construir el proyecto:
        mvn clean install
3. Una vez completada la construcción, ejecuta la aplicación con el siguiente comando:
       mvn spring-boot:run
   Esto iniciará el servidor de Spring Boot en el puerto por defecto 8082 o si desea usar otro puerto,
   puede configurarlo en application.properties.

## Probar la API
1. Postman o cURL: Si prefiere usar una herramienta como Postman o cURL para probar los endpoints:
   En el enlace podra descargar la collecion en Postman para probar los enpoints
   https://wetransfer.com/downloads/57f83f18aec9ce6778851bf43c56f89d20250120125758/d09ac4f6279ea7597fd08038b1f323e720250120125827/d95e2a?t_exp=1737637078&t_lsid=dde9e8ac-6e6b-4e43-815d-16fd2f8d312e&t_network=email&t_rid=Z29vZ2xlLW9hdXRoMnwxMTE4NjIxMTI1NTIwNjA0ODQ0NDA=&t_s=download_link&t_ts=1737377907
   Tenga en cuenta lo siguiente:
   Encontrara un archivo en la carpeta Autenticacion el cual realiza una peticion post y genera 
   un token JWT, este tiene una duracion de 24 horas.
   Luego este token debe ser añadido a cada una de las peticiones, en la pestaña Authorization en AuthType
   escoger Bearer Token, ingresar el token generado desde la autenticacion.
    
2. Swagger UI: Una vez que la aplicación esté en funcionamiento, puede acceder a la documentación interactiva 
   de la API a través de Swagger UI. Abra el navegador y diríjase a:
   http://localhost:8082/swagger-ui.html
   Allí podra ver todos los endpoints disponibles y probarlos directamente desde el navegador.

3. Configuracion de la Base de Datos:
   spring.application.name=pruebatecnica
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.datasource.url=jdbc:mysql://localhost:3306/tickets
   spring.datasource.username=root
   spring.datasource.password=root
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.format_sql=true
   server.port=8082
 







