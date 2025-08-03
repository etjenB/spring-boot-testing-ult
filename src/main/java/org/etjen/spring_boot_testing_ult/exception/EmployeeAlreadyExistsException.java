package org.etjen.spring_boot_testing_ult.exception;

public class EmployeeAlreadyExistsException extends RuntimeException {

  public EmployeeAlreadyExistsException(String message) {
      super(message);
  }

  public EmployeeAlreadyExistsException(String message, Throwable cause) {
      super(message, cause);
  }
}
