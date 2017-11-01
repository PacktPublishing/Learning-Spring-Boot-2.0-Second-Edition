#!/usr/bin/env bash

cf delete -f learning-spring-boot-config-server &
cf delete -f learning-spring-boot-eureka-server &
cf delete -f learning-spring-boot &
cf delete -f learning-spring-boot-comments &
cf delete -f learning-spring-boot-images &
cf delete -f learning-spring-boot-hystrix-dashboard &
