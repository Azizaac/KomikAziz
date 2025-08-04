# 📚 KomikAziz

Aplikasi Android pembaca komik online berbasis API [Komikku](https://github.com/Romi666/komikku-api).  
Aplikasi ini dirancang untuk menampilkan daftar komik, detail komik, dan membaca halaman per halaman chapter komik menggunakan `ViewPager2`.

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
<img src="https://github.com/user-attachments/assets/41941dee-0f05-439f-9f15-8d76aae19ec3" alt="Screenshot" style="width: 300px; border-radius: 10px;" />
---

## 🧩 Teknologi yang Digunakan

- **Java** (Android)
- **Volley** – Untuk HTTP request
- **ViewPager2** – Untuk pembaca halaman komik
- **SQLite** – Untuk menyimpan data komik favorit
- **Komikku API** – Sumber data komik online

---

## 🛠️ Instalasi

1. Clone repository ini:

```bash
git clone https://github.com/username/komikaziz.git
cd komikaziz
```

Buka proyek dengan Android Studio

Pastikan koneksi internet aktif

Jalankan aplikasi di emulator atau perangkat fisik

🔌 Struktur API
📚 Daftar Komik: https://komiku-api.fly.dev/api/comic/popular

📄 Info Komik: https://komiku-api.fly.dev/api/comic/info/{slug}

📖 Chapter Komik: https://komiku-api.fly.dev/api/comic/chapter{endpoint}

⚠️ Catatan
Pastikan chapter memiliki gambar sebelum ditampilkan.

Beberapa endpoint mungkin belum memiliki konten.

Aplikasi ini hanya sebagai client reader dan tidak menyimpan konten di perangkat.

Semua data dan gambar berasal dari Komikku API.

📩 Kontribusi
Pull request dan saran sangat diterima!
Buatlah issue jika kamu menemukan bug atau ingin menambahkan fitur.

📄 Lisensi
Proyek ini menggunakan lisensi MIT.

🙋 Tentang
Aplikasi ini dibuat oleh Aziz Choirudin sebagai proyek pembelajaran Android dan integrasi API publik.

