# Readme
## ⭐ Introduction
This is a simple library console app made by four students
simulating a library where you can find and loan books. 
We utilize JAVA21 and MySQL to accomplish this. Follow the
instructions below to use the program correctly. 

## 📝 Prerequisites
The following are things that you need to have installed on your machine before
you begin.

- Java Development Kit (JDK) 21
- Java Virtual Machine (JVM)
- Maven
- Docker
- Any **IDE** that supports **JAVA** and **SQL**. 

## ⏩ Docker Setup
You need a running docker of MySQL on port 3306.

## 💼 Dependencies
If not already cloned from repository you need the following dependencies:
You can copy this into your 📑 *pom.xml* file.
-       <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <version>9.4.0</version>
        </dependency>

## 📦 Database Setup

Use any program that supports **SQL** to run the **schema.sql** file in the **sql**
folder. You have to make sure that the database is named 📑 **library_db** and it 
needs to have the following configuration: 

- 📗 **User:** root
- 📗 **Password:** rootpassword
- 📗 **Port:** 3306
---
- Run the 📑 **data.sql** file in the **sql** folder to fill the **tables**.

- ❌ If anything goes wrong run the 📑 **reset.sql** file in the **sql**
folder to clear all the **tables**. 

## ✅ Program
Compile in your **IDE** of choice and navigate the program by 
inputting the numbers that corresponds to the menu options.
