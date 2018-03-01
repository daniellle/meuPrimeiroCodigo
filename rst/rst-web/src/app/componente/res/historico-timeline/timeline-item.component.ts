import {isNullOrUndefined} from 'util';
import {ResService} from './../../../servico/res-service.service';
import {ResItemComponent} from './res-item.component';
import {ComposicaoDirective} from 'app/diretiva/composicao.directive';
import {
    Component,
    ComponentFactoryResolver,
    EventEmitter,
    Input,
    OnInit,
    Output,
    ViewChild,
    ViewEncapsulation
} from '@angular/core';
import {
    Action,
    Activity,
    AdminEntry,
    CareEntry,
    Cluster,
    Composition,
    ContentItem,
    DvBoolean,
    DvCodedText,
    DvCount,
    DvDate,
    DvDateTime,
    DvInterval,
    DvMultimedia,
    DvOrdinal,
    DvProportion,
    DvQuantity,
    DvText,
    DvTime,
    Element,
    Entry,
    Evaluation,
    Event,
    History,
    Instruction,
    InstructionDetails,
    IntervalEvent,
    Item,
    ItemList,
    ItemSingle,
    ItemStructure,
    ItemTable,
    ItemTree,
    Locatable,
    Observation,
    OperationalTemplate,
    Section,
    TypedJSON
} from '@ezvida/adl-core';
import {IconesUtils} from 'app/compartilhado/utilitario/icones.utils';

@Component({
    selector: 'app-timeline-item',
    template: `
        <div class="timeline-item row">
            <div (click)='expandir()' class=' col-lg-1  timeline-item-icon-area' [class.expanded]='expandido'>
                <div class="timeline-icon" [class.selected]='expandido'>
                    <img [src]='timelineIcon' widht='65px' height='65px'/>
                </div>
            </div>
            <div (click)='expandir()' class='col-12 col-lg-11 col-xl-11'>
                <div class='row timeline-header' [style.border-color]='estilo?.cor'
                     [style.background-color]='estilo?.cor' [class.expanded]='expandido'>
                    <div class="col">
                                                        <span>
                                                        <label class='data-encontro'> {{ data | date:'longDate'
                                                            }} </label>
                                                                <label class='separator'> &#8212; </label>
                                                                <label class='encounter tipo-encontro'> {{ titulo
                                                                    }} </label>
                                                        </span>
                    </div>
                </div>
                <div (click)='$event.stopPropagation()' class="timeline-content row" [class.hidden]='!expandido'>
                    <div class='col-lg-12'>
                        <ng-template appComposicao></ng-template>
                    </div>
                    <div class='col-lg-12' *ngIf='semDadosParaMostrar'>
                        Não foi possível exibir nenhum dado deste atendimento.
                    </div>
                </div>
            </div>
        </div>`,
    encapsulation: ViewEncapsulation.None,
    styleUrls: ['../../../../assets/scss/timeline/timeline.scss']
})
export class TimelineItemComponent implements OnInit {
    
    data: Date;
    expandido = false;
    titulo = 'Encontro Médico';
    composition: any;
    timelineIcon: string;
    estilo: {
        cor: string,
        svg: string,
        svg_full: string,
    };
    @Input() elementoHistorico: { _id: any, created: Date; composition: any };
    @Input() form: any;
    @Input() filtrarInformacoes: boolean;
    @ViewChild(ComposicaoDirective) composicao: ComposicaoDirective;
    @Output() onRemover = new EventEmitter<string>();
    dadosCarregados = false;
    semDadosParaMostrar = false;
    private templatesIgnorados = [
        'openEHR-EHR-SECTION.ovl-ficha_clinica_ocupacional-adhoc-010.v1.0.0', //portador de deficiencia
        'openEHR-EHR-SECTION.ovl-ficha_clinica_ocupacional-adhoc-008.v1.0.0', // historico ocupacional
        'openEHR-EHR-SECTION.ovl-ficha_clinica_ocupacional-adhoc-007.v1.0.0', // historico ocupacional pregresso
        'openEHR-EHR-SECTION.ovl-ficha_clinica_ocupacional-adhoc-009.v1.0.0', // história clínica atual
        'openEHR-EHR-EVALUATION.ovl-ficha_clinica_ocupacional-hipoteses_diagnosticas_detalhes-001.v1.0.0' // detalhes-diagnostico
    ];
    
    constructor(private resolver: ComponentFactoryResolver, private service: ResService) { }
    
    ngOnInit() {
        this.montarCabecalho(this.elementoHistorico);
    }
    
    expandir() {
        const expandir = !this.expandido;
        if ( expandir ) {
            if ( !this.dadosCarregados ) {
                this.carregarDados();
            }
            else {
                this.aplicarLayoutExpandido();
            }
        }
        else {
            this.timelineIcon = this.estilo.svg;
            this.expandido = false;
        }
    }
    
    carregarDados() {
        this.service.buscarEncontro(this.elementoHistorico._id).subscribe((data) => {
            this.composition = TypedJSON.parse(data.resultado.composition, Composition);
            const form = data.form as OperationalTemplate;
            
            const informacoes = this.coletarInformacoesTemplate(this.composition);
            informacoes.forEach((element) => {
                try {
                    if ( !this.composicao.conteiner
                              .createComponent(this.resolver.resolveComponentFactory(ResItemComponent))
                              .instance.processar(element, form) ) {
                        this.composicao.conteiner.remove();
                    }
                } catch (error) {
                    this.composicao.conteiner.remove();
                }
            });
            this.semDadosParaMostrar = this.composicao.conteiner.length === 0;
            this.dadosCarregados = true;
            this.aplicarLayoutExpandido();
        });
        
    }
    
    aplicarLayoutExpandido() {
        this.timelineIcon = this.estilo.svg_full;
        this.expandido = true;
    }
    
    isPathInterno(path: Locatable): boolean {
        return path.isArchetypeRoot() ||
               path instanceof Cluster ||
               path instanceof IntervalEvent ||
               path instanceof Evaluation;
    }
    
    private montarCabecalho(elementoHistorico: { created: Date; composition: any }) {
        this.data = elementoHistorico.created;
        this.composition = elementoHistorico.composition;
        
        this.definirTitulo(this.composition);
        
        this.estilo = IconesUtils.getIconeTemplate(this.composition.archetypeNodeId);
        this.timelineIcon = this.estilo.svg;
    }
    
    private definirTitulo(composition: any) {
    }
    
    private coletarInformacoesTemplate(composition: Composition): InformacoesTemplate[] {
        const informacoes: InformacoesTemplate[] = [];
        
        composition.content.forEach((locatable: Locatable) => {
            if ( this.templatesIgnorados.indexOf(locatable.archetypeNodeId) === -1 ) {
                console.log(locatable.archetypeNodeId);
                const info = new InformacoesTemplate();
                info.isRoot = this.isPathInterno(locatable);
                info.arquetipoReferencia = locatable.archetypeNodeId;
                info.titulo = locatable.name.value;
                
                if ( locatable instanceof Element ) {
                    this.processarAtributo(locatable, info, info);
                }
                else if ( locatable instanceof Event ) {
                    this.processStructure(locatable.data, info, info);
                    this.processStructure(locatable.state, info, info);
                }
                else if ( locatable instanceof Cluster ) {
                    if ( !isNullOrUndefined((<Cluster>locatable).items) ) {
                        (<Cluster>locatable).items.filter(i => !this.templatesIgnorados
                                                                    .includes(i.archetypeNodeId))
                                            .forEach((item: Item) => {
                                                this.processCluster(item, info, info);
                                            });
                    }
                }
                else if ( locatable instanceof Entry ) {
                    this.processEntry(<Entry>locatable, info, info);
                }
                else if ( locatable instanceof Section ) {
                    if ( !isNullOrUndefined((<Section>locatable).items) ) {
                        (<Section>locatable).items.filter(i => !this.templatesIgnorados
                                                                    .includes(i.archetypeNodeId))
                                            .forEach((item: ContentItem) => {
                                                this.compositionProcess(item, info, info);
                                            });
                    }
                }
                
                informacoes.push(info);
            }
        });
        
        for (const info of informacoes) {
            if ( info.informacoesInternas && info.informacoesInternas.length > 0 ) {
                info.informacoesInternas.forEach((inf) => {
                    const todosPaths = inf.pathsComValor.splice(0);
                    
                    inf.pathsComValor.push(...this.agrupar(inf), ...todosPaths);
                });
            }
        }
        
        return informacoes;
    }
    
    private agrupar(informacaoInterna: InformacoesTemplate) {
        const resultado: any[] = [];
        if ( informacaoInterna.pathsComValor ) {
            const todosPaths = informacaoInterna.pathsComValor.splice(0);
            
            resultado.push(...todosPaths);
        }
        
        if ( informacaoInterna.informacoesInternas ) {
            informacaoInterna.informacoesInternas.forEach((info) => {
                const todosInternos = resultado.splice(0);
                
                resultado.push(...this.agrupar(info), ...todosInternos);
            });
        }
        
        return resultado;
    }
    
    private processarAtributo(entidade: Element, informacoes: InformacoesTemplate, root: InformacoesTemplate) {
        // História clínica atual
        if ( root.arquetipoReferencia
             === 'openEHR-EHR-OBSERVATION.ovl-ficha_clinica_ocupacional-historia_clinica_atual-001.v1.0.0' ) {
            // CIDS RELACIONADOS COM O CMAN DESTA EMPRESA
            if ( entidade.archetypeNodeId === 'id10' ) {
                return;
            }
        }
        
        if ( this.filtrarInformacoes ) {
            // História Ocupacional Atual
            if (
                root.arquetipoReferencia
                === 'openEHR-EHR-ADMIN_ENTRY.ovl-ficha_clinica_ocupacional-historia_ocupacional_atual-001.v1.0.0'
            ) {
                // Função
                if ( entidade.archetypeNodeId === 'id7' ) {
                    return;
                }
                // Exame Físico
            }
            else if ( root.arquetipoReferencia
                      === 'openEHR-EHR-OBSERVATION.ovl-ficha_clinica_ocupacional-exame_fisico_sesi-001.v1.0.0' ) {
                return;
                // Exame de Auxílio
            }
            else if (
                root.arquetipoReferencia
                === 'openEHR-EHR-EVALUATION.ovl-ficha_clinica_ocupacional-exame_auxilio_diagnostico-001.v1.0.0'
            ) {
                return;
                // Hipóteses diagnosticas
            }
            else if (
                root.arquetipoReferencia
                === 'openEHR-EHR-EVALUATION.ovl-ficha_clinica_ocupacional-problem_diagnosis-SESI_BR-002.v1.0.0'
            ) {
                // Conduta
                // Observações diagnósticas
                // Observações
                if ( entidade.archetypeNodeId === 'id70.0.1' ||
                     entidade.archetypeNodeId === 'id73.0.1' ||
                     entidade.archetypeNodeId === 'id7' ) {
                    return;
                }
            }
        }
        
        if ( isNullOrUndefined(informacoes.pathsComValor) ) {
            informacoes.pathsComValor = [];
        }
        
        if ( entidade.value instanceof DvOrdinal ) {
            if ( !isNullOrUndefined(entidade.value.value) ) {
                informacoes.pathsComValor.push(entidade);
            }
        }
        else if ( entidade.value instanceof DvCodedText ) {
            if ( entidade.value ) {
                if ( !isNullOrUndefined(entidade.value.value) ) {
                    informacoes.pathsComValor.push(entidade);
                }
            }
        }
        else if ( entidade.value instanceof DvQuantity ||
                  entidade.value instanceof DvCount ) {
            if ( !isNullOrUndefined(entidade.value.magnitude) ) {
                informacoes.pathsComValor.push(entidade);
            }
        }
        else if ( entidade.value instanceof DvText ||
                  entidade.value instanceof DvDateTime ||
                  entidade.value instanceof DvDate ||
                  entidade.value instanceof DvBoolean ||
                  entidade.value instanceof DvTime ) {
            if ( !isNullOrUndefined(entidade.value.value) ) {
                informacoes.pathsComValor.push(entidade);
            }
        }
        else if ( entidade.value instanceof DvProportion ) {
            if ( !isNullOrUndefined(entidade.value.denominator) || !isNullOrUndefined(entidade.value.numerator) ) {
                informacoes.pathsComValor.push(entidade);
            }
        }
        else if ( entidade.value instanceof DvInterval ) {
            if ( entidade.value.upper instanceof DvQuantity && entidade.value.lower instanceof DvQuantity ) {
                if ( !isNullOrUndefined(entidade.value.lower.magnitude) ||
                     !isNullOrUndefined(entidade.value.lower.units) ||
                     !isNullOrUndefined(entidade.value.upper.magnitude) ||
                     !isNullOrUndefined(entidade.value.upper.units) ) {
                    informacoes.pathsComValor.push(entidade);
                }
            }
            else if (
                (entidade.value.upper instanceof DvDate && entidade.value.lower instanceof DvDate) ||
                (entidade.value.upper instanceof DvDateTime && entidade.value.lower instanceof DvDateTime) ||
                (entidade.value.upper instanceof DvTime && entidade.value.lower instanceof DvTime) ) {
                if ( !isNullOrUndefined(entidade.value.lower.value)
                     || !isNullOrUndefined(entidade.value.upper.value) ) {
                    informacoes.pathsComValor.push(entidade);
                }
            }
        }
        else if ( entidade.value instanceof DvMultimedia ) {
            if ( !isNullOrUndefined(entidade.value.uri.value) ) {
                informacoes.pathsComValor.push(entidade);
            }
        }
    }
    
    private compositionProcess(locatable: Locatable, informacoes: InformacoesTemplate, root: InformacoesTemplate) {
        if ( locatable instanceof Element ) {
            this.processarAtributo(locatable, informacoes, root);
        }
        else if ( locatable instanceof Event ) {
            const info = new InformacoesTemplate();
            informacoes.isRoot = this.isPathInterno(locatable);
            informacoes.arquetipoReferencia = locatable.archetypeNodeId;
            informacoes.titulo = locatable.name.value;
            
            this.processStructure(locatable.data, info, root);
            this.processStructure(locatable.state, info, root);
            
            informacoes.informacoesInternas.push(info);
        }
        else if ( locatable instanceof Cluster ) {
            const info = new InformacoesTemplate();
            informacoes.isRoot = this.isPathInterno(locatable);
            informacoes.arquetipoReferencia = locatable.archetypeNodeId;
            informacoes.titulo = locatable.name.value;
            
            if ( !isNullOrUndefined((<Cluster>locatable).items) ) {
                (<Cluster>locatable).items.forEach((item: Item) => {
                    this.processCluster(item, info, root);
                });
            }
            
            informacoes.informacoesInternas.push(info);
        }
        else if ( locatable instanceof Entry ) {
            const info = new InformacoesTemplate();
            informacoes.isRoot = this.isPathInterno(locatable);
            informacoes.arquetipoReferencia = locatable.archetypeNodeId;
            informacoes.titulo = locatable.name.value;
            
            this.processEntry(<Entry>locatable, info, root);
            
            informacoes.informacoesInternas.push(info);
        }
        else if ( locatable instanceof Section ) {
            if ( !isNullOrUndefined((<Section>locatable).items) ) {
                const info = new InformacoesTemplate();
                informacoes.isRoot = this.isPathInterno(locatable);
                informacoes.arquetipoReferencia = locatable.archetypeNodeId;
                informacoes.titulo = locatable.name.value;
                
                (<Section>locatable).items.forEach((item: ContentItem) => {
                    this.compositionProcess(item, info, root);
                });
                
                informacoes.informacoesInternas.push(info);
            }
        }
    }
    
    private processEntry(entry: Entry, informacoes: InformacoesTemplate, root: InformacoesTemplate): void {
        if ( entry instanceof AdminEntry ) {
            const info = new InformacoesTemplate();
            informacoes.isRoot = this.isPathInterno(entry);
            informacoes.arquetipoReferencia = entry.archetypeNodeId;
            informacoes.titulo = entry.name.value;
            
            this.processStructure((<AdminEntry>entry).data, info, root);
            
            informacoes.informacoesInternas.push(info);
        }
        else if ( entry instanceof Instruction ) {
            const info = new InformacoesTemplate();
            informacoes.isRoot = this.isPathInterno(entry);
            informacoes.arquetipoReferencia = entry.archetypeNodeId;
            informacoes.titulo = entry.name.value;
            
            this.processEntryInstruction(<Instruction>entry, info, root);
            
            informacoes.informacoesInternas.push(info);
        }
        else if ( entry instanceof Action ) {
            const info = new InformacoesTemplate();
            informacoes.isRoot = this.isPathInterno(entry);
            informacoes.arquetipoReferencia = entry.archetypeNodeId;
            informacoes.titulo = entry.name.value;
            
            this.processEntryAction(<Action>entry, info, root);
            
            informacoes.informacoesInternas.push(info);
        }
        else if ( entry instanceof Section ) {
            const info = new InformacoesTemplate();
            informacoes.isRoot = this.isPathInterno(entry);
            informacoes.arquetipoReferencia = entry.archetypeNodeId;
            informacoes.titulo = entry.name.value;
            
            if ( !isNullOrUndefined((<Section>entry).items) ) {
                (<Section>entry).items.forEach((item: ContentItem) => {
                    this.compositionProcess(item, info, root);
                });
            }
            
            informacoes.informacoesInternas.push(info);
        }
        else if ( entry instanceof Evaluation ) {
            const info = new InformacoesTemplate();
            informacoes.isRoot = this.isPathInterno(entry);
            informacoes.arquetipoReferencia = entry.archetypeNodeId;
            informacoes.titulo = entry.name.value;
            
            this.processStructure((<Evaluation>entry).data, info, root);
            
            informacoes.informacoesInternas.push(info);
        }
        else if ( entry instanceof Observation ) {
            const info = new InformacoesTemplate();
            informacoes.isRoot = this.isPathInterno(entry);
            informacoes.arquetipoReferencia = entry.archetypeNodeId;
            informacoes.titulo = entry.name.value;
            
            this.processEntryObservation(<Observation>entry, info, root);
            
            informacoes.informacoesInternas.push(info);
        }
        else if ( entry instanceof CareEntry ) {
            const info = new InformacoesTemplate();
            informacoes.isRoot = this.isPathInterno(entry);
            informacoes.arquetipoReferencia = entry.archetypeNodeId;
            informacoes.titulo = entry.name.value;
            
            this.processStructure((<CareEntry>entry).protocol, info, root);
            
            informacoes.informacoesInternas.push(info);
        }
    }
    
    private processCluster(item: Item, informacoes: InformacoesTemplate, root: InformacoesTemplate): void {
        if ( item instanceof Element ) {
            this.processarAtributo(<Element>item, informacoes, root);
        }
        else {
            const info = new InformacoesTemplate();
            informacoes.isRoot = this.isPathInterno(item);
            informacoes.arquetipoReferencia = item.archetypeNodeId;
            informacoes.titulo = item.name.value;
            
            this.compositionProcess(item, info, root);
            
            informacoes.informacoesInternas.push(info);
        }
    }
    
    private processHistory(history: History, informacoes: InformacoesTemplate, root: InformacoesTemplate): void {
        if ( history && history.events ) {
            const info = new InformacoesTemplate();
            informacoes.isRoot = this.isPathInterno(history);
            informacoes.arquetipoReferencia = history.archetypeNodeId;
            informacoes.titulo = history.name.value;
            
            if ( !isNullOrUndefined(history.events) ) {
                history.events.forEach((evento: Event) => {
                    this.compositionProcess(evento, info, root);
                });
            }
            
            informacoes.informacoesInternas.push(info);
        }
    }
    
    private processStructure(item: ItemStructure, informacoes: InformacoesTemplate, root: InformacoesTemplate): void {
        if ( item instanceof ItemTree && (<ItemTree>item).items ) {
            const info = new InformacoesTemplate();
            informacoes.isRoot = this.isPathInterno(item);
            informacoes.arquetipoReferencia = item.archetypeNodeId;
            informacoes.titulo = item.name.value;
            
            (<ItemTree>item).items.forEach((itemElement: Item) => {
                this.processCluster(itemElement, info, root);
            });
            
            informacoes.informacoesInternas.push(info);
        }
        else if ( item instanceof ItemSingle ) {
            this.processarAtributo((<ItemSingle>item).item, informacoes, root);
        }
        else if ( item instanceof ItemList && (<ItemList>item).items ) {
            const info = new InformacoesTemplate();
            informacoes.isRoot = this.isPathInterno(item);
            informacoes.arquetipoReferencia = item.archetypeNodeId;
            informacoes.titulo = item.name.value;
            
            (<ItemList>item).items.forEach((element: Element) => {
                this.processarAtributo(element, info, root);
            });
            
            informacoes.informacoesInternas.push(info);
        }
        else if ( item instanceof ItemTable && (<ItemTable>item).rows ) {
            const info = new InformacoesTemplate();
            informacoes.isRoot = this.isPathInterno(item);
            informacoes.arquetipoReferencia = item.archetypeNodeId;
            informacoes.titulo = item.name.value;
            
            (<ItemTable>item).rows.forEach((cluster: Cluster) => {
                if ( cluster.items ) {
                    cluster.items.forEach((itemElement: Item) => {
                        this.processCluster(itemElement, info, root);
                    });
                }
            });
            
            informacoes.informacoesInternas.push(info);
        }
    }
    
    private processEntryAction(action: Action, informacoes: InformacoesTemplate, root: InformacoesTemplate) {
        const info = new InformacoesTemplate();
        informacoes.isRoot = this.isPathInterno(action);
        informacoes.arquetipoReferencia = action.archetypeNodeId;
        informacoes.titulo = action.name.value;
        
        this.processStructure(action.description, informacoes, root);
        
        if ( action.instructionDetails instanceof InstructionDetails ) {
            this.processStructure(action.instructionDetails.wfDetails, info, root);
        }
        
        informacoes.informacoesInternas.push(info);
    }
    
    private processEntryObservation(observation: Observation,
        informacoes: InformacoesTemplate,
        root: InformacoesTemplate) {
        const info = new InformacoesTemplate();
        informacoes.isRoot = this.isPathInterno(observation);
        informacoes.arquetipoReferencia = observation.archetypeNodeId;
        informacoes.titulo = observation.name.value;
        
        this.processHistory(observation.data, info, root);
        this.processHistory(observation.state, info, root);
        this.processStructure(observation.protocol, info, root);
        
        informacoes.informacoesInternas.push(info);
    }
    
    private processEntryInstruction(instruction: Instruction,
        informacoes: InformacoesTemplate,
        root: InformacoesTemplate) {
        const info = new InformacoesTemplate();
        informacoes.isRoot = this.isPathInterno(instruction);
        informacoes.arquetipoReferencia = instruction.archetypeNodeId;
        informacoes.titulo = instruction.name.value;
        
        this.processStructure(instruction.protocol, info, root);
        
        if ( !isNullOrUndefined(instruction.activities) ) {
            instruction.activities.forEach((activity: Activity) => {
                this.processarActivity(activity, info, root);
            });
        }
        
        informacoes.informacoesInternas.push(info);
    }
    
    private processarActivity(activity: Activity, informacoes: InformacoesTemplate, root: InformacoesTemplate) {
        const info = new InformacoesTemplate();
        informacoes.isRoot = this.isPathInterno(activity);
        informacoes.arquetipoReferencia = activity.archetypeNodeId;
        informacoes.titulo = activity.name.value;
        
        if ( activity.description ) {
            this.processStructure(activity.description, info, root);
        }
        
        informacoes.informacoesInternas.push(info);
    }
}

// tslint:disable-next-line:max-classes-per-file
export class InformacoesTemplate {
    arquetipoReferencia: string;
    isRoot = true;
    caminho: string;
    titulo: string;
    pathsComValor: Element[] = [];
    informacoesInternas: InformacoesTemplate[] = [];
    
}
