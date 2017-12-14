export class Paginacao {
    static readonly qtdRegistrosDefault = 10;
    static readonly qtdRegistos5 = 5;
    readonly textPrimeiro = '<<';
    readonly textUltimo = '>>';
    readonly textAnterior = '<';
    readonly textProximo = '>';
    pagina = 1;
    qtdRegistro = 10;
    maxSize = this.getMaxSize();
    numPages = 1;
    totalItens: number;

      constructor(pagina?: number, qtdRegistro?: number, maxSize?: number) {
        this.pagina = pagina != null ? pagina : this.pagina;
        this.maxSize = maxSize != null ? maxSize : this.getMaxSize();
        this.qtdRegistro = qtdRegistro != null ? qtdRegistro : this.qtdRegistro;
  }

  getMaxSize(): number {
    // Bootstrap Layout
     // Small devices (landscape phones, 576px and up)
    // Medium devices (tablets, 768px and up)
    // Large devices (desktops, 992px and up)
    // Extra large devices (large desktops, 1200px and up)

    const small = 576;
    const medium = 768;
    const large =  992;
    const extra = 1200;
    const tamanhoAtual = window.screen.width;
    const isExtraSmall = tamanhoAtual < small;
    const isSmall = tamanhoAtual >= small && tamanhoAtual < medium;
    const isMedium = tamanhoAtual >= medium && tamanhoAtual < large;
    const isLarge = tamanhoAtual >= large && tamanhoAtual < extra;
    const isExtraLarge = tamanhoAtual >= extra;

    if (isExtraSmall) {
      return 3;
    }
    if (isSmall) {
      return 5;
    }
    if (isMedium || isLarge) {
      return 10;
    }
    if (isExtraLarge) {
      return 10;
    }
  }

}
