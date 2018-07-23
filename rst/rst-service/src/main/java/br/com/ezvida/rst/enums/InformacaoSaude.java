package br.com.ezvida.rst.enums;

public enum InformacaoSaude {
    VACINA_HEPATITE_B("openEHR-EHR-EVALUATION.ovl-ficha_clinica_ocupacional-vacinas-001.v1.0.0",
            "/data[id2]/items[id28]/items[id29]",
            "DV_BOOLEAN",
            "Hepatite B"),
    VACINA_TRIPLICE_VIRAL("openEHR-EHR-EVALUATION.ovl-ficha_clinica_ocupacional-vacinas-001.v1.0.0",
            "/data[id2]/items[id28]/items[id31]",
            "DV_BOOLEAN",
            "Tríplice viral"),
    VACINA_DUPLA_ADULTO("openEHR-EHR-EVALUATION.ovl-ficha_clinica_ocupacional-vacinas-001.v1.0.0",
            "/data[id2]/items[id28]/items[id33]",
            "DV_BOOLEAN",
            "Dupla adulto (difteria e tétano)"),
    VACINA_FEBRE_AMARELA("openEHR-EHR-EVALUATION.ovl-ficha_clinica_ocupacional-vacinas-001.v1.0.0",
            "/data[id2]/items[id28]/items[id35]",
            "DV_BOOLEAN",
            "Febre amarela"),
    VACINA_INFLUENZA("openEHR-EHR-EVALUATION.ovl-ficha_clinica_ocupacional-vacinas-001.v1.0.0",
            "/data[id2]/items[id28]/items[id37]",
            "DV_BOOLEAN",
            "Influenza"),
    VACINA_OUTRAS("openEHR-EHR-EVALUATION.ovl-ficha_clinica_ocupacional-vacinas-001.v1.0.0",
            "/data[id2]/items[id28]/items[id43]",
            "DV_TEXT",
            "Especificar"),
    ALERGIA("openEHR-EHR-EVALUATION.allergy.v1.0.0",
            "/data[id2]/items[id3]",
            "DV_TEXT",
            "Alergia"),
    MEDICAMENTO("openEHR-EHR-ACTION.medication.v1.0.0",
            "/description[id18]/items[id25]",
            "DV_TEXT",
            "Medicamento"),
    PRESSAO_SISTOLICA("openEHR-EHR-OBSERVATION.blood_pressure.v1.0.0",
            "/data[id2]/events[id7]/data[id4]/items[id5]",
            "DV_QUANTITY",
            "Sistólica"),
    PRESSAO_DIASTOLICA("openEHR-EHR-OBSERVATION.blood_pressure.v1.0.0",
            "/data[id2]/events[id7]/data[id4]/items[id6]",
            "DV_QUANTITY",
            "Diastólica"),
    PESO("openEHR-EHR-OBSERVATION.body_weight-SESI_BR.v1.0.0",
            "/data[id3]/events[id4]/data[id2]/items[id5]",
            "DV_QUANTITY",
            "Peso"),
    ALTURA("openEHR-EHR-OBSERVATION.height.v1.0.0",
            "/data[id2]/events[id3]/data[id4]/items[id5]",
            "DV_QUANTITY",
            "Altura / comprimento"),
    IMC("openEHR-EHR-OBSERVATION.body_mass_index.v1.0.0",
            "/data[id2]/events[id3]/data[id4]/items[id5]",
            "DV_QUANTITY",
            "Índice de massa corporal"),
    TEMPERATURA("openEHR-EHR-OBSERVATION.body_temperature.v1.0.0",
            "/data[id3]/events[id4]/data[id2]/items[id5]",
            "DV_QUANTITY",
            "Temperatura"),
    CIRCUNFERENCIA_ABDOMINAL("openEHR-EHR-OBSERVATION.body_weight-SESI_BR.v1.0.0",
            "/data[id3]/events[id4]/data[id2]/items[id0.3]",
            "DV_QUANTITY",
            "Circunferência abdominal"),
    IMUNIZACAO_NOME("openEHR-EHR-EVALUATION.immunisation_summary-SESI-BR.v1.0.0",
            "/data[id2]/items[id17]/items[id3]",
            "DV_TEXT",
            "Tipo de imunobiológico"),
    IMUNIZACAO_DATA("openEHR-EHR-EVALUATION.immunisation_summary-SESI-BR.v1.0.0",
            "/data[id2]/items[id17]/items[id9]",
            "DV_TEXT",
            "Data de administração"),

    IDADE("openEHR-EHR-ADMIN_ENTRY.admission_SESI-BR.v1.0.0",
            "/data[id2]/items[id0.43]/items[id0.62]",
            "DV_TEXT",
            "Idade");
    private String idArquetipo;
    private String amPath;
    private String tipo;
    private String nome;


    private InformacaoSaude(String id, String path, String tipo, String nome) {
        this.idArquetipo = id;
        this.amPath = path;
        this.tipo = tipo;
        this.nome = nome;
    }

    public static InformacaoSaude fromString(String s) {
        return InformacaoSaude.valueOf(s);
    }

    public String getIdArquetipo() {
        return idArquetipo;
    }

    public String getAmPath() {
        return amPath;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNome() {
        return nome;
    }


}
