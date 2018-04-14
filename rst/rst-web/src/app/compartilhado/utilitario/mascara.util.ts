export class MascaraUtil {
  public static mascaraCpf = [/\d/, /\d/, /\d/, '.', /\d/, /\d/, /\d/, '.', /\d/, /\d/, /\d/, '-', /\d/, /\d/];
  public static mascaraCnpj = [/\d/, /\d/, '.', /\d/, /\d/, /\d/, '.', /\d/, /\d/, /\d/, '/', /\d/, /\d/, /\d/, /\d/, '-', /\d/, /\d/];
  public static mascaraTelefone = ['(', /\d/, /\d/, ')', ' ', /\d/, /\d/, /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, /\d/];
  public static mascaraTelefoneFixo = ['(', /\d/, /\d/, ')', ' ', /\d/, /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, /\d/];
  public static mascaraCep = [/\d/, /\d/, '.', /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/];
  public static mascaraNascimento = [/\d/, /\d/, '/', /\d/, /\d/, '/', /\d/, /\d/, /\d/, /\d/];
  public static qtdHoras = [/\d/, /\d/, /\d/, /\d/, /\d/, /\d/, /\d/, /\d/, /\d/, /\d/];
  public static mascaraNit = [/\d/, /\d/, /\d/, '.', /\d/, /\d/, /\d/, /\d/, /\d/, '.', /\d/, /\d/, '-', /\d/];

  public static removerMascara(value: string): string {
    if (value) {
      return value.replace(/[^0-^9]/g, '');
    }
    return value;
  }

  public static formatarTelefone(value: string): string {
    if (value) {
      return '(' + value.substr(0, 2) + ') ' + value.substr(2, 5) + '-' + value.substr(4, 4);
    } else {
      return '';
    }
  }

  public static formatarCpf(cpf: string) {
    if (cpf) {
      return cpf.substr(0, 3) + '.' + cpf.substr(3, 3) + '.' + cpf.substr(6, 3) + '-' + cpf.substr(9, 2);
    } else {
      return '';
    }
  }

  public static formatarCnpj(cnpj: string) {
    if (cnpj) {
      return cnpj.substr(0, 2) + '.' + cnpj.substr(2, 3) + '.' + cnpj.substr(5, 3)
        + '/' + cnpj.substr(8, 4) + '-' + cnpj.substr(12, 2);
    } else {
      return '';
    }
  }

  public static formatarCep(value: string) {
    if (value) {
      return value.substr(0, 5) + '-' + value.substr(5, 7);
    } else {
      return '';
    }
  }

  public static getNitFormatado(nit: string) {
    if (nit) {
      return nit.substr(0, 3) + '.' + nit.substr(3, 5) + '.' + nit.substr(8, 2) + '-' + nit.substr(10, 1);
    } else {
      return '';
    }
  }

  public static formatarTelefoneNoveDigitos(numero: string) {
    if (numero) {
      if (numero.length === 10) {
        numero = numero.replace(/\W/g, '');
        numero = numero.replace(/^(\d{2})(\d)/, '($1) $2');
        numero = numero.replace(/(\d{4})(\d)/, '$1-$2');
        numero = numero.replace(/(\d{4})$/, '$1');
      } else {
        numero = numero.replace(/\W/g, '');
        numero = numero.replace(/^(\d{2})(\d)/, '($1) $2');
        numero = numero.replace(/(\d{5})(\d)/, '$1-$2');
        numero = numero.replace(/(\d{4})$/, '$1');
      }
      return numero;
    } else {
      return '';
    }

  }
}
