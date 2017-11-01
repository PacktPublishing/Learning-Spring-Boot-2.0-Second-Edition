#!/usr/bin/env bash

cf push learning-spring-boot-config-server -p config-server/build/libs/learning-spring-boot-config-server-0.0.1-SNAPSHOT.jar &
cf push learning-spring-boot-eureka-server -p eureka-server/build/libs/learning-spring-boot-eureka-server-0.0.1-SNAPSHOT.jar &
cf push learning-spring-boot -p chat/build/libs/learning-spring-boot-chat-0.0.1-SNAPSHOT.jar &
cf push learning-spring-boot-comments -p comments/build/libs/learning-spring-boot-comments-0.0.1-SNAPSHOT.jar &
cf push learning-spring-boot-images -p images/build/libs/learning-spring-boot-images-0.0.1-SNAPSHOT.jar &
cf push learning-spring-boot-hystrix-dashboard -p hystrix-dashboard/build/libs/learning-spring-boot-hystrix-dashboard-0.0.1-SNAPSHOT.jar &
