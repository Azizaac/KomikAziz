# 📚 KomikAziz

Aplikasi Android pembaca komik online berbasis API [Komikku](https://github.com/Romi666/komikku-api). Aplikasi ini dirancang untuk menampilkan daftar komik, detail komik, dan membaca halaman per halaman chapter komik menggunakan `ViewPager2`.

---

## ✨ Fitur Utama

- 🔍 Pencarian komik berdasarkan judul
- 📄 Menampilkan detail komik (judul, genre, penulis, sinopsis, dll)
- 📚 Daftar chapter yang dapat dibaca
- 🖼️ Pembaca komik halaman per halaman dengan `ViewPager2`
- ❤️ Menyimpan komik favorit menggunakan SQLite
- ⚡ Terhubung ke API Komikku untuk data real-time

---

## 📱 Tampilan Aplikasi

> (Tambahkan screenshot jika ada)

---

## 🧩 Teknologi yang Digunakan

- **Java** (Android)
- **Volley** untuk HTTP request
- **ViewPager2** untuk pembaca halaman komik
- **SQLite** untuk menyimpan komik favorit
- **Komikku API** sebagai sumber data komik

---

## 🛠️ Instalasi

1. Clone repository ini:

```bash
git clone https://github.com/username/komikaziz.git
cd komikaziz
Buka dengan Android Studio

Pastikan koneksi internet aktif

Jalankan aplikasi di emulator atau perangkat fisik

🔌 Struktur API
📚 Daftar Komik: https://komiku-api.fly.dev/api/comic/popular

📄 Info Komik: https://komiku-api.fly.dev/api/comic/info/{slug}

📖 Chapter Komik: https://komiku-api.fly.dev/api/comic/chapter{endpoint}

⚠️ Catatan
Pastikan chapter memiliki gambar sebelum ditampilkan. Beberapa endpoint mungkin belum memiliki konten.

Aplikasi hanya sebagai client reader, tidak menyimpan konten di dalam perangkat.

Sumber komik sepenuhnya berasal dari Komikku API.

📩 Kontribusi
Pull request dan saran sangat diterima! Buatlah issue jika kamu menemukan bug atau ingin menambahkan fitur.

📄 Lisensi
Proyek ini menggunakan lisensi MIT.

🙋 Tentang
Aplikasi ini dibuat oleh Aziz Choirudin sebagai proyek pembelajaran Android dan integrasi API publik.
