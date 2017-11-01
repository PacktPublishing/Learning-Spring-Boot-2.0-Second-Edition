#!/bin/bash

find . -name build.gradle -exec ./build.bash {} \; -print
