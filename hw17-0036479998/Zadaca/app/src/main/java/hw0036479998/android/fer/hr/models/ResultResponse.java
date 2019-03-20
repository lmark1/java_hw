package hw0036479998.android.fer.hr.models;

import java.io.Serializable;

/**
 * This class represents a serializable result response containing inputs, operation string,
 * result (if available) and an error message (if available).
 *
 * Created by lmark on 29.6.2017..
 */
public class ResultResponse implements Serializable{

    /**
     * First input.
     */
    private String firstInput;

    /**
     * Second input.
     */
    private String secondInput;

    /**
     * Opeartion string.
     */
    private String operation;

    /**
     * Result string.
     */
    private String result;

    /**
     * Error string.
     */
    private String error;

    /**
     * Default constructor for this class.
     *
     * @param firstInput Initializes first input.
     * @param secondInput Initializes second input.
     * @param operation Initializes operation string.
     */
    public ResultResponse(String firstInput, String secondInput, String operation) {
        this.firstInput = firstInput;
        this.secondInput = secondInput;
        this.operation = operation;
    }

    /**
     * @return Returns first input.
     */
    public String getFirstInput() {
        return firstInput;
    }

    /**
     * @return Returns second input.
     */
    public String getSecondInput() {
        return secondInput;
    }

    /**
     * @return Returns operation string.
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Setter for the result string.
     *
     * @param result New result string.
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * @return Returns result.
     */
    public String getResult() {
        return result;
    }

    /**
     * Setter for the error message.
     *
     * @param error New error message.
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * @return Returns error message.
     */
    public String getError() {
        return error;
    }


    public String getString() {

        if (error != null) {
            return String.format("Prilikom obavljanja operacije %s nad unosima "
                    +"%s i %s došlo je do sljedeće grešlke: %s.",
                    operation.toLowerCase(), firstInput, firstInput, error);
        } else {
            return String.format("Rezultat operacije %s je %s.", operation.toLowerCase(), result);
        }
    }
}
