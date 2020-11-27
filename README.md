![CI with nodejs, mariadb and maven](https://github.com/Feichtmeier/genericwebapp/workflows/CI%20with%20nodejs,%20mariadb%20and%20maven/badge.svg?branch=master)

# genericwebapp

## About

This is a mobile ready, responsive, persistent, generic webapp built with [Spring Boot](https://github.com/spring-projects/spring-boot) and [Vaadin](https://github.com/vaadin/).

## Use case

This webapp is not meant to be used in production and needs to be extended for individual use cases. It provides a basic user, permission and role management.

New views/tabs can be added either into the existing notebook in `AppView.java` or somewhere else.

A REST Api exists in the controller package to manipulate the database, but should be excluded from this repository in a production environment and used in a second "REST only" repository, with the same entities.

The login is secured via Spring Security, which currently disallows API calls.

## Build

1. Install java
   * Windows/MacOs: https://adoptopenjdk.net/
   * Ubuntu: `sudo apt install default-jdk`
2. Install maven
   * Windows/MacOs: https://maven.apache.org/download.cgi
   * Ubuntu: `sudo apt install maven`
3. Install nodejs
   * Windows/MacOs: https://nodejs.org/en/
   * Ubuntu: `sudo snap install node --classic`
4. Install vscode
   * Windows/MacOs: https://code.visualstudio.com/
   * Ubuntu: `sudo snap install code --classic`
5. Install the vscode java extension pack: https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack
6. Install the vscode spring boot extension pack: https://marketplace.visualstudio.com/items?itemName=Pivotal.vscode-boot-dev-pack
7. Install git
   * Windows: https://git-scm.com/download
   * MacOs: `brew install git`
   * Ubuntu: `sudo apt install git`
8. clone this repository & open in vscode and press the play button in the spring-boot dashboard
