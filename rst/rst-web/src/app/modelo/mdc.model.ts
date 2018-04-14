import {Funcionalidade } from'./enum/enum-funcionalidade';
import { TipoOperacaoAuditoria } from './enum/enum-tipo-operacao-auditoria';

export class Mdc {
    funcionalidade: Funcionalidade;
    navegador: string;
    tipo_operacao: TipoOperacaoAuditoria;
    usuario: string;
}