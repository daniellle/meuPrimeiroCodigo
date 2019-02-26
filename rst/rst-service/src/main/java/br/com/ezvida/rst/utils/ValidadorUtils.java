package br.com.ezvida.rst.utils;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import br.com.ezvida.rst.model.Email;

public final class ValidadorUtils {

	private ValidadorUtils() {
		throw new IllegalStateException("Utility class");
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidadorUtils.class);

	private static final int[] pesoCPF = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };

	public static <T> Set<String> validar(T entidade) {
		Set<String> violationMessages = Sets.newHashSet();
		if (entidade != null) {
			LOGGER.debug("Validando Entidade = {}...", entidade.getClass().getSimpleName());
			Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
			Set<ConstraintViolation<T>> constraintViolations = validator.validate(entidade);
			if (CollectionUtils.isNotEmpty(constraintViolations)) {
				for (ConstraintViolation<?> constraintViolation : constraintViolations) {
					violationMessages
							.add(constraintViolation.getPropertyPath() + ": " + constraintViolation.getMessage());
				}
			}
		}
		return violationMessages;
	}

	private static int calcularDigito(String str, int[] peso) {
		int soma = 0;
		for (int indice = str.length() - 1, digito; indice >= 0; indice--) {
			digito = Integer.parseInt(str.substring(indice, indice + 1));
			soma += digito * peso[peso.length - str.length() + indice];
		}
		soma = 11 - soma % 11;
		return soma > 9 ? 0 : soma;
	}

	public static boolean isValidCNPJ(String cnpj) {
		return (cnpj != null) && (cnpj.length() == 14);
		/*
		 * Integer digito1 = calcularDigito(cnpj.substring(0, 12), pesoCNPJ); Integer
		 * digito2 = calcularDigito(cnpj.substring(0, 12) + digito1, pesoCNPJ); return
		 * cnpj.equals(cnpj.substring(0, 12) + digito1.toString() + digito2.toString());
		 */
	}

	public static boolean isValidCPF(String cpf) {
		if ((cpf == null) || (cpf.length() != 11)) {
			return false;
		}
		Integer digito1 = calcularDigito(cpf.substring(0, 9), pesoCPF);
		Integer digito2 = calcularDigito(cpf.substring(0, 9) + digito1, pesoCPF);
		return cpf.equals(cpf.substring(0, 9) + digito1.toString() + digito2.toString());
	}

	public static boolean validarNit(String nit) {

		LOGGER.debug("Validando NIT");
		int soma = 0;
		int resto = 0;
		int dv = 0;
		String[] numeros = new String[nit.length()];
		int multiplicador = 2;
		for (int i = nit.length() - 1; i > 0; i--) {
			if (multiplicador > 9) {
				multiplicador = 2;
			}
			numeros[i] = String.valueOf(Integer.valueOf(nit.substring(i - 1, i)) * multiplicador);
			multiplicador++;
		}
		for (int i = numeros.length; i > 0; i--) {
			if (numeros[i - 1] != null) {
				soma += Integer.valueOf(numeros[i - 1]);
			}
		}
		resto = soma % 11;
		dv = 11 - resto;
		dv = dv != 11 ? dv : 0;
		return Integer.parseInt(nit.substring(nit.length() - 1)) == dv;
	}

	public static boolean isValidEmail(String email) {
		return email.matches(Email.REGEX_EMAIL);
	}
}
