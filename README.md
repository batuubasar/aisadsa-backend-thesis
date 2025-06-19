# Yapay Zeka Destekli Veri Mimarisi Tasarım Oturumu Asistanı

Bu proje, organizasyonların veri mimarisi ihtiyaçlarını analiz etmelerine ve kendilerine en uygun mimari çözümleri tasarlamalarına yardımcı olmak için geliştirilmiş bir **yapay zeka destekli karar destek sistemidir**. Dinamik soru akışları, iş kuralı motoru ve büyük dil modeli (ör. ChatGPT) entegrasyonu ile çalışır. Sistem, kullanıcı girdilerine göre öneriler sunar ve görsel diyagramlar üreterek karmaşık bilgileri erişilebilir hale getirir.

---

## **Projenin Amacı**

- Teknik bilgi seviyesi fark etmeksizin her seviyeden kullanıcının veri mimarisi tasarım süreçlerini sadeleştirmek ve hızlandırmak.
- Dinamik soru-cevap akışı ile kullanıcıların gereksinimlerini belirlemek.
- İş kuralı motoru aracılığıyla özelleştirilmiş mimari ve teknoloji önerileri sunmak.
- Chatbot (büyük dil modeli) entegrasyonu ile etkileşimli destek sağlamak.
- Mimari diyagramlar ile tasarımları görselleştirmek.

---

## ⚙️ **Kullanılan Teknolojiler**

| Bileşen | Teknoloji |
|----------|------------|
| Backend | Java (Spring Boot), Drools (Business Rule Engine) |
| Frontend | React, JavaScript |
| Görselleştirme | Mermaid.js |
| Veri Tabanı | MySQL |
| Yapay Zeka | OpenAI API (ChatGPT tabanlı) |
| API | RESTful API |
| Diğer | Docker, IntelliJ IDEA |

---

##  **Mimari Bileşenler**

- **İş Kuralı Motoru (Drools)**: Kullanıcı yanıtlarına göre mimari kararlar alır.
- **Chatbot Entegrasyonu (ChatGPT)**: Kullanıcının sorulara yanıt verir anlamadığı terimleri ve kısımları açıklayarak interaktif bir yardım sağlar, oturumu takip edip oturum sonunda iş kuralı motoru önerisine ek olarak öneri sunar, sürece rehberlik eder.
- **Diyagram Üretimi (Mermaid.js)**: Önceden belirlenmiş özel promptlara ve kullanıcının cevaplarına ihtiyaçlarına uygun otomatik mimari diyagram oluşturur.
- **Web Uygulaması**: Kullanıcı arayüzü React ile geliştirilmiştir.

---

