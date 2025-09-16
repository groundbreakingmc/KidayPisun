## How to compile the plugin

1. Clone the repository:
   ```bash
   git clone https://github.com/groundbreakingmc/KidayPisun.git
   ```

2. Navigate to the project directory:
   ```bash
   cd KidayPisun
   ```

1. Run the build command:
   - **Standard build** (requires MyLib plugin on server):
      ```bash
     ./gradlew build
      ```
   - **With embedded MyLib**:
      ```bash
     ./gradlew build -PshadeMyLib
      ```
   - **For RuSpigot** (changes plugin name to TowerBuilder):
      ```bash
     ./gradlew build -PruSpigotPlugin
      ```
   - **For RuSpigot with embedded MyLib**:
      ```bash
     ./gradlew build -PshadeMyLib -PruSpigotPlugin
      ```
---
The compiled plugin will be available in the build/libs/ directory.