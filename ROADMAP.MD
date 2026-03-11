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
- [x] **Patrón Preview Multiplataforma:** Soporte nativo para @Preview en Android sin romper build de iOS.

## 🔗 Fase 3: Integración de Servicios (Próximamente)
- [ ] **Ktor Remote:** Sustituir mocks por llamadas a la API real de Mowi.
- [ ] **SQLDelight:** Persistencia local para historial de viajes (Offline-first).
- [ ] **Google Maps CMP:** Integración nativa de mapas en el flujo de reserva.

---
**Senior Developer:** GregHdezQroMx  
**Objetivo:** Demostrar el rendimiento nativo de CMP sobre React Native para Logística Corporativa.
