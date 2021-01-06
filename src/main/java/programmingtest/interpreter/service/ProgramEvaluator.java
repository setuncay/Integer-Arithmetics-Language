package programmingtest.interpreter.service;

import programmingtest.interpreter.domain.AddStatement;
import programmingtest.interpreter.domain.Output;
import programmingtest.interpreter.domain.Program;
import programmingtest.interpreter.domain.Variable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProgramEvaluator {
    private final Program program;
    private ProgramEvaluator(Program program) {
        this.program = program;
    }
    public static List<Output> evaluate(Program program) {
        final ProgramEvaluator programEvaluator = new ProgramEvaluator(program);
        return program
                .getPrintStatements()
                .stream()
                .map(printStatement -> new Output(programEvaluator.getValue(printStatement.getVariable())))
                .collect(Collectors.toList());
    }

    private Integer getValue(Variable variable) {
        final Optional<Integer> valueFromVariable = tryGetFromVariableValue(variable);
        return valueFromVariable.orElseGet(() -> {
            final Integer valueFromAddStatements = resolve(variable);
            variable.setValue(valueFromAddStatements);
            return valueFromAddStatements;
        });
    }

    private Integer resolve(Variable variable) {
        final AddStatement addStatement = this.forVariable(variable);
        return evaluateAddStatement(addStatement);
    }

    private AddStatement forVariable(Variable outputVariable) {
        return this.program
                .getAddStatements()
                .stream()
                .filter(addStatement -> addStatement.getResult().equals(outputVariable))
                .findFirst()
                .orElse(null);
    }

    private  Optional<Integer> tryGetFromVariableValue(Variable input) {
        return program.getVariables()
                .stream()
                .filter(variable -> variable.equals(input))
                .findFirst()
                .flatMap(variable -> Optional.ofNullable(variable.getValue()));
    }

    private Integer evaluateAddStatement(AddStatement addStatement) {
        // Case where print statement references a variable without a value and there is no add statement to calculate its value.
        if (addStatement == null) {
            return null;
        }
        final Integer operand1Value = addStatement.getOperand1().isLeft() ? addStatement.getOperand1().getLeft()
                : getValue(addStatement.getOperand1().get());
        final Integer operand2Value = addStatement.getOperand2().isLeft() ? addStatement.getOperand2().getLeft()
                : getValue(addStatement.getOperand2().get());

        return operand1Value + operand2Value;
    }
}
