import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CadastroEmpresaContratoComponent } from './cadastro-empresa-contrato.component';

describe('CadastroEmpresaContratoComponent', () => {
  let component: CadastroEmpresaContratoComponent;
  let fixture: ComponentFixture<CadastroEmpresaContratoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CadastroEmpresaContratoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CadastroEmpresaContratoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
