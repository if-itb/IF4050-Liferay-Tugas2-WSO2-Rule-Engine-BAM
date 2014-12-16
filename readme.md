##=======WSO2 Exploration=======
Rule Engine & Business Activity Monitoring
===================
Disusun oleh :

 - Alif Raditya Rochman **13511013@std.stei.itb.ac.id** 
 - Fathan Adi Pranaya **13511011@std.stei.itb.ac.id** 
 - Sonny Lazuardi Hermawan **13511030@std.stei.itb.ac.id** 
 - Muhammad Harits S.A.H.E **13511046@std.stei.itb.ac.id**

----------


Business Activity Monitor (BAM)
-------------
WSO2 BAM merupakan *framework* yang dapat digunakan untuk agregasi, analisis, dan visualisasi dari informasi mengenai aktivitas pada suatu organisasi termasuk di dalamnya customer dan partner secara ***real-time***


#### <i class="icon-file"></i> Arsitektur

Berikut ini adalah arsitektur pada framework WSO2 BAM.

![Arsitektur WSO2 BAM](http://s28.postimg.org/mqrew8759/arsitektur.png)
Pada komponen *Data Agents* dapat berkolaborasi dengan framework WSO2 lainnya seperti CEP atau ESB. Data yang digunakan pada BAM dapat diperoleh melalui : 

 - Service monitoring feature - WSO2 AS, DSS, ESB, API Manager
 - Mediation monitoring feature - BAM Mediator for WSO2 ESB
 - Custom data agents

Komponen ini menggunakan apache thrift untuk menghasilkan performansi yang maksimal dalam hal *message throughput* karena bersifat asynchronous dan non-blocking.

Eksplorasi
-------------------
Pada kesempatan kali ini, kami akan mencoba melakukan eksplorasi WSO2 BAM dengan membuat activity monitoring pada data dari BAM Mediator.

#### <i class="icon-refresh"></i> Setup Server

Pertama, unduh WSO2 BAM versi 2.3.0 [di sini]
(http://wso2.com/products/business-activity-monitor/) 
Setelah selesai, ekstrak WSO2 BAM ke dalam suatu folder dan jalankan server WSO2 BAM dengan menjalankan perintah di bawah ini.

    $ unzip wso2bam-2.3.0.zip
    $ cd wso2bam-2.3.0/bin
    $ ./wso2server.sh

> **Catatan:**

> - Kode di atas dijalankan melalui terminal pada linux. Untuk windows, harap disesuaikan
> - Setelah server berhasil dihidupkan, biasanya terminal akan memberikan informasi alamat dan port untuk mengakses halaman antarmuka WSO2 BAM. Pada kali ini saya mendapatkan alamat dan port : https://192.168.19.100:9443
> - Setelah berhasil masuk ke halaman antarmuka, login menggunakan **username : admin** dan **password : admin**

#### <i class="icon-refresh"></i> Siapkan Data

Setelah melakukan set-up server WSO2 BAM, buka terminal baru, dan jalankan Activity Monitoring sample yang berada pada folder samples pada folder home WSO2 BAM.

    $ cd <BAM_HOME>
    $ cd samples/activity-monitoring
    $ ant
Tunggu sampai message "*100 events are published and BUILD SUCCESSFUL*" ditampilkan pada terminal. Proses ini merupakan proses publish events dari ESB ke database cassandra pada BAM.

Setelah selesai, events yang telah berhasil dipublish ke database cassandra dapat dilihat di halaman antarmuka BAM pada menu **tools**->**Cassandra Explorer**->**Explore Cluster**. 

Kemudian klik **org_wso2_bam_activity_monitoring** pada **EVENT_KS** keyspace. Dan klik **view more ** untuk melihat konten dari tiap event. Berikut adalah tampilannya.

![tampilan detail event](http://s14.postimg.org/f2z81ims1/event_ks.png)

#### <i class="icon-refresh"></i> Agregasi Data

Setelah data tersedia, selanjutnya yang akan dilakukan adalag melakukan agregasi data atau bisa dikatakan *summarization data*. Langkah pertama yang harus dilakukan adalah menambahkan HIVE script pada BAM dengan langkah-langkah berikut.

 - Buka management console lalu buka **Analytics**-> **Add** dari main
   menu. Kemudian Script Editor window akan ditampilkan.

![tampilan script editor](http://s3.postimg.org/c18ysotxv/script_editor.png)

 - Setelah itu, tambahkan kode berikut pada field script editor.
   ![query](http://s22.postimg.org/4tdst49wh/query.png)
   
 - Jalankan script tersebut dan atur scheduling untuk menjalankan script tersebut secara otomatis pada rentang waktu yang ditentukan. 
 
 - Berikut adalah contoh eksekusi query melalui H2 client
![hasil query](http://s16.postimg.org/5lxde46d1/3_a.png)


#### <i class="icon-refresh"></i> Visualisasi Data

Langkah berikutnya adalah menvisualisasikan data yang sudah di-*summarization* pada langkah sebelumnya. Dibawah ini akan dijelaskan bagaimana men-*generate* *gadget*. Untuk contoh visualisasi data kali ini, kami akan menggunakan bar chart. Berikut adalah langkah-langkahnya.

 1. Masuk ke management console klik **Tools**->**Gadget Gen Tool** isikan informasi berikut pada field yang tersedia. Lalu klik next.
 ![gadget gen tool](http://s13.postimg.org/5hwszt4lj/7_b.png)

 2. Masukkan query agregasi pada field SQL statement. Kemudian klik next.
 ![query agregasi](http://s11.postimg.org/jro1a426r/8_aa.png)
 
 3. Pilih *bar graph* untuk UI element. Dan atur konfigurasi seperti pada gambar di bawah ini. Kemudian klik next.
![pilihan chart](http://s8.postimg.org/88x1w47rp/9_b.png)
 4. Masukkan judul, nama file untuk gadget. Dan atur refresh rate untuk menampilkan update secara realtime. Jika sudah, klik generate
 ![judul](http://s16.postimg.org/cuine9et1/10_aa.png)
 5. Maka BAM akan menampilkan gadget yang telah dibuat seperti pada gambar di bawah ini.
![hasil akhir](http://s29.postimg.org/4jwqv1p4n/11_axb.png)


----------
Rule Engine
-------------
## Rule Server

Setiap bisnis memiliki jenis sistem yang berbeda untuk mengatur operasi. WSO2 BRS memungkinan kita untuk menulis, me-mantain dan mengekspos business rule sebagai service.

![Architecture](http://image.slidesharecdn.com/practicalsoaforthesolutionarchitectv03-111206030915-phpapp02/95/practical-soa-for-the-solution-architect-12-728.jpg?cb=1323224297)

Rule Server pada WSO2 termasuk business services yang mempunyai kategori yang sama dengan Complex Event Processing Server.

## Kuliah Service Example

Dalam pengambilan SKS, kita perlu mempertimbangkan sks yang sudah diambil dan ipk dari mahasiswa. Service ini digunakan untuk menentukan apakah mahasiswa tersebut boleh mengambil kuliah.

## Struktur Kode

        |__ rule
          |__ greeting.service
          |__ build
          |__ facts\src\main\java\samples\greeting
            |__ AmbilKuliah.java
            |__ User.java
          |__ service
            |__ META-INF
              |__ service.rsl
          |__ build.xml

## Installation

 1. Unduh WSO2 Business Rules Server [disini](http://wso2.com/products/business-rules-server/)
 2. Copy folder rule ke wso2brs/samples/greeting.service
 3. Jalankan executable pada folder bin/wso2server.bat
 4. buka alamat yang tertera pada console melalui browser

![alamat](https://lh6.googleusercontent.com/-7UDlZbO-h-0/VJB8tYHQHBI/AAAAAAAABKI/0UwzR2LHE9Q/s0/Capture.JPG "Alamat")

 5. login (default user : admin, password : admin)
 6. Pilih menu services > List lalu akan muncul GreetingService
 7. Pilih menu Try this service

![menu](https://lh3.googleusercontent.com/-e4SzOGewX7I/VJB9YD6KDNI/AAAAAAAABKY/mcwUuALxqaA/s0/Capture.JPG "menu")

## Rule Service

Pada folder facts di greeting.service terdapat dua file java yaitu `User.java` dan `AmbilKuliah.java`. `User.java` digunakan untuk membuat kelas User dalam kasus ini adalah Mahasiswa beserta atribut-atribut yang bisa diakses. Lalu AmbilKuliah hanya merupakan Message output dari rule kita.

 ![source](https://lh3.googleusercontent.com/-ChZ5XjWbI8c/VJB-j7cPQDI/AAAAAAAABKw/LokaAuHXwEo/s0/Capture.JPG "source")

Selanjutnya untuk menentukan rule kita bisa mengedit file service.rsl pada folder service

![enter image description here](https://lh4.googleusercontent.com/-wHIW8I9o6Ms/VJB_IXaq23I/AAAAAAAABLA/2pGNt_t1MYQ/s0/Capture.JPG "services")

Pada rule di atas ada tiga state output dari rule yaitu `Boleh`, `Bersyarat`, dan `Tidak Boleh` mengambil mata kuliah.
File ini sebenearnya berformat xml namun dengan ruleset yang bisa mengakses atribut dari kelas interface java `User.java` yang sudah kita buat.

## Pengujian

Salah satu contoh kasus yang kita coba adalah memberikan input sebagai berikut :

  SKS : 25
  IP : 3.25
  name : sahe
  nim : 13511046

output yang dihasilkan adalah

  sahe mengambil kuliah dengan status bersyarat 

![Kuliah Service Test](https://lh3.googleusercontent.com/-sHcIF5fOKaU/VH6-8Nm8SvI/AAAAAAAABE4/-E7DqxhP8ls/s0/Capture.JPG "KuliahService Test")
