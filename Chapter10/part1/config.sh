# tag::eureka[]
#!/usr/bin/env bash

cf set-env learning-spring-boot-eureka-server spring.cloud.config.uri https://learning-spring-boot-config-server.cfapps.io
cf set-env learning-spring-boot-eureka-server spring.cloud.config.label production
# end::eureka[]

# tag::chat[]
cf set-env learning-spring-boot spring.cloud.config.uri https://learning-spring-boot-config-server.cfapps.io
cf set-env learning-spring-boot spring.cloud.config.label production

cf bind-service learning-spring-boot learning-spring-boot-mongodb
cf set-env learning-spring-boot spring.data.mongodb.uri \${vcap.services.learning-spring-boot-mongodb.credentials.uri}

cf bind-service learning-spring-boot learning-spring-boot-rabbitmq
# end::chat[]

# tag::comments[]
cf set-env learning-spring-boot-comments spring.cloud.config.uri https://learning-spring-boot-config-server.cfapps.io
cf set-env learning-spring-boot-comments spring.cloud.config.label production

cf bind-service learning-spring-boot-comments learning-spring-boot-mongodb
cf set-env learning-spring-boot-comments spring.data.mongodb.uri \${vcap.services.learning-spring-boot-mongodb.credentials.uri}

cf bind-service learning-spring-boot-comments learning-spring-boot-rabbitmq
# end::comments[]

# tag::images[]
cf set-env learning-spring-boot-images spring.cloud.config.uri https://learning-spring-boot-config-server.cfapps.io
cf set-env learning-spring-boot-images spring.cloud.config.label production

cf bind-service learning-spring-boot-images learning-spring-boot-mongodb
cf set-env learning-spring-boot-images spring.data.mongodb.uri \${vcap.services.learning-spring-boot-mongodb.credentials.uri}

cf bind-service learning-spring-boot-images learning-spring-boot-rabbitmq
# end::images[]