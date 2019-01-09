import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PesquisaPerfilUsuarioComponent } from './pesquisa-perfil-usuario.component';

describe('PesquisaPerfilUsuarioComponent', () => {
  let component: PesquisaPerfilUsuarioComponent;
  let fixture: ComponentFixture<PesquisaPerfilUsuarioComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PesquisaPerfilUsuarioComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PesquisaPerfilUsuarioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
