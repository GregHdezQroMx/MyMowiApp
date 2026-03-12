# Mowi App - Roadmap de Migración KMP/CMP

## 🚀 Fase 1: Arquitectura y Lógica Compartida (Completado)
- [x] **Configuración Multiplataforma:** Sincronización de AGP, Kotlin y Compose Multiplatform.
- [x] **Dominio y Modelos:** Implementación de modelos tipados para Viajes y Finanzas (`TripModels`).
- [x] **Repositorio Mock:** Simulación de API de Logística con soporte para Ktor.
- [x] **ViewModel Multiplataforma:** Lógica de negocio compartida con `StateFlow` y `viewModelScope`.

## 🎨 Fase 2: UI Nativa Multiplataforma (Completado)
- [x] **Selector de Destinos:** Componente de selección corporativa nativa.
- [x] **Resumen Financiero:** Componente de visualización de costos corporativos (Logística).
- [x] **Optimización por IA:** Feedback visual de ahorro inteligente de Mowi.
- [x] **Navegación Avanzada:** Integración de Navigation 3 (JetBrains alpha06) con tipos seguros.
- [x] **Refactor de Arquitectura UI:** Descomposición de componentes bajo el patrón Feature-Responsibility.

## 🔗 Fase 3: Integración de Servicios (En Progreso)
- [ ] **Ktor Remote:** Sustituir mocks por llamadas a la API real de Mowi.
- [x] **Sincronización de Entorno:** Actualización a SDK 36, AGP 8.11.2 y Kotlin 2.3.0 (Estándar KommonHotel).
- [x] **Internacionalización (i18n):** Soporte completo para Español e Inglés con recursos indexados.
- [ ] **SQLDelight:** Persistencia local para historial de viajes (Offline-first).
- [ ] **Google Maps CMP:** Integración nativa de mapas real.

---
**Senior Developer:** GregHdezQroMx  
**Objetivo:** Demostrar el rendimiento nativo de CMP sobre React Native para Logística Corporativa mediante una arquitectura modular y escalable.
