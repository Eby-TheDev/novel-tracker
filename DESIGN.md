# Design Document: Novel Tracker (Goal & Schedule App)

## 1. Objective
A mobile application for Android to track long-term goals (like reading a novel) and schedule daily tasks to achieve those goals.

## 2. Tech Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Native Android)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Local Database**: Room
- **Dependency Injection**: Hilt
- **Navigation**: Jetpack Navigation Component
- **Time/Date Handling**: java.time (API 26+)

## 3. Data Models

### Goal
- `id`: Long (Primary Key)
- `title`: String
- `currentProgress`: Int (e.g., current chapter)
- `totalProgress`: Int? (e.g., total chapters, optional)
- `dueDate`: Long? (Timestamp, optional)
- `createdAt`: Long

### TaskPlan
- `id`: Long (Primary Key)
- `goalId`: Long (Foreign Key to Goal)
- `startTime`: Long (Timestamp)
- `durationMinutes`: Int
- `isCompleted`: Boolean

## 4. UI/UX Design

### Main Screen (Bottom Navigation)
- **Dashboard Tab**:
    - List of `GoalCard` components.
    - `GoalCard` displays title, a progress bar (if `totalProgress` exists) or a simple count, and due date.
    - Floating Action Button (FAB) to "Add Goal".
- **Schedule Tab**:
    - A daily view of tasks.
    - Items showing "At [Time], do [Goal Title] for [Duration]".
    - FAB to "Add Plan".

### Add/Edit Dialogs
- **Add Goal**: Input title, optional total progress, optional due date picker.
- **Add Plan**: Select goal from dropdown, time picker for start time, duration input.

## 5. Implementation Phases

### Phase 1: Project Setup (Completed)
- Initialize Android project with Compose.
- Setup Hilt, Room, and Navigation.

### Phase 2: Data Layer (Completed)
- Define Room entities and DAOs.
- Create Repository for Goals and Plans.

### Phase 3: Dashboard Implementation (Completed)
- Create Goal ViewModel.
- Build Dashboard Screen and Goal Card.
- Implement "Add Goal" dialog.

### Phase 4: Schedule Implementation (Completed)
- Create Schedule ViewModel.
- Build Schedule Screen with daily task list.
- Implement "Add Plan" dialog.

### Phase 5: Polishing (Completed)
- Add animations for progress updates.
- Refine Material 3 styling.

## 6. Verification Plan
- Unit tests for DAOs and Repositories.
- UI tests for adding a goal and seeing it on the dashboard.
- Verification of progress calculation logic.
