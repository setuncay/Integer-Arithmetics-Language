# Integer-Arithmetics-Language

An interpreter, preferably in Java  for a simple XML-based programming language for integer arithmetics.

## Assumptions:

* Variable values can be positive integers.
* Add statements can only take two operands that is represented by n1 and n2 attributes correspondingly.

## Pre-requisites For Building the source:

* Java Jdk 8
* Maven 3.6.x
* Network Connection so that maven can download dependencies based on your local maven settings.

## Build Steps

1. Clone the project.
2. Goto project root on a terminal / console window and execute
    * mvn clean install
    
## How to Run

1. After completing build steps, goto project root on a terminal / console window
2. cd target
3. java -jar IntegerArithmetic-1.0-SNAPSHOT.jar <path to source file>

* Example:

`java -jar IntegerArithmetic-1.0-SNAPSHOT.jar "/home/user1/source.xml"`

## Scenarios

See unit tests, in particular ArithmeticExecutorTest, that cover various cases including unhappy execution paths like syntax errors, undefined variable references etc.
These tests use sample source files placed under test/resources.

* Example

`java -jar IntegerArithmetic-1.0-SNAPSHOT.jar ../src/test/resources/add_statement_references_undefined_variable_3.xml `

should yield:

`Interpretation Error: add_statement_references_undefined_variable_3.xml (4,4) to references an undefined variable`

as add_statement_references_undefined_variable_3.xml contains an 'add' statement on line 4 is referencing an undefined variable.

