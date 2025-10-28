# CounterPlusPlus
CS501 (Asign4-Q2) Counter++ that increments or decrements the count using buttons but also performs a background “auto-increment” operation every few seconds using a coroutine

## Feature
- Use ViewModel to manage the counter state.
- Implement StateFlow for unidirectional data flow.
- Add buttons for +1, -1, and Reset.
- Launch a coroutine that increments the counter every 3 seconds when “Auto” mode is toggled on.
- Display current count and status (“Auto mode: ON/OFF”).
- Add a settings screen to configure the auto-increment interval.

# Getting Started
### Prerequisites
- Android Studio (latest version recommended)

### How to Run
1. Clone the repository:
2. Open the project in Android Studio.

## Project Structure
- `MainActivity.kt`: Contains main Compose UI and state code.
- `ui.theme`: Contains app theme and styling.
