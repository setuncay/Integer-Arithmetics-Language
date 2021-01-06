package programmingtest.interpreter.domain;


import io.vavr.control.Either;
import org.antlr.v4.runtime.Token;

/**
 * A simple POJO to encapsulate:
 * <add n1="a" n2="b" to="c"/>
 * <add n1="c" n2="3" to="d" />
 */
public class AddStatement {
    private Either<Integer, Variable> operand1;
    private Either<Integer, Variable> operand2;
    private Variable result;
    private final Token token;

    public AddStatement(Either<Integer, Variable> operand1, Either<Integer, Variable> operand2, Variable result, Token token) {
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.result = result;
        this.token = token;
    }

    public Either<Integer, Variable> getOperand1() {
        return operand1;
    }

    public Either<Integer, Variable> getOperand2() {
        return operand2;
    }

    public Variable getResult() {
        return result;
    }

    public Token getToken() {
        return token;
    }

}

