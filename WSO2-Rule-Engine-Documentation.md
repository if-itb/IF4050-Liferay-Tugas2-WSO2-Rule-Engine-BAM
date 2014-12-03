# WSO2 Rule Engine

## Rule Server

Setiap bisnis memiliki jenis sistem yang berbeda untuk mengatur operasi. WSO2 BRS memungkinan kita untuk menulis, me-mantain dan mengekspos business rule sebagai service.

## Installation

 1. Download the WSO2 Business Rules Server
 2. Run the executable in the folder bin/wso2server.bat

## Kuliah Service Example

Dalam pengambilan SKS, kita perlu mempertimbangkan sks yang sudah diambil dan ipk dari mahasiswa. Service ini digunakan untuk menentukan apakah mahasiswa tersebut boleh mengambil kuliah.

## Code Struktur

  |__ rule
     |__ greeting.service
        |__ build
        |__ facts
           |__ src\main\java\samples\greeting
              |__ AmbilKuliah.java
              |__ User.java
      |__ service
      |__ build.xml
