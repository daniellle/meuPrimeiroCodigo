package br.com.ezvida.rst.enums;

public enum InformacaoSaude {
    VACINA_HEPATITE_B("openEHR-EHR-EVALUATION.ovl-ficha_clinica_ocupacional-vacinas-001.v1.0.0",
            "/data[id2]/items[id28]/items[id29]",
            "DV_BOOLEAN",
            ".*/data\\[id2(\\.\\d+)*\\]\\/items\\[id28(\\.\\d+)*\\]\\/items\\[id29(\\.\\d+)*\\]",
            "(openEHR-EHR-EVALUATION).ovl-(?:\\w+)-(vacinas)[^\\w\\ ](.*)",
            "Hepatite B"),
    VACINA_TRIPLICE_VIRAL("openEHR-EHR-EVALUATION.ovl-ficha_clinica_ocupacional-vacinas-001.v1.0.0",
            "/data[id2]/items[id28]/items[id31]",
            "DV_BOOLEAN",
            ".*/data\\[id2(\\.\\d+)*\\]\\/items\\[id28(\\.\\d+)*\\]\\/items\\[id31(\\.\\d+)*\\]",
            "(openEHR-EHR-EVALUATION).ovl-(?:\\w+)-(vacinas)[^\\w\\ ](.*)",
            "Tríplice viral"),
    VACINA_DUPLA_ADULTO("openEHR-EHR-EVALUATION.ovl-ficha_clinica_ocupacional-vacinas-001.v1.0.0",
            "/data[id2]/items[id28]/items[id33]",
            "DV_BOOLEAN",
            ".*/data\\[id2(\\.\\d+)*\\]\\/items\\[id28(\\.\\d+)*\\]\\/items\\[id33(\\.\\d+)*\\]",
            "(openEHR-EHR-EVALUATION).ovl-(?:\\w+)-(vacinas)[^\\w\\ ](.*)",
            "Dupla adulto (difteria e tétano)"),
    VACINA_FEBRE_AMARELA("openEHR-EHR-EVALUATION.ovl-ficha_clinica_ocupacional-vacinas-001.v1.0.0",
            "/data[id2]/items[id28]/items[id35]",
            "DV_BOOLEAN",
            ".*/data\\[id2(\\.\\d+)*\\]\\/items\\[id28(\\.\\d+)*\\]\\/items\\[id35(\\.\\d+)*\\]",
            "(openEHR-EHR-EVALUATION).ovl-(?:\\w+)-(vacinas)[^\\w\\ ](.*)",
            "Febre amarela"),
    VACINA_INFLUENZA("openEHR-EHR-EVALUATION.ovl-ficha_clinica_ocupacional-vacinas-001.v1.0.0",
            "/data[id2]/items[id28]/items[id37]",
            "DV_BOOLEAN",
            ".*/data\\[id2(\\.\\d+)*\\]\\/items\\[id28(\\.\\d+)*\\]\\/items\\[id37(\\.\\d+)*\\]",
            "(openEHR-EHR-EVALUATION).ovl-(?:\\w+)-(vacinas)[^\\w\\ ](.*)",
            "Influenza"),
    VACINA_OUTRAS("openEHR-EHR-EVALUATION.ovl-ficha_clinica_ocupacional-vacinas-001.v1.0.0",
            "/data[id2]/items[id28]/items[id43]",
            "DV_TEXT",
            ".*/data\\[id2(\\.\\d+)*\\]\\/items\\[id28(\\.\\d+)*\\]\\/items\\[id43(\\.\\d+)*\\]",
            "(openEHR-EHR-EVALUATION).ovl-(?:\\w+)-(vacinas)[^\\w\\ ](.*)",
            "Especificar"),
    ALERGIA("openEHR-EHR-EVALUATION.allergy.v1.0.0",
            "/data[id2]/items[id3]",
            "DV_TEXT",
            ".*/data\\[id2(\\.\\d+)*\\]\\/items\\[id3(\\.\\d+)*\\]",
            "(openEHR-EHR-EVALUATION).ovl-(?:\\w+)-(allergy)[^\\w\\ ](.*)",
            "Alergia"),
    MEDICAMENTO("openEHR-EHR-ACTION.medication.v1.0.0",
            "/description[id18]/items[id25]",
            "DV_TEXT",
            ".*/description\\[id18(\\.\\d+)*\\]\\/items\\[id25(\\.\\d+)*\\]",
            "(openEHR-EHR-ACTION).ovl-(?:\\w+)-(medication)[^\\w\\ ](.*)",
            "Medicamento"),
    PRESSAO_SISTOLICA("openEHR-EHR-OBSERVATION.blood_pressure.v1.0.0",
            "/data[id2]/events[id7]/data[id4]/items[id5]",
            "DV_QUANTITY",
            ".*/data\\[id2(\\.\\d+)*\\]\\/events\\[id7(\\.\\d+)*\\]\\/data\\[id4(\\.\\d+)*\\]\\/items\\[id5(\\.\\d+)*\\]",
            "(openEHR-EHR-OBSERVATION).ovl-(?:\\w+)-(blood_pressure)[^\\w\\ ](.*)",
            "Sistólica"),
    PRESSAO_DIASTOLICA("openEHR-EHR-OBSERVATION.blood_pressure.v1.0.0",
            "/data[id2]/events[id7]/data[id4]/items[id6]",
            "DV_QUANTITY",
            ".*/data\\[id2(\\.\\d+)*\\]\\/events\\[id7(\\.\\d+)*\\]\\/data\\[id4(\\.\\d+)*\\]\\/items\\[id6(\\.\\d+)*\\]",
            "(openEHR-EHR-OBSERVATION).ovl-(?:\\w+)-(blood_pressure)[^\\w\\ ](.*)",
            "Diastólica"),
    PESO("openEHR-EHR-OBSERVATION.body_weight-SESI_BR.v1.0.0",
            "/data[id3]/events[id4]/data[id2]/items[id5]",
            "DV_QUANTITY",
            ".*/data\\[id3(\\.\\d+)*\\]\\/events\\[id4(\\.\\d+)*\\]\\/data\\[id2(\\.\\d+)*\\]\\/items\\[id5(\\.\\d+)*\\]",
            "(openEHR-EHR-OBSERVATION).ovl-(?:\\w+)-(body_weight)[^\\w\\ ](.*)",
            "Peso"),
    ALTURA("openEHR-EHR-OBSERVATION.height.v1.0.0",
            "/data[id2]/events[id3]/data[id4]/items[id5]",
            "DV_QUANTITY",
            ".*/data\\[id2(\\.\\d+)*\\]\\/events\\[id3(\\.\\d+)*\\]\\/data\\[id4(\\.\\d+)*\\]\\/items\\[id5(\\.\\d+)*\\]",
            "(openEHR-EHR-OBSERVATION).ovl-(?:\\w+)-(height)[^\\w\\ ](.*)",
            "Altura / comprimento"),
    IMC("openEHR-EHR-OBSERVATION.body_mass_index.v1.0.0",
            "/data[id2]/events[id3]/data[id4]/items[id5]",
            "DV_QUANTITY",
            ".*/data\\[id2(\\.\\d+)*\\]\\/events\\[id3(\\.\\d+)*\\]\\/data\\[id4(\\.\\d+)*\\]\\/items\\[id5(\\.\\d+)*\\]",
            "(openEHR-EHR-OBSERVATION).ovl-(?:\\w+)-(body_mass_index)[^\\w\\ ](.*)",
            "Índice de massa corporal"),
    TEMPERATURA("openEHR-EHR-OBSERVATION.body_temperature.v1.0.0",
            "/data[id3]/events[id4]/data[id2]/items[id5]",
            "DV_QUANTITY",
            ".*/data\\[id3(\\.\\d+)*\\]\\/events\\[id4(\\.\\d+)*\\]\\/data\\[id2(\\.\\d+)*\\]\\/items\\[id5(\\.\\d+)*\\]",
            "(openEHR-EHR-OBSERVATION).ovl-(?:\\w+)-(body_temperature)[^\\w\\ ](.*)",
            "Temperatura"),
    CIRCUNFERENCIA_ABDOMINAL("openEHR-EHR-OBSERVATION.body_weight-SESI_BR.v1.0.0",
            "/data[id3]/events[id4]/data[id2]/items[id0.3]",
            "DV_QUANTITY",
            ".*/data\\[id3(\\.\\d+)*\\]\\/events\\[id4(\\.\\d+)*\\]\\/data\\[id2(\\.\\d+)*\\]\\/items\\[id0.3(\\.\\d+)*\\]",
            "(openEHR-EHR-OBSERVATION).ovl-(?:\\w+)-(body_weight)[^\\w\\ ](.*)",
            "Circunferência abdominal"),
    IDADE("openEHR-EHR-ADMIN_ENTRY.admission_SESI-BR.v1.0.0",
            "/data[id2]/items[id0.43]/items[id0.62]",
            "DV_TEXT",
            ".*/data\\[id2(\\.\\d+)*\\]\\/events\\[id3(\\.\\d+)*\\]\\/data\\[id4(\\.\\d+)*\\]\\/items\\[id5(\\.\\d+)*\\]",
            "(openEHR-EHR-OBSERVATION-ADMIN_ENTRY).ovl-(?:\\w+)-(admission)[^\\w\\ ](.*)",
            "Idade");

    private String idArquetipo;
    private String pathDado;
    private String tipoDado;
    private String regexPath;
    private String regexArquetipo;
    private String nome;

    private InformacaoSaude(String id, String path, String tipo, String regex, String regexArquetipo, String nome) {
        this.idArquetipo = id;
        this.pathDado = path;
        this.tipoDado = tipo;
        this.regexPath = regex;
        this.regexArquetipo = regexArquetipo;
        this.nome = nome;
    }

    public static InformacaoSaude fromString(String s) {
        return InformacaoSaude.valueOf(s);
    }

    public String getIdArquetipo() {
        return idArquetipo;
    }

    public String getPathDado() {
        return pathDado;
    }

    public String getTipoDado() {
        return tipoDado;
    }

    public String getRegexPath() {
        return regexPath;
    }

    public String getRegexArquetipo() {
        return regexArquetipo;
    }

    public String getNome() {
        return nome;
    }


}
