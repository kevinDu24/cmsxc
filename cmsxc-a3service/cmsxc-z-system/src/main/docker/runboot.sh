sleep 3
java -Djava.security.egd=file:/dev/./urandom -jar -Xms4196m -Xmx4196m -XX:PermSize=128M -XX:MaxNewSize=512m -XX:MaxPermSize=512m /app/app.jar