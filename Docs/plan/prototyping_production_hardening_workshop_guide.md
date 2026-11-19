# Prototyping & Production-Hardening App — Complete Workshop Playbook

* **Session Title:** Prototyping & Production-Hardening App
* **Total Duration:** 100 Minutes (15 min Intro + 55 min hard task budget (Scoping → Build → Release) + 10 min Buffer + 20 min Live Q&A)
* **Application Package:** `com.deardhruv.ptr`
* **GitHub Repository:** `https://github.com/DearDhruv/prototype-to-release-workshop`
* **Core Stack:** Kotlin 2.x, Jetpack Compose, Material 3, Jetpack Navigation 3, ViewModel + StateFlow, DataStore/Room, R8 Full-Mode Shrinking

---

## 1. 100-Minute Run-of-Show

| Time Block | Duration | Phase | Core Objective & Gate |
| :--- | :--- | :--- | :--- |
| **Pre-Session** | Before Stage | **Phase 0: Environment & Repo Seed** | Run AGY prompt, add `PRIVACY.md`, push to GitHub, upload baseline `v1` `.aab` to Play Console, and import repo into AI Studio. |
| **00:00 – 00:15** | 15 min | **Phase 1: Entry, Intro & Audience Pulse** | Stage entry, framing the session, gauging audience Android/AI experience, and collecting attendee Gmail addresses via QR code. |
| **00:15 – 00:23** | 8 min | **Phase 2: Problem Framing & Prompt Prep** | Pick one audience problem, lock a 1-feature Core MVP scope, and use Gemini to generate `SPEC.md` and `BUILD_PROMPT`. |
| **00:23 – 01:05** | 42 min | **Phase 3: AI Studio Build & Hardening** | Generate the Kotlin/Compose/Navigation 3 app in 2 batched AI Studio prompts, pull locally, and build the signed release `.aab`. |
| **01:05 – 01:10** | 5 min | **Phase 4: GitHub Push & Play Store Rollout** | Sync final commits to GitHub, upload signed `v2` `.aab` to Play Console Internal Testing, and share the install QR/link. **Hard stop: the workshop task must be finished here — 55 min after handover at 00:15.** |
| **01:10 – 01:20** | 10 min | **Buffer** | Reserved for overruns only, never planned work. If unused, start Q&A early. |
| **01:20 – 01:40** | 20 min | **Phase 5: Live Q&A & Teardown** | Walk through `AGENTS.md`, `DECISIONS.md`, Navigation 3 code, and live installs on attendee phones. |

---

## 2. Phase 0: Pre-Workshop Setup (Before Going on Stage)

### Step 2.1: Run This Setup Prompt in AGY
Open your local Android project workspace in **AGY** and paste this entire prompt to configure `com.deardhruv.ptr`, Jetpack Navigation 3, Play Store R8/signing configs, Android Skills, and AI Studio context files:

```text
You are preparing this Android repository for a live 100-minute technical workshop titled "Prototyping & Production-Hardening App" (with a strict 55-minute live build window on Google AI Studio's free tier, followed by a Google Play Store Internal Testing release).

Execute every task below in this workspace. Do NOT initialize Git remotes, configure Git credentials, or push to GitHub—I will connect and push to Git manually after you finish.

### 1. Lock Package Name (`com.deardhruv.ptr`) & Latest Android Stack
- Set `namespace = "com.deardhruv.ptr"` and `applicationId = "com.deardhruv.ptr"` in `app/build.gradle.kts`.
- Refactor/create all Kotlin source directories under `app/src/main/java/com/deardhruv/ptr/` (including `ui/`, `domain/`, `data/`, and `navigation/`), update package declarations and imports across all `.kt` files, and verify `AndroidManifest.xml` resolves `.MainActivity` cleanly under `com.deardhruv.ptr`.
- Inspect `settings.gradle.kts`, `gradle/libs.versions.toml`, `build.gradle.kts`, and `app/build.gradle.kts`, and configure Google's latest stable Android stack:
  - Kotlin 2.x with Compose Compiler plugin and `org.jetbrains.kotlin.plugin.serialization`.
  - Jetpack Compose BOM, Material 3 (`material3`), Lifecycle ViewModel Compose (`lifecycle-viewmodel-compose`, `lifecycle-runtime-compose`), and Kotlin Coroutines.
  - **Jetpack Navigation 3**: Add `androidx.navigation3:navigation3-runtime` and `androidx.navigation3:navigation3-ui` along with `org.jetbrains.kotlinx:kotlinx-serialization-json` in `gradle/libs.versions.toml` and `app/build.gradle.kts`.
  - Local persistence ready for offline-first MVPs (`androidx.datastore:datastore-preferences` or Room).
- Ensure `compileSdk` and `targetSdk` are aligned with the latest stable Android SDK configured in the project, and `minSdk` is at least `26`.

### 2. Configure Play Store Production Hardening & Release Signing (`.aab`)
- Update `app/build.gradle.kts` `release` build type so it is Google Play Store ready:
  - Set `isMinifyEnabled = true` and `isShrinkResources = true`.
  - Attach `getDefaultProguardFile("proguard-android-optimize.txt")` and `"proguard-rules.pro"`.
- Configure `signingConfigs.create("release")` safely without hardcoding any secrets:
  - Load `storeFile`, `storePassword`, `keyAlias`, and `keyPassword` from a root `keystore.properties` file if present, falling back to environment variables (`PLAY_STORE_KEYSTORE_PATH`, `PLAY_STORE_KEYSTORE_PASSWORD`, `PLAY_STORE_KEY_ALIAS`, `PLAY_STORE_KEY_PASSWORD`), and only assign `signingConfig = signingConfigs.getByName("release")` when the keystore file exists so debug/CI builds never break before the keystore is placed.
  - Create a `keystore.properties.template` file showing the 4 required keys, and ensure `keystore.properties`, `*.jks`, and `*.keystore` are listed in `.gitignore`.
- Update `app/proguard-rules.pro` with explicit keep rules for:
  - Jetpack Navigation 3 `@Serializable` `NavKey` destinations and `kotlinx.serialization` serializers/companions inside `com.deardhruv.ptr.**` so R8 minification does not break route serialization.

### 3. Create AI Studio Context & Governance Files in Repo Root
Create three clean, high-signal Markdown files in the repository root so Google AI Studio reads and enforces them automatically alongside every user prompt:

1. **`AGENTS.md`** (Global Rules & AI Studio Execution Contract):
   - Instruct AI Studio to read `AGENTS.md`, `DECISIONS.md`, `SPEC.md`, and `.agents/skills/` before generating or modifying any code.
   - Lock the base package `com.deardhruv.ptr` and mandatory tech stack: 100% Kotlin, Jetpack Compose, Material 3, **Jetpack Navigation 3 ONLY** (`@Serializable` interfaces/objects implementing `NavKey`, `rememberNavBackStack`, `NavDisplay`, and `entryProvider`—strictly forbid legacy `NavHost` / string-route Navigation 2 APIs), MVVM with Unidirectional Data Flow (`StateFlow` + sealed `UiState`), and `enableEdgeToEdge()` with proper `WindowInsets` (`safeDrawing`) on every screen.
   - Enforce Free-Tier & 55-Minute MVP Scope Guardrails (hard 55-minute delivery budget): implement ONLY what is defined in `SPEC.md`, keep changes atomic across 1–2 core screens, include realistic deterministic sample seed data so the app works immediately on launch, and never add unrequested third-party SDKs or network backends unless specified in `SPEC.md`.
   - Enforce Production & Play Policy Rules: never hardcode API keys or secrets, mark Compose state/UI models `@Immutable` or `@Stable`, provide stable `key` lambdas in `LazyColumn`/`LazyRow`, handle empty/loading/error states gracefully, and maintain R8 compatibility.

2. **`DECISIONS.md`** (Architecture Decision Records):
   - Document **ADR-001**: Adoption of Jetpack Navigation 3 (`NavKey` + `NavDisplay`) for compile-time type-safe backstack ownership and predictive back gestures.
   - Document **ADR-002**: Single-module clean package architecture (`com.deardhruv.ptr.ui`, `.domain`, `.data`, `.navigation`) to maximize AI Studio iteration speed and keep token context compact during the 55-minute live build.
   - Document **ADR-003**: Offline-first local persistence + deterministic seed data so attendees installing `com.deardhruv.ptr` from Play Store Internal Testing get an instant, zero-login experience.
   - Document **ADR-004**: R8 full-mode shrinking (`isMinifyEnabled = true`, `isShrinkResources = true`) and environment-driven `.aab` signing from day zero.

3. **`SPEC.md`** (Live Workshop Feature Specification Template):
   - Create a structured template ready to be filled during the live session with sections:
     - `## 1. Problem Statement & Target User`
     - `## 2. Core MVP Solution (Single Primary Flow)`
     - `## 3. Navigation 3 Destinations (NavKey Graph)`
     - `## 4. Data Model & Local Persistence`
     - `## 5. Screen States (Loading, Empty, Success, Error)`
     - `## 6. Play Store Hardening & Acceptance Checklist`

### 4. Install Official Android Skills (`.agents/skills/`)
- Run the Android Skills CLI in the project root to install the four essential workshop skills locally into `.agents/skills/`:
  `android skills add --project=. navigation-3 edge-to-edge optimize-compose-performance audit-play-policy-compliance`
- If the `android` CLI is not installed in this environment, create `.agents/skills/` with concise, actionable `SKILL.md` reference files for `navigation-3`, `edge-to-edge`, `optimize-compose-performance`, and `audit-play-policy-compliance` covering their core rules, and print the exact CLI command for me to run later.

### 5. Scaffold Compiling Baseline & Verify Build
- Update `com.deardhruv.ptr.MainActivity` and the theme/navigation package with a minimal, clean compiling baseline that uses `enableEdgeToEdge()`, Material 3 theme, and a working Navigation 3 `NavDisplay` shell displaying a placeholder "PTR Workshop Ready" screen.
- Run `./gradlew assembleDebug` in the terminal to verify all Gradle dependencies, Kotlin serialization plugins, Navigation 3 imports, and Compose compiler settings resolve and compile with zero errors.
- Summarize every file created/modified and verify that no Git remote or push actions were executed.
```

---

### Step 2.2: Create `PRIVACY.md` in the Repository Root
Save this file as `PRIVACY.md` in the root of `prototype-to-release-workshop` so Google Play Console validates your Privacy Policy URL:

```markdown
# Privacy Policy for PTR: Prototype to Production (`com.deardhruv.ptr`)

**Effective Date:** November 2026  
**Developer:** DearDhruv  
**Source Repository:** https://github.com/DearDhruv/prototype-to-release-workshop

## 1. Overview
**PTR: Prototype to Production** (`com.deardhruv.ptr`) is an open-source educational Android application built during the *Prototyping & Production-Hardening App* live workshop.

## 2. Data Collection and Storage
- **No Personal Data Collected:** This application does not collect, transmit, sell, or share personal information, device identifiers, location data, or usage analytics.
- **Local-Only Storage:** Any entries or preferences created inside the app are stored locally on your device using Android local storage (Jetpack DataStore / Room) and are removed when you clear app storage or uninstall the application.
- **No Third-Party Tracking:** The release bundle contains no third-party advertising SDKs or behavioral analytics trackers.

## 3. Contact
For questions regarding this application or workshop repository, open an issue at https://github.com/DearDhruv/prototype-to-release-workshop/issues or contact the developer via the email listed on the Google Play Store listing.
```

---

### Step 2.3: Connect & Push Workspace to GitHub
Copy `keystore.properties.template` to `keystore.properties` (with your real keystore details; verify it stays gitignored), then run:

```bash
# 1. Verify keystore.properties and *.jks files are ignored and untracked
git status

# 2. Initialize git and set branch to main
git init
git branch -M main

# 3. Connect your GitHub repository
git remote add origin https://github.com/DearDhruv/prototype-to-release-workshop.git

# 4. Stage and commit the baseline architecture, skills, and privacy policy
git add .
git commit -m "chore: bootstrap com.deardhruv.ptr baseline, AGENTS.md, Navigation 3, and Play Store hardening"

# 5. If GitHub already has an initial commit (README/LICENSE), rebase before pushing
git pull --rebase origin main
git push -u origin main
```

---

### Step 2.4: Google Play Console Store Listing & Compliance Copy

#### Main Store Listing (`Grow -> Store presence -> Main store listing`)
* **Package Name:** `com.deardhruv.ptr`
* **App Name (28 / 30 chars):**
  ```text
  PTR: Prototype to Production
  ```
* **Short Description (74 / 80 chars):**
  ```text
  Native Kotlin & Jetpack Compose MVP built live in the 100-minute workshop.
  ```
* **Full Description:**
  ```text
  PTR: Prototype to Production is the official live-built Android application created during the "Prototyping & Production-Hardening App" workshop.

  Built from scratch in under 55 minutes using AI-assisted workflows and modern Android architecture, this app demonstrates how to take an audience-selected problem from concept to a production-hardened Google Play release.

  WHAT THIS BUILD DEMONSTRATES
  • Live Core MVP: Solves a real-world daily workflow chosen and prioritized live by workshop attendees.
  • 100% Native Kotlin & Jetpack Compose: Declarative Material 3 user interface with full edge-to-edge system bar handling and adaptive layouts.
  • Jetpack Navigation 3: Type-safe, state-driven backstack management with predictive back support.
  • Offline-First Reliability: Instant local persistence with zero mandatory sign-up walls or external network dependencies.
  • Production-Hardened Release: Compiled with R8 full-mode code shrinking, resource optimization, and strict serialization keep rules.

  ABOUT THE WORKSHOP
  This release accompanies the "Prototyping & Production-Hardening App" session, showcasing deterministic AI code generation governed by repository-level architecture rules (AGENTS.md), Architecture Decision Records (DECISIONS.md), and official Android Compose/Navigation 3 skills.

  Note: This build is distributed via Google Play Internal Testing for workshop attendees and developer evaluation.
  ```

#### Store URLs, Assets & App Content Declarations
* **Website URL:** `https://github.com/DearDhruv/prototype-to-release-workshop`
* **Privacy Policy URL:** `https://github.com/DearDhruv/prototype-to-release-workshop/blob/main/PRIVACY.md`
* **Category / Pricing:** `Tools` (or `Education`) / `Free`
* **Graphic Assets:**
  * App Icon: `512 x 512 px` (32-bit PNG, max 1 MB)
  * Feature Graphic: `1024 x 500 px` (JPEG or 24-bit PNG, max 1 MB)
  * Phone Screenshots: At least `2` screenshots captured from your baseline build
* **App Content Declarations (`Policy and programs -> App content`):**
  * **Data Safety:** *No, this app does not collect or share any user data*
  * **Ads:** *No, my app does not contain ads*
  * **App Access:** *All functionality is available without special access*
  * **Target Audience:** *18 and over*
  * **Content Rating:** Complete IARC questionnaire (*Utility, Productivity, Communication, or Other* -> *No* to restricted content)

#### Pre-Stage Final Checklist
1. Build baseline `.aab` locally (`./gradlew clean bundleRelease`) and upload `versionCode = 1` to **Play Console -> Testing -> Internal testing** so your signing key and listing are pre-approved.
2. Create a 1-field **Google Form** collecting attendee Google Play Gmail addresses, generate a QR code for Slide 2, and keep the Play Console **Internal Testing -> Testers** tab open.
3. Open **[aistudio.google.com](https://aistudio.google.com/) -> Build -> Import from GitHub**, select **`DearDhruv/prototype-to-release-workshop`** (`main`), confirm two-way sync is active, and select a **Gemini Flash** model to preserve free-tier quota during the live 55-minute build.

---

## 3. Phase 1: Stage Entry, Intro & Audience Pulse (00:00 – 00:15)

1. **Stage Entry & Framing (3 min):** Introduce *"Prototyping & Production-Hardening App"*—moving beyond throwaway AI demos to an R8-hardened, Navigation 3 Android app published to Google Play.
2. **Audience Calibration (5 min):** Quick show of hands:
   * Daily Jetpack Compose developers?
   * Published an `.aab` to Google Play Console?
   * Used `AGENTS.md` + Android Skills in AI Studio / Gemini CLI?
3. **Tester Signup & Problem Selection (7 min):**
   * Project the Google Form QR code so attendees submit their Play Store emails (paste them into Play Console Internal Testing during Phase 2).
   * Collect 2–3 narrow, high-value app problems from the audience and vote on one 2-screen Core MVP.

---

## 4. Phase 2: Problem-to-Prompt Synthesis (00:15 – 00:23)

Run this **Meta-Prompt** in **Gemini Chat** (1 turn) to convert the winning audience problem into `SPEC.md` and `BUILD_PROMPT` before touching code:

```text
Act as a Principal Android Architect. We are building a production-hardened native Android MVP (`com.deardhruv.ptr`) within a hard 55-minute budget (scoping → signed release) during a live workshop.

Audience Problem & Chosen Solution:
[INSERT AUDIENCE PROBLEM & 1-SENTENCE SOLUTION HERE]

Constraints:
- Scope strictly to ONE core user flow (maximum 2 screens: Primary Screen + Detail/Result Screen) so it can be generated and tested in 2 prompts without exhausting free-tier API limits.
- Stack: Kotlin, Jetpack Compose, Material 3, Jetpack Navigation 3 (`NavKey`, `rememberNavBackStack`, `NavDisplay`, `entryProvider`), ViewModel + StateFlow, local persistence (DataStore or Room), and clean edge-to-edge insets.

Output two blocks ONLY:
1. `SPEC.md`: Crisp data model, screen states (`Loading`, `Empty`, `Success`, `Error`), and acceptance criteria.
2. `BUILD_PROMPT`: A single, self-contained technical prompt referencing our repo's `AGENTS.md`, `DECISIONS.md`, and `.agents/skills/navigation-3` to generate the full working feature.
```

---

## 5. Phase 3: AI Studio Build & Production Hardening (00:23 – 01:05)

### Prompt 1: Core Feature & Navigation 3 Implementation (15 min — AI Studio Build Mode)
Paste the generated `BUILD_PROMPT` (or use this structured prompt) into **AI Studio Build mode** (which already has `DearDhruv/prototype-to-release-workshop` imported):

```text
Read `AGENTS.md`, `DECISIONS.md`, and `.agents/skills/navigation-3` in this repository before writing code. Keep package namespace `com.deardhruv.ptr`.

Implement the Core MVP defined below:
[PASTE GENERATED SPEC.md HERE]

Technical Requirements:
1. Navigation 3: Define `@Serializable` destination keys implementing `NavKey`, manage state via `rememberNavBackStack`, and render screens inside `NavDisplay` using an `entryProvider` with predictive back support.
2. UI & Edge-to-Edge: Use Material 3 components, `Scaffold` with proper `WindowInsets` (`safeDrawing`), and state hoisting (`UiState` sealed interface).
3. Production Hardening: Include input validation, error recovery states, zero hardcoded secrets, and deterministic sample seed data so the app works immediately on first launch.
4. Update `SPEC.md` in the repo with the implemented architecture.
```

---

### Prompt 2: Production Hardening & Version Bump (12 min — AI Studio Build Mode)
Once Prompt 1 compiles cleanly, run this single hardening pass:

```text
Audit the generated code against `.agents/skills/optimize-compose-performance`, `.agents/skills/edge-to-edge`, and `.agents/skills/audit-play-policy-compliance`:
1. Ensure all Compose UI models are `@Immutable` or `@Stable` and lazy lists use stable `key` lambdas to eliminate unnecessary recompositions.
2. Verify `enableEdgeToEdge()` in `MainActivity` and verify top/bottom system bar padding on every screen.
3. Verify ProGuard/R8 keep rules in `app/proguard-rules.pro` cover all `@Serializable` `NavKey` routes and data models under `com.deardhruv.ptr.**` so `isMinifyEnabled = true` does not strip serialization metadata.
4. Increment `versionCode` to `2` and `versionName` to `"1.1.0"` in `app/build.gradle.kts`, and fix any compiler warnings.
```

---

### Step 5.3: Local Verification & Signed `.aab` Build (15 min — Android Studio / Terminal)
1. In AI Studio Build mode, open **Settings -> GitHub** and click **Push to GitHub**.
2. Pull the AI-generated commits locally and compile the production release bundle:
   ```bash
   git pull origin main
   ./gradlew clean bundleRelease
   ```
3. Verify `app/build/outputs/bundle/release/app-release.aab` builds cleanly with R8 shrinking enabled and sanity-check on an emulator/device.

---

## 6. Phase 4: Final GitHub Sync & Play Store Rollout (01:05 – 01:10) — HARD STOP at 55 min

1. **Push any final local hardening tweaks to GitHub:**
   ```bash
   git add .
   git commit -m "chore: release v1.1.0 (versionCode 2) R8 bundle hardening"
   git push origin main
   ```
2. **Publish to Google Play Console Internal Testing:**
   * Open **Google Play Console -> Testing -> Internal testing -> Create new release**.
   * Upload `app/build/outputs/bundle/release/app-release.aab` (`versionCode 2`).
   * Add release notes (`Workshop Live Build: Core MVP + Navigation 3 + R8 Hardened`), click **Next -> Save and publish**.
   * Verify attendee emails are saved under **Testers**, copy the **Opt-in URL**, and display it on screen so attendees can install `com.deardhruv.ptr` live.

---

## 7. Phase 5: Live Q&A & Teardown (01:20 – 01:40)

* **Minutes 80–85:** Show the live GitHub commit history (`https://github.com/DearDhruv/prototype-to-release-workshop`) comparing AI Studio commits against local hardening commits, and highlight how `AGENTS.md` + `.agents/skills/navigation-3` enforced Navigation 3 and R8 safety.
* **Minutes 85–100:** Open Q&A while attendees test the Play Store Internal Testing build on their devices.