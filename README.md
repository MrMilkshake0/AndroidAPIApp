
# NIT3213 Android App (XML + Hilt + Retrofit)

![Kotlin](https://img.shields.io/badge/Kotlin-1.9-blueviolet?logo=kotlin)
![Android](https://img.shields.io/badge/Android-UI-green?logo=android)
![Hilt](https://img.shields.io/badge/Hilt-DI-informational)
![Retrofit](https://img.shields.io/badge/Retrofit-Networking-brightgreen)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

A simple Android app demonstrating API integration, Material 3 UI, and clean architecture with **three screens**:

- **Login** → authenticates and retrieves a `keypass`  
- **Dashboard** → fetches and lists assets/entities from the API  
- **Details** → shows full information for a selected entity  

---

## ✨ Features
- XML-based UI with Material 3 components  
- Hilt for dependency injection  
- Retrofit + OkHttp + Gson for networking  
- ViewModel + Coroutines + StateFlow for async work  
- RecyclerView + DiffUtil for Dashboard list  
- Unit test samples (ViewModel with MockK)  

---

## 🌐 API

**Base URL:** `https://nit3213api.onrender.com/`

### Login (POST)

`/{campus}/auth`  
where `campus ∈ { footscray | sydney | br }`

**Request body:**
```json
{
  "username": "YourFirstName",
  "password": "YourStudentID"   // without 's'
}
````

**Success:**

```json
{ "keypass": "topicName" }
```

---

### Dashboard (GET)

`/dashboard/{keypass}`

**Success:**

```json
{
  "entities": [
    { "assetType": "Stock", "ticker": "ABC", "currentPrice": 12.5, "dividendYield": 0.04, "description": "..." }
  ],
  "entityTotal": 7
}
```

---

## 🛠 Tech Stack

* Kotlin, Android XML, Material 3
* Hilt (DI)
* Retrofit, OkHttp, Gson
* Coroutines, StateFlow
* RecyclerView + ViewBinding
* JUnit, MockK

---

## 📂 Project Structure

```
app/src/main/java/com/example/nit3213/
├─ App.kt                     # @HiltAndroidApp
├─ data/
│  ├─ ApiService.kt           # Retrofit endpoints
│  ├─ Entity.kt               # AssetEntity model
│  ├─ Models.kt               # DTOs (LoginRequest/Response, DashboardResponse)
│  └─ Repository.kt           # API orchestration + keypass storage
├─ di/
│  └─ NetworkModule.kt        # Retrofit/OkHttp/Gson providers
├─ ui/
│  ├─ login/                  # LoginActivity + ViewModel
│  ├─ dashboard/              # DashboardActivity + Adapter + ViewModel
│  └─ details/                # DetailsActivity
└─ util/
   └─ UiState.kt              # Loading/Success/Error sealed class
```

---

## 🚀 Getting Started

### Prerequisites

* Android Studio (latest stable)
* JDK 17
* Android SDK 34+

### Setup

1. Clone the project and open in Android Studio.
2. Ensure Internet permission is in `AndroidManifest.xml`:

   ```xml
   <uses-permission android:name="android.permission.INTERNET"/>
   ```
3. Perform a Gradle sync (project uses Hilt + ViewBinding).

---

## ▶️ Run

1. Select an emulator or device → Run.
2. On **Login** screen:

   * Campus = `footscray`, `sydney`, or `br`
   * Username = your **first name**
   * Password = your **student ID** (no leading `s`)
3. On success → navigates to **Dashboard**.
4. Tap an item → view **Details**.

---

## 🧪 Testing

Run unit tests:

```bash
./gradlew test
```

**Included:**

* `LoginViewModelTest` – login success & failure states
* Dispatcher rule for coroutines

---

## ⚠️ Troubleshooting

* **401/400 on login** → Check campus selection, first-name username, and numeric student ID (no `s`).
* **“Missing keypass. Please login first.”** → Log in again; `keypass` is stored in memory only for this demo.
* **Network errors** → Verify device/emulator internet connectivity and API availability.
* **Theme issues** → Ensure `Theme.NIT3213` (Material 3) is applied in `styles.xml`.

---

