package programmingtest.interpreter.domain;

public class Output {
    private final Integer result;

    public Output(Integer result) {
        this.result = result;
    }

    public Integer getResult() {
        return result;
    }

    @Override
    public String toString() {
        return String.valueOf(this.result);
    }
}
