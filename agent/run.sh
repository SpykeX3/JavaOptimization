#!/bin/bash
cd demo && gradle jar && cd .. && cp demo/app/build/libs/app.jar demo.jar
cd agent && gradle shadowJar && cd .. && cp agent/lib/build/libs/agent.jar agent.jar
java -javaagent:agent.jar -jar demo.jar
