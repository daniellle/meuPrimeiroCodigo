export class IconesUtils {

    private static readonly IMAGENS_ATENDIMENTO = [
        {
            regex: /habitos|estilo|vida|lifestyle|fco|saudavel/i,
            svg: 'assets/img/res/timeline/estilodevida.svg',
            svg_full: 'assets/img/res/timeline/estilodevida_full.svg',
            svg_bg: 'assets/img/res/timeline/estilodevida_bg.svg',
            svg_clean: 'assets/img/res/timeline/estilodevida_clean.svg',
            cor: '#67536c',
        },
        {
            regex: /evolucao|evolution|historico|historia|familiar/i,
            svg: 'assets/img/res/timeline/evolucao.svg',
            svg_full: 'assets/img/res/timeline/evolucao_full.svg',
            svg_bg: 'assets/img/res/timeline/evolucao_bg.svg',
            svg_clean: 'assets/img/res/timeline/evolucao_clean.svg',
            cor: '#ee7627',
        },
        {
            regex: /laboratorio|laboratory|lab/i,
            svg: 'assets/img/res/timeline/laboratorio.svg',
            svg_full: 'assets/img/res/timeline/laboratorio_full.svg',
            svg_bg: 'assets/img/res/timeline/laboratorio_bg.svg',
            svg_clean: 'assets/img/res/timeline/laboratorio_clean.svg',
            cor: '#782121',
        },
        {
            regex: /medicamento|medication|medica/i,
            svg: 'assets/img/res/timeline/medicamento.svg',
            svg_full: 'assets/img/res/timeline/medicamento_full.svg',
            svg_bg: 'assets/img/res/timeline/medicamento_bg.svg',
            svg_clean: 'assets/img/res/timeline/medicamento_clean.svg',
            cor: '#d66994',
        },
        {
            regex: /periodico/i,
            svg: 'assets/img/res/timeline/periodico.svg',
            svg_full: 'assets/img/res/timeline/periodico_full.svg',
            svg_bg: 'assets/img/res/timeline/periodico_bg.svg',
            svg_clean: 'assets/img/res/timeline/periodico_clean.svg',
            cor: '#5fbcd4',
        },
        {
            regex: /pressao|blood_pressure|sanguinia/i,
            svg: 'assets/img/res/timeline/pressao.svg',
            svg_full: 'assets/img/res/timeline/pressao_full.svg',
            svg_bg: 'assets/img/res/timeline/pressao_bg.svg',
            svg_clean: 'assets/img/res/timeline/pressao_clean.svg',
            cor: '#d66994',
        },
        {
            regex: /encounter|encontro|atendimento|vital_signs|socorro/i,
            svg: 'assets/img/res/timeline/prontosocorro.svg',
            svg_full: 'assets/img/res/timeline/prontosocorro_full.svg',
            svg_bg: 'assets/img/res/timeline/prontosocorro_bg.svg',
            svg_clean: 'assets/img/res/timeline/prontosocorro_clean.svg',
            cor: '#c83737',
        },
        {
            regex: /admissional|saude_ocupacional/i,
            svg: 'assets/img/res/timeline/saudeocupacional.svg',
            svg_full: 'assets/img/res/timeline/saudeocupacional_full.svg',
            svg_bg: 'assets/img/res/timeline/saudeocupacional_bg.svg',
            svg_clean: 'assets/img/res/timeline/saudeocupacional_clean.svg',
            cor: '#ee7627',
        },
        {
            regex: /triagem|screening/i,
            svg: 'assets/img/res/timeline/triagem.svg',
            svg_full: 'assets/img/res/timeline/triagem_full.svg',
            svg_bg: 'assets/img/res/timeline/triagem_bg.svg',
            svg_clean: 'assets/img/res/timeline/triagem_clean.svg',
            cor: '#37abc8',
        },
        {
            regex: /imunobiologico/i,
            svg: 'assets/img/res/timeline/vacina.svg',
            svg_full: 'assets/img/res/timeline/vacina_full.svg',
            svg_bg: 'assets/img/res/timeline/triagem_bg.svg',
            svg_clean: 'assets/img/res/timeline/triagem_clean.svg',
            cor: '#ff802aff',
        },
    ];

    public static getIconeTemplate(id: string): { svg: string, svg_full: string, svg_bg: string, svg_clean: string, cor: string } {

        const imagem = IconesUtils.IMAGENS_ATENDIMENTO.filter((image: {
            regex: RegExp,
            svg: string,
            svg_full: string,
            svg_bg: string,
            svg_clean: string,
            cor: string,
        }) => {
            return image.regex.test(id);
        }).pop() || {
                svg: 'assets/img/res/timeline/triagem.svg',
                svg_full: 'assets/img/res/timeline/triagem_full.svg',
                svg_bg: 'assets/img/res/timeline/triagem_bg.svg',
                svg_clean: 'assets/img/res/timeline/triagem_clean.svg',
                cor: '#37abc8',
            };

        return {
            svg: imagem.svg,
            svg_full: imagem.svg_full,
            svg_bg: imagem.svg_bg,
            svg_clean: imagem.svg_clean,
            cor: imagem.cor,
        };

    }

}
