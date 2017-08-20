Para construir el proyecto:
	mvn install -DskipTests
	
Iniciar los servicios:
	docker-compose up -d
	
Detener los servicios:
	 docker-compose stop
 
 
 NOTA:
 - Para despliegue en ambiente local no olvidar agregar a etc/hosts
 	127.0.0.1			influxdb
 	

Consultas para Influxdb:

# Listar los tagKey para una métrica
- SHOW TAG KEYS ON "mydb" FROM "gs-pps"

# Listar los tagValues para un tagKey
- SHOW TAG VALUES ON "mydb" FROM "gs-pps" WITH KEY = "jvm-pid"


