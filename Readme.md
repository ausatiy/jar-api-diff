## About
This tool compares API of two jar files and prints difference in api.
Supported cases:
* Added classes/methods
* Removed classes/methods
* Renamed classes. Note: this tools detects class names changes in method signatures, thus you should not get false positive signature changes of methods
* Reanmed methods (rename could be detected if list of attributes did not change, argument types did not change and two methods contain the same code (byte-code will be compared))
* Changes in class attributes (visibitily, abstract, final, e.t.c.)


## How to build
Run build.bat or build.sh

Note: this module contains two independent gradle builds because of some compatibility issues of Idea IDE and code generation (Immutables)

## Test data
Please find demo data in 'demoFrom' and 'demoTo' projects. 

## Usage
<program> <jar1 file> <jar2 file>