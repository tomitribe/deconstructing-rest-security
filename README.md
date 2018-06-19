# deconstructing-rest-security

To build:

    mvn clean install $skip

Then to run the hashes it's:

    ./target/hashes all <file>

For example, to hash the README.md file:

    ./target/hashes all README.md
    
The `hashes` binary is self-contained and can be moved into a bin directory in your `PATH`.  I put mine in `~/bin/`, which is where I tend to put small tools and personals scripts.

