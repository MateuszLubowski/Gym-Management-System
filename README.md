# Gym Management System

Desktop application for booking gym training zones.
Aplikacja desktopowa do rezerwacji stref treningowych na siłowni.

## Features
- Choose a training zone, date and 2-hour time slot
- Slots generated from the zone's weekly opening schedule
- Optional personal trainer, with availability check
- Membership discount (-50% on zone price)
- Payment and booking confirmation with reservation number
- Conflict detection for zones and trainers

## Tech stack
Java 25, JavaFX (FXML), SQLite (JDBC), Maven

## Architecture
- MVC with FXML views and controllers
- DAO pattern for database access
- Mediator (`RezerwacjaController`) coordinating the three screens
- Domain model designed in UML (association Strefa-Harmonogram 1:7,
  composition Rezerwacja-Platnosc with ON DELETE CASCADE)

## Screenshots
<img width="599" height="794" alt="image" src="https://github.com/user-attachments/assets/33c45c58-2803-4609-b1a3-86b96d2c6472" />
<img width="645" height="1036" alt="image" src="https://github.com/user-attachments/assets/95002b9b-f9b5-4b9d-bb1a-aabd74021ce8" />
<img width="635" height="1038" alt="image" src="https://github.com/user-attachments/assets/db1f826d-60ca-41da-b558-b9f09be8aacb" />


## Requirements
- JDK 25
- IntelliJ IDEA (bundles Maven) or Maven 3.8+

Open the project in IntelliJ and run `javafx:run` from the Maven panel
(Plugins → javafx), or use `mvn clean javafx:run` in a terminal `

## Author
Mateusz Łubowski
