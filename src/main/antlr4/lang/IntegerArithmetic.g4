grammar IntegerArithmetic;


program:
        '<program>' (statement)* '</program>'
        ;

statement:
          varStatement
          |
          addStatement
          |
          printStatement
          ;

// <variable name="a" value="5" />
// <variable name="a"  />
varStatement:
              '<var'  nameAttribute (valueAttribute)? '/>'
              ;

// <addStatement n1="a" n2="b" to="c" />
addStatement:
             '<add' n1Attribute n2Attribute toAttribute'/>'
             ;

// <print n="d" />
printStatement:
                '<print' nAttribute '/>'
                ;

nameAttribute :  'name="' ID '"' ;
valueAttribute : 'value="' INTLIT '"' ;
nAttribute : 'n="' ID '"' ;
n1Attribute: 'n1="' (ID | INTLIT) '"';
n2Attribute: 'n2="' (ID | INTLIT) '"';
toAttribute: 'to="' ID '"';

ID                 : [_]*[a-z][A-Za-z0-9_]* ;

fragment
DIGIT : [0-9] ;

INTLIT             : [1-9][0-9]* ;
WS                 :  [ \t\r\n\u000C]+ -> skip;