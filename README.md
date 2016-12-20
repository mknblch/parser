# parser 1.0-SNAPSHOT

Proof of concept dynamic LL1 parser generator.

## Example 

```
;; Options
#START ::= S
#EPSILON ::= EPSILON

;; Omit
~DELIMITER ::= \s+|;

;; Terminal Expressions
%a ::= a
%b ::= b

;; Rules
S ::= A S A | EPSILON
A ::= %a
B ::= %b
```