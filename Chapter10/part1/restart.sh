#!/usr/bin/env bash

cf restart learning-spring-boot-config-server

sleep 10

cf restart learning-spring-boot-eureka-server &
cf restart learning-spring-boot &
cf restart learning-spring-boot-comments &
cf restart learning-spring-boot-images &