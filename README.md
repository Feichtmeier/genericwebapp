![CI with nodejs, mariadb and maven](https://github.com/Feichtmeier/genericwebapp/workflows/CI%20with%20nodejs,%20mariadb%20and%20maven/badge.svg?branch=master)

# genericwebapp

TODO:

- [ ] use current github action to deploy dev branch on azure / google / amazon
- [X] make vaadins `@PWA` work
- [X] make parts of the gridviews re-usable via abstract class and/or interface
- [X] use proper CSS theming
- [X] add responsive padding
- [X] make avatars persistent
- [ ] add delete account option in settings
- [ ] use international Strings
- [ ] add oAuth account creation via google and/or github

## About

This is a mobile ready, responsive, persistent, generic webapp built with [Spring Boot](https://github.com/spring-projects/spring-boot) and [Vaadin](https://github.com/vaadin/) which can also be installed as a progressive webapp (PWA) directly from within your browsers (currently Firefox desktop does not support PWAs fully yet).

![](showcaseassets/mobile.gif)

![](showcaseassets/desktop.gif)

![](showcaseassets/pwa_01.gif)

![](showcaseassets/pwa_02.gif)

## Use case

This webapp provides a basic user, permission and role management.

New views can be injected to [AppView.java](https://github.com/Feichtmeier/genericwebapp/blob/415faace1343e40281d07c5760015183eb68525d/genericwebapp/src/main/java/org/feichtmeier/genericwebapp/view/AppView.java#L41).

The login is secured via Spring Security.

An insecure REST Api exists in the `restapi` maven project.

## Build

1. Install java
   * Windows/MacOs: https://adoptopenjdk.net/
   * Ubuntu: `sudo apt install default-jdk`
2. Install maven
   * Windows/MacOs: https://maven.apache.org/download.cgi
   * Ubuntu: `sudo apt install maven`
3. Install vscode
   * Windows/MacOs: https://code.visualstudio.com/
   * Ubuntu: `sudo snap install code --classic`
4. Install the vscode java extension pack: https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack
5. Install the vscode spring boot extension pack: https://marketplace.visualstudio.com/items?itemName=Pivotal.vscode-boot-dev-pack
6. Setup a local database and database user ([Tutorial](https://github.com/hs-duesseldorf/se2rest#2-database))
7. Change `application.properties` to match your database/user/password
8. Install git
   * Windows: https://git-scm.com/download
   * MacOs: `brew install git`
   * Ubuntu: `sudo apt install git`
9. clone this repository with `git clone https://github.com/Feichtmeier/genericwebapp`
10. start your local database
11. Open your local clone of the repository with vscode and press the play button in the spring-boot dashboard
    ![](showcaseassets/start.gif)
12. Navigate to `localhost:8080` in your browser to access the webapp
