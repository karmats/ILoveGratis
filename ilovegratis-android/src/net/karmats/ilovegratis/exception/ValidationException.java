package net.karmats.ilovegratis.exception;

import java.util.List;

/**
 * Class that is thrown when an ad can't be validated. Takes string ids from the string resources as argument.
 * 
 * @author mats
 * 
 */
public class ValidationException extends Exception {

    private static final long serialVersionUID = 1L;

    private final List<Integer> validationErrorIds;

    public ValidationException(List<Integer> validationErrorIds) {
        super();
        this.validationErrorIds = validationErrorIds;
    }

    public List<Integer> getValidationErrorIds() {
        return validationErrorIds;
    }

}
