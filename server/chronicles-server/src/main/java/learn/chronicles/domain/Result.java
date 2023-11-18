package learn.chronicles.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Result <T> {
    private final ArrayList<String> messages = new ArrayList<>();
    private T payload;

    //set the result type
    private ResultType type = ResultType.SUCCESS;

    public List<String> getErrorMessages() {
        return new ArrayList<>(messages);
    }

    public void addErrorMessage(String message) {
        //set the type to invalid
        this.type = ResultType.INVALID;
        messages.add(message);
    }

    public void addErrorMessage(String format, Object... args) {
        messages.add(String.format(format, args));
        //set the type to invalid
        this.type = ResultType.INVALID;
    }

    /**
     * Sets the result to not found if there was a case where a valid search
     * ends up with nothing
     */
    public void setNotFound(){
        this.type = ResultType.NOT_FOUND;
    }

    public ResultType getType() {
        return type;
    }

    public boolean isSuccess() {
        // If an error message exists, the operation failed.
        return type == ResultType.SUCCESS;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result<?> result = (Result<?>) o;
        return Objects.equals(messages, result.messages) && Objects.equals(payload, result.payload) && type == result.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(messages, payload, type);
    }
}
