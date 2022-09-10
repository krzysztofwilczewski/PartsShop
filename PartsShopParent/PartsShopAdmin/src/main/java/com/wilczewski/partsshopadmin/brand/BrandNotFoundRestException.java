package com.wilczewski.partsshopadmin.brand;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Producent nie odnaleziony")
public class BrandNotFoundRestException extends Exception{
}
