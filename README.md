## How to compile the plugin

1. Clone the repository:
   ```bash
   git clone https://github.com/groundbreakingmc/KidayPisun.git
   ```

2. Run the command (Enter the command in the console and press ctrl+enter):
    - **With shaded MyLib**:
      ```
      mvn clean package -P kidaypisun-mylib
      ```
    - **Without MyLib**:
      ```
      mvn clean package -P kidaypisun
      ```

After all, the plugin can be found in the target/kidaypisun-mylib or target/kidaypisun directory! 