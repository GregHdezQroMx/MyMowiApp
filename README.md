# Mowi App - Smart Corporate Logistics (KMP/CMP)

This project is a **functional MVP** for the Mowi application, built using **Kotlin Multiplatform (KMP)** and **Compose Multiplatform (CMP)**. It implements a smart trip planning flow designed to showcase the technical superiority of a shared native architecture over hybrid solutions like React Native.

## 🏗️ Architecture
The project follows a **Modular Clean Architecture with MVVM** pattern:

### Shared Module (`:shared`)
- **`domain/`**: Pure Kotlin entities (`TripModels`) and repository interfaces.
- **`data/`**: Repository implementations, including `remote` (Ktor Client) and mock data logic.
- **`presentation/`**: Platform-agnostic ViewModels using `StateFlow` and `viewModelScope`.

### Compose App (`:composeApp`)
- **`commonMain/`**: Shared UI logic, screens, and components built with Material 3.
- **`androidMain/` / `iosMain/`**: Platform-specific entry points and native configurations.

### Server (`:server`)
- **Ktor Backend**: Simulates the corporate logistics API and business orchestration.

## ✨ Key Features (Mock)
- **Smart Trip Planning:** End-to-end flow from destination selection to booking confirmation.
- **AI Optimization:** Real-time route optimization simulation and cost-saving analysis.
- **Financial Components:** Detailed corporate logistics cost summary and tax breakdown.
- **Multiplatform Previews:** Native `@Preview` support in `commonMain` using the `expect/actual` pattern for high developer productivity.

## 🛠️ Tech Stack
- **Compose Multiplatform:** 100% native shared UI.
- **Koin:** Multiplatform dependency injection.
- **Navigation 3:** Next-generation type-safe navigation for Compose Multiplatform.
- **Kotlinx Coroutines & Flow:** Reactive state management and concurrency.
- **Kotlinx Serialization:** Type-safe data handling.
- **Ktor:** Network communication for both client and server.

## 🚀 Getting Started
### Android
```
./gradlew :composeApp:assembleDebug
```
### iOS
Open the `iosApp` directory in Xcode or use the run configuration in Android Studio.

### Server (Mock API)
```
./gradlew :server:run
```

## 📉 Migration from React Native
This project demonstrates why KMP/CMP is the superior choice for corporate logistics:
1. **Strong Typing:** Eliminates runtime errors common in JS/TS environments.
2. **Native Performance:** 60/120 FPS UI execution without the overhead of a Bridge or JSI.
3. **Maintainability:** A single source of truth for business logic and UI, compiled directly to native binaries.

## 🗺️ Roadmap
- [x] **Phase 1**: Initial MVP Architecture & Core UI Components.
- [x] **Phase 2**: Navigation 3 Integration (JetPack/JetBrains Alpha).
- [ ] **Phase 3**: Real-time Ktor Server synchronization & Web Support.
- [ ] **Phase 4**: Advanced AI Route Optimization & Analytics Dashboard.

---
Developed by **GregHdezQroMx** based on the architecture proven in [KommHotel](https://github.com/GregHdezQroMx/KommHotel).
