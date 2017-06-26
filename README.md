# Smart-mirror

This github repo is made for project Cluster & SmartMirror, 
It works in combination with the clust to show the output of the different API's that are running on said cluster.


The mirror is build with a raspberry pi 3 which runs piCore 8. This is a fork of TinyCore.

The rest of the system is build with java version 8. 
The system interfaces with the raspberry through the use of shell scripts to make functions available like Wifi.

Currently the mirror supports the following:
 - 4 Buttons for navigations ( up, down, menu, back)
 - Scanning of Wifi and connecting to it
 - A framework to add user build applications
 - The API's that are made available in the CLuster & SmartMirror project
 
 
The API's will only work when its connected to the cluster.
