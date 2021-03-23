#!/bin/bash
gradle run && echo "Running generated class" && java -cp . Guess
rm Guess.class