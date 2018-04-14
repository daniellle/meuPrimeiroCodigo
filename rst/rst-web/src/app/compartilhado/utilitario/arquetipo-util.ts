import {
    Archetype, Path, DictionaryItem,
    TerminologyQuery, OperationalTemplate
} from '@ezvida/adl-core';

export class ArquetipoUtil {

    static terminologiaParaPath(operationalTemplate: OperationalTemplate, aPath: string, overlayId?: string): DictionaryItem {
        let termo = new DictionaryItem();
        const overlay = overlayId ? { overlayId: overlayId } : {};
        termo = TerminologyQuery.findTermDefinitionByPath(operationalTemplate, aPath, {
            overlayId: overlayId
        });
        return termo;
    }

    static terminologiaParaNo(template: OperationalTemplate, id: string): DictionaryItem {

        let termo;
        termo = TerminologyQuery.findTermDefinitionByPath(template, template.definition.nodeId);

        return termo;
    }

    static terminologiaParaReferencia(template: OperationalTemplate, overlay: string): DictionaryItem {

        let termo;
        termo = TerminologyQuery.findTermDefinition(template, template.definition.nodeId, {
            overlayId: overlay
        });

        return termo;
    }


    static getValorPrimitivo(path: Path): any {
        switch (path.type) {
            case 'DV_QUANTITY':
                return (path.value as any).magnitude;
            case 'DV_PROPORTION':
                return Number(path.value as string);
            case 'DV_DATE_TIME':
            case 'DV_DATE':
                return new Date(path.value.toString());
            case 'DV_TEXT':
                return path.value as string;
            case 'DV_CODED_TEXT':
                return (path.value as any).code;
            default:
                return null;
        }
    }


}
