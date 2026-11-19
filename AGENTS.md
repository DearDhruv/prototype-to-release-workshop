# AGENTS.md — Global Rules & AI Studio Execution Contract

> **Contract Authority:** This document defines mandatory technical constraints and execution guidelines for Google AI Studio, Gemini CLI, and automated coding agents operating in this repository (`com.deardhruv.ptr`).

---

## 1. Mandatory Prerequisites Before Code Generation
Before generating, refactoring, or modifying any code in this repository, you **MUST** read and adhere to:
1. `AGENTS.md` (this file — execution contract & scope guardrails)
2. `DECISIONS.md` (Architecture Decision Records ADR-001 through ADR-004)
3. `SPEC.md` (active feature specification and acceptance criteria)
4. `.agents/skills/` (official Android guidelines for Navigation 3, edge-to-edge, Compose performance, and Play policy compliance)

**Repository layout:** The Android Gradle project lives under `Project/` (`Project/settings.gradle.kts`, `Project/app/`, `Project/gradlew`). All source changes go under `Project/app/src/main/java/com/deardhruv/ptr/`; run Gradle from `Project/`. Governance files (`AGENTS.md`, `DECISIONS.md`, `SPEC.md`, `PRIVACY.md`) and `.agents/skills/` stay in the repository root.

---

## 2. Locked Architecture & Mandatory Tech Stack
- **Application Package / Namespace:** `com.deardhruv.ptr` (strictly enforce across all files and manifests).
- **Language & Runtime:** 100% Kotlin 2.x, Kotlin Coroutines, Kotlinx Serialization.
- **UI Toolkit:** Jetpack Compose with Material 3 (`androidx.compose.material3`).
- **Navigation (Navigation 3 ONLY):**
  - **Strict Prohibition:** Do NOT use legacy `NavHost`, `NavController`, XML navigation, or string-based Navigation 2 routes.
  - **Allowed API:** Jetpack Navigation 3 (`androidx.navigation3`).
  - Destinations MUST be `@Serializable` classes or data objects implementing `NavKey`.
  - Backstack managed via `rememberNavBackStack`.
  - Screens rendered using `NavDisplay` and `entryProvider { entry<NavKey> { ... } }`.
  - Predictive back gestures supported natively via `onBack` handler.
- **Edge-to-Edge & System Insets:**
  - Call `enableEdgeToEdge()` in `MainActivity.onCreate()`.
  - Apply proper `WindowInsets` handling (e.g., `WindowInsets.safeDrawing`, `Modifier.safeDrawingPadding()`, or `Scaffold` inner padding) to avoid UI clipping under the status bar or navigation bar.
- **State Management & Architecture:**
  - Architecture: MVVM with Unidirectional Data Flow (UDF).
  - State exposure: `StateFlow<UiState>` inside `ViewModel`.
  - UI State modeling: Sealed interface/class `UiState` representing explicit states (`Loading`, `Empty`, `Success`, `Error`).
- **Local Persistence:**
  - Jetpack DataStore Preferences via the prebuilt `JsonStore` (see §2.1). Do not add Room during the live build.

### 2.1 Prebuilt Skeleton — USE these, never re-implement them
The repository ships a compiling skeleton so the live build only writes feature code. All paths are under `Project/app/src/main/java/com/deardhruv/ptr/`.

| Need | Use | File |
| :--- | :--- | :--- |
| Add a screen/destination | `@Serializable` key in `AppRoute`, register with `entry<Key> { }` | `navigation/AppRoute.kt`, `navigation/AppNavigation.kt` |
| Navigate / go back | `backStack.add(Route(id))` / `backStack.removeLastOrNull()` (pass lambdas into screens) | `navigation/AppNavigation.kt` |
| Screen shell (insets, top bar, back arrow, FAB, snackbar) | `PtrScreen(title, onBack, largeTitle, actions, floatingActionButton)` | `ui/components/PtrScreen.kt` |
| List padding under bars | `LazyColumn(contentPadding = padding + 16.dp)` | `ui/components/PtrScreen.kt` |
| Loading / Empty / Success / Error | `UiState<T>` + `StateContent(state, onRetry) { data -> ... }` | `ui/common/UiState.kt`, `ui/components/StateContent.kt` |
| Flow → UI state in a ViewModel | `repo.items.asUiState().stateInUi(viewModelScope)` | `ui/common/UiState.kt` |
| Persist `@Serializable` data offline | `JsonStore(dataStore, json, "key", ListSerializer(Model.serializer()), default = SEED)` with `.data` / `.update { }` | `data/local/JsonStore.kt` |
| Dependencies (repositories) | Add `by lazy` properties to `AppContainer` | `di/AppContainer.kt` |
| Create a ViewModel | `val vm = ptrViewModel { MyViewModel(myRepository) }` (scoped to the nav entry) | `di/AppContainer.kt` |
| Icons | `Icons.Default.*`, `Icons.Outlined.*`, `Icons.AutoMirrored.Filled.*` from `material-icons-core` only. Extended icons are NOT available. | — |
| Colors, type, shapes | `MaterialTheme.colorScheme / typography / shapes` only — never hardcode colors or fonts | `ui/theme/` |

Placement: models in `domain/`, repositories in `data/`, one package per feature under `ui/<feature>/` (`XScreen.kt` + `XViewModel.kt`). Replace the placeholder `ui/home/HomeScreen.kt` with the primary screen. Do not modify `ui/theme/`, `navigation/AppNavigation.kt` transitions, `PtrScreen`, `StateContent`, `JsonStore`, or Gradle files.

---

## 3. Free-Tier & 55-Minute MVP Scope Guardrails

### 3.1 Hard 55-Minute Delivery Budget (Non-Negotiable)
The task given on workshop day **MUST be fully finished within 55 minutes** of being handed over: from problem framing to a compiled, R8-hardened release `.aab` ready for Play Console Internal Testing. Treat the budget as a hard deadline, not a target.

| Step | Budget | Done when |
| :--- | :--- | :--- |
| Problem framing → `SPEC.md` + `BUILD_PROMPT` | 8 min | Spec locked to one primary flow |
| Prompt 1: Core feature + Navigation 3 | 15 min | App compiles and runs the primary flow |
| Prompt 2: Hardening + version bump | 12 min | Hardening checklist applied, `versionCode` bumped |
| Local verify + signed `bundleRelease` | 15 min | `Project/app/build/outputs/bundle/release/app-release.aab` builds |
| GitHub push + Play Console rollout | 5 min | Release uploaded, opt-in link shared |
| **Total** | **55 min** | |

Rules for agents operating under this budget:
- **Ship in at most 2 generation passes.** Batch all work for a step into a single response. Never propose follow-up iterations, optional extras, or "next steps" that require additional prompts.
- **Prefer the smallest change that satisfies `SPEC.md`.** Choose the simplest working implementation over the most elegant one; build on the prebuilt skeleton (§2.1) instead of writing new infrastructure.
- **Compile-first.** Every response must leave the project compiling (`cd Project && ./gradlew assembleDebug`). A smaller feature that builds beats a larger one that does not.
- **Descope, don't overrun.** If the requested scope cannot be completed within the remaining budget, cut it to the single primary flow, implement that fully, and list what was cut in `SPEC.md` under "Deferred". Never leave a half-implemented screen.
- **No exploratory detours.** Do not add dependencies, refactor unrelated code, or upgrade Gradle/AGP/library versions during the live build.
- **Strict Spec Adherence:** Implement **ONLY** what is explicitly specified in `SPEC.md`. Do not invent auxiliary features, multi-tab drawer layouts, or complex settings panels unless requested.
- **Compact Surface Area:** Confine UI changes to 1–2 focused screens (Primary List/Action Screen + Detail/Result Screen).
- **Deterministic Seed Data:** Provide realistic initial seed data so the application is completely functional, interactive, and visually populated immediately upon first launch.
- **No Third-Party SDK Bloat:** Do not introduce unrequested external dependencies, network backends, analytics frameworks, or advertising SDKs.

---

## 4. Production Hardening & Google Play Compliance Rules
- **No Hardcoded Secrets:** Never hardcode API keys, signing passwords, tokens, or personal identifiers in source code or resources.
- **Compose Performance & Stability:**
  - Mark data classes representing UI state or models with `@Immutable` or `@Stable`.
  - Provide explicit, stable `key` expressions in `LazyColumn`, `LazyRow`, and list items.
  - Hoist state and event callbacks to top-level composables.
- **Graceful State Handling:** Every user-facing screen must handle Loading, Empty, Success, and Error states without crashing or blank screens.
- **R8 / ProGuard Compatibility:** Keep routes and serialization models compatible with R8 full-mode shrinking. Ensure keep rules in `proguard-rules.pro` remain valid.
