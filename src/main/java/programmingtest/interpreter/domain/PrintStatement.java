package programmingtest.interpreter.domain;

import org.antlr.v4.runtime.Token;

/**
 * A simple POJO to encapsulate:
 *  <print n="c"/>
 */
public class PrintStatement {
   private final Variable variable;
    private final Token token;

    public PrintStatement(Variable variable, Token token) {
        this.variable = variable;
        this.token = token;
    }

    public Variable getVariable() {
        return variable;
    }

    public Token getToken() {
        return token;
    }
}
