# ProgramAndStatement

## Objectives
1. Familiarity with writing a recursive-descent parser (for the BL language).
2. Familiarity with writing layered secondary methods (parse for Program and parse and parseBlock for Statement).
3. Familiarity with developing and using specification-based test plans.

## The Problem
The problem is to complete and carefully test an implementation of a recursive-descent parser for the BL language. This is a fundamental piece of the BL compiler. The parser must follow the context-free grammar (CFG) for the BL language.

As an example of the expected behavior of your parser and the kind of error messages it should generate, a program is available that parses a BL program and pretty prints it if it is syntactically valid, otherwise it prints an error message. To run the program open a terminal window and type the command /class/software/bin/bw-parser < input.bl where input.bl is the name of a BL file in the current directory.
