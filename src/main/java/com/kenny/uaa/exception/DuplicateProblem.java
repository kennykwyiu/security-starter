package com.kenny.uaa.exception;

import com.kenny.uaa.config.Constants;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class DuplicateProblem extends AbstractThrowableProblem {
    private static final URI TYPE = URI.create(Constants.PROBLEM_BASE_URI + "/duplicate");

    public DuplicateProblem(String message) {
        super(TYPE, "find out duplicated data", Status.CONFLICT, message);
    }

}
