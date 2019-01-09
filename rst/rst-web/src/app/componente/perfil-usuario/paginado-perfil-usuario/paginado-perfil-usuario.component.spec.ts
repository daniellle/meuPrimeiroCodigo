import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PaginadoPerfilUsuarioComponent } from './paginado-perfil-usuario.component';

describe('PaginadoPerfilUsuarioComponent', () => {
  let component: PaginadoPerfilUsuarioComponent;
  let fixture: ComponentFixture<PaginadoPerfilUsuarioComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PaginadoPerfilUsuarioComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PaginadoPerfilUsuarioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
