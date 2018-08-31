import { OperationalTemplate, Element } from '@ezvida/adl-core';
export interface ResElemento {
    processar(operationalTemplate: OperationalTemplate, path: Element, extras?: any);
}
