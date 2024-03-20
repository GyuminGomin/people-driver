package com.gls.ppldv.configuration.userException;

public class RegisterFailedException extends Exception {

	private static final long serialVersionUID = -933591257338884385L;

	public RegisterFailedException(String message) {
		super(message);
	}
}
