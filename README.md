Selenium Tests
==============

Drivers are in directory `drivers`

### What should I know?

- To run any unit tests that test your Selenium framework you just need to ensure that all unit test file names end, or start with "test" and they will be run as part of the build.
- The maven failsafe plugin has been used to create a profile with the id "selenium-tests".  This is active by default, but if you want to perform a build without running your selenium tests you can disable it using:

        mvn clean verify -Pselenium-tests
        
- The maven-failsafe-plugin will pick up any files that end in ```*IT.java``` by default.  You can customise this is you would prefer to use a custom identifier for your Selenium tests.

### Anything else?

If you want to configure OS or BROWSER do it in pom.xml -> os/browser variables

Yes you can specify which browser to use by using one of the following switches:

- -Dbrowser=firefox
- -Dbrowser=chrome

If you want to toggle the use of chrome or firefox in headless mode set the headless flag (by default the headless flag is set to true)

- -Dheadless=true
- -Dheadless=false

You can specify a grid to connect to where you can choose your browser, browser version and platform:

- -Dremote=true 
- -DseleniumGridURL=http://{username}:{accessKey}@ondemand.saucelabs.com:80/wd/hub 
- -Dplatform=xp 
- -Dbrowser=firefox 
- -DbrowserVersion=44

You can even specify multiple threads (you can do it on a grid as well!):

- -Dthreads=2

You can also specify a proxy to use

- -DproxyEnabled=true
- -DproxyHost=localhost
- -DproxyPort=8080
- -DproxyUsername=fred
- -DproxyPassword=Password123

If the tests fail screenshots will be saved in ${project.basedir}/target/screenshots

If you need to force a binary overwrite you can do:

- -Doverwrite.binaries=true