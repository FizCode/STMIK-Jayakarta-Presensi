# STMIK-Jayakarta-Presensi
[![STMIK Jayakarta Presensi App CI](https://github.com/FizCode/STMIK-Jayakarta-Presensi/actions/workflows/ci.yml/badge.svg?branch=master)](https://github.com/FizCode/STMIK-Jayakarta-Presensi/actions/workflows/ci.yml)

## APK
<a href="https://github.com/FizCode/STMIK-Jayakarta-Presensi/raw/master/app/build/outputs/apk/debug/app-debug.apk"><img src="https://user-images.githubusercontent.com/101188749/195594803-085ae479-f9c8-4384-9226-fe18ac6b694d.svg" alt="download"></a>

## Tentang Presensi STMIK Jayakarta
<p align="justify">
Sistem presensi Mahasiswa STMIK Jayakarta pada waktu skripsi ini dibuat masih menggunakan metode konvensional. Pada kelas <i>offline</i> dosen memanggil nama sesuai urutan lalu mahasiswa mengonfirmasi kemudian dosen mencatat kehadirannya. Kemudian pada kelas <i>online</i>, mahasiswa mengisi Google Form dari dosen yang setiap minggunya akan sama sehingga mahasiswa selalu mengulang pengisian form tersebut.
<br><br>
Pada kasus kelas <i>offline</i>, waktu kelas akan terbuang untuk dosen melakuakan presensi mahasiswa. Kemudian pada kasus kelas <i>online</i>, mahasiswa yang tidak dapat hadir pada kelas akan tetap dapat melakukan presensi. Sehingga ini dapat menyebabkan kecurangan dilakukan oleh mahasiwa yang tidak hadir seolah mahasiswa tersebut hadir di kelas.
<br><br>
Jika kelas <i>offline</i> diterapkan sistem presensi seperti kelas <i>online</i>, akan ada kecurangan yaitu semua mahasiswa termasuk yang tidak hadir akan dianggap tidak hadir.
<br><br>
Pada kasus ini, maka penulis akan membuat sistem presensi yang hanya dapat dilakukan di radius Jayakarta dengan konfirmasi <i>auth. biometric</i> dari ponsel masing - masing mahasiswa. Sehingga harapannya dapat memudahkan mahasiswa dalam melakukan presensi, mengurangi waktu presensi yang terbuang dan menghindari kecurangan mahasiswa dalam presensi.
</p>

## Design Pattern MVVM (Model, view, viewModel)
![Gambaran MVVM](https://github.com/FizCode/STMIK-Jayakarta-Presensi/assets/101188749/724e779b-aed6-4e72-9527-ed283aac6b77)


## Dependency Tambahan
>- <a href='https://developer.android.com/jetpack/compose/navigation'>Navigation Compose</a>: Dipakai untuk navigasi pada Jetpack Compose
>- <a href='https://developer.android.com/jetpack/androidx/releases/lifecycle'>Lifecycle ViewModel</a>: Dipakai untuk memisahkan Live data dengan UI.
>- <a href='https://square.github.io/retrofit/'>Retrofit</a>: Dipakai untuk mengambil, mengubah, dan post data JSON file melalui web service berbasis REST
>- <a href='https://kotlinlang.org/docs/coroutines-guide.html'>Kotlin Coroutines</a>: Dipakai untuk async code menjadi sync code pada Live Data
>- <a href='https://developer.android.com/training/dependency-injection/hilt-android'>Hilt (Dagger-hilt)</a>: Dipakai untuk dependency injection
>- <a href='https://developer.android.com/jetpack/androidx/releases/datastore'>Datastore</a>: Dipakai untuk menyimpan access token key-value pair
>- <a href='https://developer.android.com/jetpack/androidx/releases/room'>Room Database</a>: Dipakai untuk database local
>- <a href='https://developer.android.com/reference/kotlin/androidx/compose/material/icons/package-summary'>Extended Icon</a>: Tambahan Material Icon
>- <a href='https://developer.android.com/jetpack/androidx/releases/biometric'>Biometric</a>: Digunakan untuk auth. biometrics
>- <a href='https://developers.google.com/android/guides/setup'>Location</a>: Digunakan untuk akses lokasi

## Aplikasi Pendukung
>- <a href='https://developer.android.com/studio'>Android Studio</a> <a>: Sebagai Andorid IDE </a>
>- <a href='https://www.postman.com/'>Postman</a> <a>: Sebagai Pengetesan API</a>
>- <a href='https://json2kt.com/'>Json2KT</a> <a>: Mengubah data API menjadi Data Class</a>

## Video Aplikasi
soon

## Screenshots
soon
