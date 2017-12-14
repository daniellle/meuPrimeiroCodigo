import { ResService } from './../../../servico/res-service.service';
import { ResElemento } from './res-elemento.component';
import { OperationalTemplate, Element, DvUri, DvMultimedia } from '@ezvida/adl-core';
import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { saveAs as fileSaver } from 'file-saver';

@Component({
    selector: 'app-dv-midia',
    template: `
        <div class='row'>
            <div class='col-lg-12 col-res-multimedia' *ngIf='isDesconhecido'>
               <span> <label class='mat-caption nobottom' > {{ titulo }}: </label>
               <label  (click)='$event.stopPropagation();download()'> {{ this.multimidia?.alternateText}} </label>
               <i (click)='$event.stopPropagation();download()' class="material-icons">file_download</i>
               </span>
             </div>
            <div class='col-lg-12 col-res-multimedia' >
                <div class='row'>
                     <div class='col-12 col-sm-3' style='display:flex; flex-flow:row; align-items:flex-end;'>
                        <label class='mat-caption nobottom' > {{ titulo }}: </label>
                     </div>
                     <div class='col-12 col-sm-9 '*ngIf='isImagem && !midia?.value'>
                        <button (click)='$event.stopPropagation();download();'>Carregar Imagem </button>
                     </div>
                     <div class='col-12 col-sm-9'*ngIf='isAudio || isVideo'>
                     </div>
               </div>
               <div class='row' style='margin-top:8px'>
                    <div class='col-lg-12'*ngIf='isImagem'>
                            <img [src]='midia?.value' style='max-width: 100%'/>
                    </div>
                    <div class='col-lg-12' *ngIf='isAudio'>
                            <audio #audio *ngIf='!midia?.value'
                                     style='max-width: 100%' controls (play)='$event.stopPropagation();download();'>
                                <source >
                                Seu browser não suporta tocar este áudio.
                            </audio>
                            <audio #audio autoplay *ngIf='midia?.value' style='max-width: 100%' controls  controlsList='nodownload'>
                                <source [src]='midia?.value' [type]='multimidia?.mediaType?.codeString'>
                                Seu browser não suporta o audio escolhido
                            </audio>
                    </div>
                    <div class='col-lg-12' *ngIf='isVideo'>
                            <video *ngIf='!midia?.value'
                                     style='max-width: 100%' controls (play)='$event.stopPropagation();download();'>
                                <source >
                                Seu browser não suporta a visualização de vídeos.
                            </video>
                            <video  autoplay *ngIf='midia?.value' style='max-width: 100%' controls  controlsList='nodownload'>
                                <source [src]='midia?.value' [type]='multimidia?.mediaType?.codeString'>
                                Seu browser não suporta a visualização de vídeos
                            </video>
                    </div>
               </div>
             </div>

       </div>
    `,
    encapsulation: ViewEncapsulation.None,
})
export class ResMultiMidiaComponent implements OnInit, ResElemento {

    titulo: string;
    isImagem = false;
    isVideo = false;
    isDesconhecido = false;
    isAudio = false;
    tipoMidia = '';
    templateId: string;
    midiaPath: string;
    multimidia: any;
    nomeArquivo: string;
    midia: DvUri;
    carregando = false;

    constructor(private service: ResService) {}

    transformarStringEmMultimedia(midia: string): any {
        const base64 = ';base64,';
        const start = midia.indexOf(base64);
        if (start !== -1) {
            const tipoArquivo = midia.substring(5, midia.indexOf(';'));
            this.nomeArquivo = tipoArquivo.substring(0, tipoArquivo.indexOf('/'));

            return {
                alternateText: 'Clique aqui para download',
                uri: midia,
                mediaType: {
                    codeString: tipoArquivo,
                },
            };
        }
        return null;
    }

    base64ComoArquivo(midia: string): Uint8Array | null {
        const base64 = ';base64,';
        const start = midia.indexOf(base64);
        if (start !== -1) {
            const data = midia.substring(start + base64.length);
            const raw = window.atob(data);
            const array = new Uint8Array(new ArrayBuffer(raw.length));
            for (let i = 0; i < raw.length; i++) {
                array[i] = raw.charCodeAt(i);
            }
            return array;
        }
        return null;
    }

    ngOnInit() {
    }

    processar(arquetipo: OperationalTemplate, dvMidia: Element) {
        if (!arquetipo || !dvMidia) {
            throw new Error(`nao é possível processar com dados nulos`);
        }

        const midia = <DvMultimedia>dvMidia.value;

        this.templateId = (arquetipo as any)._id;
        this.midiaPath = dvMidia.aPath;
        if (typeof midia.data === 'string') {
            this.multimidia = this.transformarStringEmMultimedia(midia.data);
        } else {
            this.multimidia = midia.data;
        }

        if (this.multimidia == null) {
            throw new Error(`nenhuma midia encontrada para o path ${JSON.stringify(dvMidia)}`);
        }

        this.verificaTipo(this.multimidia);

        this.titulo = dvMidia.name.value;
        if (this.titulo == null) {
            throw new Error(`Titulo não encontrado para o path ${JSON.stringify(dvMidia)} no arquetipo ${arquetipo.archetypeId}`);
        }
    }

    download() {
        if (!this.carregando) {
            if (this.midia) {

                if ((this.isAudio || this.isVideo)) {
                    return;
                }
                if (this.isDesconhecido) {

                    fileSaver.saveAs(new Blob([this.base64ComoArquivo(this.midia.value)], { type: this.tipoMidia }), this.nomeArquivo);
                    return;
                }
            }
            this.carregando = true;
            this.service.getMidia(this.templateId, this.midiaPath, this.isImagem).subscribe((dadosBlob) => {
                if (dadosBlob != null) {
                    this.midia = dadosBlob;
                    if (this.isDesconhecido) {
                        fileSaver.saveAs(new Blob([this.base64ComoArquivo(this.midia.value)], { type: this.tipoMidia }), this.nomeArquivo);
                    }
                }
                this.carregando = false;
            });
        }
    }

    verificaTipo(multimedia: any) {
        if (multimedia.mediaType.codeString.startsWith('image')) {
            this.isImagem = true;
        } else if (multimedia.mediaType.codeString.startsWith('video')) {
            this.isVideo = true;
        } else if (multimedia.mediaType.codeString.startsWith('audio')) {
            this.isAudio = true;
        } else {
            this.isDesconhecido = true;
        }
    }

}
