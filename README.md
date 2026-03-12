# Mowi App - Smart Corporate Logistics (KMP/CMP)

This project is a **functional MVP** for the Mowi application, built using **Kotlin Multiplatform (KMP)** and **Compose Multiplatform (CMP)**. It implements a smart trip planning flow designed to showcase the technical superiority of a shared native architecture over hybrid solutions like React Native.

## 🏗️ Architecture
The project follows a **Modular Clean Architecture with MVVM** pattern organized by **Feature-Responsibility**:

### Shared Module (`:shared`)
- **`features/mobility/domain/`**: Pure Kotlin entities (`TripModels`), Route definitions, and repository interfaces.
- **`features/mobility/data/`**: Repository implementations, including `remote` (Ktor Client) and mock data logic.
- **`features/mobility/presentation/viewmodel/`**: Platform-agnostic ViewModels using `StateFlow` and `viewModelScope`.
- **`di/`**: Koin module definitions for cross-platform dependency injection.

### Compose App (`:composeApp`)
- **`features/mobility/presentation/ui/`**: Feature-specific UI logic and screen components.
    - `components/`: Reusable UI elements (Maps, Cards, Drawer).
    - `HomeScreen.kt`, `ActiveTripScreen.kt`, `TripBookingScreen.kt`, etc.
- **`commonMain/composeResources/`**: Multi-language support (ES/EN) and platform-agnostic assets.
- **`androidMain/` / `iosMain/`**: Platform-specific entry points and native configurations.

### Server (`:server`)
- **Ktor Backend**: Simulates the corporate logistics API and business orchestration.

## ✨ Key Features (Implemented)
- **Premium Navigation Drawer:** Profile management and corporate contact integration.
- **Real-time Trip Simulation:** 1s = 1min time scaling with Manhattan-style map routing.
- **Smart AI Optimization:** Real-time feedback on cost savings and route efficiency.
- **Full i18n Support:** Professional localized content in Spanish and English.
- **Type-safe Navigation:** Built on Navigation 3 (alpha06) for robust multiplatform routing.

## 🛠️ Tech Stack
- **Compose Multiplatform:** 100% native shared UI.
- **Koin:** Multiplatform dependency injection.
- **Navigation 3:** Next-generation type-safe navigation.
- **Kotlinx Coroutines & Flow:** Reactive state management.
- **Ktor:** Multiplatform network communication.

## 🚀 Getting Started
### Android
```
./gradlew :composeApp:assembleDebug
```
### iOS
Open the `iosApp` directory in Xcode.

---
Developed by **GregHdezQroMx** based on the architecture proven in [KommHotel](https://github.com/GregHdezQroMx/KommHotel).
