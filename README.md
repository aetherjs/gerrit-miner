# Usage

Firstly, navigate to gerrit-miner/ directory and build the project using: 

    ./gradlew build 

or otherwise from your favourite IDE. After the project is built, run the following command:

    ./gradlew run --args='arg1 arg2 arg3 arg4'
    
Where:

- ___Arg1___ is the target Gerrit repository base URL (For instance, https://git.eclipse.org/r/).
- ___Arg2___ is a convenient nickname of the Gerrit repository in question. For instance, if second argument is set to _"opendev"_, the results will be stored in a file named _opendev2020-08-25T23:41:47_ - with the standard ISO-8601 timestamp at the end.
- ___Arg3___ is the project name, for which you wish to mine review history. For instance, 'platform/eclipse.platform.ui' in the Eclipse ecosystem.
- ___Arg4___ is the maximum limit of reviews you want to include in the dataset.

For example, to mine 3000 reviews from the Eclipse project eclipse.platform.ui you must run:

    ./gradlew run --args='https://git.eclipse.org/r/ eclipse-platform-ui platform/eclipse.platform.ui 3000'
    
The resulting output will be saved to output/ directory as 'eclipse-platform-ui-timestamp', where timestamp is the currect timestamp in the aforementioned format. Please refer to ecosystems Gerrit web repositories list for precise names to be used for projects!