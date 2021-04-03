# bot-linkedin

## Important
1. To avoid spam requests, the default value of `SEND_CONNECTION_REQUEST` is `false`
which only displays the data of followers in the console. To really send a connection request
set `SEND_CONNECTION_REQUEST = true` in `LinkedInFollowersBot.java` class.
   

2. Set `HIDE_BROWSER = true` in  `LinkedInFollowersBot.java` to hide the browser during execution. Default value is `false`

## Steps to use the linkedin-bot
1. run the `build.gradle` file as it will install all the required dependencies
2. Create a `.env` file in `src` directory. i.e `src/.env`
    - Add your LinkedIn credentials in the `.env` file
    ```text
    EMAIL=yourname@example.com
    PASSWORD=yourpassword
    ```
3. Run the `LinkedInFollowersBot.java` Class
4. Enter the query in the console. Ex.
    ```text
    Enter the Query
    People -> [Software Engineer, SDE] -> [Google] -> [Bangalore]
    ```
   Currently, Supported Companies are
   - Google
   - Amazon
   - Microsoft
   - BrowserStack
   - Dream11
   
   Currently, Supported Locations are
   - Mumbai
   - Pune
   - Bangalore


5. Enter the number of followers Ex.
    ```text
    Enter number of Followers:
    28
    ```
   

6. After successful execution, The summary will be displayed in a tabular format in the console

7. A copy of results will be saved in a `.xlsx` file in `Saved_Results` in the project directory


## How to use QueryBuilder Effectively
You need to provide a query in this sequence
```
Entity -> Keywords -> Companies -> Locations
```
Where
```
First Parameter: Entity (String)
Second Paramater: Keywords (List)
Third Parameter: Companies (List)
Fourth Parameter: Locations (List)
```

Example 1:
```
People -> [Recruiter, Talent Acquistition] -> [Google, Microsoft] -> [Bangalore, Pune]
```

Example 2:
```
People -> [Software Engineer, SDE] -> [Microsoft, Amazon, Google] -> [Bangalore, Pune]
```

Then input the number of followers

Example
```30```

Sit Back And Relax!

## Libraries / Tools Used
1. io.github.cdimascio:dotenv-java:2.2.0 (https://github.com/cdimascio/dotenv-java)
2. Selenium Standalone Server (https://selenium-release.storage.googleapis.com/3.141/selenium-server-standalone-3.141.59.jar)
3. Selenium Java Client (https://selenium-release.storage.googleapis.com/4.0-beta-1/selenium-java-4.0.0-beta-1.zip)
4. Selenium WebDriver
    - ChromeDriver (https://chromedriver.storage.googleapis.com/index.html?path=88.0.4324.96/)
    
# Note
This project is still in scope for new changes, feel free to create a pull request incase if you have found a better way to handle things in this script.
