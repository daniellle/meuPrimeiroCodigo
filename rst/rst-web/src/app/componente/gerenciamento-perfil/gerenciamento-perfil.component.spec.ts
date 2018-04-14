import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GerenciamentoPerfilComponent } from './gerenciamento-perfil.component';

describe('GerenciamentoPerfilComponent', () => {
  let component: GerenciamentoPerfilComponent;
  let fixture: ComponentFixture<GerenciamentoPerfilComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GerenciamentoPerfilComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GerenciamentoPerfilComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
