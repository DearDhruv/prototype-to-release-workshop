# Architecture Decision Records (ADRs)

Project: **PTR: Prototype to Production** (`com.deardhruv.ptr`)

---

## ADR-001: Adoption of Jetpack Navigation 3 (`NavKey` + `NavDisplay`)

### Status
Accepted

### Context
Traditional Android navigation relied on XML navigation graphs or Jetpack Navigation 2 string-based routes and `NavController`. Navigation 2 introduces runtime parsing overhead, reflection risks under R8 minification, and complex backstack synchronizations.

### Decision
Adopt **Jetpack Navigation 3** (`androidx.navigation3:navigation3-runtime` and `androidx.navigation3:navigation3-ui`):
1. All destination keys are strongly typed `@Serializable` data classes or data objects implementing `NavKey`.
2. Backstack state is explicitly owned and remembered via `rememberNavBackStack()`.
3. Navigation routing is declared via the declarative `entryProvider` DSL and rendered using `NavDisplay`.
4. Predictive back animations and system back handling are built-in and testable.

### Consequences
- Compile-time type safety across all route arguments.
- Zero reflection-based string parsing for routes.
- Full compatibility with R8 full-mode shrinking when accompanied by appropriate Kotlinx Serialization keep rules.
- Legacy Navigation 2 classes (`NavController`, `NavHost`, `composable("route/{id}")`) are strictly forbidden.

---

## ADR-002: Single-Module Clean Architecture

### Status
Accepted

### Context
The workshop requires rapid, deterministic prototyping and release within a hard 55-minute live build budget using AI Studio's free tier. Multi-module Gradle setups increase Gradle sync latency, configuration complexity, and token context consumption during AI code generation.

### Decision
Structure the application as a single Gradle module (`:app`) organized cleanly by feature and layer under `com.deardhruv.ptr`:
- `com.deardhruv.ptr.ui`: Jetpack Compose screens, components, theme, and UI state models.
- `com.deardhruv.ptr.domain`: Pure Kotlin domain entities and business rules (if applicable).
- `com.deardhruv.ptr.data`: Local repositories, DataStore preferences, and data sources.
- `com.deardhruv.ptr.navigation`: Navigation 3 destinations, `NavKey` declarations, and entry providers.

### Consequences
- Maximizes AI Studio context density and token efficiency.
- Simplifies dependency resolution and reduces Gradle build times.
- Clean package boundaries make future modularization straightforward if the project expands.

---

## ADR-003: Offline-First Local Persistence & Deterministic Seed Data

### Status
Accepted

### Context
Workshop attendees install the `.aab` / `.apk` directly from Google Play Internal Testing. Requiring cloud backend logins, complex authentication, or network connectivity risks workshop failure during stage demos.

### Decision
1. Implement local-only offline persistence using Jetpack DataStore Preferences (or Room).
2. Seed the database/datastore with deterministic, realistic sample data on first launch so the UI is immediately populated and responsive.
3. Completely avoid mandatory sign-up screens, third-party backend servers, or analytics pings.

### Consequences
- Zero external point of failure during live demos.
- Complies out-of-the-box with Google Play Data Safety policies (no user data collected or shared).
- Instant, delightful user experience for attendees.

---

## ADR-004: R8 Full-Mode Shrinking & Environment-Driven Signing

### Status
Accepted

### Context
Google Play Console requires production release bundles (`.aab`) to be cryptographically signed, optimized, and compliant with size and security recommendations. Hardcoding keystore passwords or keys in source control poses severe security hazards.

### Decision
1. Enable R8 full-mode code shrinking and resource shrinking (`isMinifyEnabled = true`, `isShrinkResources = true`) on the `release` build type from day zero.
2. Maintain explicit ProGuard / R8 keep rules for Navigation 3 and `kotlinx.serialization` models.
3. Configure release signing dynamically: read credentials from `Project/keystore.properties` if present, falling back to environment variables (`PLAY_STORE_KEYSTORE_PATH`, `PLAY_STORE_KEYSTORE_PASSWORD`, `PLAY_STORE_KEY_ALIAS`, `PLAY_STORE_KEY_PASSWORD`).
4. Only attach the release `signingConfig` if the keystore file exists on disk, ensuring debug and CI builds never fail due to missing release credentials.

### Consequences
- Releases are hardened, minified, and verified against obfuscation issues before reaching production.
- Keystore credentials remain strictly out of Git history.
- CI/CD and developer local environments can seamlessly build both debug and signed release variants without friction.
