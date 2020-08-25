# Usage

Firstly, build the project using ./gradlew build or otherwise from your favourite IDE. Navigate to gerrit-miner/ directory and run the following command:

    ./gradlew run --args='arg1 arg2 arg3'
    
Where:

- ___Arg1___ is the target Gerrit repository base URL (For instance, https://review.opendev.org).
- ___Arg2___ is a convenient nickname of the Gerrit repository in question. For instance, if second argument is set to _"opendev"_, the results will be stored in a file named _opendev2020-08-25T23:41:47_ - with the standard ISO-8601 timestamp at the end.
- ___Arg3___ is the number of reviews you want to include in the dataset.