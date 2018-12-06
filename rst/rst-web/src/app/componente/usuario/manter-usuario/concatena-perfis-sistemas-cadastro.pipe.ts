import { Pipe, PipeTransform } from '@angular/core';

import { Sistema } from 'app/modelo/sistema.model';

const COD_SISTEMAS_RELACIONADOS = ['dw', 'indigev', 'portal'];
const COD_SISTEMA_CADASTRO = 'cadastro';

@Pipe({ name: 'concatenaPerfisSistemasCadastro' })
export class ConcatenaPerfisSistemasCadastroPipe implements PipeTransform {
  
  transform(sistemas: Sistema[], ...args: any[]) {
    return sistemas
      .filter(sistema => !COD_SISTEMAS_RELACIONADOS.includes(sistema.codigo) && 
        sistema.codigo !== COD_SISTEMA_CADASTRO)
      .concat(this.getSistemasRelacionados(sistemas))
      .sort((sistema, outroSistema) => {
        if(sistema.codigo > outroSistema.codigo) return 1;
        if(sistema.codigo < outroSistema.codigo) return -1;

        return 0;
      })
  }

  private getSistemasRelacionados(sistemas: Sistema[]) {
    const sistemaCadastro = sistemas.find(sistema => sistema.codigo === COD_SISTEMA_CADASTRO);

    sistemas.filter(sistema => COD_SISTEMAS_RELACIONADOS.includes(sistema.codigo))
      .map(sistema => sistema.sistemaPerfis)
      .reduce((arr, sistemaPerfil) => arr.concat(sistemaPerfil), [])
      .forEach(sistemaPerfil => {
        !sistemaCadastro.sistemaPerfis.some(s => s.perfil.id === sistemaPerfil.perfil.id)
        && sistemaCadastro.sistemaPerfis.push(sistemaPerfil);
      });
  
    return sistemaCadastro;
  }
}
