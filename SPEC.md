# Live Workshop Feature Specification Template

> This document defines the live feature scope and acceptance criteria for **PTR: Prototype to Production** (`com.deardhruv.ptr`), created during the 100-minute workshop. Fill out this template with the audience-selected feature during Phase 2.

---

## 1. Problem Statement & Target User
- **Target User:** [Define target user persona, e.g., Workshop attendees tracking personal conference learnings]
- **Core Problem:** [Define the single acute problem to be solved and shipped within the 55-minute live build budget (see `AGENTS.md` §3.1)]
- **Value Proposition:** [1-sentence summary of why this native app solves the problem immediately]

---

## 2. Core MVP Solution (Single Primary Flow)
- **Primary Screen:** [Describe Main screen, e.g., List of items with quick add affordance]
- **Secondary / Detail Screen:** [Describe Detail screen, e.g., Detailed view / edit state]
- **Interaction Constraints:**
  - Strict 2-screen maximum.
  - Zero external cloud/API dependencies.
  - Interactive immediately upon launch with pre-seeded data.

---

## 3. Navigation 3 Destinations (NavKey Graph)
- **Destination Keys (`NavKey`):**
  ```kotlin
  @Serializable
  data object HomeDestination : NavKey

  @Serializable
  data class DetailDestination(val itemId: String) : NavKey
  ```
- **Backstack Behavior:**
  - Initial backstack: `rememberNavBackStack(HomeDestination)`
  - Forward navigation: `backStack.add(DetailDestination(id))`
  - Back navigation: `backStack.removeLastOrNull()` with predictive back gesture support

---

## 4. Data Model & Local Persistence
- **Core Entities:**
  ```kotlin
  @Immutable
  @Serializable
  data class Item(
      val id: String,
      val title: String,
      val description: String,
      val timestamp: Long,
      val isCompleted: Boolean = false
  )
  ```
- **Storage Strategy:** Jetpack DataStore Preferences (or Room) for offline-first persistence.
- **Initial Seed Data:** 3–5 deterministic, realistic items pre-loaded into local storage on first launch.

---

## 5. Screen States (Loading, Empty, Success, Error)
- **Loading State:** CircularProgressIndicator centered with descriptive loading label.
- **Empty State:** Visual icon + clear call-to-action ("No entries yet. Tap + to add one.").
- **Success State:** Populated `LazyColumn` with stable keys (`key = { it.id }`) and item click handlers.
- **Error State:** Accessible message banner / card with a "Retry" button.

---

## 6. Play Store Hardening & Acceptance Checklist
- [ ] Base package matches `com.deardhruv.ptr` everywhere.
- [ ] Application compiles with zero errors under `cd Project && ./gradlew assembleDebug`.
- [ ] Release bundle builds with R8 full-mode shrinking: `cd Project && ./gradlew clean bundleRelease`.
- [ ] Edge-to-edge system insets verified (status and navigation bars do not obscure UI or interactive buttons).
- [ ] Zero unhandled crashes on orientation change or rapid navigation.
- [ ] Privacy policy verified in `PRIVACY.md` with no undeclared data collection.

---

## 7. Deferred (Cut to Meet the 55-Minute Budget)
- [List any scope cut during the live build per `AGENTS.md` §3.1, or "None"]
